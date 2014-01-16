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

import eu.trentorise.smartcampus.openservices.entities.Organization;

public interface OrganizationDao {
	
	/**
	 * Show all organization in database
	 * @return
	 */
	public List<Organization> showOrganizations() throws DataAccessException;
	
	/**
	 * Show my organizations
	 * Using user id
	 * @param id_user
	 * @return
	 */
	public List<Organization> showMyOrganizations(int id_user) throws DataAccessException;
	
	/**
	 * Get organization by id
	 * @param org_id
	 * @return
	 * @throws DataAccessException
	 */
	public Organization getOrganizationById(int org_id) throws DataAccessException;
	
	/**
	 * Get organization data by name
	 * @param org_name
	 * @return
	 * @throws DataAccessException
	 */
	public Organization getOrganizationByName(String org_name) throws DataAccessException;
	
	/**
	 * Create an organization
	 * @param username
	 */
	public void createOrganization(Organization org) throws DataAccessException;
	
	/**
	 * Modify Organization
	 * @param username
	 * @param org_name
	 */
	public void modifyOrganization(Organization org) throws DataAccessException;
	
	/**
	 * Delete an organization
	 * @param username
	 * @param org_name
	 */
	public void deleteOrganization(int org) throws DataAccessException;
	
	
	/**
	 * Delete service_owner of an organization
	 * @param username
	 * @param org_name
	 */
	//public void deleteServiceOwner(String username, String org_name) throws DataAccessException;
	
	/**
	 * Add an organization owner to an organization
	 * @param username
	 * @param org_owner
	 * @param org_name
	 */
	//public void addOrganizationOwner(String username, String org_name) throws DataAccessException;
	
	/**
	 * Delete organization owner
	 * @param username
	 * @param org_owner
	 * @param org_name
	 */
	//public void deleteOrganizationOwner(String username, String org_name) throws DataAccessException;
	
	/**
	 * Get a list of organizations where user is owner
	 * @param owner_id
	 * @return
	 */
	public List<Organization> getOrganizationByOwnerId(int owner_id) throws DataAccessException;
	
	/**
	 * Search Organization given a token
	 * For now I suppose that token is part of organization name (LIKE)
	 * @param token
	 * @return
	 * @throws DataAccessException
	 */
	public List<Organization> searchOrganization(String token) throws DataAccessException;
	
	/**
	 * Browse Organization using filters
	 * category (LIKE)
	 * geography - for now I suppose that this is a string (LIKE)
	 * @param category
	 * @param geography
	 * @return
	 * @throws DataAccessException
	 */
	public List<Organization> browseOrganization(Integer category, String geography) throws DataAccessException;

	/**
	 * @param id
	 * @return all the organization of the specific category
	 */
	public List<Organization> findByCategory(int id) throws DataAccessException;
	
}
