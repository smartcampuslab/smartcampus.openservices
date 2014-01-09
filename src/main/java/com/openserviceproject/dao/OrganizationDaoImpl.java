package com.openserviceproject.dao;

import java.util.List;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.openserviceproject.entities.*;

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
				"( SELECT UR.id_org FROM UserRole UR WHERE UR.id_user=:id_user AND UR.role='ROLE_ORGOWNER')")
				.setParameter("id_user", id_user);
		System.out.println("------------");
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
	public void deleteOrganization(Organization org)
			throws DataAccessException {
		getEntityManager().remove(getEntityManager().merge(org));
		
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
		Query q = getEntityManager().createQuery("SELECT Org.id, Org.name, Org.descriptor, Org.category, Org.contacts FROM Organization Org WHERE Org.name LIKE :token")
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
			q = getEntityManager().createQuery("SELECT Org.id, Org.name, Org.descriptor, Org.category, Org.contacts FROM Organization Org WHERE Org.category LIKE :category")
					.setParameter("category", "%"+category+"%");
		}
		else if(category==null && geography!=null){
			
		}
		else if(category!=null && geography!=null){
			q = getEntityManager().createQuery("SELECT Org.id, Org.name, Org.descriptor, Org.category, Org.contacts FROM Organization Org WHERE Org.category LIKE :category")
					.setParameter("category", "%"+category+"%");
		}
		List<Organization> orgs = q.getResultList();
		return orgs;
	}
}
