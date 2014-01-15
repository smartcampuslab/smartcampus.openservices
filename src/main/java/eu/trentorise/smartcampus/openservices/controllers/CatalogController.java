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

@Controller
@RequestMapping(value="/api/catalog")
public class CatalogController {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private OrganizationDao orgDao;
	
	/*
	 * ACCESS SERVICE CATALOG 
	 */
	
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
		List<Organization> orgs = orgDao.searchOrganization(token);
		ListOrganization lorg = new ListOrganization();
		lorg.setOrgs(orgs);
		return lorg;
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
		List<Organization> orgs = orgDao.browseOrganization(category,null);
		ListOrganization lorg = new ListOrganization();
		lorg.setOrgs(orgs);
		return lorg;
	}
	//browse catalog using filters (by geography) - when add address of organization - TODO
	
	
}
