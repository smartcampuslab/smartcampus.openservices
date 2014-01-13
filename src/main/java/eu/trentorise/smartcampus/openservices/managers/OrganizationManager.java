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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.dao.OrganizationDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceDao;
import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.dao.UserRoleDao;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.entities.UserRole;

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
		} else {
			throw new SecurityException();
		}
	}
}
