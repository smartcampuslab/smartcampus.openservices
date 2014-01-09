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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.entities.*;

@Repository
public class UserDaoImpl implements UserDao{
	
	@PersistenceContext(unitName="JpaPersistenceUnit")
	protected EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

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

	@Transactional
	@Override
	public User getUserById(int id) throws DataAccessException{
		return getEntityManager().find(User.class, id);
	}

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

	@Transactional
	@Override
	public void modifyUser(User user) throws DataAccessException{
		ShaPasswordEncoder passw = new ShaPasswordEncoder();
		String newPassw = passw.encodePassword(user.getPassword(), null);
		user.setPassword(newPassw);
		getEntityManager().merge(user);
	}

	@Transactional
	@Override
	public void addUser(User user) throws DataAccessException{
		ShaPasswordEncoder passw = new ShaPasswordEncoder();
		String newPassw = passw.encodePassword(user.getPassword(), null);
		user.setPassword(newPassw);
		getEntityManager().persist(user);
	}

	@Transactional
	@Override
	public void disableUser(User user) throws DataAccessException{
		user.setEnabled(0);
		getEntityManager().merge(user);
		
	}

}
