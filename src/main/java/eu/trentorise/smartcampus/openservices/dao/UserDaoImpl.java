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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.entities.*;

/**
 * User Dao Implementation
 * Retrieve, add, modify and delete User data
 * 
 * @author Giulia Canobbio
 *
 */
@Repository
public class UserDaoImpl implements UserDao{
	
	@PersistenceContext(unitName="JpaPersistenceUnit")
	protected EntityManager entityManager;

	/**
	 * 
	 * @return entity manager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Set entity manager
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Retrieve all user data from database
	 * @return list of {@link User} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<User> getUsers() throws DataAccessException{
		// TODO Auto-generated method stub
		Query q = getEntityManager().createQuery("FROM User");
		List<User> users = q.getResultList();
		if(users.size()==0){
			return null;
		}
		else return users;
	}

	/**
	 * Retrieve user data by user id
	 * @param id : int user id
	 * @return {@link User} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public User getUserById(int id) throws DataAccessException{
		return getEntityManager().find(User.class, id);
	}

	/**
	 * Retrieve user data by username
	 * @param username : String
	 * @return {@link User} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public User getUserByUsername(String username) throws DataAccessException{
		// TODO Auto-generated method stub
		Query q = getEntityManager().createQuery("FROM User WHERE username=:username")
				.setParameter("username", username);
		List<User> users = q.getResultList();
		if(users.size()==0){
			return null;
		}
		else return users.get(0);
	}

	/**
	 * Modify an existing user profile from database
	 * if password is different (not null) then save new password in database
	 * @param user_id : int
	 * @param user : {@link User} instance with modified fields
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void modifyUser(int user_id, User user) throws DataAccessException{
		User oldUser = getUserById(user_id);
		//new password
		if(user.getPassword()!=null && !oldUser.getPassword().equalsIgnoreCase(user.getPassword())){
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = ((BCryptPasswordEncoder) passwordEncoder)
					.encode(user.getPassword());
			user.setPassword(encodedPassword);
		}
		oldUser.setProfile(user.getProfile());
		oldUser.setEmail(user.getEmail());
		getEntityManager().merge(oldUser);
	}

	/**
	 * Add a new user to database
	 * @param user : {@link User} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void addUser(User user) throws DataAccessException{
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); 
		String encodedPassword = ((BCryptPasswordEncoder) passwordEncoder).encode(user.getPassword());
		user.setPassword(encodedPassword);
		getEntityManager().persist(user);
	}

	/**
	 * Disable a user
	 * @param user_id : int
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void disableUser(int user_id) throws DataAccessException{
		User oldUser = getUserById(user_id);
		oldUser.setEnabled(0);
		getEntityManager().merge(oldUser);
		
	}
	
	/**
	 * Enable a user
	 * @param user_id : int
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void enableUser(int user_id) throws DataAccessException{
		User oldUser = getUserById(user_id);
		oldUser.setEnabled(1);
		getEntityManager().merge(oldUser);
	}

}
