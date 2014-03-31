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
 * Service History Dao Implementation
 * Retrieve, Add, Modify and Delete service history from database
 * 
 * @author Giulia Canobbio
 *
 */
@Repository
public class ServiceHistoryDaoImpl implements ServiceHistoryDao{

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
	 * Retrieve service history by its id
	 * @param id : int service history id
	 * @return {@link ServiceHistory} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public ServiceHistory getServiceHistoryById(int id)
			throws DataAccessException {
		return getEntityManager().find(ServiceHistory.class, id);
	}

	/**
	 * Retrieve service history by service id
	 * @param service_id : int service id
	 * @return list of {@link ServiceHistory} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<ServiceHistory> getServiceHistoryByServiceId(int service_id)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM ServiceHistory SH WHERE SH.id_service=:id_service")
				.setParameter("id_service", service_id);
		List<ServiceHistory> sh = q.getResultList();
		return sh;
	}

	/**
	 * Add a new service history instance in database
	 * @param serviceHistory : {@link ServiceHistory} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void addServiceHistory(ServiceHistory serviceHistory)
			throws DataAccessException {
		getEntityManager().persist(serviceHistory);		
	}

	/**
	 * Modify an existing service history instance in database
	 * @param serviceHistory : {@link ServiceHistory} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void modifyServiceHistory(ServiceHistory serviceHistory)
			throws DataAccessException {
		getEntityManager().merge(serviceHistory);
	}

	/**
	 * Delete an existing service history instance in database
	 * @param serviceHistory : {@link ServiceHistory} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void deleteServiceHistory(ServiceHistory serviceHistory)
			throws DataAccessException {
		getEntityManager().remove(getEntityManager().merge(serviceHistory));
	}

	/**
	 * Retrieve service history by organization id
	 * @param org_id : int organization id
	 * @return list of {@link ServiceHistory} instances for given organization
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<ServiceHistory> getServiceHistoryByOrgId(int org_id)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM ServiceHistory SH WHERE SH.id_service IN (" +
				"SELECT S.id FROM Service S WHERE S.organizationId=:organization_id )")
				.setParameter("organization_id", org_id);
		return (List<ServiceHistory>)q.getResultList();
	}

	/**
	 * Retrieve n news on service
	 */
	@Transactional
	@Override
	public List<ServiceHistory> getNews(int n) throws DataAccessException{
		/*Date now = new Date();
		System.out.println("Now + 1 days: "+now);
		
		Date start = now;
		start.setTime(start.getTime() - (5 * 1000L * 60L * 60L * 24L ));
		System.out.println("Start - 6 days: "+start);
				
		//query select news with interval of 5 days
		Query q = getEntityManager().createQuery("FROM ServiceHistory S WHERE S.date BETWEEN :start AND " +
				"NOW() " +
				"AND S.id = ( SELECT MAX(S1.id) FROM ServiceHistory S1 WHERE S1.operation = S.operation " +
				"AND S1.id_service = S.id_service AND S1.id_serviceMethod = S.id_serviceMethod " +
				"AND S1.date BETWEEN :start AND " +
				"NOW() )")
				.setParameter("start", start)
				;//.setParameter("end", now);
		*/
		Query q = getEntityManager().createQuery("FROM ServiceHistory S ORDER BY S.id DESC");
		List<ServiceHistory> news = q.setMaxResults(n).getResultList();
		System.out.println("News size: "+news.size());
		return news;
	}

}
