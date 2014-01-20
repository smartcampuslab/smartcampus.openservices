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
	@Autowired
	private CatalogManager catalogManager;
	
	
	/**
	 * Show all services in catalog which are published.
	 * @return
	 */
	@RequestMapping(value="/service", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ListService catalogServices(){
		logger.info("-- Service Catalog Publish Service --");
		ListService lserv = new ListService();
		lserv.setServices(catalogManager.catalogServices());
		return lserv;
	}
	
	/**
	 * Get data of a service
	 * @param service_id
	 * @return
	 */
	@RequestMapping(value="/service/{service_id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Service cataogServiceById(@PathVariable int service_id){
		logger.info("-- Service Catalog Service By Id--");
		return catalogManager.catalogServiceById(service_id);
	}
	
	/**
	 * Return methods of a service.
	 * Search by service id
	 * @param service_id
	 * @return
	 */
	@RequestMapping(value="/service/methods/{service_id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ListMethod cataogServiceMethods(@PathVariable int service_id){
		logger.info("-- Service Catalog Show Methods --");
		ListMethod lmeth = new ListMethod();
		lmeth.setMethods(catalogManager.catalogServiceMethods(service_id));
		return lmeth;
	}
	
	/**
	 * Return history of a service.
	 * Search by service id
	 * @param service_id
	 * @return
	 */
	@RequestMapping(value="/service/history/{service_id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public ListServiceHistory cataogServiceHistory(@PathVariable int service_id){
		logger.info("-- Service Catalog Show Methods --");
		ListServiceHistory lsh = new ListServiceHistory();
		lsh.setLserviceh(catalogManager.catalogServiceHistory(service_id));
		return lsh;
	}
	
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
		lserv.setServices(catalogManager.catalogServiceSimpleSearch(token));
		return lserv;
	}
	
	/**
	 * Browse service in catalog by category
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/service/browse/category/{category}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListService catalogServiceBrowseByCategory(@PathVariable int category){
		logger.info("-- Service Catalog browse (category) --");
		ListService lserv = new ListService();
		lserv.setServices(catalogManager.catalogServiceBrowseByCategory(category));
		return lserv;
	}

	/**
	 * Browse service in catalog by org
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/service/browse/org/{org}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListService catalogServiceBrowseByOrg(@PathVariable int org){
		logger.info("-- Service Catalog browse (org) --");
		ListService lserv = new ListService();
		lserv.setServices(catalogManager.catalogServiceBrowseByOrg(org));
		return lserv;
	}

	/**
	 * Browse service in catalog by tags
	 * @param tags
	 * @return
	 */
	@RequestMapping(value = "/service/browse/tags/{tags}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ListService catalogServiceBrowseByTags(@PathVariable String tags) {
		logger.info("-- Service Catalog browse (category) --");
		ListService lserv = new ListService();
		lserv.setServices(catalogManager.catalogServiceBrowseByTags(tags));
		return lserv;
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
	public ListOrganization catalogOrg(){
		logger.info("-- Organization Catalog data --");
		ListOrganization lorg = new ListOrganization();
		lorg.setOrgs(catalogManager.catalogOrg());
		return lorg;
	}
	
	/**
	 * Get organization
	 * @return
	 */
	@RequestMapping(value="/org/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Organization catalogOrg(@PathVariable int id){
		logger.info("-- Organization Catalog data --");
		return catalogManager.catalogOrgById(id);
	}
	
	
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
		lserv.setOrgs(catalogManager.catalogOrgSimpleSearch(token));
		return lserv;
	}
	
	/**
	 * Browse organization by category
	 * @param category
	 * @return
	 */
	@RequestMapping(value = "/org/browse/category/{category}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListOrganization catalogOrgBrowse(@PathVariable int category){
		logger.info("-- Organization Catalog browse --");
		ListOrganization lserv = new ListOrganization();
		lserv.setOrgs(catalogManager.catalogOrgBrowse(category));
		return lserv;
	}
	//browse catalog using filters (by geography) - when add address of organization - TODO
	
	@RequestMapping(value = "/service/browse/category", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public CategoryServices catalogCategoryServices() {
		logger.info("-- Category Catalog browse --");
		return catalogManager.getCategoryServices();
	}
	
}
