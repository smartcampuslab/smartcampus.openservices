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
package eu.trentorise.smartcampus.openservices.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import eu.trentorise.smartcampus.openservices.Constants.ORDER;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.User;

/**
 * Service Dao Interface
 * 
 * @author Giulia Canobbio
 * 
 */
public interface ServiceDao { // extends JpaRepository<Service, Integer>{

	/**
	 * Show all service.
	 * 
	 * @return list of {@link Service} instances
	 * @throws DataAccessException
	 */
	public List<Service> showService() throws DataAccessException;

	/**
	 * Show only published services.
	 * 
	 * @return list of {@link Service} instances
	 * @throws DataAccessException
	 */
	public List<Service> showPublishedService(int firstResult, int maxResult, ORDER param_order) throws DataAccessException;

	/**
	 * Show all service of a given user.
	 * 
	 * @param username
	 * @return list of {@link Service} instances
	 * @throws DataAccessException
	 */
	public List<Service> showMyService(String username) throws DataAccessException;

	/**
	 * User can interact and use a chosen service.
	 * 
	 * @param service_name
	 * @return a {@link Service} instance
	 * @throws DataAccessException
	 */
	public Service useService(String service_name) throws DataAccessException;

	/**
	 * Create a service to an organization. Set user as owner.
	 * 
	 * @param service
	 *            : a {@link Service} instance
	 * @throws DataAccessException
	 */
	public void createService(Service service) throws DataAccessException;

	/**
	 * 
	 * Modify a service.
	 * 
	 * @param service
	 *            : a {@link Service} instance
	 * @throws DataAccessException
	 */
	public void modifyService(Service service) throws DataAccessException;

	/**
	 * Delete a service.
	 * 
	 * @param service
	 *            : a {@link Service} instance
	 * @throws DataAccessException
	 */
	public void deleteService(Service service) throws DataAccessException;

	/**
	 * Get organization of service.
	 * 
	 * @param service_id
	 * @return a {@link Organization} instance
	 * @throws DataAccessException
	 */
	public Organization getOrganizationofService(int service_id) throws DataAccessException;

	/**
	 * Get owner of service.
	 * 
	 * @param service_id
	 * @return a {@link User} instance
	 * @throws DataAccessException
	 */
	public User getOwnerOfService(int service_id) throws DataAccessException;

	/**
	 * Get service by id.
	 * 
	 * @param service_id
	 * @return a {@link Service} instance
	 * @throws DataAccessException
	 */
	public Service getServiceById(int service_id) throws DataAccessException;

	/**
	 * Get service by owner id.
	 * 
	 * @param id_owner
	 * @return list of {@link Service} instances
	 * @throws DataAccessException
	 */
	public List<Service> getServiceByIdOwner(int id_owner) throws DataAccessException;

	/**
	 * Get service by organization id.
	 * 
	 * @param id_org
	 * @param firstResult
	 * @param maxResult
	 * @param param_order
	 * @return list of {@link Service} instances
	 * @throws DataAccessException
	 */
	public List<Service> getServiceByIdOrg(int id_org, int firstResult, int maxResult, String param_order)
			throws DataAccessException;

	/**
	 * Find services having in name the token in input
	 * 
	 * @param token
	 * @param firstResult
	 * @param maxResult
	 * @param param_order
	 * @return list of {@link Service} instances
	 * @throws DataAccessException
	 */
	public List<Service> searchService(String token, int firstResult, int maxResult, ORDER param_order)
			throws DataAccessException;

	/**
	 * Browse services having input category and tags
	 * 
	 * @param category
	 * @param tags
	 * @return list of {@link Service} instances
	 * @throws DataAccessException
	 */
	public List<Service> browseService(Integer category, int firstResult, int maxResult, ORDER param_order)
			throws DataAccessException;

	/**
	 * Browse services having input categories and tags
	 * 
	 * @param category
	 * @param tags
	 * @return list of {@link Service} instances
	 * @throws DataAccessException
	 */
	public List<Service> browseService(int[] categories, int firstResult, int maxResult, ORDER param_order)
			throws DataAccessException;

	/**
	 * Retrieve service searching by tag
	 * 
	 * @param tag
	 * @param firstResult
	 * @param maxResult
	 * @param param_order
	 * @return list of {@link Service} instances
	 * @throws DataAccessException
	 */
	public List<Service> getServiceByTag(String tag, int firstResult, int maxResult, ORDER param_order)
			throws DataAccessException;

	/**
	 * @param id
	 * @return list {@link Service} instances in a specific category
	 * @throws DataAccessException
	 */
	public List<Service> findByCategory(int id);

	/**
	 * @return Map<Integer, Integer>: category, number of services for each
	 *         category;
	 * @throws DataAccessException
	 */
	public Map<Integer, Integer> findCategoryServices() throws DataAccessException;

	/**
	 * 
	 * @return number of saved services in db
	 * @throws DataAccessException
	 */
	public Long countService() throws DataAccessException;

	/**
	 * 
	 * @param token
	 *            String
	 * @return number of services retrieved by simple search
	 * @throws DataAccessException
	 */
	public Long countServiceSimpleSearch(String token) throws DataAccessException;

	/**
	 * @param id_org
	 * @return number of services retrieved by searching by organization id
	 * @throws DataAccessException
	 */
	public Long countServiceByOrgSearch(int id_org) throws DataAccessException;

	/**
	 * 
	 * @param tags
	 * @return number of services retrieved by searching by tags
	 * @throws DataAccessException
	 */
	public Long countServiceTagsSearch(String tags) throws DataAccessException;

	/**
	 * 
	 * @param category
	 * @return number of services retrieved by searching by category
	 * @throws DataAccessException
	 */
	public Long countServiceCategorySearch(int category) throws DataAccessException;

	/**
	 * 
	 * @param order
	 * @param group
	 * @return Map<String, Integer>: tag name, number of services for each tag;
	 * @throws DataAccessException
	 */
	public Map<String, Integer> findTagServices(String group, String order) throws DataAccessException;

}
