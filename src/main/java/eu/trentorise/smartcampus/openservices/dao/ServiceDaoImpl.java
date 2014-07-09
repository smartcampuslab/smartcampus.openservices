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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.Constants;
import eu.trentorise.smartcampus.openservices.Constants.ORDER;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.User;

/**
 * Service Dao Implementation Retrieve, add, modify, delete service object from
 * database
 * 
 * @author Giulia Canobbio
 * 
 */
@Repository
public class ServiceDaoImpl implements ServiceDao {
	/**
	 * Instance of {@link EntityManager}
	 */
	@PersistenceContext(unitName = "JpaPersistenceUnit")
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
	 * This method retrieves all service data from database.
	 * It does not matter if a service is Published, Unpublished and Deprecated
	 * service.
	 */
	@Transactional
	@Override
	public List<Service> showService() throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Service");
		List<Service> s = q.getResultList();
		return s;
	}

	/**
	 * This method retrieves all service data but unpublished from database.
	 */
	@Transactional
	@Override
	public List<Service> showPublishedService(int firstResult, int maxResult, ORDER param_order) throws DataAccessException {
		String queryString = "FROM Service S WHERE S.state!='UNPUBLISH' ORDER BY S."
				+ (ORDER.namedesc.equals(param_order) ? "name DESC" : ORDER.name.toString());

		if (ORDER.date.equals(param_order)) {
			queryString = "SELECT S FROM Service S, ServiceHistory SH WHERE S.state!='UNPUBLISH'"
					+ " AND S.id=SH.id_service AND SH.id_serviceMethod=0 AND SH.operation='ADD'" + "ORDER BY SH.date DESC";
		}

		Query q = getEntityManager().createQuery(queryString);
		List<Service> s = q.setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		return s;
	}

	/**
	 * This method retrieves all user's service from database.
	 */
	@Transactional
	@Override
	public List<Service> showMyService(String username) throws DataAccessException {
		Query q = getEntityManager().createQuery(
				"FROM Service S " + "WHERE S.creatorId = (" + "SELECT U.id FROM User U WHERE U.username=:username " + ")")
				.setParameter("username", username);

		/*
		 * Query q = getEntityManager().createQuery("SELECT S FROM Service S " +
		 * "WHERE S.organization_id IN " +
		 * "(SELECT Ur.id_org FROM UserRole UR WHERE UR.role=:role AND UR.id_user = "
		 * + "(SELECT U.id FROM User U WHERE U.username=:username ) " + ")")
		 * .setParameter("username",username) .setParameter("role",
		 * ROLES.ROLE_ORGOWNER.toString());
		 */
		List<Service> s = q.getResultList();
		return s;
	}

	/**
	 * This method retrieves service data from database Searching by service name.
	 */
	@Transactional
	@Override
	public Service useService(String service_name) throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.name=:name").setParameter("name", service_name);
		List<Service> s = q.getResultList();
		if (s.size() == 0) {
			return null;
		} else {
			return s.get(0);
		}
	}

	/**
	 * This method adds a new service in database.
	 */
	@Transactional
	@Override
	public void createService(Service service) throws DataAccessException {
		getEntityManager().persist(service);
	}

	/**
	 * This method modifies an existing service from database.
	 */
	@Transactional
	@Override
	public void modifyService(Service service) throws DataAccessException {
		getEntityManager().merge(service);
	}

	/**
	 * This method deletes an existing service from database.
	 */
	@Transactional
	@Override
	public void deleteService(Service service) throws DataAccessException {
		getEntityManager().remove(getEntityManager().merge(service));
	}

	/**
	 * This method retrieves organization data for a particular service searching by service id.
	 */
	@Transactional
	@Override
	public Organization getOrganizationofService(int service_id) throws DataAccessException {
		Service service = getEntityManager().find(Service.class, service_id);
		return getEntityManager().find(Organization.class, service.getOrganizationId());
	}

	/**
	 * This method retrieves user data for a particular service in which user is owner.
	 */
	@Transactional
	@Override
	public User getOwnerOfService(int service_id) throws DataAccessException {
		Service service = getEntityManager().find(Service.class, service_id);
		return getEntityManager().find(User.class, service.getCreatorId());
	}

	/**
	 * This method finds service by its id.
	 */
	@Transactional
	@Override
	public Service getServiceById(int service_id) throws DataAccessException {
		return getEntityManager().find(Service.class, service_id);
	}

	/**
	 * This method finds a service by its owner.
	 */
	@Transactional
	@Override
	public List<Service> getServiceByIdOwner(int id_owner) throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.creatorId=:id_owner").setParameter("id_owner",
				id_owner);
		List<Service> s = q.getResultList();
		return s;
	}

	/**
	 * This method finds a service by its organization id.
	 */
	@Transactional
	@Override
	public List<Service> getServiceByIdOrg(int id_org, int firstResult, int maxResult, String param_order)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Service WHERE organizationId=:idOrg ORDER BY " + param_order)
				.setParameter("idOrg", id_org)
		/* .setParameter("order", param_order) */;
		List<Service> s = q.setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		return s;
	}

	/**
	 * This method retrieves all services but unpublished one Search by a token in name.
	 */
	@Transactional
	@Override
	public List<Service> searchService(String token, int firstResult, int maxResult, ORDER param_order)
			throws DataAccessException {

		String queryString = "FROM Service S WHERE (S.name LIKE :token OR S.description LIKE :token)"
				+ " AND S.state!='UNPUBLISH' " + "ORDER BY S."
				+ (ORDER.namedesc.equals(param_order) ? "name DESC" : ORDER.name.toString());

		if (ORDER.date.equals(param_order)) {
			queryString = "SELECT S FROM Service S, ServiceHistory SH WHERE (S.name LIKE :token OR S.description LIKE :token)"
					+ " AND S.id=SH.id_service AND S.state!='UNPUBLISH'" + " AND SH.id_serviceMethod=0 AND SH.operation='ADD'"
					+ " ORDER BY SH.date DESC";
		}

		Query q = getEntityManager().createQuery(queryString).setParameter("token", "%" + token + "%");
		List<Service> s = q.setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		return s;
	}

	/**
	 * This method browses all services but unpublished one by category and tags.
	 */
	@Transactional
	@Override
	public List<Service> browseService(Integer category, int firstResult, int maxResult, ORDER param_order)
			throws DataAccessException {
		String queryString = "FROM Service S WHERE S.category=:category AND " + "S.state!='UNPUBLISH' ORDER BY S."
				+ (ORDER.namedesc.equals(param_order) ? "name DESC" : ORDER.name.toString());

		if (ORDER.date.equals(param_order)) {
			queryString = "SELECT S FROM Service S, ServiceHistory SH WHERE SH.id_serviceMethod=0 AND SH.operation='ADD' AND S.id=SH.id_service"
					+ " AND S.state!='UNPUBLISH' AND S.category=:category ORDER BY SH.date DESC";
		}

		Query q = getEntityManager().createQuery(queryString).setParameter("category", category);
		List<Service> s = q.setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		return s;
	}

	/**
	 * This method browses all services but unpublished one by categories and tags.
	 */
	@Transactional
	@Override
	public List<Service> browseService(int[] categories, int firstResult, int maxResult, ORDER param_order)
			throws DataAccessException {
		String categoriesQuery = "(";
		for (int i = 0; i < categories.length; i++) {
			categoriesQuery += (i > 0 ? ", " : "") + categories[i] + (i + 1 == categories.length ? ")" : "");
		}

		String queryString = "SELECT S FROM Service S WHERE S.category in " + categoriesQuery
				+ " AND S.state!='UNPUBLISH' ORDER BY S."
				+ (ORDER.namedesc.equals(param_order) ? "name DESC" : ORDER.name.toString());

		if (ORDER.date.equals(param_order)) {
			queryString = "SELECT S FROM Service S, ServiceHistory SH WHERE SH.id_serviceMethod=0 AND SH.operation='ADD' AND S.id=SH.id_service"
					+ " AND S.state!='UNPUBLISH' AND S.category in " + categoriesQuery + " ORDER BY SH.date DESC";
		}

		Query q = getEntityManager().createQuery(queryString);
		List<Service> s = q.setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		return s;
	}

	/**
	 * This method retrieves Services searching by tag.
	 */
	@Transactional
	@Override
	public List<Service> getServiceByTag(String tag, int firstResult, int maxResult, ORDER param_order) {
		String order = param_order.toString();

		if (ORDER.namedesc.equals(param_order)) {
			order = "name DESC";
		}

		Query q = getEntityManager().createQuery(
				"SELECT S FROM Service S JOIN S.tags T WHERE T.name=:tag " + "AND S.state!='UNPUBLISH' ORDER BY S." + order)
				.setParameter("tag", tag);
		List<Service> list = q.setFirstResult(firstResult).setMaxResults(maxResult).getResultList();
		return list;
	}

	/**
	 * This method retrieves services by category.
	 */
	@Transactional
	@Override
	public List<Service> findByCategory(int id) {
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.category=:category").setParameter("category", id);
		List<Service> s = q.getResultList();
		return s;
	}

	/**
	 * This method retrieves how many services there are group by categories.
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Map<Integer, Integer> findCategoryServices() {
		Map<Integer, Integer> res = new HashMap<Integer, Integer>();
		List<Object[]> results = entityManager
				.createQuery(
						"SELECT s.category AS category, COUNT(s) AS total FROM Service AS s WHERE s.state != 'UNPUBLISH' GROUP BY s.category")
				.getResultList();
		for (Object[] result : results) {
			int category = ((Integer) result[0]).intValue();
			int count = ((Number) result[1]).intValue();
			res.put(category, count);
		}
		return res;
	}

	/**
	 * This method counts all services saved in database but unpublished ones.
	 */
	@Transactional
	@Override
	public Long countService() throws DataAccessException {
		return (Long) getEntityManager().createQuery("SELECT COUNT(s) FROM Service s WHERE s.state!='UNPUBLISH'")
				.getSingleResult();
	}

	/**
	 * This method counts services resulted from simple search by name.
	 */
	@Transactional
	@Override
	public Long countServiceSimpleSearch(String token) throws DataAccessException {
		return (Long) getEntityManager()
				.createQuery("SELECT COUNT(s) FROM Service s WHERE s.name LIKE :token " + "AND s.state!='UNPUBLISH'")
				.setParameter("token", "%" + token + "%").getSingleResult();
	}

	/**
	 *  This method counts services resulted from search by organization id.
	 */
	@Transactional
	@Override
	public Long countServiceByOrgSearch(int id_org) throws DataAccessException {
		return (Long) getEntityManager()
				.createQuery("SELECT COUNT(s) FROM Service S WHERE S.organizationId=:id_org " + "AND s.state!='UNPUBLISH'")
				.setParameter("id_org", id_org).getSingleResult();
	}
	
	/**
	 *  This method counts services resulted from search by category.
	 */
	@Transactional
	@Override
	public Long countServiceCategorySearch(int category) throws DataAccessException {
		return (Long) getEntityManager()
				.createQuery("SELECT COUNT(s) FROM Service S WHERE S.category=:category " + "AND S.state!='UNPUBLISH' ")
				.setParameter("category", category).getSingleResult();
	}

	/**
	 *  This method counts services resulted from search by tag.
	 */
	@Transactional
	@Override
	public Long countServiceTagsSearch(String tag) throws DataAccessException {
		return (Long) getEntityManager()
				.createQuery("SELECT COUNT(S) FROM Service S JOIN S.tags T WHERE " + "T.name=:tag AND S.state!='UNPUBLISH'")
				.setParameter("tag", tag).getSingleResult();
	}

	/**
	 * This method returns a map with tag name and total number of tag.
	 */
	@Transactional
	@Override
	public Map<String, Integer> findTagServices(String group, String order) throws DataAccessException {
		Map<String, Integer> res = new LinkedHashMap<String, Integer>();
		List<Object[]> results;

		String queryString = "SELECT T.name AS tag, COUNT(T.name) FROM Service S JOIN S.tags T"
				+ " WHERE S.state!='UNPUBLISH' GROUP BY T.name ORDER BY ";

		if (group.equalsIgnoreCase(Constants.ORDER.tag.toString())) {
			queryString += "T.name";
		} else {
			queryString += "COUNT(T.name)";
		}
		queryString += " " + order;

		results = entityManager.createQuery(queryString).getResultList();

		for (Object[] result : results) {
			String tag = ((String) result[0]).toString();
			int count = ((Number) result[1]).intValue();
			res.put(tag, count);
		}
		return res;
	}

}
