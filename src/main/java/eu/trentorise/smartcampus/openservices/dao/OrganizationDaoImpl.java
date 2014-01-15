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

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.entities.*;

@Repository
public class OrganizationDaoImpl implements OrganizationDao{
	
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
	public List<Organization> showOrganizations() throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Organization");
		List<Organization> os = q.getResultList();
		return os;
	}

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
	
	@Transactional
	@Override
	public Organization getOrganizationById(int org_id)
			throws DataAccessException {
		return getEntityManager().find(Organization.class, org_id);
	}
	
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


	@Transactional
	@Override
	public void createOrganization(Organization org) throws DataAccessException {
		getEntityManager().persist(org);
	}

	@Transactional
	@Override
	public void modifyOrganization(Organization org)
			throws DataAccessException {
		getEntityManager().merge(org);
		
	}

	@Transactional
	@Override
	public void deleteOrganization(int org)
			throws DataAccessException {
		getEntityManager().remove(getEntityManager().find(Organization.class, org));
		
	}

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

	@Transactional
	@Override
	public List<Organization> searchOrganization(String token)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Organization Org WHERE Org.name LIKE :token")
				.setParameter("token", "%"+token+"%");
		List<Organization> orgs = q.getResultList();
		return orgs;
	}

	@Transactional
	@Override
	public List<Organization> browseOrganization(String category,
			String geography) throws DataAccessException {
		//TODO check what geography is
		Query q = null;
		if(category!=null && geography==null){
			q = getEntityManager().createQuery("FROM Organization Org WHERE Org.category LIKE :category")
					.setParameter("category", "%"+category+"%");
		}
		else if(category==null && geography!=null){
			
		}
		else if(category!=null && geography!=null){
			q = getEntityManager().createQuery("FROM Organization Org WHERE Org.category LIKE :category")
					.setParameter("category", "%"+category+"%");
		}
		List<Organization> orgs = q.getResultList();
		return orgs;
	}
}
