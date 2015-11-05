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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.entities.User;

/**
 * User Dao Implementation Retrieve, add, modify and delete User data
 * 
 * @author Giulia Canobbio
 * 
 */
@Repository
public class UserDaoImpl implements UserDao {
	/**
	 * Instance of {@link EntityManager}
	 */
	@PersistenceContext(unitName = "JpaPersistenceUnit")
	protected EntityManager entityManager;

	/**
	 * 
	 * @return entity manager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * This method retrieves all user data from database.
	 */
	@Transactional
	@Override
	public List<User> getUsers() throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM User");
		List<User> users = q.getResultList();
		if (users.size() == 0) {
			return null;
		} else
			return users;
	}

	/**
	 * This method retrieves user data by user id.
	 */
	@Transactional
	@Override
	public User getUserById(int id) throws DataAccessException {
		return getEntityManager().find(User.class, id);
	}

	/**
	 * This method retrieves user data by username.
	 */
	@Transactional
	@Override
	public User getUserByUsername(String username) throws DataAccessException {
		Query q = getEntityManager().createQuery(
				"FROM User WHERE username=:username").setParameter("username",
				username);
		List<User> users = q.getResultList();
		if (users.size() == 0) {
			return null;
		} else
			return users.get(0);
	}

	/**
	 * This method modifies an existing user profile from database.
	 */
	@Transactional
	@Override
	public void modifyUser(int userId, User user) throws DataAccessException {
		User oldUser = getUserById(userId);
		oldUser.setProfile(user.getProfile());
		oldUser.setEmail(user.getEmail());
		getEntityManager().merge(oldUser);
	}

	/**
	 * This method adds a new user to database.
	 */
	@Transactional
	@Override
	public void addUser(User user) throws DataAccessException {
		String passw = user.getPassword();
		if (passw != null) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = ((BCryptPasswordEncoder) passwordEncoder)
					.encode(passw);
			user.setPassword(encodedPassword);
		}
		getEntityManager().persist(user);
	}

	/**
	 * This method changes user's status to disabled (zero value).
	 */
	@Transactional
	@Override
	public void disableUser(int userId) throws DataAccessException {
		User oldUser = getUserById(userId);
		oldUser.setEnabled(0);
		getEntityManager().merge(oldUser);

	}

	/**
	 * This method changes user's status to enabled (one value).
	 */
	@Transactional
	@Override
	public void enableUser(int userId) throws DataAccessException {
		User oldUser = getUserById(userId);
		oldUser.setEnabled(1);
		getEntityManager().merge(oldUser);
	}

	/**
	 * This method checks if an email address is already saved in db.
	 */
	@Transactional
	@Override
	public boolean isEmailAlreadyUse(String email) throws DataAccessException {
		Query q = getEntityManager()
				.createQuery("FROM User WHERE email=:email").setParameter(
						"email", email);
		if (q.getResultList().size() > 0
				&& (User) q.getResultList().get(0) != null) {
			return true;
		}
		return false;
	}

	/**
	 * This method retrieves user by his/her email address.
	 */
	@Transactional
	@Override
	public User getUserByEmail(String email) throws DataAccessException {
		Query q = getEntityManager()
				.createQuery("FROM User WHERE email=:email").setParameter(
						"email", email);
		List<User> users = q.getResultList();
		if (users.size() == 0) {
			return null;
		} else
			return users.get(0);
	}

	/**
	 * This method modifies user's password.
	 */
	@Transactional
	@Override
	public boolean modifyPassword(String username, String newPassw)
			throws DataAccessException {
		User user = getUserByUsername(username);
		// new password
		if (newPassw != null && !newPassw.equalsIgnoreCase("")) {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = ((BCryptPasswordEncoder) passwordEncoder)
					.encode(newPassw);
			user.setPassword(encodedPassword);
			getEntityManager().merge(user);
			return true;
		}
		return false;
	}

}
