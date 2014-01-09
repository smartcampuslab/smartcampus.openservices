package com.openserviceproject.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.openserviceproject.entities.Method;

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
		Query q = getEntityManager().createQuery("FROM Method M WHERE M.id_service=:id_service")
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
		getEntityManager().remove(getEntityManager().merge(method));
	}
}
