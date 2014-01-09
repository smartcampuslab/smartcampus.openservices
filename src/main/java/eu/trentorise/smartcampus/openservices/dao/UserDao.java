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

public interface UserDao {
	
	/**
	 * Get a list of users
	 * @return
	 */
	public List<User> getUsers() throws DataAccessException;
	
	/**
	 * Get a user by id
	 * @param id
	 * @return
	 */
	public User getUserById(int id) throws DataAccessException;
	
	/**
	 * Get a user by username, which is not null and unique
	 * @param username
	 * @return
	 */
	public User getUserByUsername(String username) throws DataAccessException;
	
	/**
	 * Modify User data
	 * @param user
	 * @throws DataAccessException
	 */
	public void modifyUser(User user) throws DataAccessException;
	
	/**
	 * Add a new user
	 * @param user
	 */
	public void addUser(User user) throws DataAccessException;
	
	/**
	 * disable a user
	 * @param id
	 */
	public void disableUser(User user) throws DataAccessException;

}
