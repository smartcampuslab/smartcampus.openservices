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

import eu.trentorise.smartcampus.openservices.entities.Organization;

/**
 * Organization Dao Implementation
 * Retrieve, add, modify, delete organizations from database.
 * 
 * @author Giulia Canobbio
 *
 */
@Repository
public class OrganizationDaoImpl implements OrganizationDao{
	
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
	 * 
	 * @param entityManager
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Retrieve all organization data from database
	 * @return List of {@Organization} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Organization> showOrganizations() throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Organization");
		List<Organization> os = q.getResultList();
		return os;
	}

	/**
	 * Retrieve user's organizations data from database
	 * @param int user id
	 * @return List of {@Organization} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Organization> showMyOrganizations(int id_user)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Organization O WHERE O.id IN " +
				"( SELECT UR.id_org FROM UserRole UR WHERE UR.id_user=:id_user)")
				.setParameter("id_user", id_user);
		List<Organization> os = q.getResultList();
		return os;
	}
	
	/**
	 * Find organization by its id
	 * @param int organization id
	 * @return {@Organization} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public Organization getOrganizationById(int org_id)
			throws DataAccessException {
		return getEntityManager().find(Organization.class, org_id);
	}
	
	/**
	 * Find organization by name
	 * @param String organization name
	 * @return {@Organization} instance
	 * @throws DataAccessException
	 */
	@Override
	public Organization getOrganizationByName(String org_name)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Organization O WHERE O.name=:name")
				.setParameter("name", org_name);
		List<Organization> os = q.getResultList();
		if(os.size()==0){
			return null;
		}
		else return os.get(0);
	}

	/**
	 * Add a new organization
	 * @param {@Organization} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void createOrganization(Organization org) throws DataAccessException {
		getEntityManager().persist(org);
	}

	/**
	 * Modify an existing organization from database
	 * @param {@Organization} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void modifyOrganization(Organization org)
			throws DataAccessException {
		getEntityManager().merge(org);
		
	}

	/**
	 * Delete an existing organziation from database
	 * @param {@Organization} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void deleteOrganization(int org)
			throws DataAccessException {
		getEntityManager().remove(getEntityManager().find(Organization.class, org));
		
	}

	/**
	 * Find all organization where user is an organization owner
	 * @param int organization owner id
	 * @return List of {@Organization} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Organization> getOrganizationByOwnerId(int owner_id)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Organization Org WHERE Org.id IN " +
				"( SELECT Ur.id_org FROM UserRole Ur " +
				"WHERE Ur.id_user=:id_user AND Ur.role='ROLE_ORGOWNER')")
				.setParameter("id_user", owner_id);
		List<Organization> orgs = q.getResultList();
		return orgs;
	}

	/**
	 * Retrieve organization where its name contains a token.
	 * Simple search
	 * @param String token
	 * @return List of {@Organization} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Organization> searchOrganization(String token)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Organization Org WHERE Org.name LIKE :token")
				.setParameter("token", "%"+token+"%");
		List<Organization> orgs = q.getResultList();
		return orgs;
	}

	/**
	 * Browse organization by category OR geography
	 * @param int category id
	 * @param String geography
	 * @return List of {@Organization} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Organization> browseOrganization(Integer category, String geography) throws DataAccessException {
		//TODO check what geography is
		Query q = null;
		if(category!=null && geography==null){
			q = getEntityManager().createQuery("FROM Organization Org WHERE Org.category=:category")
					.setParameter("category", category);
		}
		else if(category==null && geography!=null){
			
		}
		else if(category!=null && geography!=null){
			q = getEntityManager().createQuery("FROM Organization Org WHERE Org.category=:category")
					.setParameter("category", category);
		}
		List<Organization> orgs = q.getResultList();
		return orgs;
	}

	/**
	 * Find organization by category
	 * @param int category id
	 * @return List of {@Organization} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public List<Organization> findByCategory(int id) throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Organization Org WHERE Org.category=:category")
					.setParameter("category", id);
		List<Organization> orgs = q.getResultList();
		return orgs;
	}
	
	
}
