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

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
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
	public void deleteOrganization(String username, int orgId) throws SecurityException {
		User user = userDao.getUserByUsername(username);
		//check user role
		UserRole ur = urDao.getRoleOfUser(user.getId(), orgId);
		if(ur.getRole().equalsIgnoreCase("ROLE_ORGOWNER")){
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
			// delete org
			orgDao.deleteOrganization(orgId);
			// TODO history
		} else {
			throw new SecurityException();
		}
	}
	
	/**
	 * Create organization and associate the user as the organization owner
	 * @param username
	 * @param org
	 */
	@Transactional
	public void createOrganization(String username, Organization org) {
		User user = userDao.getUserByUsername(username);
		org.setCreatorId(user.getId());
		orgDao.createOrganization(org);
		//get org id
		Organization orgSaved = orgDao.getOrganizationByName(org.getName());
		//add UserRole
		urDao.createUserRole(orgSaved.getCreatorId(), orgSaved.getId(), "ROLE_ORGOWNER");
		// TODO history
	}

	/**
	 * update organization data
	 * @param username
	 * @param org
	 */
	@Transactional
	public void updateOrganization(String username, Organization org) {
		User user = userDao.getUserByUsername(username);
		Organization o = orgDao.getOrganizationById(org.getId());
		//check user role
		UserRole ur = urDao.getRoleOfUser(user.getId(), org.getId());
		if(ur.getRole().equalsIgnoreCase("ROLE_ORGOWNER")){
			//TODO which values can be modified by user?
			o.setDescription(org.getDescription());
			o.setCategory(org.getCategory());
			o.setContacts(org.getContacts());
			orgDao.modifyOrganization(o);
			// TODO history
		} else {
			throw new SecurityException();
		}
	}

	/**
	 * @param username
	 * @return list of organizations, where the user is org owner or data owner
	 */
	public List<Organization> getUserOrganizations(String username) {
		User user = userDao.getUserByUsername(username);
		List<Organization> orgs = orgDao.showMyOrganizations(user.getId());
		return orgs;
	}
	
	public String createInvitation(String username, int org_id, String role, String email) throws SecurityException {
		User user = userDao.getUserByUsername(username);
		//check user role
		Organization org = orgDao.getOrganizationById(org_id);
		UserRole ur = urDao.getRoleOfUser(user.getId(), org.getId());
		if(ur.getRole().equalsIgnoreCase("ROLE_ORGOWNER")){
			
			//Generate a key
			GenerateKey g = new GenerateKey();
			String s = g.getPriv().toString().split("@")[1];
			
			//saved in a temporary table
			TemporaryLink entity = new TemporaryLink();	
			entity.setKey(s);
			entity.setId_org(org_id);
			entity.setRole(role);
			entity.setEmail(email);
			tlDao.save(entity);
			
			return s;
		}
		throw new SecurityException();
	}

	/**
	 * @param org_id
	 * @return {@link Organization} instance
	 */
	public Organization getOrganizationById(int org_id) {
		return orgDao.getOrganizationById(org_id);
	}

	/**
	 * @return all {@link Organization} instances
	 */
	public List<Organization> getOrganizations() {
		return orgDao.showOrganizations();
	}

	/**
	 * @param org_id
	 * @return list of {@link ServiceHistory} instances for the organization
	 */
	public List<ServiceHistory> getHistory(int org_id) {
		return shDao.getServiceHistoryByOrgId(org_id);
	}

	/**
	 * Add invited user to the organization
	 * @param username
	 * @param key
	 */
	public void addOwner(String username, String key) throws SecurityException, EntityNotFoundException {
		User user = userDao.getUserByUsername(username);
		//Check in table TemporaryLink if this key is saved and if user is correct
		TemporaryLink tl = tlDao.getTLByKey(key);
		if (tl != null) {
			// delete it if it is all ok
			if (tl.getEmail() == user.getEmail()) {
				// add a UserRole data in table: user_id, org_id, role ORG_OWNER
				urDao.createUserRole(user.getId(), tl.getId_org(), tl.getRole());
				// delete temporary link
				tlDao.delete(key);
			} else
				throw new SecurityException();
		}
		else throw new EntityNotFoundException(key);
		
	}
	
	/**
	 * Remove the specified user from the organization members
	 * @param org_id
	 * @param user_id
	 */
	public void deleteOrgUser(int org_id, int user_id) {
		UserRole ur = new UserRole(user_id, org_id, "ROLE_ORGOWNER");
		urDao.deleteUserRole(ur);
	}

}
