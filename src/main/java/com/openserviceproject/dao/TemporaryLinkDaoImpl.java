package com.openserviceproject.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.openserviceproject.entities.TemporaryLink;

@Repository
public class TemporaryLinkDaoImpl implements TemporaryLinkDao{

	@PersistenceContext(name="JpaPersistenceUnit")
	protected EntityManager entityManager;

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional
	@Override
	public TemporaryLink getTLByKey(String key) throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM TemporaryLink Tl WHERE Tl.key=:key")
				.setParameter("key", key);
		List<TemporaryLink> ltl = q.getResultList();
		if(ltl.size()==0){
			return null;
		}
		else return ltl.get(0);
	}

	@Transactional
	@Override
	public void save(TemporaryLink tl) throws DataAccessException {
		getEntityManager().persist(tl);
		
	}

	@Transactional
	@Override
	public void delete(String key) throws DataAccessException{
		TemporaryLink tl = getTLByKey(key);
		getEntityManager().remove(getEntityManager().merge(tl));
		
	}
	
	
}
