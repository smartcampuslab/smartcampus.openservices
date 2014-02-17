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

/**
 * Catalog, a restful web services that retrieves data about Organization or Services 
 * for all logged and not logged users.
 * 
 * A visible service is a deprecated or published service.
 * 
 * @author Giulia Canobbio
 *
 */
@Controller
@RequestMapping(value="/api/catalog")
public class CatalogController {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);
	/**
	 * {@link ResponseObject} Response object contains requested data, 
	 * status of response and if necessary a custom error message.
	 */
	private ResponseObject responseObject;
	/**
	 * Instance of {@link CatalogManager} to retrieve data using Dao classes.
	 */
	@Autowired
	private CatalogManager catalogManager;
	
	
	/**
	 * Show all services in catalog which are published or deprecated.
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with list of published and deprecated services data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/service/{firstResult}/{maxResult}/{param_order}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject catalogServices(@PathVariable int firstResult, @PathVariable int maxResult, 
			@PathVariable String param_order, HttpServletResponse response){
		logger.info("-- Service Catalog Publish Service --");
		//ListService lserv = new ListService();
		List<Service> services = catalogManager.catalogServices(firstResult,maxResult,param_order);
		//lserv.setServices(catalogManager.catalogServices());
		responseObject = new ResponseObject();
		if(services==null || services.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No service available");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}else{
			responseObject.setData(services);
			responseObject.setStatus(HttpServletResponse.SC_OK);
			responseObject.setError(""+catalogManager.countService());
		}
		return responseObject;
	}
	
	/**
	 * Retrieve data of a specific service, which must be published or deprecated.
	 * Search is done by service id.
	 * @param service_id : int id of published or deprecated service.
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with services data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
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
		return responseObject;
	}
	
	/**
	 * Retrieve methods data of a specific published or deprecated service.
	 * Search by service id.
	 * @param service_id : int id of published or deprecated service.
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with services method data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/service/methods/{service_id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject cataogServiceMethods(@PathVariable int service_id, HttpServletResponse response){
		logger.info("-- Service Catalog Show Methods --");
		List<Method> methods = catalogManager.catalogServiceMethods(service_id);
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
		return responseObject;
	}
	
	/**
	 * Retrieve data history of a specific service.
	 * Search by service id
	 * @param service_id : int id of published or deprecated service.
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with services history data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/service/history/{service_id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject cataogServiceHistory(@PathVariable int service_id, HttpServletResponse response){
		logger.info("-- Service Catalog Show Methods --");
		List<ServiceHistory> history = catalogManager.catalogServiceHistory(service_id);
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
		return responseObject;
	}
	
	/**
	 * Simple search in service catalog.
	 * Search is done comparing a token with service name.
	 * @param token : String token compared with service name
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/service/search/{token}/{firstResult}/{maxResult}/{param_order}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogServiceSimpleSearch(@PathVariable String token, @PathVariable int firstResult,
			@PathVariable int maxResult, @PathVariable String param_order, HttpServletResponse response){
		logger.info("-- Service Catalog simple search --");
		List<Service> services = catalogManager.catalogServiceSimpleSearch(token, firstResult, maxResult, param_order);
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
		return responseObject;
	}
	
	/**
	 * Browse service in catalog by category.
	 * Every category has its own id and search is done by category id.
	 * @param category : int category id
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/service/browse/category/{category}/{firstResult}/{maxResult}/{param_order}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogServiceBrowseByCategory(@PathVariable int category, @PathVariable int firstResult,
			@PathVariable int maxResult, @PathVariable String param_order, HttpServletResponse response){
		logger.info("-- Service Catalog browse (category) --");
		List<Service> services = catalogManager.catalogServiceBrowseByCategory(category, firstResult, maxResult, param_order);
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
	 * Browse service in catalog by organization.
	 * Search is done by organization id.
	 * @param org : int organization id
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/service/browse/org/{org}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogServiceBrowseByOrg(@PathVariable int org, HttpServletResponse response){
		logger.info("-- Service Catalog browse (org) --");
		List<Service> services = catalogManager.catalogServiceBrowseByOrg(org);
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
	 * Browse service in catalog by tags.
	 * @param tags : String tags
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/service/browse/tags/{tags}/{firstResult}/{maxResult}/{param_order}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject catalogServiceBrowseByTags(@PathVariable String tags, @PathVariable int firstResult,
			@PathVariable int maxResult, @PathVariable String param_order, HttpServletResponse response) {
		logger.info("-- Service Catalog browse (category) --");
		List<Service> services = catalogManager.catalogServiceBrowseByTags(tags, firstResult, maxResult, param_order);
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
	 * Retrieve all organization data for users.
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of organization data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/org/{firstResult}/{maxResult}/{param_order}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject catalogOrg(@PathVariable int firstResult, @PathVariable int maxResult, 
			@PathVariable String param_order, HttpServletResponse response){
		logger.info("-- Organization Catalog data --");
		List<Organization> orgs = catalogManager.catalogOrg(firstResult, maxResult, param_order);
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
	 * Retrieve data of a specific organization.
	 * Search by its id.
	 * @param id : int organization id
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with organization data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
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
	 * Simple search in organization catalog.
	 * Search is done comparing token with organization name.
	 * @param token : String token
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of organization data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/org/search/{token}/{firstResult}/{maxResult}/{param_order}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogOrgSimpleSearch(@PathVariable String token, @PathVariable int firstResult, 
			@PathVariable int maxResult, @PathVariable String param_order, HttpServletResponse response){
		logger.info("-- Organization Catalog simple search --");
		List<Organization> orgs = catalogManager.catalogOrgSimpleSearch(token, firstResult, maxResult, param_order);
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
	 * Browse organization by category.
	 * Every category has its own id and search is done by category id.
	 * @param category : path variable
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of organization data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/org/browse/category/{category}/{firstResult}/{maxResult}/{param_order}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogOrgBrowse(@PathVariable int category, @PathVariable int firstResult, 
			@PathVariable int maxResult, @PathVariable String param_order, HttpServletResponse response){
		logger.info("-- Organization Catalog browse --");
		List<Organization> orgs = catalogManager.catalogOrgBrowse(category, firstResult, maxResult, param_order);
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
	
	/**
	 * Browse services group by category.
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service and category data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
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
