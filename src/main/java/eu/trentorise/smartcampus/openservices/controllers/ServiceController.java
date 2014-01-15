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

import eu.trentorise.smartcampus.openservices.entities.ListMethod;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;
import eu.trentorise.smartcampus.openservices.managers.ServiceManager;
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
	 */
	@RequestMapping(value = "/my", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListService myServices(){
		logger.info("-- User: Access my data service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		ListService lserv = new ListService();
		lserv.setServices(serviceManager.getUserServices(username));
		return lserv;
	}
	
	//Service - View Service
	/**
	 * View services
	 * @return
	 */
	@RequestMapping(value = "/view", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListService viewServices(){
		logger.info("-- View Services --");
		ListService lserv = new ListService();
		lserv.setServices(serviceManager.getServices());
		return lserv;
	}
	
	//Service - View Service - view service description
	/**
	 * View description of service
	 * @param service_id
	 * @return
	 */
	@RequestMapping(value = "/view/description/{service_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public Service viewServiceDescription(@PathVariable int service_id){
		logger.info("-- View service description --");
		Service service = serviceManager.getServiceById(service_id);
		return service;
	}
	
	//Service - View Service - view service method
	/**
	 * View service methods
	 * @param service_id
	 * @return
	 */
	@RequestMapping(value = "/view/method/{service_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListMethod viewServiceMethod(@PathVariable int service_id){
		logger.info("-- View service method --");
		ListMethod lmethod = new ListMethod();
		List<Method> m = serviceManager.getServiceMethodsByServiceId(service_id);
		lmethod.setMethods(m);
		return lmethod;
	}
	
	//Service - View Service - view service history
	/**
	 * View service history
	 * @param service_id
	 * @return
	 */
	@RequestMapping(value = "/view/history/{service_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListServiceHistory viewServiceHistory(@PathVariable int service_id){
		logger.info("-- View service history --");
		ListServiceHistory lsh = new ListServiceHistory();
		List<ServiceHistory> sh = serviceManager.getServiceHistoryByServiceId(service_id);
		lsh.setLserviceh(sh);
		return lsh;
	}
	
	//Service - Manage Service - create Service
	/**
	 * Add new service to an organization
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public HttpStatus createService(@RequestBody Service service){
		logger.info("-- Create new service entry --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.createService(username, service);
		return HttpStatus.CREATED;
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
	public HttpStatus modService(@RequestBody Service service){
		logger.info("-- Modify service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.updateService(username,service);
		return HttpStatus.OK;
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
	public HttpStatus publishService(@PathVariable int id){
		logger.info("-- Publish service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.changeState(username, id, "PUBLISH");
		return HttpStatus.OK;
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
	public HttpStatus unpublishService(@PathVariable int id){
		logger.info("-- Unpublish service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.changeState(username, id,"UNPUBLISH");
		//Change service state
		return HttpStatus.OK;
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
	public HttpStatus deprecateService(@PathVariable int id){
		logger.info("-- Deprecate service --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.changeState(username, id,"DEPRECATE");
		return HttpStatus.OK;
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
	public HttpStatus createMethod(@RequestBody Method method){
		logger.info("-- Create new service method --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.addMethod(username, method);
		return HttpStatus.CREATED;
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
	public HttpStatus modifyMethod(@RequestBody Method method){
		logger.info("-- Modify a service method --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.updateMethod(username, method);
		return HttpStatus.OK;
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
	public HttpStatus deleteMethod(@PathVariable int id){
		logger.info("-- Delete a service method --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		serviceManager.deleteMethod(username, id);
		return HttpStatus.OK;
	}
}
