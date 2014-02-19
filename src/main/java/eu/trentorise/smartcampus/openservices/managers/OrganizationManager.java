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
 * Manager that retrieves, adds, modifies and deletes organization data.
 * These operations are allowed only for logged user, who has role in organization.
 * 
 * @author raman
 *
 */
@Component
@Transactional
public class OrganizationManager {
	/**
	 * Instance of {@link UserDao} to retrieve user data using Dao classes.
	 */
	@Autowired
	private UserDao userDao;
	/**
	 * Instance of {@link UserRoleDao} to retrieve role of user data using Dao classes.
	 */
	@Autowired
	private UserRoleDao urDao;
	/**
	 * Instance of {@link OrganizationDao} to retrieve organization data using Dao classes.
	 */
	@Autowired
	private OrganizationDao orgDao;
	/**
	 * Instance of {@link ServiceDao} to retrieve service data using Dao classes.
	 */
	@Autowired
	private ServiceDao serviceDao;
	/**
	 * Instance of {@link TemporaryLinkDao} to retrieve temporary data using Dao classes.
	 */
	@Autowired
	private TemporaryLinkDao tlDao;
	/**
	 * Instance of {@link ServiceHistoryDao} to retrieve service history data using Dao classes.
	 */
	@Autowired
	private ServiceHistoryDao shDao;


	/**
	 * Delete an existing organization from database
	 * @param username : String username of logged in user
	 * @param orgId : int organizatin id
	 * @return boolean: true if it is ok, else false
	 * @throws SecurityException when user has not correct role
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
				if(serviceList!=null){
					for (Service s : serviceList) {
						serviceDao.deleteService(s);
					}
				}
				// delete history
				List<ServiceHistory> shList = shDao.getServiceHistoryByOrgId(orgId);
				if(shList!=null){
					for(ServiceHistory sh: shList){
						shDao.deleteServiceHistory(sh);
					}
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
	 * Add a new organization in database
	 * Add organization owner role to this user
	 * @param username : String username of user
	 * @param org : Organization data
	 * @return boolean: true if it is ok, else false
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
	 * Update an existing organization data from database
	 * @param username : String username of user
	 * @param org : Organization data
	 * @return boolean: true if it is ok, else false
	 * @throws SecurityException when user has not correct role
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
				o.setActivityArea(org.getActivityArea());
				o.setCategory(org.getCategory());
				o.setContacts(org.getContacts());
				o.setLogo(org.getLogo());
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
	 * @param username : String username of user
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
	
	/**
	 * Invite a user to become part of an organization
	 * User who send invitation must have role organization owner for this organization
	 * The invitation create a temporary link object with a key
	 * @param username : String, user who invites new organization owner
	 * @param org_id : int organization id
	 * @param role : String, role for new organization owner
	 * @param email : String, email of new organization owner
	 * @return String key
	 * @throws SecurityException when user has not correct role
	 */
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
	 * @param org_id : int organization id
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
	public List<Organization> getOrganizations(int firstResult, int maxResult, String param_order) {
		try{
			return orgDao.showOrganizations(firstResult, maxResult, param_order);
		}catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * @param org_id : String organization id
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
	 * New organization owner user must use its username and key to connect with organization
	 * If it is ok, then a new user role is created and user become an organization owner
	 * @param username : String username of logged in user
	 * @param key : String private key of invitation
	 * @return boolean: true if it is ok, else false
	 * @throws SecurityException when user has not correct role
	 * @throws EntityNotFoundException when temporary link object does not exist
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
	 * Remove an organization owner from organization
	 * @param username : String username of logged in user
	 * @param org_id : int organization id
	 * @param user_id : int user id of user we want to delete
	 * @return boolean: true if it is ok, else false
	 * @throws SecurityException when user has not correct role
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
	
	/**
	 * Retrieve members of an organization.
	 * Search by organization id
	 * @param org_id
	 * @return instances of {@link User}
	 */
	public List<User> organizationMembers(int org_id){
		List<User> members = new ArrayList<User>();
		try{
			List<UserRole> urlist = urDao.getUserRoleByIdOrg(org_id);
			User u;
			for (int i = 0; i < urlist.size(); i++) {
				u = userDao.getUserById(urlist.get(i).getId_user());
				u.setPassword(null);
				members.add(u);
			}
		}catch(DataAccessException d){
			
		}
		return members;
	}

}
