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

import org.springframework.dao.DataAccessException;

import eu.trentorise.smartcampus.openservices.entities.*;

public interface ServiceDao {

	/**
	 * Show all service
	 * @return
	 */
	public List<Service> showService() throws DataAccessException;
	
	/**
	 * Show all service of a given user
	 * @param username
	 * @return
	 */
	public List<Service> showMyService(String username) throws DataAccessException;
	
	/**
	 * User can interact and use a chosen service
	 * @param service_name
	 */
	public Service useService(String service_name) throws DataAccessException;
	
	/**
	 * Create a service to an organization.
	 * Set username as owner_id
	 */
	public void createService(Service service) throws DataAccessException;
	
	/**
	 * Modify a service 
	 */
	public void modifyService(Service service) throws DataAccessException;
	
	/**
	 * Delete a service
	 */
	public void deleteService(Service service) throws DataAccessException;
	
	/**
	 * Get organization of service
	 * @param service_id
	 * @return
	 */
	public Organization getOrganizationofService(int service_id) throws DataAccessException;
	
	/**
	 * Get owner of service
	 * @param service_id
	 * @return
	 */
	public User getOwnerOfService(int service_id) throws DataAccessException;
	
	/**
	 * Get service by id
	 * @param service_id
	 * @return
	 */
	public Service getServiceById(int service_id) throws DataAccessException;
	
	/**
	 * Get service by owner id
	 * @param id_owner
	 * @return
	 */
	public List<Service> getServiceByIdOwner(int id_owner) throws DataAccessException;
	
	/**
	 * Get service by organization id
	 * @param id_org
	 * @return
	 */
	public List<Service> getServiceByIdOrg(int id_org) throws DataAccessException;
	
	/**
	 * Find services having in name the token in input
	 * @param token
	 * @return
	 */
	public List<Service> searchService(String token) throws DataAccessException;
	
	/**
	 * Browse services having input category and tags
	 * @param category
	 * @param tags
	 * @return
	 * @throws DataAccessException
	 */
	public List<Service> browseService(String category, String tags) throws DataAccessException;
	
}
