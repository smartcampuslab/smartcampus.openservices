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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.openservices.dao.*;
import eu.trentorise.smartcampus.openservices.entities.*;
import eu.trentorise.smartcampus.openservices.support.ListOrganization;
import eu.trentorise.smartcampus.openservices.support.ListService;
import eu.trentorise.smartcampus.openservices.support.ListServiceHistory;

@Controller
@RequestMapping(value="/api/catalog")
public class CatalogController {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private OrganizationDao orgDao;
	@Autowired
	private MethodDao methodDao;
	@Autowired
	private ServiceHistoryDao shDao;
	
	/*
	 * ACCESS SERVICE CATALOG 
	 */
	
	//accessing published services
	/**
	 * Show all services in catalog which are published.
	 * @return
	 */
	@RequestMapping(value="/service", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ListService catalogServices(){
		logger.info("-- Service Catalog Publish Service --");
		ListService lserv = new ListService();
		List<Service> s = serviceDao.showPublishedService();
		lserv.setServices(s);
		return lserv;
	}
	
	//accessing service data of publish service
	//Method
	@RequestMapping(value="/service/methods/{service_id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ListMethod cataogServiceMethods(@PathVariable int service_id){
		logger.info("-- Service Catalog Show Methods --");
		ListMethod lmeth = new ListMethod();
		List<Method> meth = methodDao.getMethodByServiceId(service_id);
		lmeth.setMethods(meth);
		return lmeth;
	}
	
	//Service History
	@RequestMapping(value="/service/history/{service_id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ListServiceHistory cataogServiceHistory(@PathVariable int service_id){
		logger.info("-- Service Catalog Show Methods --");
		ListServiceHistory lsh = new ListServiceHistory();
		List<ServiceHistory> sh = shDao.getServiceHistoryByServiceId(service_id);
		lsh.setLserviceh(sh);
		return lsh;
	}
	
	//simple search
	/**
	 * Simple search in service catalog
	 * Catalog shows publish services
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/service/search/{token}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListService catalogServiceSimpleSearch(@PathVariable String token){
		logger.info("-- Service Catalog simple search --");
		ListService lserv = new ListService();
		List<Service> s = serviceDao.searchService(token);
		lserv.setServices(s);
		return lserv;
	}
	
	//browse catalog using filters (by category, tag, protocols/formats, using apps)
	/**
	 * Browse service in catalog by category
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/service/browse/category/{category}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListService catalogServiceBrowseByCategory(@PathVariable String category){
		logger.info("-- Service Catalog browse (category) --");
		List<Service> s = serviceDao.browseService(category, null);
		ListService lserv = new ListService();
		lserv.setServices(s);
		return lserv;
	}
	
	// browse catalog using filters (by category, tag, protocols/formats, using apps)
	/**
	 * Browse service in catalog by tags
	 * @param tags
	 * @return
	 */
	@RequestMapping(value = "/service/browse/tags/{tags}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ListService catalogServiceBrowseByTags(@PathVariable String tags) {
		logger.info("-- Service Catalog browse (category) --");
		List<Service> s = serviceDao.browseService(null, tags);
		ListService lserv = new ListService();
		lserv.setServices(s);
		return lserv;
	}
	//browse catalog using filter (by protocols) - TO DEFINE - TODO

	//view most used - depends on App - TODO
	public ListService catalogServiceMostUsed(){
		logger.info("-- Service Catalog most used --");
		return null;
	}
	
	/*
	 * ACCESS ORGANIZATION CATALOG
	 */
	
	//Get All Organization
	@RequestMapping(value="/org", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ListOrganization catalogOrg(){
		logger.info("-- Organization Catalog data --");
		ListOrganization lorg = new ListOrganization();
		List<Organization> orgs = orgDao.showOrganizations();
		lorg.setOrgs(orgs);
		return lorg;
	}
	
	//simple search
	/**
	 * Simple search in organization catalog
	 * @param token
	 * @return
	 */
	@RequestMapping(value = "/org/search/{token}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListOrganization catalogOrgSimpleSearch(@PathVariable String token){
		logger.info("-- Organization Catalog simple search --");
		ListOrganization lserv = new ListOrganization();
		List<Organization> orgs = orgDao.searchOrganization(token);
		lserv.setOrgs(orgs);
		return lserv;
	}
	
	//browse catalog using filters(by category, geography)
	/**
	 * Browse organization by category
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/org/browse/category/{category}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListOrganization catalogOrgBrowse(@PathVariable String category){
		//TODO for now by category
		logger.info("-- Organization Catalog browse --");
		ListOrganization lserv = new ListOrganization();
		List<Organization> orgs = orgDao.browseOrganization(category,null);
		lserv.setOrgs(orgs);
		return lserv;
	}
	//browse catalog using filters (by geography) - when add address of organization - TODO
	
	
}
