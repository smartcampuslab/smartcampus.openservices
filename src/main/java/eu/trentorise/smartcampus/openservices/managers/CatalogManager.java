/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.openservices.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.Constants.SERVICE_STATE;
import eu.trentorise.smartcampus.openservices.dao.MethodDao;
import eu.trentorise.smartcampus.openservices.dao.OrganizationDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceHistoryDao;
import eu.trentorise.smartcampus.openservices.entities.Authentication;
import eu.trentorise.smartcampus.openservices.entities.Category;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;
import eu.trentorise.smartcampus.openservices.support.CategoryServices;

/**
 * Manager that retrieves data about service and organization for 
 * all users.
 * 
 * @author Giulia Canobbio
 *
 */
@Component
public class CatalogManager {
	/**
	 * Instance of {@link ServiceDao} to retrieve service data using Dao classes.
	 */
	@Autowired
	private ServiceDao serviceDao;
	/**
	 * Instance of {@link OrganizationDao} to retrieve organization data using Dao classes.
	 */
	@Autowired
	private OrganizationDao orgDao;
	/**
	 * Instance of {@link MethodDao} to retrieve service method data using Dao classes.
	 */
	@Autowired
	private MethodDao methodDao;
	/**
	 * Instance of {@link ServiceHistoryDao} to retrieve service history data using Dao classes.
	 */
	@Autowired
	private ServiceHistoryDao shDao;
	/**
	 * Instance of {@link CategoryManager} to retrieve category data.
	 */
	@Autowired
	private CategoryManager categoryManager;
	
	/**
	 * Get list of published and deprecated services
	 * @return all {@link Service} instances
	 */
	public List<Service> catalogServices(int firstResult, int maxResult, String param_order){
		try{
			return cleanAuthenticationData(serviceDao.showPublishedService(firstResult,maxResult, param_order));
			/*PageRequest pageReq = new PageRequest(page, size, Direction.ASC, "name");
			Page<Service> s =  serviceDao.findAll(pageReq);
			return s.getContent();
			*/		
		}catch(DataAccessException d){
			return null;
		}
	}
	
	private List<Service> cleanAuthenticationData(List<Service> services) {
		if (services != null) {
			for (Service s : services) {
				cleanAuthenticationData(s);
			}
		}
		return services;
	}
	private Service cleanAuthenticationData(Service s) {
		if (s == null) return null;
		if (s.getAccessInformation() != null) {
			Authentication a = s.getAccessInformation().getAuthentication();
			if (a != null) {
				a.setAccessAttributes(null);
			}
		}
		return s;
	}
	private List<Method> cleanMethodAuthenticationData(List<Method> methods) {
		if (methods != null) {
			for (Method m : methods) {
				cleanMethodAuthenticationData(m);
			}
		}
		return methods;
	}

	private Method cleanMethodAuthenticationData(Method m) {
		if (m == null) return null;
		if (m.getTestboxProperties() != null) {
			Authentication a = m.getTestboxProperties().getAuthenticationDescriptor();
			if (a != null) {
				a.setAccessAttributes(null);
			}
		}
		return m;
	}


	/**
	 *  Get Service data, searching by id
	 * @param service_id : int service id
	 * @return a {@link Service} instance
	 */
	public Service catalogServiceById(int service_id){
		Service s=new Service();
		try{
			s = serviceDao.getServiceById(service_id);
			if(s!=null && s.getState().equalsIgnoreCase(SERVICE_STATE.UNPUBLISH.toString())){
				s = null;
			}
		}
		catch(DataAccessException d){
			s = null;
		}
		return cleanAuthenticationData(s);
	}
	
	/**
	 * Get list of methods for a given Service.
	 * Search by service id
	 * @param service_id : int service id
	 * @return all {@link Method} instances
	 */
	public List<Method> catalogServiceMethods(int service_id){
		try{
			return cleanMethodAuthenticationData(methodDao.getMethodByServiceId(service_id));			
		}catch(DataAccessException e){
			return null;
		}
	}
	
	/**
	 * Get list of service history for a given Service.
	 * Search by service id
	 * @param service_id : int service id
	 * @return all {@link ServiceHistory} instances
	 */
	public List<ServiceHistory> catalogServiceHistory(int service_id){
		try{
			return shDao.getServiceHistoryByServiceId(service_id);
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Get list of all services.
	 * Their name contains token.
	 * @param token : String token compared with service name
	 * @return all {@link Service} instances
	 */
	public List<Service> catalogServiceSimpleSearch(String token, int firstResult, int maxResult, String param_order){
		try{
			return serviceDao.searchService(token, firstResult, maxResult, param_order);
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Get list of all services, searching by category.
	 * @param category : int category id
	 * @return all {@link Service} instances
	 */
	public List<Service> catalogServiceBrowseByCategory(int category, int firstResult, int maxResult, String param_order){
		try{
			return serviceDao.browseService(category, null, firstResult, maxResult, param_order);
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Retrieve all service of a specific organization
	 * @param org : id of organization
	 * @return all {@link Service} instances
	 */
	public List<Service> catalogServiceBrowseByOrg(int org) {
		List<Service> s = new ArrayList<Service>();
		try {
			s = serviceDao.getServiceByIdOrg(org);
			if (s != null) {
				for (Iterator<Service> iterator = s.iterator(); iterator.hasNext();) {
					Service service = iterator.next();
					if (SERVICE_STATE.UNPUBLISH.toString().equals(
							service.getState()))
						iterator.remove();
				}
			}
		} catch (DataAccessException d) {
			s = null;
		}
		return s;
	}

	/**
	 * Get list of all services, searching by tags
	 * @param tags : String tags for searching in service tags
	 * @return all {@link Service} instances
	 */
	public List<Service> catalogServiceBrowseByTags(String tags, int firstResult, int maxResult, String param_order) {
		try{
			return serviceDao.browseService(null, tags, firstResult, maxResult, param_order);
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Get list of organizations
	 * @return all {@link Organization} instances
	 */
	public List<Organization> catalogOrg( int firstResult, int maxResult,  String param_order){
		try{
			return orgDao.showOrganizations(firstResult, maxResult, param_order);
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Simple search for organization.
	 * Their names contains token.
	 * @param token : String token for comparing it with organization name
	 * @return all {@link Organization} instances
	 */
	public List<Organization> catalogOrgSimpleSearch(String token, int firstResult, int maxResult,  String param_order){
		try{
			return orgDao.searchOrganization(token, firstResult, maxResult, param_order);
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Get list of organization searching by category
	 * @param category : int category id
	 * @return all {@link Organization} instances
	 */
	public List<Organization> catalogOrgBrowse(int category, int firstResult, int maxResult,  String param_order){
		try{
			return orgDao.browseOrganization(category,null, firstResult, maxResult, param_order);
		}catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * Retrieve organization data searching by its id
	 * @param id : int organization id
	 * @return {@link Organization} instance
	 */
	public Organization catalogOrgById(int id) {
		try{
			return orgDao.getOrganizationById(id);
		}catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * Browse services by category
	 * @return {@link CategoryServices} instance
	 */
	public CategoryServices getCategoryServices() {
		CategoryServices res = new CategoryServices();
		try {
			List<Category> list = categoryManager.getCategories();
			res.setCategories(list);
			if (res.getCategories() != null) {
				Map<Integer, Integer> counts = serviceDao
						.findCategoryServices();
				res.setServices(new ArrayList<Integer>());
				for (Category c : res.getCategories()) {
					Integer count = counts.get(c.getId());
					res.getServices().add(count == null ? 0 : count);
				}
			}
		} catch (DataAccessException d) {
			res = null;
		}
		
		return res;
	}
	
	/**
	 * 
	 * @return number of services saved in database
	 */
	public Long countService(){
		return serviceDao.countService();
	}
	
	/**
	 * 
	 * @param token : String
	 * @return number of services retrieved by simple search
	 */
	public Long countServiceSimpleSearch(String token){
		return serviceDao.countServiceSimpleSearch(token);
	}
	
	public Long countServiceCategorySearch(){
		return serviceDao.countServiceCategorySearch();
	}
	
	public Long countServiceTagsSearch(){
		return serviceDao.countServiceTagsSearch();
	}
	
	/**
	 * 
	 * @return number of organizations saved in database
	 */
	public Long countOrg(){
		return orgDao.countOrganization();
	}
	/*
	public Long countOrgSimpleSearch(){
		return orgDao.countOrgSimpleSearch();
	}
	
	public Long countOrgCategorySearch(){
		return orgDao.countOrgCategorySearch;
	}
	*/

}
