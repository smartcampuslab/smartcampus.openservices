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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.Constants;
import eu.trentorise.smartcampus.openservices.Constants.ROLES;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.Tag;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.CatalogManager;

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
	/**
	 * Instance of {@link UserDao} to retrieve user data.
	 */
	@Autowired
	private UserDao userDao;
	/**
	 * Instance of {@link OrganizationDao} to retrieve organization data.
	 */
	@Autowired
	private OrganizationDao orgDao;
	/**
	 * Instance of {@link UserRoleDao} to retrieve role of user data.
	 */
	@Autowired
	private UserRoleDao urDao;	
	
	/**
	 * 
	 * @return instance of entity manager
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
	 * @return List of {@link Service} instance
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
	 * @return List of {@link Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Service> showPublishedService(int firstResult, int maxResult, String param_order) 
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.state!='UNPUBLISH' ORDER BY S."+param_order)
				/*.setParameter("porder", param_order)*/;
		List<Service> s = q.setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		return s;
	}

	/**
	 * Retrieve all user's service from database
	 * @param username : String username
	 * @return List of {@link Service} instance
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
		
		/*Query q = getEntityManager().createQuery("SELECT S FROM Service S " +
				"WHERE S.organization_id IN " +
				"(SELECT Ur.id_org FROM UserRole UR WHERE UR.role=:role AND UR.id_user = " +
					"(SELECT U.id FROM User U WHERE U.username=:username ) " +
				")")
				.setParameter("username",username)
				.setParameter("role", ROLES.ROLE_ORGOWNER.toString());*/
		List<Service> s = q.getResultList();
		return s;
	}

	/**
	 * Retrieve service data from database
	 * Searching by service name
	 * @param service_name : String service name
	 * @return {@link Service} instance
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
	 * @param service : {@link Service}
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void createService(Service service) throws DataAccessException {
		getEntityManager().persist(service);
	}

	/**
	 * Modify an existing service from database
	 * @param service : {@link Service} 
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void modifyService(Service service) throws DataAccessException {
		getEntityManager().merge(service);
	}

	/**
	 * Delete an existing service from database
	 * @param service : {@link Service}
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
	 * @param service_id : int service id
	 * @return {@link Organization} instance
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
	 * @param service_id : int service id
	 * @return {@link User} instance
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
	 * @param service_id : int service id
	 * @return {@link Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public Service getServiceById(int service_id) throws DataAccessException{
		return getEntityManager().find(Service.class, service_id);
	}

	/**
	 * Find a service by its owner
	 * @param id_owner : int service owner id
	 * @return {@link Service} instance
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
	 * @param id_org : int organization id
	 * @return {@link Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Service> getServiceByIdOrg(int id_org, int firstResult,int maxResult, String param_order) 
			throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM Service WHERE organizationId=:idOrg ORDER BY "+param_order)
				.setParameter("idOrg", id_org)
				/*.setParameter("order", param_order)*/;
		List<Service> s = q.setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		return s;
	}

	/**
	 * Retrieve all services but unpublished one
	 * Search by a token in name
	 * @param token : String token
	 * @return a list of {@link Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Service> searchService(String token, int firstResult, int maxResult, String param_order) 
			throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM Service S WHERE (S.name LIKE :token " +
				"OR S.description LIKE :token) AND S.state!='UNPUBLISH' " +
				"ORDER BY :order")
				.setParameter("token", "%"+token+"%")
				.setParameter("order", param_order);
		List<Service> s = q.setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		return s;
	}

	/**
	 * Browse all services but unpublished one by category and tags
	 * @param category : int category id
	 * @param tags : String tags
	 * @return list of {@link Service} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Service> browseService(Integer category, int firstResult, int maxResult, String param_order)
			throws DataAccessException {
		Query q =  getEntityManager().createQuery("FROM Service S WHERE S.category=:category AND " +
				"S.state!='UNPUBLISH' ORDER BY :order")
				.setParameter("category", category)
				.setParameter("order", param_order);
		List<Service> s = q.setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		return s;
	}
	/**
	 * Retrieve Services searching by tag
	 */
	@Transactional
	@Override
	public List<Service> getServiceByTag(String tag, int firstResult, int maxResult, String param_order){
		Query q =  getEntityManager().createQuery("SELECT S FROM Service S JOIN S.tags T WHERE T.name=:tag " +
				"ORDER BY :order")
				.setParameter("tag", tag)
				.setParameter("order", param_order);
		List<Service> list = q.setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		return list;
		
	}

	/**
	 * Retrieve services by category
	 * @param id : int category id
	 * @return list of {@link Service} instance
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

	/**
	 * Count all services saved in database with state different from unpublish
	 */
	@Transactional
	@Override
	public Long countService() throws DataAccessException {
		return (Long) getEntityManager().createQuery("SELECT COUNT(s) FROM Service s WHERE s.state!='UNPUBLISH'").
				getSingleResult();
	}

	@Transactional
	@Override
	public Long countServiceSimpleSearch(String token) throws DataAccessException {
		return (Long) getEntityManager().createQuery("SELECT COUNT(s) FROM Service s WHERE s.name LIKE :token " +
				"AND s.state!='UNPUBLISH'")
				.setParameter("token", "%"+token+"%").getSingleResult();
	}

	@Transactional
	@Override
	public Long countServiceByOrgSearch(int id_org) throws DataAccessException {
		return (Long) getEntityManager().createQuery("SELECT COUNT(s) FROM Service S WHERE S.organizationId=:id_org " +
				"AND s.state!='UNPUBLISH'")
				.setParameter("id_org", id_org).getSingleResult();
	}

	@Transactional
	@Override
	public Long countServiceCategorySearch(int category) throws DataAccessException {
		return (Long) getEntityManager().createQuery("SELECT COUNT(s) FROM Service S WHERE S.category=:category " +
				"AND S.state!='UNPUBLISH' ")
				.setParameter("category", category).getSingleResult();
	}
	
	@Transactional
	@Override
	public Long countServiceTagsSearch(String tag) throws DataAccessException {
		return (Long) getEntityManager().createQuery("SELECT COUNT(S) FROM Service S JOIN S.tags T WHERE T.name=:tag")
				.setParameter("tag", tag).getSingleResult();
	}

	/**
	 * Return a map with tag name and total number of tag
	 */
	@Transactional
	@Override
	public Map<String, Integer> findTagServices() throws DataAccessException {
		Map<String,Integer> res = new HashMap<String, Integer>();
		List<Object[]> results = entityManager
		        .createQuery("SELECT tag.name AS tag, COUNT(tag.name) AS total FROM Tag AS tag GROUP BY tag.name")
		        .getResultList();
		for (Object[] result : results) {
		    String tag = ((String) result[0]).toString();
		    int count = ((Number) result[1]).intValue();
		    res.put(tag, count);
		}		
		return res;
	}

}
