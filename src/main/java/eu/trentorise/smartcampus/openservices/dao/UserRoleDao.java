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

import eu.trentorise.smartcampus.openservices.entities.UserRole;

/**
 * User Role Dao Interface
 * @author Giulia Canobbio
 *
 */
public interface UserRoleDao {
	
	/**
	 * Get a list of User_Role data by user id.
	 * @param id_user
	 * @return list of {@link UserRole} instances
	 * @throws DataAccessException
	 */
	public List<UserRole> getUserRoleByIdUser(int id_user) throws DataAccessException;
	
	/**
	 * Get a list of User_Role data by organization id.
	 * @param id_org
	 * @return list of {@link UserRole} instances
	 * @throws DataAccessException
	 */
	public List<UserRole> getUserRoleByIdOrg(int id_org) throws DataAccessException;
	
	/**
	 * Get list of role of a user in User_Role, which can be:
	 * ROLE_ORGOWNER and 
	 * ROLE_SERVICEOWNER.
	 * @param user_id
	 * @return string list of user'roles
	 * @throws DataAccessException
	 */
	public List<String> getRoleOfUser(int user_id) throws DataAccessException;
	
	/**
	 * Get role of a user in a given organization
	 * @param user_id
	 * @param org_id
	 * @return a {@link UserRole} instance
	 * @throws DataAccessException
	 */
	public UserRole getRoleOfUser(int user_id, int org_id) throws DataAccessException;
	
	/**
	 * Create a new User_Role data
	 * @param user_id
	 * @param org_id
	 * @param role
	 * @throws DataAccessException
	 */
	public void createUserRole(int user_id, int org_id, String role) throws DataAccessException;
	
	/**
	 * Delete a User_Role data
	 * @param ur : {@link UserRole} instance
	 * @throws DataAccessException
	 */
	public void deleteUserRole(UserRole ur) throws DataAccessException;
	
	/**
	 * Get a list of User_Role data where user has id and his/her role is role.
	 * @param user_id
	 * @param role
	 * @return a list of {@link UserRole} instances
	 * @throws DataAccessException
	 */
	public List<UserRole> getUserRoleByIdRole(int user_id, String role) throws DataAccessException;
	

}
