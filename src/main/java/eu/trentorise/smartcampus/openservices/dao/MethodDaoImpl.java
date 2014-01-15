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

@Repository
public class MethodDaoImpl implements MethodDao{
	
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
	public Method getMethodById(int id) throws DataAccessException {
		return getEntityManager().find(Method.class, id);
	}

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
	
	@Transactional
	@Override
	public List<Method> getMethodByServiceId(int service_id)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Method M WHERE M.serviceId=:id_service")
				.setParameter("id_service", service_id);
		List<Method> ms = q.getResultList();
		return ms;
	}


	@Transactional
	@Override
	public void addMethod(Method method) throws DataAccessException {
		getEntityManager().persist(method);
	}

	@Transactional
	@Override
	public void modifyMethod(Method method) throws DataAccessException {
		getEntityManager().merge(method);
		
	}

	@Transactional
	@Override
	public void deleteMethod(Method method) throws DataAccessException {
		getEntityManager().remove(method);
	}
}
