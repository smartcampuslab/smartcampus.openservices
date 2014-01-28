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

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.dao.TemporaryLinkDao;
import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.entities.TemporaryLink;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.support.GenerateKey;

/**
 * Manager that retrieves, adds, modifies and deletes user data
 * calling dao classes.
 * 
 * @author Giulia Canobbio
 *
 */
@Component
@Transactional
public class UserManager {
	/**
	 * Instance of {@link UserDao} to retrieve data of user.
	 */
	@Autowired
	private UserDao userDao;
	/**
	 * Instance of {@link TemporaryLinkManager} to retrieve data of temporary link.
	 */
	@Autowired
	private TemporaryLinkDao tlDao;
	
	/**
	 * Get user data by id
	 * @param id : int id of user 
	 * @return a {@link User} instance
	 */
	public User getUserById( int id){
		try{ 
			return userDao.getUserById(id);
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Get user by username
	 * @param username : String username of user
	 * @return a {@link User} instance
	 */
	public User getUserByUsername(String username){
		try{
			return userDao.getUserByUsername(username);
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Add a new user in database.
	 * Retrieves new user.
	 * @param user : {@link User} instance
	 * @return a {@link User} instance
	 */
	public User createUser(User user){
		user.setEnabled(0);
		user.setRole("ROLE_NORMAL");
		try{
			userDao.addUser(user);
			return userDao.getUserByUsername(user.getUsername());
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Modify user data.
	 * User can modify only profile data and email.
	 * @param username : String username of user
	 * @param user : {@link User} instance
	 * @return a {@link User} instance
	 */
	public User modifyUserData(String username, User user){
		try{
			User sessionU = userDao.getUserByUsername(username);
			userDao.modifyUser(sessionU.getId(), user);
			User userN = userDao.getUserById(sessionU.getId());
			return userN;
		}catch(DataAccessException d){
			return null;
		}
		
	}
	
	
	/**
	 * Enable user.
	 * This can be done by verifying email.
	 * @param username : String username of user
	 * @return a {@link User} instance of enabled user
	 */
	public User enableUser(String username){
		try{
			User user = userDao.getUserByUsername(username);
			userDao.enableUser(user.getId());
			User userN = userDao.getUserById(user.getId());
			return userN;
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Disable a user.
	 * Only admin user can disable a user.
	 * @param username : String username of user that admin wants to disable.
	 * @return a {@link User} instance of disabled user
	 */
	public User disabledUser(String username){
		try{
			User user = userDao.getUserByUsername(username);
			userDao.disableUser(user.getId());
		
			User userN = userDao.getUserById(user.getId());
			return userN;
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Save in temporary link table, key and email address of user.
	 * After doing registration, link + key value to enable his/her account is sent by email.
	 * @param username : String username of user that we want to verify email address
	 * @return String value, it is key value if it is ok, else it is null. In case user is enable then 
	 * a security exception is threw.
	 */
	public String addKeyVerifyEmail(String username){
		String key;
		try{
			User user = userDao.getUserByUsername(username);
			//check if user is enable or not
			if(user.getEnabled()==0){
				// Generate a key
				GenerateKey g = new GenerateKey();
				key = g.getPriv().toString().split("@")[1];
				//save in temporary link table
				TemporaryLink tl = new TemporaryLink();
				tl.setId_org(0);
				tl.setKey(key);
				tl.setEmail(user.getEmail());
				tlDao.save(tl);
				return key;
				
			}else{
				throw new SecurityException();
			}
			
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Enable user account by checking user email and key.
	 * After finding key in database, this object is deleted.
	 * @param username : String username of user that wants to enable his/her account
	 * @param key : String key sent by email for verifying email address
	 * @return {@link User} instance of enabled account otherwise if key does not exist then EntityNotFoundException
	 */
	public User enableUserAfterVerification(String username, String key){
		try{
		User user = userDao.getUserByUsername(username);
		TemporaryLink tl = tlDao.getTLByKey(key);
		if(tl!=null && user.getEmail().equalsIgnoreCase(tl.getEmail()) && tl.getId_org()==0 && tl.getRole()==null){
			tlDao.delete(key);
			return enableUser(username);
		}else{
			throw new EntityNotFoundException();
		}
		}catch(DataAccessException d){
			return null;
		}
	}
}
