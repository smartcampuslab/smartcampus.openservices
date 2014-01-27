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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.User;

/**
 * Service Dao Implementation
 * Retrieve, add, modify, delete service object from database
 * 
 * @author Giulia Canobbio
 *
 */
@Repository
public class ServiceDaoImpl implements ServiceDao{
	
	@PersistenceContext(unitName="JpaPersistenceUnit")
	protected EntityManager entityManager;
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private OrganizationDao orgDao;
	@Autowired
	private UserRoleDao urDao;	
	
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
	 * Retrieve all service data from database
	 * Publish, Unpublish and Deprecated service.
	 * @return List of {@Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Service> showService() throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Service");
		List<Service> s = q.getResultList();
		return s;
	}
	
	/**
	 * Retrieve all service data but unpublished from database
	 * @return List of {@Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Service> showPublishedService() throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.state!='UNPUBLISH'");
		List<Service> s = q.getResultList();
		return s;
	}

	/**
	 * Retrieve all user's service from database
	 * @param String username
	 * @return List of {@Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Service> showMyService(String username)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Service S " +
				"WHERE S.creatorId = (" +
				"SELECT U.id FROM User U WHERE U.username=:username " +
				")")
				.setParameter("username",username);
		List<Service> s = q.getResultList();
		return s;
	}

	/**
	 * Retrieve service data from database
	 * Searching by service name
	 * @param String service name
	 * @return {@Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public Service useService(String service_name) throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.name=:name")
				.setParameter("name",service_name);
		List<Service> s = q.getResultList();
		if(s.size()==0){
			return null;
		}
		else return s.get(0);
	}

	/**
	 * Add a new service in database
	 * @param {@Service} service
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void createService(Service service) throws DataAccessException {
		getEntityManager().persist(service);
	}

	/**
	 * Modify an existing service from database
	 * @param {@Service} service
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void modifyService(Service service) throws DataAccessException {
		getEntityManager().merge(service);
	}

	/**
	 * Delete an existing service from database
	 * @param {@Service} service
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void deleteService(Service service) throws DataAccessException {
		getEntityManager().remove(getEntityManager().merge(service));
	}

	/**
	 * Retrieve organization data for a particular service
	 * Search by service id
	 * @param int service id
	 * @return {@Organization} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public Organization getOrganizationofService(int service_id) throws DataAccessException{
		Service service = getEntityManager().find(Service.class, service_id);
		return getEntityManager().find(Organization.class, service.getOrganizationId());
	}
	
	/**
	 * Retrieve user data for a particular service in which user is owner
	 * @param int service id
	 * @return {@User} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public User getOwnerOfService(int service_id) throws DataAccessException{
		Service service = getEntityManager().find(Service.class, service_id);
		return getEntityManager().find(User.class, service.getCreatorId());
	}

	/**
	 * Find service by its id
	 * @param int service id
	 * @return {@Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public Service getServiceById(int service_id) throws DataAccessException{
		return getEntityManager().find(Service.class, service_id);
	}

	/**
	 * Find a service by its owner
	 * @param int service owner id
	 * @return {@Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Service> getServiceByIdOwner(int id_owner) throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.creatorId=:id_owner")
				.setParameter("id_owner", id_owner);
		List<Service> s = q.getResultList();
		return s;
	}

	/**
	 * Find a service by its organization id
	 * @param int organization id
	 * @return {@Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Service> getServiceByIdOrg(int id_org) throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.organizationId=:id_org")
				.setParameter("id_org", id_org);
		List<Service> s = q.getResultList();
		return s;
	}

	/**
	 * Retrieve all services but unpublished one
	 * Search by a token in name
	 * @param String token
	 * @return a list of {@Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Service> searchService(String token) throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.name LIKE :token AND S.state!='UNPUBLISH'")
				.setParameter("token", "%"+token+"%");
		List<Service> s = q.getResultList();
		return s;
	}

	/**
	 * Browse all services but unpublished one by category and tags
	 * @param int category id
	 * @param String tags
	 * @return list of {@Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Service> browseService(Integer category, String tags) throws DataAccessException {
		Query q = null;
		if(category!=null && tags!=null){
			q = getEntityManager().createQuery("FROM Service S WHERE S.category=:category AND " +
				"S.tags LIKE :tags AND S.state!='UNPUBLISH'")
				.setParameter("category", category)
				.setParameter("tags", "%"+tags+"%");
		}
		else if(category==null && tags!=null){
			q = getEntityManager().createQuery("FROM Service S WHERE S.tags LIKE :tags AND S.state!='UNPUBLISH'")
					.setParameter("tags", "%"+tags+"%");
		}
		else if(category!=null && tags==null){
			q = getEntityManager().createQuery("FROM Service S WHERE S.category=:category AND S.state!='UNPUBLISH'")
					.setParameter("category", category);
		}
		List<Service> s = q.getResultList();
		return s;
	}

	/**
	 * Retrieve services by category
	 * @param int category id
	 * @return list of {@Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Service> findByCategory(int id) {
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.category=:category")
				.setParameter("category", id);
		List<Service> s = q.getResultList();
		return s;
	}


	/**
	 * Retrieve how many services there are group by categories
	 * @return Map<Integer,Integer> res
	 * @throws DataAccessException
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Map<Integer,Integer> findCategoryServices() {
		Map<Integer,Integer> res = new HashMap<Integer, Integer>();
		List<Object[]> results = entityManager
		        .createQuery("SELECT s.category AS category, COUNT(s) AS total FROM Service AS s WHERE s.state != 'UNPUBLISH' GROUP BY s.category")
		        .getResultList();
		for (Object[] result : results) {
		    int category = ((Integer) result[0]).intValue();
		    int count = ((Number) result[1]).intValue();
		    res.put(category, count);
		}		
		return res;
	}

}
