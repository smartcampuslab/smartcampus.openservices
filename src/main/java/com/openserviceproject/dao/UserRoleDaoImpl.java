package com.openserviceproject.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.openserviceproject.entities.UserRole;

@Repository
public class UserRoleDaoImpl implements UserRoleDao{
	
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
	public List<UserRole> getUserRoleByIdUser(int id_user) throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM UserRole UR WHERE id_user=:id_user")
				.setParameter("id_user", id_user);
		List<UserRole> ur = q.getResultList();
		return ur;
	}

	@Transactional
	@Override
	public List<UserRole> getUserRoleByIdOrg(int id_org) throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM UserRole UR WHERE id_org=:id_org")
				.setParameter("id_org", id_org);
		List<UserRole> ur = q.getResultList();
		return ur;
	}

	@Transactional
	@Override
	public void createUserRole(int user_id, int org_id, String role) throws DataAccessException{
		UserRole ur = new UserRole(user_id, org_id, role);
		getEntityManager().persist(ur);
		
	}

	@Transactional
	@Override
	public List<String> getRoleOfUser(int user_id) throws DataAccessException{
		Query q = getEntityManager().createQuery("SELECT UR.role FROM UserRole UR WHERE UR.id_user=:id_user")
				.setParameter("id_user", user_id);
		List<UserRole> ur = q.getResultList();
		if (ur.size() == 0) {
			return null;
		} else {
			List<String> roles = new ArrayList<String>();
			for (int i = 0; i < ur.size(); i++) {
				roles.add(ur.get(i).getRole());
			}
			return roles;
		}
	}
	
	@Transactional
	@Override
	public UserRole getRoleOfUser(int user_id, int org_id) throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM UserRole UR WHERE UR.id_user=:id_user AND UR.id_org=:id_org")
				.setParameter("id_user", user_id).setParameter("id_org", org_id);
		List<UserRole> ur = q.getResultList();
		if(ur.size()==0){
			return null;
		}
		else return ur.get(0);
	}

	@Transactional
	@Override
	public void deleteUserRole(UserRole ur) throws DataAccessException{
		getEntityManager().remove(getEntityManager().merge(ur));
		
	}

	@Transactional
	@Override
	public List<UserRole> getUserRoleByIdRole(int user_id, String role)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("UserRole UR WHERE UR.id_user=:id_user AND UR.role=:role")
				.setParameter("id_user", user_id).setParameter("role", role);
		List<UserRole> ur = q.getResultList();
		return ur;
	}
}
