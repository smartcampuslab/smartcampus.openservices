package com.openserviceproject.openservice;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.openserviceproject.dao.*;
import com.openserviceproject.entities.*;
import com.openserviceproject.support.ListOrganization;
import com.openserviceproject.support.ListService;

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
