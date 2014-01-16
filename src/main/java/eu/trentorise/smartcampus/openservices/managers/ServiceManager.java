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

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.dao.MethodDao;
import eu.trentorise.smartcampus.openservices.dao.OrganizationDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceHistoryDao;
import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.dao.UserRoleDao;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.entities.UserRole;

/**
 * @author raman
 *
 */
@Component
@Transactional
public class ServiceManager {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleDao urDao;
	@Autowired
	private OrganizationDao orgDao;
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private ServiceHistoryDao shDao;
	@Autowired
	private MethodDao methodDao;

	/**
	 * Create service
	 * @param username
	 * @param service
	 */
	@Transactional
	public void createService(String username, Service service) {
		User user = userDao.getUserByUsername(username);
		UserRole ur = urDao.getRoleOfUser(user.getId(), service.getOrganizationId());
		if (ur == null) throw new SecurityException();
		service.setCreatorId(user.getId());
		service.setState("UNPUBLISH");
		serviceDao.createService(service);
		// create history
		ServiceHistory sh = new ServiceHistory();
		sh.setOperation("service added");
		sh.setId_service(service.getId());
		sh.setDate(new Date());
		shDao.addServiceHistory(sh);
	}

	/**
	 * @param username
	 * @param service
	 */
	@Transactional
	public void updateService(String username, Service service) {
		User user = userDao.getUserByUsername(username);

		UserRole ur = urDao.getRoleOfUser(user.getId(), service.getOrganizationId());
		if (ur == null) throw new SecurityException();

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
		s.setState(service.getState());
		s.setVersion(service.getVersion());
		serviceDao.modifyService(s);

		//Add a new ServiceHistory
		ServiceHistory sh = new ServiceHistory();
		sh.setOperation("Modify service");
		sh.setId_service(s.getId());
		sh.setDate(new Date());
		shDao.addServiceHistory(sh);
		
	}

	/**
	 * Make the service published
	 * @param service
	 */
	@Transactional
	public void changeState(String username, int serviceId, String state) {
		User user = userDao.getUserByUsername(username);
		Service service = serviceDao.getServiceById(serviceId);

		UserRole ur = urDao.getRoleOfUser(user.getId(), service.getOrganizationId());
		if (ur == null) throw new SecurityException();

		//Change service state
		service.setState(state);
		serviceDao.modifyService(service);
		//add service history
		ServiceHistory sh = new ServiceHistory();
		sh.setOperation(state + " service");
		sh.setId_service(service.getId());
		sh.setDate(new Date());
		shDao.addServiceHistory(sh);
	}

	/**
	 * @param username
	 * @param id
	 */
	public void deleteService(String username, int id) {
		User user = userDao.getUserByUsername(username);
		Service service = serviceDao.getServiceById(id);
		UserRole ur = urDao.getRoleOfUser(user.getId(), service.getOrganizationId());
		if (ur == null) throw new SecurityException();
		serviceDao.deleteService(service);
		//add service history
		ServiceHistory sh = new ServiceHistory();
		sh.setOperation("delete service");
		sh.setId_service(service.getId());
		sh.setDate(new Date());
		shDao.addServiceHistory(sh);
		
	}

	/**
	 * @return all {@link Service} instances
	 */
	@Transactional
	public List<Service> getServices() {
		return serviceDao.showService();
	}

	/**
	 * @param username
	 * @return all the {@link Service} instances the user can see/modify (i.e., all
	 * the services of the organizations the user is member of)
	 */
	@Transactional
	public List<Service> getUserServices(String username) {
		return serviceDao.showMyService(username);
	}

	/**
	 * @param service_id
	 * @return {@link Service} instance with the specified ID
	 */
	@Transactional
	public Service getServiceById(int service_id) {
		return serviceDao.getServiceById(service_id);
	}

	/**
	 * @param service_id
	 * @return all the {@link Method} instances of the specified service
	 */
	@Transactional
	public List<Method> getServiceMethodsByServiceId(int service_id) {
		return methodDao.getMethodByServiceId(service_id);
	}

	/**
	 * @param service_id
	 * @return {@link ServiceHistory} instances of the specified service
	 */
	@Transactional
	public List<ServiceHistory> getServiceHistoryByServiceId(int service_id) {
		return shDao.getServiceHistoryByServiceId(service_id);
	}

	/**
	 * Create new service method
	 * @param username
	 * @param method
	 */
	@Transactional
	public void addMethod(String username, Method method) {
		User user = userDao.getUserByUsername(username);
		Service s = getServiceById(method.getServiceId());
		UserRole ur = urDao.getRoleOfUser(user.getId(), s.getOrganizationId());
		if (ur == null) throw new SecurityException();
		methodDao.addMethod(method);
	}

	/**
	 * Modify existing method
	 * @param username
	 * @param method
	 */
	@Transactional
	public void updateMethod(String username, Method method) {
		User user = userDao.getUserByUsername(username);
		Service s = getServiceById(method.getServiceId());
		UserRole ur = urDao.getRoleOfUser(user.getId(), s.getOrganizationId());
		if (ur == null) throw new SecurityException();

		Method m = methodDao.getMethodById(method.getId());
		m.setName(method.getName());
		m.setSynopsis(method.getSynopsis());
		m.setDocumentation(method.getDocumentation());
		m.setTestboxProprieties(method.getTestboxProperties());
		methodDao.modifyMethod(m);
	}
	
	/**
	 * Create new service method
	 * @param username
	 * @param method
	 */
	@Transactional
	public void deleteMethod(String username, int methodId) {
		User user = userDao.getUserByUsername(username);

		Method m = methodDao.getMethodById(methodId);
		Service s = getServiceById(m.getServiceId());
		UserRole ur = urDao.getRoleOfUser(user.getId(), s.getOrganizationId());
		if (ur == null) throw new SecurityException();
		methodDao.deleteMethod(m);
	}

}
