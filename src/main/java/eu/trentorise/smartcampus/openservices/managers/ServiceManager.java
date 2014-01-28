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
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.Constants.SERVICE_STATE;
import eu.trentorise.smartcampus.openservices.dao.MethodDao;
import eu.trentorise.smartcampus.openservices.dao.OrganizationDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceHistoryDao;
import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.dao.UserRoleDao;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;
import eu.trentorise.smartcampus.openservices.entities.TestBoxProperties;
import eu.trentorise.smartcampus.openservices.entities.TestInfo;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.entities.UserRole;

/**
 * Manager that retrieves, adds, modifies and deletes Service data from database.
 * These operations are allowed only for logged user, who has role in organization of 
 * wanted service.
 * 
 * @author raman
 *
 */
@Component
@Transactional
public class ServiceManager {
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
	 * Instance of {@link ServiceHistoryDao} to retrieve service history data using Dao classes.
	 */
	@Autowired
	private ServiceHistoryDao shDao;
	/**
	 * Instance of {@link MethodDao} to retrieve method data using Dao classes.
	 */
	@Autowired
	private MethodDao methodDao;

	/**
	 * Add a new service in database
	 * @param username : String username of logged in user
	 * @param service : {@link Service} instance
	 * @return boolean value: true if it is ok, false otherwise
	 * @throw SecurityException if user has not a role in service organization
	 */
	@Transactional
	public boolean createService(String username, Service service) {
		try {
			User user = userDao.getUserByUsername(username);
			UserRole ur = urDao.getRoleOfUser(user.getId(), service.getOrganizationId());
			if (ur == null) throw new SecurityException();//TODO
			service.setCreatorId(user.getId());
			service.setState(SERVICE_STATE.UNPUBLISH.toString());
			serviceDao.createService(service);
			// create history
			ServiceHistory sh = new ServiceHistory();
			sh.setOperation("Unpublish Service Added");
			sh.setId_service(service.getId());
			sh.setDate(new Date());
			shDao.addServiceHistory(sh);
			// check if service is created
			if (serviceDao.useService(service.getName()) != null) {
				return true;
			}
			return false;
		} catch (DataAccessException d) {
			return false;
		}
	}

	/**
	 * Update an existing service from database.
	 * @param username : String username of logged in user
	 * @param service : {@link Service} instance
	 * @return boolean value: true if it is ok, false otherwise
	 * @throw SecurityException if user has not a role in service organization
	 */
	@Transactional
	public boolean updateService(String username, Service service) {
		
		try {
			User user = userDao.getUserByUsername(username);

			UserRole ur = urDao.getRoleOfUser(user.getId(),
					service.getOrganizationId());
			if (ur == null)
				throw new SecurityException();

			Service s = serviceDao.getServiceById(service.getId());
			// cannot change name
			s.setDescription(service.getDescription());
			s.setTags(service.getTags());
			s.setCategory(service.getCategory());
			s.setDocumentation(service.getDocumentation());
			s.setAccessInformation(service.getAccessInformation());
			s.setExpiration(service.getExpiration());
			s.setImplementation(service.getImplementation());
			s.setLicense(service.getLicense());
			//s.setState(service.getState());
			s.setVersion(service.getVersion());
			serviceDao.modifyService(s);

			// Add a new ServiceHistory
			ServiceHistory sh = new ServiceHistory();
			sh.setOperation("Modify Service");
			sh.setId_service(s.getId());
			sh.setDate(new Date());
			shDao.addServiceHistory(sh);
			return true;
		} catch (DataAccessException d) {
			return false;
		}
		
	}

	/**
	 * Change service state, which can be
	 * publish
	 * unpublish
	 * deprecate
	 * @param username : String username of logged in user
	 * @param serviceId : int service id
	 * @param state : String state that service will have
	 * @return boolean value: true if it is ok, false otherwise
	 * @throw SecurityException if user has not a role in service organization
	 */
	@Transactional
	public boolean changeState(String username, int serviceId, String state) {
		try {
			User user = userDao.getUserByUsername(username);
			Service service = serviceDao.getServiceById(serviceId);

			UserRole ur = urDao.getRoleOfUser(user.getId(),
					service.getOrganizationId());
			if (ur == null)
				throw new SecurityException();

			// Change service state
			service.setState(state);
			serviceDao.modifyService(service);
			// add service history
			ServiceHistory sh = new ServiceHistory();
			sh.setOperation(state + " service");
			sh.setId_service(service.getId());
			sh.setDate(new Date());
			shDao.addServiceHistory(sh);
			
			return true;
		} catch (DataAccessException d) {
			return false;
		}
	}

	/**
	 * Delete an existing service from database
	 * @param username : String username of logged in user
	 * @param id : int service id that user wants to delete
	 * @return boolean value: true if it is ok, false otherwise
	 * @throw SecurityException if user has not a role in service organization
	 */
	public boolean deleteService(String username, int id) {
		try {
			User user = userDao.getUserByUsername(username);
			Service service = serviceDao.getServiceById(id);
			UserRole ur = urDao.getRoleOfUser(user.getId(),
					service.getOrganizationId());
			if (ur == null)
				throw new SecurityException();
			serviceDao.deleteService(service);
			// add service history
			ServiceHistory sh = new ServiceHistory();
			sh.setOperation("Delete Service");
			sh.setId_service(service.getId());
			sh.setDate(new Date());
			shDao.addServiceHistory(sh);
			return true;
		} catch (DataAccessException d) {
			return false;
		}
		
	}

	/**
	 * @return all {@link Service} instances
	 */
	@Transactional
	public List<Service> getServices() {
		try{
			return serviceDao.showService();
		}catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * Retrieve all user services, even unpublished services
	 * @param username : String username of logged in user
	 * @return all the {@link Service} instances the user can see/modify (i.e., all
	 * the services of the organizations the user is member of)
	 */
	@Transactional
	public List<Service> getUserServices(String username) {
		try{
			return serviceDao.showMyService(username);
		}catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * Retrieve service data searching by service id
	 * @param service_id : int service id
	 * @return {@link Service} instance with the specified ID
	 */
	@Transactional
	public Service getServiceById(int service_id) {
		try{
			return serviceDao.getServiceById(service_id);
		}catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * @param service_id : int service id
	 * @return all the {@link Method} instances of the specified service
	 */
	@Transactional
	public List<Method> getServiceMethodsByServiceId(int service_id) {
		try{
			return methodDao.getMethodByServiceId(service_id);
		}catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * @param service_id : int service id
	 * @return {@link ServiceHistory} instances of the specified service
	 */
	@Transactional
	public List<ServiceHistory> getServiceHistoryByServiceId(int service_id) {
		try{
			return shDao.getServiceHistoryByServiceId(service_id);
		}catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * Add a new service method in database.
	 * Only user has role in organization which service belongs can add a new service.
	 * @param username : String username of logged in user
	 * @param method : a new {@link Method} instance
	 * @return boolean value: true if it is ok, false otherwise
	 * @throw SecurityException if user has not a role in service organization
	 */
	@Transactional
	public boolean addMethod(String username, Method method) {
		try {
			User user = userDao.getUserByUsername(username);
			Service s = getServiceById(method.getServiceId());
			UserRole ur = urDao.getRoleOfUser(user.getId(),
					s.getOrganizationId());
			if (ur == null)
				throw new SecurityException();
			methodDao.addMethod(method);
			//Add history
			ServiceHistory sh = new ServiceHistory();
			sh.setOperation("Add new service method");
			sh.setId_service(method.getServiceId());
			sh.setDate(new Date());
			shDao.addServiceHistory(sh);
			return true;
		} catch (DataAccessException d) {
			return false;
		}
	}

	/**
	 * Modify an existing service method from database.
	 * Only user being part of organization can modify a service.
	 * @param username : String username of logged in user
	 * @param method : a modified {@link Method} instance
	 * @return boolean value: true if it is ok, false otherwise
	 * @throw SecurityException if user has not a role in service organization
	 */
	@Transactional
	public boolean updateMethod(String username, Method method) {
		try {
			User user = userDao.getUserByUsername(username);
			Service s = getServiceById(method.getServiceId());
			UserRole ur = urDao.getRoleOfUser(user.getId(),
					s.getOrganizationId());
			if (ur == null)
				throw new SecurityException();

			Method m = methodDao.getMethodById(method.getId());
			m.setName(method.getName());
			m.setSynopsis(method.getSynopsis());
			m.setDocumentation(method.getDocumentation());
			m.setTestboxProprieties(method.getTestboxProperties());
			methodDao.modifyMethod(m);
			//Add history
			ServiceHistory sh = new ServiceHistory();
			sh.setOperation("Modify service method");
			sh.setId_service(method.getServiceId());
			sh.setDate(new Date());
			shDao.addServiceHistory(sh);
			return true;
		} catch (DataAccessException d) {
			return false;
		}
	}
	
	/**
	 * Delete a service method from database
	 * @param username : String username of logged user
	 * @param methodId : int id of method that user wants to delete
	 * @return boolean value: true if it is ok, false otherwise
	 * @throw SecurityException if user has not a role in service organization
	 */
	@Transactional
	public boolean deleteMethod(String username, int methodId) {
		try {
			User user = userDao.getUserByUsername(username);

			Method m = methodDao.getMethodById(methodId);
			Service s = getServiceById(m.getServiceId());
			UserRole ur = urDao.getRoleOfUser(user.getId(),
					s.getOrganizationId());
			if (ur == null)
				throw new SecurityException();
			methodDao.deleteMethod(m);
			//Add history
			ServiceHistory sh = new ServiceHistory();
			sh.setOperation("Delete service method");
			sh.setId_service(m.getServiceId());
			sh.setDate(new Date());
			shDao.addServiceHistory(sh);
			return true;
		} catch (DataAccessException d) {
			return false;
		}
	}

	/**
	 * Retrieve service method searching by method id
	 * @param method: id of service method
	 * @return {@link Method} instance
	 */
	@Transactional
	public Method getMethodById(int method) {
		try{
			return methodDao.getMethodById(method);
		}catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * Add a test to an existing service method from database
	 * @param username : String username of user
	 * @param id : int method id
	 * @param testinfo : {@link TestInfo} instance
	 * @return boolean value: true if it is ok, false otherwise
	 * @throw SecurityException if user has not a role in service organization
	 */
	public boolean addTest(String username, int id, TestInfo testinfo) {
		try {
			User user = userDao.getUserByUsername(username);
			Method m = methodDao.getMethodById(id);
			Service s = getServiceById(m.getServiceId());
			UserRole ur = urDao.getRoleOfUser(user.getId(),
					s.getOrganizationId());
			if (ur == null)
				throw new SecurityException();
			TestBoxProperties props = m.getTestboxProperties();
			if (props == null) {
				props = new TestBoxProperties();
				m.setTestboxProprieties(props);
			}
			List<TestInfo> tests = props.getTests();
			if (tests == null) {
				tests = new ArrayList<TestInfo>();
				props.setTests(tests);
			}
			tests.add(testinfo);
			methodDao.modifyMethod(m);
			return true;
		} catch (DataAccessException d) {
			return false;
		}
	}

	/**
	 * Modify an existing test for a service method from database
	 * @param username : String username
	 * @param id : method id
	 * @param pos : position of existing test in array (index)
	 * @param testinfo : {@link TestInfo} instance
	 * @return boolean value: true if it is ok, false otherwise
	 * @throw SecurityException if user has not a role in service organization
	 */
	public boolean modifyTest(String username, int id, int pos, TestInfo testinfo) {
		try {
			User user = userDao.getUserByUsername(username);
			Method m = methodDao.getMethodById(id);
			Service s = getServiceById(m.getServiceId());
			UserRole ur = urDao.getRoleOfUser(user.getId(),
					s.getOrganizationId());
			if (ur == null)
				throw new SecurityException();
			TestBoxProperties props = m.getTestboxProperties();
			List<TestInfo> tests = props.getTests();
			tests.set(pos, testinfo);
			methodDao.modifyMethod(m);
			return true;
		} catch (DataAccessException d) {
			return false;
		}
	}

	/**
	 * Delete an existing test for a service method from database
	 * @param username : String username of logged user
	 * @param id : method id
	 * @param pos : position of existing test in array (index)
	 * @return boolean value: true if it is ok, false otherwise
	 * @throw SecurityException if user has not a role in service organization
	 */
	public boolean deleteTest(String username, int id, int pos) {
		try {
			User user = userDao.getUserByUsername(username);
			Method m = methodDao.getMethodById(id);
			Service s = getServiceById(m.getServiceId());
			UserRole ur = urDao.getRoleOfUser(user.getId(),
					s.getOrganizationId());
			if (ur == null)
				throw new SecurityException();
			TestBoxProperties props = m.getTestboxProperties();
			List<TestInfo> tests = props.getTests();
			tests.remove(pos);
			methodDao.modifyMethod(m);
			return true;
		} catch (DataAccessException d) {
			return false;
		}
	}

}
