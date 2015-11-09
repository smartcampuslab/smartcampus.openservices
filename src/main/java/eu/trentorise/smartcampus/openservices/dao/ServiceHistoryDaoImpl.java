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

import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;

/**
 * Service History Dao Implementation Retrieve, Add, Modify and Delete service
 * history from database
 * 
 * @author Giulia Canobbio
 * 
 */
@Repository
public class ServiceHistoryDaoImpl implements ServiceHistoryDao {
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
	 * This method retrieves service history by its id.
	 */
	@Transactional
	@Override
	public ServiceHistory getServiceHistoryById(int id)
			throws DataAccessException {
		return getEntityManager().find(ServiceHistory.class, id);
	}

	/**
	 * This method retrieves service history by service id.
	 */
	@Transactional
	@Override
	public List<ServiceHistory> getServiceHistoryByServiceId(int service_id)
			throws DataAccessException {
		Query q = getEntityManager().createQuery(
				"FROM ServiceHistory SH WHERE SH.id_service=:id_service")
				.setParameter("id_service", service_id);
		List<ServiceHistory> sh = q.getResultList();
		return sh;
	}

	/**
	 * This method adds a new service history instance in database.
	 */
	@Transactional
	@Override
	public void addServiceHistory(ServiceHistory serviceHistory)
			throws DataAccessException {
		getEntityManager().persist(serviceHistory);
	}

	/**
	 * This method modifies an existing service history instance in database.
	 */
	@Transactional
	@Override
	public void modifyServiceHistory(ServiceHistory serviceHistory)
			throws DataAccessException {
		getEntityManager().merge(serviceHistory);
	}

	/**
	 * This method deletes an existing service history instance in database.
	 */
	@Transactional
	@Override
	public void deleteServiceHistory(ServiceHistory serviceHistory)
			throws DataAccessException {
		getEntityManager().remove(getEntityManager().merge(serviceHistory));
	}

	/**
	 * This method retrieves service history by organization id.
	 */
	@Transactional
	@Override
	public List<ServiceHistory> getServiceHistoryByOrgId(int org_id)
			throws DataAccessException {
		Query q = getEntityManager()
				.createQuery(
						"FROM ServiceHistory SH WHERE SH.id_service IN ("
								+ "SELECT S.id FROM Service S WHERE S.organizationId=:organization_id )")
				.setParameter("organization_id", org_id);
		return (List<ServiceHistory>) q.getResultList();
	}

	/**
	 * This method retrieves n news on service.
	 */
	@Transactional
	@Override
	public List<ServiceHistory> getNews(int n) throws DataAccessException {
		Query q = getEntityManager().createQuery(
				"FROM ServiceHistory S Order BY S.id DESC");
		List<ServiceHistory> news = q.setMaxResults(n).getResultList();
		System.out.println("News size: " + news.size());
		return news;
	}

}
