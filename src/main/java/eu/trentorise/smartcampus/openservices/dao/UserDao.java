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

/**
 * User Dao Interface
 * 
 * @author Giulia Canobbio
 *
 */
public interface UserDao {
	
	/**
	 * Get a list of users.
	 * @return list of {@link User} instances
	 * @throws DataAccessException
	 */
	public List<User> getUsers() throws DataAccessException;
	
	/**
	 * Get a user by id.
	 * @param id
	 * @return a {@link User} instance
	 * @throws DataAccessException
	 */
	public User getUserById(int id) throws DataAccessException;
	
	/**
	 * Get a user by username, which is not null and unique.
	 * @param username
	 * @return a {@link User} instance
	 * @throws DataAccessException
	 */
	public User getUserByUsername(String username) throws DataAccessException;
	
	/**
	 * Modify User data.
	 * @param user_id : int
	 * @param user : a {@link User} instance
	 * @throws DataAccessException
	 */
	public void modifyUser(int user_id, User user) throws DataAccessException;
	
	/**
	 * Add a new user.
	 * @param user : a {@link User} instance
	 * @throws DataAccessException
	 */
	public void addUser(User user) throws DataAccessException;
	
	/**
	 * disable a user.
	 * @param id
	 * @throws DataAccessException
	 */
	public void disableUser(int user_id) throws DataAccessException;
	
	/**
	 * Enable user.
	 * @param user_id
	 * @throws DataAccessException
	 */
	public void enableUser(int user_id) throws DataAccessException;

}
