/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.openservices.controllers;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.openservices.Constants.SERVICE_STATE;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;
import eu.trentorise.smartcampus.openservices.entities.TestInfo;
import eu.trentorise.smartcampus.openservices.managers.ServiceManager;
import eu.trentorise.smartcampus.openservices.model.Service;

/**
 * Controller that retrieves service data and adds, modifies and deletes them for 
 * authenticated users.
 * 
 * @author Giulia Canobbio
 *
 */
@Controller
@RequestMapping(value="/api/service")
public class ServiceController {

	private static final Logger logger = LoggerFactory
			.getLogger(ServiceController.class);
	/**
	 * {@link ResponseObject} Response object contains requested data, 
	 * status of response and if necessary a custom error message.
	 */
	private ResponseObject responseObject;
	/**
	 * Instance of {@link ServiceManager} to retrieve data using Dao classes.
	 */
	@Autowired
	private ServiceManager serviceManager;
	
	/*
	 * REST WEB SERVICE
	 */
	
	//User - Access my data: service
	/**
	 * Logged user retrieves his/her list of service data.
	 * Service can be published, deprecated and unpublished.
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with list of service data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/my", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject myServices(HttpServletResponse response){
		logger.info("-- User: Access my data service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Service> services = Service.fromServiceEntities(serviceManager.getUserServices(username));
		responseObject = new ResponseObject();
		if(services==null || services.size()==0){
			responseObject.setError("You have zero service");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(services);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	//Service - View Service
	/**
	 * Retrieve all service data for logged user.
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with list of service data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	/*@RequestMapping(value = "/view", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject viewServices(HttpServletResponse response){
		logger.info("-- View Services --");
		List<Service> services = Service.fromServiceEntities(serviceManager.getServices());
		responseObject = new ResponseObject();
		if(services==null || services.size()==0){
			responseObject.setError("There is no available service");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(services);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}*/
	
	//Service - View Service - view service description
	/**
	 * View data of a specific service.
	 * Searching by service id.
	 * @param service_id : int service id
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with service data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/view/description/{service_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject viewServiceDescription(@PathVariable int service_id, HttpServletResponse response){
		logger.info("-- View service description --");
		Service service = Service.fromServiceEntity(serviceManager.getServiceById(service_id));
		responseObject = new ResponseObject();
		if(service==null){
			responseObject.setError("There is no service with this id");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(service);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	//Service - View Service - view service method
	/**
	 * Retrieve all methods for a specific service.
	 * Search is done by service id.
	 * @param service_id : int service id
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with list of method data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/view/method/{service_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject viewServiceMethod(@PathVariable int service_id, HttpServletResponse response){
		logger.info("-- View service method --");
		List<Method> m = serviceManager.getServiceMethodsByServiceId(service_id);
		responseObject = new ResponseObject();
		if(m==null || m.size()==0){
			responseObject.setError("There is no method for this service");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(m);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	//Service - View Service - view service history
	/**
	 * Retrieve all service history data of a specific service.
	 * Search is done by service id.
	 * @param service_id : int service id
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with list of service history data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	/*@RequestMapping(value = "/view/history/{service_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject viewServiceHistory(@PathVariable int service_id, HttpServletResponse response){
		logger.info("-- View service history --");
		List<ServiceHistory> sh = serviceManager.getServiceHistoryByServiceId(service_id);
		responseObject = new ResponseObject();
		if(sh==null || sh.size()==0){
			responseObject.setError("There is no history for this service");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(sh);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}*/
	
	//Service - Manage Service - create Service
	/**
	 * Add a new service to an organization.
	 * User must have role 'organization owner' or 'service owner'.
	 * @param service : {@link Service} service object
	 * @param response : {@link HttpServletResponse} which returns status of response CREATED, BAD REQUEST or 
	 * UNAUTHORIZED
	 * @return {@link ResponseObject} with status (CREATED, BAD REQUEST or UNAUTHORIZED) and 
	 * error message (if status is BAD REQUEST or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public ResponseObject createService(@RequestBody Service service, HttpServletResponse response){
		logger.info("-- Create new service entry --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		//check structure for required field
		responseObject = new ResponseObject();
		if (service.getName() != null && service.getVersion() != null
				&& service.getOrganizationId() != 0) {
			try {
				boolean result = serviceManager
						.createService(username, service.toServiceEntity());
				if (result) {
					responseObject.setStatus(HttpServletResponse.SC_CREATED);
					response.setStatus(HttpServletResponse.SC_CREATED);
				} else {
					responseObject.setError("Organization is not created");
					responseObject
							.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				}
			} catch (SecurityException s) {
				responseObject.setError("You are not allowed to create a new service for this organization");
				responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		else{
			responseObject.setError("Name, version and organizaton id of service are required.");
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return responseObject;
	}
	//Service - Manage Service - modify Service
	/**
	 * Modify an existing service in database.
	 * User must have role 'organization owner' for organization service and can modify the following service fields:
	 * description, tags, category, documentation, access information, expiration, implementation,
	 * license and version.
	 * @param service : {@link Service} object
	 * @param response : {@link HttpServletResponse} which returns status of response OK, SERVICE UNAVAILABLE or 
	 * UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public ResponseObject modService(@RequestBody Service service, HttpServletResponse response){
		logger.info("-- Modify service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		responseObject = new ResponseObject();
		try {
			boolean result = serviceManager.updateService(username, service.toServiceEntity());
			if (result) {
				responseObject.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseObject.setError("Connection problem with database");
				responseObject
						.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		}catch(SecurityException s){
			responseObject.setError("User must be a member of this organizaiton");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return responseObject;
	}
	
	//Service - Manage Service - publish Service (create ServiceHistory.operation)
	/**
	 * Publish a service.
	 * User must have role 'organization owner' for organization service.
	 * @param id : int service id
	 * @param response : {@link HttpServletResponse} which returns status of response OK, SERVICE UNAVAILABLE or 
	 * UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/publish/{id}", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public ResponseObject publishService(@PathVariable int id, HttpServletResponse response){
		logger.info("-- Publish service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		responseObject = new ResponseObject();
		try {
			boolean result = serviceManager.changeState(username, id,
					SERVICE_STATE.PUBLISH.toString());
			if (result) {
				responseObject.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseObject.setError("Connection problem with database");
				responseObject
						.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		}
		catch(SecurityException s){
			responseObject.setError("User must be part of this organization before changing this service");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return responseObject;
	}
	
	//Service - Manage Service - unpublish Service (create ServiceHistory.operation)
	/**
	 * Unpublish a service.
	 * User must have role 'organization owner' for organization service.
	 * @param id : int service id
	 * @param response : {@link HttpServletResponse} which returns status of response OK, SERVICE UNAVAILABLE or 
	 * UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/unpublish/{id}", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public ResponseObject unpublishService(@PathVariable int id, HttpServletResponse response){
		logger.info("-- Unpublish service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		responseObject = new ResponseObject();
		try {
			boolean result = serviceManager.changeState(username, id,SERVICE_STATE.UNPUBLISH.toString());
			if (result) {
				responseObject.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseObject.setError("Connection problem with database");
				responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		} catch (SecurityException s) {
			responseObject.setError("User must be part of this organization before changing this service");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return responseObject;
	}
	
	//Service - Manage Service - deprecate Service (create ServiceHistory.operation)
	/**
	 * Deprecate a service.
	 * User must have role 'organization owner' for organization service.
	 * @param id : int service id
	 * @param response : {@link HttpServletResponse} which returns status of response OK, SERVICE UNAVAILABLE or 
	 * UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/deprecate/{id}", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public ResponseObject deprecateService(@PathVariable int id, HttpServletResponse response){
		logger.info("-- Deprecate service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		responseObject = new ResponseObject();
		try {
			boolean result = serviceManager.changeState(username, id,SERVICE_STATE.DEPRECATE.toString());
			if (result) {
				responseObject.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseObject.setError("Connection problem with database");
				responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		} catch (SecurityException s) {
			responseObject.setError("User must be part of this organization before changing this service");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return responseObject;
	}
	
	//Service - Manage Service - deprecate Service (create ServiceHistory.operation)
	/**
	 * Delete an existing service.
	 * User must have role 'organization owner' for service organization.
	 * @param id : int service id
	 * @param response : {@link HttpServletResponse} which returns status of response OK, SERVICE UNAVAILABLE or 
	 * UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE) 
	@ResponseBody
	public ResponseObject deleteService(@PathVariable int id, HttpServletResponse response){
		logger.info("-- Delete service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		responseObject = new ResponseObject();
		try {
			boolean result = serviceManager.deleteService(username, id);
			if (result) {
				responseObject.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseObject.setError("Connection problem with database");
				responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		} catch (SecurityException s) {
			responseObject.setError("User must be part of this organization before deleting this service");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return responseObject;
	}
	
	
	//Service - Manage Service method - create Method (create ServiceHistory.operation)
	/**
	 * Add a new method to a service.
	 * User must have role 'organization owner' for organization service.
	 * @param method : {@link Method} instance
	 * @param response : {@link HttpServletResponse} which returns status of response CREATED, SERVICE UNAVAILABLE or 
	 * UNAUTHORIZED
	 * @return {@link ResponseObject} with status (CREATED, SERVICE UNAVAILABLE, BAD REQUEST or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE, BAD REQUEST or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/method/add", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public ResponseObject createMethod(@RequestBody Method method, HttpServletResponse response){
		logger.info("-- Create new service method --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		responseObject = new ResponseObject();
		try {
			boolean result = serviceManager.addMethod(username, method);
			if (result) {
				responseObject.setStatus(HttpServletResponse.SC_CREATED);
				response.setStatus(HttpServletResponse.SC_CREATED);
			} else {
				responseObject.setError("Connection problem with database");
				responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		} catch (SecurityException s) {
			responseObject.setError("User must be part of this organization before adding a method to this service");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch(EntityExistsException e){
			responseObject.setError("Method with specified name already exists. Please change it.");
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return responseObject;
	}
	
	//Service - Manage Service method - modify Method (create ServiceHistory.operation)
	/**
	 * Modify a service method.
	 * User must have role 'organization owner' for organization service and can modify only the following fields:
	 * name, synopsis, test, documentation.
	 * @param method : {@link Method} instance
	 * @param response : {@link HttpServletResponse} which returns status of response OK, SERVICE UNAVAILABLE or 
	 * UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/method/modify", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public ResponseObject modifyMethod(@RequestBody Method method, HttpServletResponse response){
		logger.info("-- Modify a service method --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		responseObject = new ResponseObject();
		try {
			boolean result = serviceManager.updateMethod(username, method);
			if (result) {
				responseObject.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseObject.setError("Connection problem with database");
				responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		} catch (SecurityException s) {
			responseObject.setError("User must be part of this organization before modifying a method to this service");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch(EntityExistsException e){
			responseObject.setError("Another method with specified name already exists. Please change it.");
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return responseObject;
	}
	
	/**
	 * Retrieves method data of a specific method.
	 * Searching by method id.
	 * @param method_id : int method id
	 * @return {@link ResponseObject} with method data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/method/{method_id}", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseObject getMethodData(@PathVariable int method_id, HttpServletResponse response){
		responseObject = new ResponseObject();
		Method method = serviceManager.getMethodById(method_id);
		if(method!=null){
			responseObject.setData(method);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No method with this id");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}
	
	//Service - Manage Service method - delete method (create ServiceHistory.operation)
	/**
	 * Delete a service method from a service.
	 * User must have role 'organization owner' for organization service.
	 * @param id : int method id
	 * @param response : {@link HttpServletResponse} which returns status of response OK, SERVICE UNAVAILABLE or 
	 * UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/method/delete/{id}", method = RequestMethod.DELETE) 
	@ResponseBody
	public ResponseObject deleteMethod(@PathVariable int id, HttpServletResponse response){
		logger.info("-- Delete a service method --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		responseObject = new ResponseObject();
		try {
			boolean result = serviceManager.deleteMethod(username, id);
			if (result) {
				responseObject.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseObject.setError("Connection problem with database");
				responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		} catch (SecurityException s) {
			responseObject.setError("User must be part of this organization before deleting a method to this service");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return responseObject;
	}
	
	/**
	 * Add a test to a service method.
	 * User must have role 'organization owner' for organization service.
	 * @param testinfo : {@link TestInfo} instance
	 * @param id : int method id
	 * @param response : {@link HttpServletResponse} which returns status of response OK, SERVICE UNAVAILABLE or 
	 * UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/method/{id}/test/add", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public ResponseObject createTest(@RequestBody TestInfo testinfo, @PathVariable int id, HttpServletResponse response){
		logger.info("-- Create new method test --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		responseObject = new ResponseObject();
		try {
			boolean result = serviceManager.addTest(username, id, testinfo);
			if (result) {
				responseObject.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseObject.setError("Connection problem with database");
				responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		} catch (SecurityException s) {
			responseObject.setError("User must be part of this organization before adding a test for this service method");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return responseObject;
	}

	/**
	 * Update a test to a service method.
	 * User must have role 'organization owner' for organization service and can modify all test information.
	 * @param testinfo : {@link TestInfo} instance
	 * @param id : int method id
	 * @param pos : int index of test that user wants to update
	 * @param response : {@link HttpServletResponse} which returns status of response OK, SERVICE UNAVAILABLE or 
	 * UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/method/{id}/test/{pos}", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public ResponseObject updateTest(@RequestBody TestInfo testinfo, @PathVariable int id, @PathVariable int pos,
			HttpServletResponse response){
		logger.info("-- Update method test --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		responseObject = new ResponseObject();
		try {
			boolean result = serviceManager.modifyTest(username, id, pos, testinfo);
			if (result) {
				responseObject.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseObject.setError("Connection problem with database");
				responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		} catch (SecurityException s) {
			responseObject.setError("User must be part of this organization before modifying test for this method service");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return responseObject;
	}

	/**
	 * Delete a test from a service method.
	 * User must have role 'organization owner' for organization service.
	 * @param id : int method id
	 * @param pos : int index of test that user wants to delete
	 * @param response : {@link HttpServletResponse} which returns status of response OK, SERVICE UNAVAILABLE or 
	 * UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/method/{id}/test/{pos}", method = RequestMethod.DELETE) 
	@ResponseBody
	public ResponseObject deleteTest(@PathVariable int id, @PathVariable int pos, HttpServletResponse response){
		logger.info("-- Delete method test --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		responseObject = new ResponseObject();
		try {
			boolean result = serviceManager.deleteTest(username, id, pos);
			if (result) {
				responseObject.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseObject.setError("Connection problem with database");
				responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		} catch (SecurityException s) {
			responseObject.setError("User must be part of this organization before deleting test for this method service");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return responseObject;
	}

}
