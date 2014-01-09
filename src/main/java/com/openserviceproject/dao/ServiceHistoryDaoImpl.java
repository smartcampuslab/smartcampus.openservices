package com.openserviceproject.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.openserviceproject.entities.ServiceHistory;

@Repository
public class ServiceHistoryDaoImpl implements ServiceHistoryDao{

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
	public ServiceHistory getServiceHistoryById(int id)
			throws DataAccessException {
		return getEntityManager().find(ServiceHistory.class, id);
	}

	@Transactional
	@Override
	public List<ServiceHistory> getServiceHistoryByServiceId(int service_id)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM ServiceHistory SH WHERE SH.id_service=:id_service")
				.setParameter("id_service", service_id);
		List<ServiceHistory> sh = q.getResultList();
		return sh;
	}

	@Transactional
	@Override
	public void addServiceHistory(ServiceHistory serviceHistory)
			throws DataAccessException {
		getEntityManager().persist(serviceHistory);		
	}

	@Transactional
	@Override
	public void modifyServiceHistory(ServiceHistory serviceHistory)
			throws DataAccessException {
		getEntityManager().merge(serviceHistory);
	}

	@Transactional
	@Override
	public void deleteServiceHistory(ServiceHistory serviceHistory)
			throws DataAccessException {
		getEntityManager().remove(getEntityManager().merge(serviceHistory));
	}

	@Transactional
	@Override
	public List<ServiceHistory> getServiceHistoryByOrgId(int org_id)
			throws DataAccessException {
		getEntityManager().createQuery("FROM ServiceHistory SH WHERE SH.id_service = (" +
				"SELECT S.id FROM Service S WHERE S.id_org=:id_org )")
				.setParameter("id_org", org_id);
		return null;
	}

}
