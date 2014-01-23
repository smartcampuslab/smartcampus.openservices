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

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.openservices.Constants.SERVICE_STATE;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;
import eu.trentorise.smartcampus.openservices.entities.TestInfo;
import eu.trentorise.smartcampus.openservices.managers.ServiceManager;
import eu.trentorise.smartcampus.openservices.support.ListMethod;
import eu.trentorise.smartcampus.openservices.support.ListService;
import eu.trentorise.smartcampus.openservices.support.ListServiceHistory;

@Controller
@RequestMapping(value="/api/service")
public class ServiceController {

	private static final Logger logger = LoggerFactory
			.getLogger(ServiceController.class);

	@Autowired
	private ServiceManager serviceManager;
	
	/*
	 * REST WEB SERVICE
	 */
	
	//User - Access my data: service
	/**
	 * Access my data
	 * @param user_id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/my", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListService myServices(HttpServletResponse response) throws IOException{
		logger.info("-- User: Access my data service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		ListService lserv = new ListService();
		List<Service> services = serviceManager.getUserServices(username);
		lserv.setServices(services);
		if(services==null || services.size()==0){
			response.getWriter().println("No service for this user");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return lserv;
	}
	
	//Service - View Service
	/**
	 * View services
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListService viewServices(HttpServletResponse response) throws IOException{
		logger.info("-- View Services --");
		ListService lserv = new ListService();
		List<Service> services = serviceManager.getServices();
		lserv.setServices(services);
		if(services==null || services.size()==0){
			response.getWriter().println("No service availables");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return lserv;
	}
	
	//Service - View Service - view service description
	/**
	 * View description of service
	 * @param service_id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/view/description/{service_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public Service viewServiceDescription(@PathVariable int service_id, HttpServletResponse response) throws IOException{
		logger.info("-- View service description --");
		Service service = serviceManager.getServiceById(service_id);
		if(service==null){
			response.getWriter().println("No service with this id");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return service;
	}
	
	//Service - View Service - view service method
	/**
	 * View service methods
	 * @param service_id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/view/method/{service_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListMethod viewServiceMethod(@PathVariable int service_id, HttpServletResponse response) throws IOException{
		logger.info("-- View service method --");
		ListMethod lmethod = new ListMethod();
		List<Method> m = serviceManager.getServiceMethodsByServiceId(service_id);
		lmethod.setMethods(m);
		if(m==null || m.size()==0){
			response.getWriter().println("No methods for this service");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return lmethod;
	}
	
	//Service - View Service - view service history
	/**
	 * View service history
	 * @param service_id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/view/history/{service_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListServiceHistory viewServiceHistory(@PathVariable int service_id, HttpServletResponse response) throws IOException{
		logger.info("-- View service history --");
		ListServiceHistory lsh = new ListServiceHistory();
		List<ServiceHistory> sh = serviceManager.getServiceHistoryByServiceId(service_id);
		lsh.setLserviceh(sh);
		if(sh==null || sh.size()==0){
			response.getWriter().println("No history for this service or this service does not exist");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return lsh;
	}
	
	//Service - Manage Service - create Service
	/**
	 * Add new service to an organization
	 * @param service
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public void createService(@RequestBody Service service, HttpServletResponse response) throws IOException{
		logger.info("-- Create new service entry --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		//check structure for required field
		if(service.getName()!=null && service.getVersion()!=null && service.getOrganizationId()!=0){
		boolean result = serviceManager.createService(username, service);
		if(result){
			response.setStatus(HttpServletResponse.SC_CREATED);
		}
		else{
			response.getWriter().println("User has not role ORG_OWNER for Organization OR " +
					"Organization is not created");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		}
		else{
			response.getWriter().println("Name, version and organizaton id of service are required.");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	//Service - Manage Service - modify Service
	/**
	 * Modify a service
	 * User must be service_owner
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public void modService(@RequestBody Service service, HttpServletResponse response){
		logger.info("-- Modify service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.updateService(username,service);
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	//Service - Manage Service - publish Service (create ServiceHistory.operation)
	/**
	 * Publish a service
	 * User must be service owner
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/publish/{id}", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public void publishService(@PathVariable int id, HttpServletResponse response){
		logger.info("-- Publish service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.changeState(username, id, SERVICE_STATE.PUBLISH.toString());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	//Service - Manage Service - unpublish Service (create ServiceHistory.operation)
	/**
	 * Unpublish service
	 * User must be service owner
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/unpublish/{id}", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public void unpublishService(@PathVariable int id, HttpServletResponse response){
		logger.info("-- Unpublish service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.changeState(username, id,SERVICE_STATE.UNPUBLISH.toString());
		//Change service state
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	//Service - Manage Service - deprecate Service (create ServiceHistory.operation)
	/**
	 * Deprecate a service
	 * User must be service owner
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/deprecate/{id}", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public void deprecateService(@PathVariable int id, HttpServletResponse response){
		logger.info("-- Deprecate service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.changeState(username, id,SERVICE_STATE.DEPRECATE.toString());
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	//Service - Manage Service - deprecate Service (create ServiceHistory.operation)
	/**
	 * Deprecate a service
	 * User must be service owner
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE) 
	@ResponseBody
	public void deleteService(@PathVariable int id, HttpServletResponse response){
		logger.info("-- Delete service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.deleteService(username, id);
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	
	//Service - Manage Service method - create Method (create ServiceHistory.operation)
	/**
	 * Add service method to a service
	 * User must be service owner
	 * @param method
	 * @return
	 */
	@RequestMapping(value = "/method/add", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public void createMethod(@RequestBody Method method, HttpServletResponse response){
		logger.info("-- Create new service method --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.addMethod(username, method);
		response.setStatus(HttpServletResponse.SC_CREATED);
	}
	
	//Service - Manage Service method - modify Method (create ServiceHistory.operation)
	/**
	 * Modify a service method
	 * User must be service owner
	 * @param method
	 * @return
	 */
	@RequestMapping(value = "/method/modify", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public void modifyMethod(@RequestBody Method method, HttpServletResponse response){
		logger.info("-- Modify a service method --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.updateMethod(username, method);
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	//Service - Manage Service method - delete method (create ServiceHistory.operation)
	/**
	 * Delete service method
	 * User must be service owner
	 * @param method
	 * @return
	 */
	@RequestMapping(value = "/method/delete/{id}", method = RequestMethod.DELETE) 
	@ResponseBody
	public void deleteMethod(@PathVariable int id, HttpServletResponse response){
		logger.info("-- Delete a service method --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.deleteMethod(username, id);
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	/**
	 * Add a test to a service method 
	 * User must be service owner
	 * @param method
	 * @return
	 */
	@RequestMapping(value = "/method/{id}/test/add", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public void createTest(@RequestBody TestInfo testinfo, @PathVariable int id, HttpServletResponse response){
		logger.info("-- Create new method test --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.addTest(username, id, testinfo);
		response.setStatus(HttpServletResponse.SC_CREATED);
	}

	/**
	 * Modify a test of the service method 
	 * User must be service owner
	 * @param method
	 * @return
	 */
	@RequestMapping(value = "/method/{id}/test/{pos}", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public void updateTest(@RequestBody TestInfo testinfo, @PathVariable int id, @PathVariable int pos,
			HttpServletResponse response){
		logger.info("-- Update method test --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.modifyTest(username, id, pos, testinfo);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * Add service method to a service
	 * User must be service owner
	 * @param method
	 * @return
	 */
	@RequestMapping(value = "/method/{id}/test/{pos}", method = RequestMethod.DELETE) 
	@ResponseBody
	public void deleteTest(@PathVariable int id, @PathVariable int pos, HttpServletResponse response){
		logger.info("-- Delete method test --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.deleteTest(username, id, pos);
		response.setStatus(HttpServletResponse.SC_OK);
	}

}
