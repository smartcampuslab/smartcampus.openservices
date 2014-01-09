package com.openserviceproject.openservice;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.openserviceproject.dao.*;
import com.openserviceproject.entities.*;
import com.openserviceproject.support.ListService;
import com.openserviceproject.support.ListServiceHistory;

@Controller
@RequestMapping(value="/api/service")
public class ServiceController {

	private static final Logger logger = LoggerFactory
			.getLogger(ServiceController.class);

	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private UserRoleDao urDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private OrganizationDao orgDao;
	@Autowired
	private MethodDao methodDao;
	@Autowired
	private ServiceHistoryDao shDao;
	
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
		User user = userDao.getUserByUsername(username);
		ListService lserv = new ListService();
		List<Service> s = serviceDao.getServiceByIdOwner(user.getId());
		lserv.setServices(s);
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
		List<Service> s = serviceDao.showService();
		lserv.setServices(s);
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
		Service service = serviceDao.getServiceById(service_id);
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
		List<Method> m = methodDao.getMethodByServiceId(service_id);
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
		List<ServiceHistory> sh = shDao.getServiceHistoryByServiceId(service_id);
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
		service.setState("UNPUBLISH");
		serviceDao.createService(service);
		//get service id from db
		Service newservice = serviceDao.useService(service.getName());
		//add UserRole ROLE_SERVICEOWNER if user has not ROLE_ORGOWNER TODO
		//create ServiceHistory
		ServiceHistory sh = new ServiceHistory();
		sh.setOperation("service added");
		sh.setId_service(newservice.getId());
		sh.setDate(new Date());
		shDao.addServiceHistory(sh);
		return HttpStatus.CREATED;
	}
	//Service - Manage Service - modify Service
	/**
	 * Modify a service
	 * User must be service_owner
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public HttpStatus modService(@RequestBody Service service){
		logger.info("-- Modify service --");
		//check UserRole ROLE_SERVICEOWNER if user has not ROLE_ORGOWNER TODO
		Service s = serviceDao.getServiceById(service.getId());
		s.setDescription(service.getDescription());
		s.setTags(service.getTags());
		s.setCategory(service.getCategory());
		s.setDocumentation(service.getDocumentation());
		serviceDao.modifyService(s);
		//Add a new ServiceHistory
		ServiceHistory sh = new ServiceHistory();
		sh.setOperation("Modify service");
		sh.setId_service(s.getId());
		sh.setDate(new Date());
		shDao.addServiceHistory(sh);
		return HttpStatus.OK;
	}
	
	//Service - Manage Service - publish Service (create ServiceHistory.operation)
	/**
	 * Publish a service
	 * User must be service owner
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/publish", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public HttpStatus publishService(@RequestBody Service service){
		logger.info("-- Publish service --");
		//Change service state
		service.setState("PUBLISH");
		serviceDao.modifyService(service);
		//add service history
		ServiceHistory sh = new ServiceHistory();
		sh.setOperation("Publish service");
		sh.setId_service(service.getId());
		sh.setDate(new Date());
		shDao.addServiceHistory(sh);
		return HttpStatus.OK;
	}
	
	//Service - Manage Service - unpublish Service (create ServiceHistory.operation)
	/**
	 * Unpublish service
	 * User must be service owner
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/unpublish", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public HttpStatus unpublishService(@RequestBody Service service){
		logger.info("-- Unpublish service --");
		//Change service state
		service.setState("UNPUBLISH");
		serviceDao.modifyService(service);
		// Add a new ServiceHistory
		ServiceHistory sh = new ServiceHistory();
		sh.setOperation("Unpublish service");
		sh.setId_service(service.getId());
		sh.setDate(new Date());
		shDao.addServiceHistory(sh);
		return HttpStatus.OK;
	}
	
	//Service - Manage Service - deprecate Service (create ServiceHistory.operation)
	/**
	 * Deprecate a service
	 * User must be service owner
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/deprecate", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public HttpStatus deprecateService(@RequestBody Service service){
		logger.info("-- Deprecate service --");
		//Change service state
		service.setState("DEPRECATE");
		serviceDao.modifyService(service);
		// Add a new ServiceHistory
		ServiceHistory sh = new ServiceHistory();
		sh.setOperation("Deprecate service");
		sh.setId_service(service.getId());
		sh.setDate(new Date());
		shDao.addServiceHistory(sh);
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
		methodDao.addMethod(method);
		return HttpStatus.CREATED;
	}
	
	//Service - Manage Service method - modify Method (create ServiceHistory.operation)
	/**
	 * Modify a service method
	 * User must be service owner
	 * @param method
	 * @return
	 */
	@RequestMapping(value = "/method/modify", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public HttpStatus modifyMethod(@RequestBody Method method){
		logger.info("-- Modify a service method --");
		Method m = methodDao.getMethodById(method.getId());
		m.setName(method.getName());
		m.setSynopsis(method.getSynopsis());
		m.setDocumentation(method.getDocumentation());
		m.setTestboxProprieties(method.getTestboxProperties());
		methodDao.modifyMethod(m);
		return HttpStatus.OK;
	}
	
	//Service - Manage Service method - delete method (create ServiceHistory.operation)
	/**
	 * Delete service method
	 * User must be service owner
	 * @param method
	 * @return
	 */
	@RequestMapping(value = "/method/delete", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public HttpStatus deleteMethod(@RequestBody Method method){
		logger.info("-- Delete a service method --");
		methodDao.deleteMethod(method);
		return HttpStatus.OK;
	}
	//Service - Manage Service method - copy method(s) (create ServiceHistory.operation)
	/**
	 * Copy a service method to another service
	 * @param method
	 * @param service
	 * @return
	 */
	@RequestMapping(value = "/method/copy", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public ListMethod copyMethod(@RequestBody Method method, @RequestBody Service service){
		logger.info("-- Copy a service method --");
		//copy a service-method in another service
		method.setId_service(service.getId());
		//TODO remember to change method name, because in DB it is a unique index
		methodDao.addMethod(method);
		List<Method> methods = methodDao.getMethodByServiceId(service.getId());
		ListMethod lmethod = new ListMethod();
		lmethod.setMethods(methods);
		return lmethod;
	}
}
