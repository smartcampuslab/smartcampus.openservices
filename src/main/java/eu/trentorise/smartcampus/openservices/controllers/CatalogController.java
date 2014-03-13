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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;
import eu.trentorise.smartcampus.openservices.managers.CatalogManager;
import eu.trentorise.smartcampus.openservices.model.Service;
import eu.trentorise.smartcampus.openservices.support.CategoryServices;
import eu.trentorise.smartcampus.openservices.support.TagCounter;

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
	
	private static final Logger logger = LoggerFactory.getLogger(CatalogController.class);
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
	
	
	@RequestMapping(value="/service", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject catalogServices(
			@RequestParam(value="q",required=false) String token,
			@RequestParam(value="first",required=false, defaultValue="0") Integer firstResult, 
			@RequestParam(value="last",required=false, defaultValue="0") Integer maxResult, 
			@RequestParam(value="order",required=false, defaultValue="name") String param_order, 
			@RequestParam(required=false) String tags,
			HttpServletResponse response){
		logger.info("-- Service Catalog --");
		responseObject = new ResponseObject();
		List<Service> services = new ArrayList<Service>();
		if (token == null && tags == null) {
			logger.info("-- List of service --");
			services = Service.fromServiceEntities(catalogManager.catalogServices(firstResult, maxResult,param_order));
			if (services == null || services.size() == 0) {
				responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseObject.setError("No service available");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				responseObject.setData(services);
				responseObject.setStatus(HttpServletResponse.SC_OK);
				responseObject.setTotalNumber(catalogManager.countService());
			}

		} else if (token != null && tags == null) {
			logger.info("-- Simple Search --");
			services = Service.fromServiceEntities(catalogManager.catalogServiceSimpleSearch(token,
					firstResult, maxResult, param_order));
			if (services == null || services.size() == 0) {
				responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseObject.setError("No published service for this search by name");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				responseObject.setData(services);
				responseObject.setStatus(HttpServletResponse.SC_OK);
				responseObject.setTotalNumber(catalogManager.countServiceSimpleSearch(token));
			}
		}

		else if (token == null && tags != null) {//TODO pagination on tags
			logger.info("-- Simple Search by tags: {} --", tags);
			List<Service> s = Service.fromServiceEntities(catalogManager.catalogServiceBrowseByTags(tags,firstResult, maxResult, param_order));
			if (s == null || s.size() == 0) {
				responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseObject.setError("No service for this search by tags");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				responseObject.setData(s);
				responseObject.setStatus(HttpServletResponse.SC_OK);
				responseObject.setTotalNumber(catalogManager.countServiceByTagsSearch(tags));
			}
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
	@RequestMapping(value="/service/{serviceId}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject cataogServiceById(@PathVariable int serviceId, HttpServletResponse response){
		logger.info("-- Service Catalog Service By Id--");
		Service service = Service.fromServiceEntity(catalogManager.catalogServiceById(serviceId));
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
	@RequestMapping(value="/service/{serviceId}/methods", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject cataogServiceMethods(@PathVariable int serviceId, HttpServletResponse response){
		logger.info("-- Service Catalog Show Methods --");
		List<Method> methods = catalogManager.catalogServiceMethods(serviceId);
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
	@RequestMapping(value="/service/{serviceId}/history", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject cataogServiceHistory(@PathVariable int serviceId, HttpServletResponse response){
		logger.info("-- Service Catalog Show Methods --");
		List<ServiceHistory> history = catalogManager.catalogServiceHistory(serviceId);
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
	 * Browse service in catalog by category.
	 * Every category has its own id and search is done by category id.
	 * @param category : int category id
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/service/category/{categoryId}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogServiceByCategory(@PathVariable int categoryId, 
			@RequestParam(value="first", required=false, defaultValue="0") Integer firstResult,
			@RequestParam(value="last", required=false, defaultValue="0") Integer maxResult, 
			@RequestParam(value="order", required=false, defaultValue="name") String param_order, 
			HttpServletResponse response){
		logger.info("-- Service Catalog browse (category) --");
		List<Service> services = Service.fromServiceEntities(catalogManager.catalogServiceBrowseByCategory(categoryId, firstResult, 
				maxResult, param_order));
		responseObject=new ResponseObject();
		if(services==null || services.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No published service for this category");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(services);
			responseObject.setStatus(HttpServletResponse.SC_OK);
			responseObject.setTotalNumber(catalogManager.countServiceByCategorySearch(categoryId));
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
	@RequestMapping(value = "/service/org/{org}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogServiceByOrg(@PathVariable int org, 
			@RequestParam(value="first", required=false, defaultValue="0") Integer firstResult,
			@RequestParam(value="last", required=false, defaultValue="0") Integer maxResult, 
			@RequestParam(value="order", required=false, defaultValue="name") String param_order, 
			HttpServletResponse response){
		logger.info("-- Service Catalog browse (org) --");
		List<Service> services = Service.fromServiceEntities(catalogManager.catalogServiceBrowseByOrg(org, firstResult, maxResult, param_order));
		responseObject=new ResponseObject();
		if(services==null || services.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No service for this organization");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(services);
			responseObject.setStatus(HttpServletResponse.SC_OK);
			responseObject.setTotalNumber(catalogManager.countServiceByOrgSearch(org));
		}
		return responseObject;
	}

	@RequestMapping(value="/org", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ResponseObject catalogOrgs(
			@RequestParam(value="q", required=false) String token,
			@RequestParam(value="first", required=false, defaultValue="0") Integer firstResult,
			@RequestParam(value="last", required=false, defaultValue="0") Integer maxResult, 
			@RequestParam(value="order", required=false, defaultValue="name") String param_order,  
			HttpServletResponse response){
		logger.info("-- Organization Catalog --");
		responseObject = new ResponseObject();
		List<Organization> orgs = new ArrayList<Organization>();
		if (token == null) {
			logger.info("-- All organization --");
			orgs = catalogManager.catalogOrg(firstResult, maxResult,param_order);
			if (orgs == null || orgs.size() == 0) {
				responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseObject.setError("No organization available");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				responseObject.setData(orgs);
				responseObject.setStatus(HttpServletResponse.SC_OK);
				responseObject.setTotalNumber(catalogManager.countOrg());
			}
		} else if (token != null) {
			logger.info("-- Simple Organization Search: {} --", token);
			orgs = catalogManager.catalogOrgSimpleSearch(token, firstResult,maxResult, param_order);
			responseObject = new ResponseObject();
			if (orgs == null || orgs.size() == 0) {
				responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseObject.setError("No organization for this search by name");
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			} else {
				responseObject.setData(orgs);
				responseObject.setStatus(HttpServletResponse.SC_OK);
				responseObject.setTotalNumber(catalogManager.countOrg());
			}
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
	public ResponseObject catalogOrgById(@PathVariable int id, HttpServletResponse response){
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
	 * Browse organization by category.
	 * Every category has its own id and search is done by category id.
	 * @param category : path variable
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of organization data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/org/category/{categoryId}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject catalogOrgByCategory(@PathVariable int categoryId, 
			@RequestParam(value="first", required=false, defaultValue="0") Integer firstResult,
			@RequestParam(value="last", required=false, defaultValue="0") Integer maxResult, 
			@RequestParam(value="order", required=false, defaultValue="name") String param_order,  
			HttpServletResponse response){
		logger.info("-- Organization Catalog browse --");
		List<Organization> orgs = catalogManager.catalogOrgBrowse(categoryId, firstResult, maxResult, param_order);
		responseObject = new ResponseObject();
		if(orgs==null || orgs.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No organization for this search by category");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(orgs);
			responseObject.setStatus(HttpServletResponse.SC_OK);
			responseObject.setTotalNumber(catalogManager.countOrg());
		}
		return responseObject;
	}
	
	/**
	 * Browse services group by category.
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service and category data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/category/services", method = RequestMethod.GET, produces="application/json") 
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
	
	/**
	 * Retrieve news
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK or NOT FOUND)
	 * @return {@link ResponseObject} with list of service history (news), status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/news", method=RequestMethod.GET)
	@ResponseBody
	public ResponseObject catalogNews(HttpServletResponse response){
		logger.info("-- Category Catalog browse --");
		List<ServiceHistory> news = catalogManager.getNews();
		responseObject = new ResponseObject();
		if(news==null || news.size()==0){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("There is no recent news");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(news);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	/**
	 * Retrieve tag counter
	 * @param response : {@link HttpServletResponse} which is needed for status of response (OK, BAD REQUEST 
	 * or NOT FOUND)
	 * @return {@link ResponseObject} with list of tag counter, status (OK, BAD REQUEST or NOT FOUND) and 
	 * error message (if status is NOT FOUND or BAD REQUEST).
	 */
	@RequestMapping(value="/tagcloud", method=RequestMethod.GET)
	@ResponseBody
	public ResponseObject catalogTag(HttpServletResponse response){
		logger.info("-- Tag counter --");
		try{
			List<TagCounter> tglist = catalogManager.getTagsServicesCounter();
			responseObject = new ResponseObject();
			if (tglist == null || tglist.size() == 0) {
				responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				responseObject.setError("There is a problem in connecting with database");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				responseObject.setData(tglist);
				responseObject.setStatus(HttpServletResponse.SC_OK);
			}
		}catch(EntityNotFoundException e){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("There is no tags");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}
	
}
