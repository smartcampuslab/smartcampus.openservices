/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/

package eu.trentorise.smartcampus.openservices.managers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.dao.OrganizationDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceHistoryDao;
import eu.trentorise.smartcampus.openservices.dao.TemporaryLinkDao;
import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.dao.UserRoleDao;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;
import eu.trentorise.smartcampus.openservices.entities.TemporaryLink;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.entities.UserRole;
import eu.trentorise.smartcampus.openservices.support.GenerateKey;

/**
 * @author raman
 *
 */
@Component
@Transactional
public class OrganizationManager {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleDao urDao;
	@Autowired
	private OrganizationDao orgDao;
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private TemporaryLinkDao tlDao;
	@Autowired
	private ServiceHistoryDao shDao;


	/**
	 * Delete organization, associated user roles, and services
	 * @param username
	 * @param orgId
	 * @throws SecurityException
	 */
	@Transactional
	public boolean deleteOrganization(String username, int orgId) throws SecurityException {
		try {
			User user = userDao.getUserByUsername(username);
			// check user role
			UserRole ur = urDao.getRoleOfUser(user.getId(), orgId);
			if (ur != null && ur.getRole().equalsIgnoreCase("ROLE_ORGOWNER")) {
				// delete user roles
				List<UserRole> list = urDao.getUserRoleByIdOrg(orgId);
				for (UserRole urElem : list) {
					urDao.deleteUserRole(urElem);
				}
				// delete services
				List<Service> serviceList = serviceDao.getServiceByIdOrg(orgId);
				for (Service s : serviceList) {
					serviceDao.deleteService(s);
				}
				// delete history
				List<ServiceHistory> shList = shDao.getServiceHistoryByOrgId(orgId);
				for(ServiceHistory sh: shList){
					shDao.deleteServiceHistory(sh);
				}
				// delete org
				orgDao.deleteOrganization(orgId);
				
			} else {
				throw new SecurityException();
			}
			// check if organization is deleted
			if (orgDao.getOrganizationById(orgId) == null) {
				return true;
			}
		} catch (DataAccessException d) {
			return false;
		}
		return false;
	}
	
	/**
	 * Create organization and associate the user as the organization owner
	 * @param username
	 * @param org
	 */
	@Transactional
	public boolean createOrganization(String username, Organization org) {
		try {
			User user = userDao.getUserByUsername(username);
			// check name of Organization
			if (orgDao.getOrganizationByName(org.getName()) == null) {
				org.setCreatorId(user.getId());
				orgDao.createOrganization(org);
				// add UserRole
				urDao.createUserRole(org.getCreatorId(), org.getId(),
						"ROLE_ORGOWNER");
				// check if this new organizatione exist
				if (orgDao.getOrganizationByName(org.getName()) != null) {
					return true;
				}
			}
		} catch (DataAccessException d) {
			return false;
		}
		return false;
		
	}

	/**
	 * update organization data
	 * @param username
	 * @param org
	 */
	@Transactional
	public boolean updateOrganization(String username, Organization org) {
		try {
			User user = userDao.getUserByUsername(username);
			Organization o = orgDao.getOrganizationById(org.getId());
			// check user role
			UserRole ur = urDao.getRoleOfUser(user.getId(), org.getId());
			if (ur != null && ur.getRole().equalsIgnoreCase("ROLE_ORGOWNER")) {
				// TODO which values can be modified by user?
				o.setDescription(org.getDescription());
				o.setCategory(org.getCategory());
				o.setContacts(org.getContacts());
				orgDao.modifyOrganization(o);
				return true;
			} else {
				throw new SecurityException();
			}
		} catch (DataAccessException d) {
			return false;
		}
	}

	/**
	 * @param username
	 * @return list of organizations, where the user is org owner or data owner
	 */
	public List<Organization> getUserOrganizations(String username) {
		List<Organization> orgs = new ArrayList<Organization>();
		try {
			User user = userDao.getUserByUsername(username);
			orgs = orgDao.showMyOrganizations(user.getId());
		} catch (DataAccessException d) {
			orgs = null;
		}
		return orgs;
	}
	
	public String createInvitation(String username, int org_id, String role, String email) throws SecurityException {
		try {
			User user = userDao.getUserByUsername(username);
			// check user role
			Organization org = orgDao.getOrganizationById(org_id);
			UserRole ur = urDao.getRoleOfUser(user.getId(), org.getId());
			if (ur != null && ur.getRole().equalsIgnoreCase("ROLE_ORGOWNER")) {

				// Generate a key
				GenerateKey g = new GenerateKey();
				String s = g.getPriv().toString().split("@")[1];

				// saved in a temporary table
				TemporaryLink entity = new TemporaryLink();
				entity.setKey(s);
				entity.setId_org(org_id);
				entity.setRole(role);
				entity.setEmail(email);
				tlDao.save(entity);

				return s;
			}
			throw new SecurityException();
		} catch (DataAccessException d) {
			throw new SecurityException();
		}
	}

	/**
	 * @param org_id
	 * @return {@link Organization} instance
	 */
	public Organization getOrganizationById(int org_id) {
		try{
			return orgDao.getOrganizationById(org_id);
		}catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * @return all {@link Organization} instances
	 */
	public List<Organization> getOrganizations() {
		try{
			return orgDao.showOrganizations();
		}catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * @param org_id
	 * @return list of {@link ServiceHistory} instances for the organization
	 */
	public List<ServiceHistory> getHistory(int org_id) {
		try{
			return shDao.getServiceHistoryByOrgId(org_id);
		} catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * Add invited user to the organization
	 * @param username
	 * @param key
	 */
	public boolean addOwner(String username, String key) throws SecurityException, EntityNotFoundException {
		try {
			User user = userDao.getUserByUsername(username);
			// Check in table TemporaryLink if this key is saved and if user is
			// correct
			TemporaryLink tl = tlDao.getTLByKey(key);
			if (tl != null) {
				// delete it if it is all ok
				if (tl.getEmail().equalsIgnoreCase(user.getEmail())) {
					// add a UserRole data in table: user_id, org_id, role
					// ORG_OWNER
					urDao.createUserRole(user.getId(), tl.getId_org(),
							tl.getRole());
					// delete temporary link
					tlDao.delete(key);
					return true;
				} else
					throw new SecurityException();
			} else
				throw new EntityNotFoundException(key);
		} catch (DataAccessException d) {
			return false;
		}
		
	}
	
	/**
	 * Remove the specified user from the organization members
	 * @param org_id
	 * @param user_id
	 */
	public boolean deleteOrgUser(String username, int org_id, int user_id) {
		try {
			// Check user role
			User user = userDao.getUserByUsername(username);
			UserRole userRole = urDao.getRoleOfUser(user.getId(), org_id);
			if (userRole.getRole().equalsIgnoreCase("ROLE_ORGOWNER")) {
				UserRole ur = new UserRole(user_id, org_id, "ROLE_ORGOWNER");
				urDao.deleteUserRole(ur);
				return true;
			} else {
				throw new SecurityException();
			}
		} catch (DataAccessException d) {
			return false;
		}
	}

}
