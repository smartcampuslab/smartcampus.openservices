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

import eu.trentorise.smartcampus.openservices.Constants.ORDER;
import eu.trentorise.smartcampus.openservices.entities.Organization;

/**
 * Organization Dao Interface
 * 
 * @author Giulia Canobbio
 * 
 */
public interface OrganizationDao {

	/**
	 * Show all organization in database.
	 * 
	 * @return list of {@link Organization} instances
	 * @throws DataAccessException
	 */
	public List<Organization> showOrganizations(int firstResult, int maxResult, ORDER param_order) throws DataAccessException;

	/**
	 * Show my organizations. Using user id
	 * 
	 * @param id_user 
	 * 			: int
	 * @return list of {@link Organization} instances
	 * @throws DataAccessException
	 */
	public List<Organization> showMyOrganizations(int id_user) throws DataAccessException;

	/**
	 * Get organization by id.
	 * 
	 * @param org_id 
	 * 			: int, organization id
	 * @return a {@link Organization} instance
	 * @throws DataAccessException
	 */
	public Organization getOrganizationById(int org_id) throws DataAccessException;

	/**
	 * Get organization data by name.
	 * 
	 * @param org_name
	 * 			: String
	 * @return a {@link Organization} instance
	 * @throws DataAccessException
	 */
	public Organization getOrganizationByName(String org_name) throws DataAccessException;

	/**
	 * Create an organization.
	 * 
	 * @param org
	 *            : a {@link Organization} instance
	 * @throws DataAccessException
	 */
	public void createOrganization(Organization org) throws DataAccessException;

	/**
	 * Modify Organization.
	 * 
	 * @param org
	 *            : a {@link Organization} instance
	 * @throws DataAccessException
	 */
	public void modifyOrganization(Organization org) throws DataAccessException;

	/**
	 * Delete an organization.
	 * 
	 * @param org
	 *            : a {@link Organization} instance
	 * @throws DataAccessException
	 */
	public void deleteOrganization(int org) throws DataAccessException;

	/**
	 * Get a list of organizations where user is owner.
	 * 
	 * @param owner_id
	 * 			: int, id of organization owner
	 * @return list of {@link Organization} instances
	 * @throws DataAccessException
	 */
	public List<Organization> getOrganizationByOwnerId(int owner_id) throws DataAccessException;

	/**
	 * Search Organization given a token. For now I suppose that token is part
	 * of organization name (LIKE).
	 * 
	 * @param token
	 * 			: String
	 * @param firstResult
	 * 			: int, start index
	 * @param maxResult
	 * 			: int, number of element in list
	 * @param param_order
	 * 			: String, parameter order
	 * @return list of {@link Organization} instances
	 * @throws DataAccessException
	 */
	public List<Organization> searchOrganization(String token, int firstResult, int maxResult, String param_order)
			throws DataAccessException;

	/**
	 * Browse Organization using filters: category (LIKE), geography - for now I
	 * suppose that this is a string (LIKE).
	 * 
	 * @param category
	 * 			: Integer, category id
	 * @param geography 
	 * 			: Integer
	 * @param firstResult
	 * 			: int, start index
	 * @param maxResult
	 * 			: int, number of element in list
	 * @param param_order
	 * 			: String, parameter order
	 * @return list of {@link Organization} instances
	 * @throws DataAccessException
	 */
	public List<Organization> browseOrganization(Integer category, String geography, int firstResult, int maxResult,
			ORDER param_order) throws DataAccessException;

	/**
	 * Browse Organization using filters: category (LIKE), geography - for now I
	 * suppose that this is a string (LIKE).
	 * 
	 * @param category 
	 * 			: int list, list of category ids
	 * @param geography 
	 * 			: String
	 * @param firstResult 
	 * 			: int start index
	 * @param maxResult 
	 * 			: int number of element in list
	 * @param param_order 
	 * 			: parameter order for list
	 * @return list of {@link Organization} instances
	 * @throws DataAccessException
	 */
	public List<Organization> browseOrganization(int[] category, String geography, int firstResult, int maxResult,
			ORDER param_order) throws DataAccessException;
	
	/**
	 * Get list of Organization by category id.
	 * 
	 * @param id 
	 * 			: int, category id
	 * @return list of {@link Organization} instances in a specific category.
	 * @throws DataAccessException
	 */
	public List<Organization> findByCategory(int id) throws DataAccessException;

	/**
	 * Retrieves number of organization saved in db.
	 * 
	 * @return number of organization saved in database
	 * @throws DataAccessException
	 */
	public Long countOrganization() throws DataAccessException;

}
