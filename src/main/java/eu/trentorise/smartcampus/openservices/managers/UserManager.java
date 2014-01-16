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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.entities.User;

/**
 * 
 * @author Giulia
 *
 */
@Component
@Transactional
public class UserManager {
	
	@Autowired
	private UserDao userDao;
	
	/**
	 * Get user data by id
	 * @param id
	 * @return a {@link User} instance
	 */
	public User getUserById( int id){
		User user = userDao.getUserById(id);
		user.setPassword(null);
		return user;
	}
	
	/**
	 * Get user by username
	 * @param username
	 * @return a {@link User} instance
	 */
	public User getUserByUsername(String username){
		User user = userDao.getUserByUsername(username);
		user.setPassword(null);
		return user;
	}
	
	/**
	 * Add a new user in database
	 * Return new user
	 * @param user
	 * @return a {@link User} instance
	 */
	public User createUser(User user){
		user.setEnabled(0);
		user.setRole("ROLE_NORMAL");
		userDao.addUser(user);
		return userDao.getUserByUsername(user.getUsername());
	}
	
	public void enableUser(){
		//TODO
	}
	
	/**
	 * Modify user data
	 * Profile and email
	 * @param username
	 * @param user
	 * @return a {@link User} instance
	 */
	public User modifyUserData(String username, User user){
		User sessionU = userDao.getUserByUsername(username);
		userDao.modifyUser(sessionU.getId(), user);
		User userN = userDao.getUserById(sessionU.getId());
		userN.setPassword(null);
		return userN;
		
	}
	
	/**
	 * Disable a user
	 * Only for admin
	 * @param username
	 * @return a {@link User} instance
	 */
	public User disabledUser(String username){
		User user = userDao.getUserByUsername(username);
		userDao.disableUser(user);
		
		User userN = userDao.getUserById(user.getId());
		userN.setPassword(null);
		return userN;
	}
}
