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

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import eu.trentorise.smartcampus.openservices.entities.*;
import eu.trentorise.smartcampus.openservices.managers.CatalogManager;
import eu.trentorise.smartcampus.openservices.support.*;

@Controller
@RequestMapping(value="/api/catalog")
public class CatalogController {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);
	private ResponseObject responseObject;
	@Autowired
	private CatalogManager catalogManager;
	
	
	/**
	 * Show all services in catalog which are published.
	 * @return
	 */
	@RequestMapping(value="/service", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject catalogServices(HttpServletResponse response){
		logger.info("-- Service Catalog Publish Service --");
		//ListService lserv = new ListService();
		List<Service> services = catalogManager.catalogServices();
		//lserv.setServices(catalogManager.catalogServices());
		responseObject = new ResponseObject();
		if(services==null || services.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No service available");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}else{
			responseObject.setData(services);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		
		return responseObject;
	}
	
	/**
	 * Get data of a service
	 * @param service_id
	 * @return
	 */
	@RequestMapping(value="/service/{service_id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject cataogServiceById(@PathVariable int service_id, HttpServletResponse response){
		logger.info("-- Service Catalog Service By Id--");
		Service service = catalogManager.catalogServiceById(service_id);
		responseObject=new ResponseObject();
		if(service==null){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No published service with this id");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(service);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		//return service;
		return responseObject;
	}
	
	/**
	 * Return methods of a service.
	 * Search by service id
	 * @param service_id
	 * @return
	 */
	@RequestMapping(value="/service/methods/{service_id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject cataogServiceMethods(@PathVariable int service_id, HttpServletResponse response){
		logger.info("-- Service Catalog Show Methods --");
		//ListMethod lmeth = new ListMethod();
		List<Method> methods = catalogManager.catalogServiceMethods(service_id);
		//lmeth.setMethods(methods);
		responseObject=new ResponseObject();
		if(methods==null || methods.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No methods for this published service");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(methods);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		//return lmeth;
		return responseObject;
	}
	
	/**
	 * Return history of a service.
	 * Search by service id
	 * @param service_id
	 * @return
	 */
	@RequestMapping(value="/service/history/{service_id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject cataogServiceHistory(@PathVariable int service_id, HttpServletResponse response){
		logger.info("-- Service Catalog Show Methods --");
		//ListServiceHistory lsh = new ListServiceHistory();
		List<ServiceHistory> history = catalogManager.catalogServiceHistory(service_id);
		//lsh.setLserviceh(history);
		responseObject=new ResponseObject();
		if(history==null || history.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No service history for this published service");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(history);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		//return lsh;
		return responseObject;
	}
	
	/**
	 * Simple search in service catalog
	 * Catalog shows publish services
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/service/search/{token}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogServiceSimpleSearch(@PathVariable String token, HttpServletResponse response){
		logger.info("-- Service Catalog simple search --");
		//ListService lserv = new ListService();
		List<Service> services = catalogManager.catalogServiceSimpleSearch(token);
		//lserv.setServices(services);
		responseObject=new ResponseObject();
		if(services==null || services.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No published service for this search by name");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(services);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		//return lserv;
		return responseObject;
	}
	
	/**
	 * Browse service in catalog by category
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/service/browse/category/{category}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogServiceBrowseByCategory(@PathVariable int category, HttpServletResponse response){
		logger.info("-- Service Catalog browse (category) --");
		//ListService lserv = new ListService();
		List<Service> services = catalogManager.catalogServiceBrowseByCategory(category);
		//lserv.setServices(services);
		responseObject=new ResponseObject();
		if(services==null || services.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No published service for this category");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(services);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}

	/**
	 * Browse service in catalog by org
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/service/browse/org/{org}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogServiceBrowseByOrg(@PathVariable int org, HttpServletResponse response){
		logger.info("-- Service Catalog browse (org) --");
		//ListService lserv = new ListService();
		List<Service> services = catalogManager.catalogServiceBrowseByOrg(org);
		//lserv.setServices(services);
		responseObject=new ResponseObject();
		if(services==null || services.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No service for this organization");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(services);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}

	/**
	 * Browse service in catalog by tags
	 * @param tags
	 * @return
	 */
	@RequestMapping(value = "/service/browse/tags/{tags}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogServiceBrowseByTags(@PathVariable String tags, HttpServletResponse response) {
		logger.info("-- Service Catalog browse (category) --");
		//ListService lserv = new ListService();
		List<Service> services = catalogManager.catalogServiceBrowseByTags(tags);
		//lserv.setServices(services);
		responseObject = new ResponseObject();
		if(services==null || services.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No service for this search by tags");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(services);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	//browse catalog using filter (by protocols) - TO DEFINE - TODO

	//view most used - depends on App - TODO
	public ListService catalogServiceMostUsed(){
		logger.info("-- Service Catalog most used --");
		return null;
	}
	
	/**
	 * Get all organization
	 * @return
	 */
	@RequestMapping(value="/org", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject catalogOrg(HttpServletResponse response){
		logger.info("-- Organization Catalog data --");
		//ListOrganization lorg = new ListOrganization();
		List<Organization> orgs = catalogManager.catalogOrg();
		//lorg.setOrgs(orgs);
		responseObject = new ResponseObject();
		if(orgs==null || orgs.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No organization available");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(orgs);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	/**
	 * Get organization
	 * @return
	 */
	@RequestMapping(value="/org/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject catalogOrg(@PathVariable int id, HttpServletResponse response){
		logger.info("-- Organization Catalog data --");
		Organization org = catalogManager.catalogOrgById(id);
		responseObject = new ResponseObject();
		if(org==null){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No organization with this id");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(org);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	
	/**
	 * Simple search in organization catalog
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/org/search/{token}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogOrgSimpleSearch(@PathVariable String token, HttpServletResponse response){
		logger.info("-- Organization Catalog simple search --");
		//ListOrganization lserv = new ListOrganization();
		List<Organization> orgs = catalogManager.catalogOrgSimpleSearch(token);
		//lserv.setOrgs(orgs);
		responseObject = new ResponseObject();
		if(orgs==null || orgs.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No organization for this search by name");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(orgs);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	/**
	 * Browse organization by category
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/org/browse/category/{category}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogOrgBrowse(@PathVariable int category, HttpServletResponse response){
		logger.info("-- Organization Catalog browse --");
		//ListOrganization lserv = new ListOrganization();
		List<Organization> orgs = catalogManager.catalogOrgBrowse(category);
		//lserv.setOrgs(orgs);
		responseObject = new ResponseObject();
		if(orgs==null || orgs.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No organization for this search by category");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(orgs);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	//browse catalog using filters (by geography) - when add address of organization - TODO
	
	@RequestMapping(value = "/service/browse/category", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogCategoryServices(HttpServletResponse response) {
		logger.info("-- Category Catalog browse --");
		CategoryServices cat = catalogManager.getCategoryServices();
		responseObject = new ResponseObject();
		if(cat==null){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No services and categories");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(cat);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
}
