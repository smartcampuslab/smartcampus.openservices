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
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.entities.Method;

/**
 * Method Dao Implementation
 * Find, add, modify and delete Method object from database
 * 
 * @author Giulia Canobbio
 *
 */
@Repository
public class MethodDaoImpl implements MethodDao{
	
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
	 * Retrieve method from database searching by method id
	 * @param id : int method id
	 * @return a {@link Method} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public Method getMethodById(int id) throws DataAccessException {
		return getEntityManager().find(Method.class, id);
	}

	/**
	 * Retrieve method from database searching by method name
	 * @param name : String method name
	 * @return a {@link Method} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public Method getMethodByName(String name) throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Method M WHERE M.name=:name")
				.setParameter("name", name);
		List<Method> ms = q.getResultList();
		if(ms.size()==0){
			return null;
		}
		else return ms.get(0);
	}
	
	/**
	 * Retrieve list of methods for a service.
	 * Search methods by service id
	 * @param service_id : int service id
	 * @return a list of {@link Method} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Method> getMethodByServiceId(int service_id)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Method M WHERE M.serviceId=:id_service")
				.setParameter("id_service", service_id);
		List<Method> ms = q.getResultList();
		return ms;
	}

	/**
	 * Add a new method in database
	 * @param method : {@link Method} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void addMethod(Method method) throws DataAccessException {
		getEntityManager().persist(method);
	}

	/**
	 * Modify an existing method from database
	 * @param method : {@link Method} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void modifyMethod(Method method) throws DataAccessException {
		getEntityManager().merge(method);
		
	}

	/**
	 * Delete an existing method from database
	 * @param method : {@link Method} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void deleteMethod(Method method) throws DataAccessException {
		getEntityManager().remove(method);
	}
}
