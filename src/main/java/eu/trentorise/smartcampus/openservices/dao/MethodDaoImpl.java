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
 * Method Dao Implementation.
 * Find, add, modify and delete Method object from database.
 * 
 * @author Giulia Canobbio
 *
 */
@Repository
public class MethodDaoImpl implements MethodDao{
	
	/**
	 * Instance of {@link EntityManager}
	 */
	@PersistenceContext(unitName="JpaPersistenceUnit")
	protected EntityManager entityManager;
	
	/**
	 * @return entity manager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * This method retrieves method from database searching by method id.
	 */
	@Transactional
	@Override
	public Method getMethodById(int id) throws DataAccessException {
		return getEntityManager().find(Method.class, id);
	}

	/**
	 * This method retrieves method from database searching by method name.
	 */
	@Transactional
	@Override
	public Method getMethodByName(String name, int service_id) throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Method M WHERE M.name=:name AND service_id=:id")
				.setParameter("name", name)
				.setParameter("id", service_id);
		List<Method> ms = q.getResultList();
		if(ms.size()==0){
			return null;
		}
		else return ms.get(0);
	}
	
	/**
	 * This method retrieves list of methods for a service.
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
	 * This method adds a new method in database.
	 */
	@Transactional
	@Override
	public void addMethod(Method method) throws DataAccessException {
		getEntityManager().persist(method);
	}

	/**
	 * This method modifies an existing method from database.
	 */
	@Transactional
	@Override
	public void modifyMethod(Method method) throws DataAccessException {
		getEntityManager().merge(method);
		
	}

	/**
	 * This method deletes an existing method from database.
	 */
	@Transactional
	@Override
	public void deleteMethod(Method method) throws DataAccessException {
		getEntityManager().remove(method);
	}
}
