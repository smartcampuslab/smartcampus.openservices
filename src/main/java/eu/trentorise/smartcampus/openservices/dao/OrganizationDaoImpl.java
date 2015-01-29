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

import eu.trentorise.smartcampus.openservices.Constants.ORDER;
import eu.trentorise.smartcampus.openservices.Constants.ROLES;
import eu.trentorise.smartcampus.openservices.entities.Organization;

/**
 * Organization Dao Implementation Retrieve, add, modify, delete organizations
 * from database.
 * 
 * @author Giulia Canobbio
 * 
 */
@Repository
public class OrganizationDaoImpl implements OrganizationDao {
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
	 * This method retrieves all organization data from database.
	 */
	@Transactional
	@Override
	public List<Organization> showOrganizations(String token,
			List<Integer> categoryIds, int firstResult, int maxResult,
			ORDER param_order) throws DataAccessException {
		String order = param_order.toString();
		if (ORDER.namedesc.equals(param_order)) {
			order = "name DESC";
		}

		String query = "FROM Organization WHERE 1=1";
		if (token != null) {
			query += " AND name LIKE :name";
		}
		if (categoryIds != null) {
			query += " AND category IN :cats";
		}
		Query q = getEntityManager().createQuery(query + " ORDER BY " + order);

		if (token != null) {
			q.setParameter("name", "%" + token + "%");
		}
		if (categoryIds != null) {
			q.setParameter("cats", categoryIds);
		}
		List<Organization> os = q.setFirstResult(firstResult)
				.setMaxResults(maxResult).getResultList();
		return os;
	}

	/**
	 * This method retrieves user's organizations data from database.
	 */
	@Transactional
	@Override
	public List<Organization> showMyOrganizations(int id_user)
			throws DataAccessException {
		Query q = getEntityManager()
				.createQuery(
						"FROM Organization O WHERE O.id IN "
								+ "( SELECT UR.id_org FROM UserRole UR WHERE UR.id_user=:id_user) ORDER BY name")
				.setParameter("id_user", id_user);
		List<Organization> os = q.getResultList();
		return os;
	}

	/**
	 * This method finds organization by its id.
	 */
	@Transactional
	@Override
	public Organization getOrganizationById(int org_id)
			throws DataAccessException {
		return getEntityManager().find(Organization.class, org_id);
	}

	/**
	 * This method finds organization by name.
	 */
	@Override
	public Organization getOrganizationByName(String org_name)
			throws DataAccessException {
		Query q = getEntityManager().createQuery(
				"FROM Organization O WHERE O.name=:name").setParameter("name",
				org_name);
		List<Organization> os = q.getResultList();
		if (os.size() == 0) {
			return null;
		} else
			return os.get(0);
	}

	/**
	 * This method adds a new organization.
	 */
	@Transactional
	@Override
	public void createOrganization(Organization org) throws DataAccessException {
		getEntityManager().persist(org);
	}

	/**
	 * This method modifies an existing organization from database.
	 */
	@Transactional
	@Override
	public void modifyOrganization(Organization org) throws DataAccessException {
		getEntityManager().merge(org);

	}

	/**
	 * This method deletes an existing organization from database.
	 */
	@Transactional
	@Override
	public void deleteOrganization(int org) throws DataAccessException {
		getEntityManager().remove(
				getEntityManager().find(Organization.class, org));

	}

	/**
	 * This method finds all organization where user is an organization owner.
	 */
	@Transactional
	@Override
	public List<Organization> getOrganizationByOwnerId(int owner_id)
			throws DataAccessException {
		Query q = getEntityManager()
				.createQuery(
						"FROM Organization Org WHERE Org.id IN "
								+ "( SELECT Ur.id_org FROM UserRole Ur "
								+ "WHERE Ur.id_user=:id_user AND Ur.role=':role')")
				.setParameter("id_user", owner_id)
				.setParameter("role", ROLES.ROLE_ORGOWNER);
		List<Organization> orgs = q.getResultList();
		return orgs;
	}

	/**
	 * This method retrieves organization where its name contains a token.
	 * Simple search.
	 */
	@Transactional
	@Override
	public List<Organization> searchOrganization(String token, int firstResult,
			int maxResult, String param_order) throws DataAccessException {
		Query q = getEntityManager().createQuery(
				"FROM Organization Org WHERE Org.name LIKE :token "
						+ "OR Org.description LIKE :token ORDER BY "
						+ param_order).setParameter("token", "%" + token + "%")
		/* .setParameter("order", param_order) */;
		List<Organization> orgs = q.setFirstResult(firstResult)
				.setMaxResults(maxResult).getResultList();
		return orgs;
	}

	/**
	 * This method retrieves organization by category OR geography.
	 */
	@Transactional
	@Override
	public List<Organization> browseOrganization(Integer category,
			String geography, int firstResult, int maxResult, ORDER param_order)
			throws DataAccessException {
		// TODO check what geography is
		Query q = null;
		if (category != null && geography == null) {
			q = getEntityManager().createQuery(
					"FROM Organization Org WHERE Org.category=:category"
							+ " ORDER BY Org." + param_order.toString())
					.setParameter("category", category);
		} else if (category == null && geography != null) {
			// TODO
		} else if (category != null && geography != null) {
			q = getEntityManager().createQuery(
					"FROM Organization Org WHERE Org.category=:category"
							+ " ORDER BY Org." + param_order.toString())
					.setParameter("category", category);
		}
		List<Organization> orgs = q.setFirstResult(firstResult)
				.setMaxResults(maxResult).getResultList();
		return orgs;
	}

	/**
	 * This method retrieves organization by categories OR geography.
	 */
	@Transactional
	@Override
	public List<Organization> browseOrganization(int[] categories,
			String geography, int firstResult, int maxResult, ORDER param_order)
			throws DataAccessException {
		// TODO check what geography is

		String categoriesQuery = "(";
		for (int i = 0; i < categories.length; i++) {
			categoriesQuery += (i > 0 ? ", " : "") + categories[i]
					+ (i + 1 == categories.length ? ")" : "");
		}

		String order = param_order.toString();
		if (ORDER.namedesc.equals(param_order)) {
			order = "name DESC";
		}

		Query q = null;
		if (categories != null && geography == null) {
			q = getEntityManager().createQuery(
					"FROM Organization Org WHERE Org.category in "
							+ categoriesQuery + " ORDER BY " + order);
		} else if (categories != null && geography != null) {
			q = getEntityManager().createQuery(
					"FROM Organization Org WHERE Org.category in "
							+ categoriesQuery + " ORDER BY " + order);
		}
		List<Organization> orgs = q.setFirstResult(firstResult)
				.setMaxResults(maxResult).getResultList();
		return orgs;
	}

	/**
	 * This method finds organization by category.
	 */
	@Transactional
	@Override
	public List<Organization> findByCategory(int id) throws DataAccessException {
		Query q = getEntityManager().createQuery(
				"FROM Organization Org WHERE Org.category=:category")
				.setParameter("category", id);
		List<Organization> orgs = q.getResultList();
		return orgs;
	}

	/**
	 * This method counts organization saved in database.
	 */
	@Transactional
	@Override
	public Long countOrganization(String token, List<Integer> categoryIds)
			throws DataAccessException {
		String query = "SELECT COUNT(org) FROM Organization org WHERE 1=1";
		if (token != null) {
			query += " AND org.name LIKE :name";
		}
		if (categoryIds != null) {
			query += " AND org.category IN :cats";
		}
		Query q = getEntityManager().createQuery(query);
		if (token != null) {
			q.setParameter("name", "%" + token + "%");
		}
		if (categoryIds != null) {
			q.setParameter("cats", categoryIds);
		}
		return (Long) q.getSingleResult();
	}

}
