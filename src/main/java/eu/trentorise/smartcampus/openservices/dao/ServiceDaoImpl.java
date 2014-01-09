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
import java.util.List;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.entities.*;

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
	
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Transactional
	@Override
	public List<Service> showService() throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Service");
		List<Service> s = q.getResultList();
		return s;
	}

	@Transactional
	@Override
	public List<Service> showMyService(String username)
			throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Service S " +
				"WHERE S.id_owner = (" +
				"SELECT U.id FROM User U WHERE U.username=:username " +
				")")
				.setParameter("username",username);
		List<Service> s = q.getResultList();
		return s;
	}

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

	@Transactional
	@Override
	public void createService(Service service) throws DataAccessException {
		//ALTER TABLE Org to add new service
		getEntityManager().persist(service);
	}

	@Transactional
	@Override
	public void modifyService(Service service) throws DataAccessException {
		getEntityManager().merge(service);
	}

	@Transactional
	@Override
	public void deleteService(Service service) throws DataAccessException {
		getEntityManager().remove(getEntityManager().merge(service));
	}

	@Transactional
	@Override
	public Organization getOrganizationofService(int service_id) throws DataAccessException{
		Service service = getEntityManager().find(Service.class, service_id);
		return getEntityManager().find(Organization.class, service.getId_org());
	}

	@Transactional
	@Override
	public User getOwnerOfService(int service_id) throws DataAccessException{
		Service service = getEntityManager().find(Service.class, service_id);
		return getEntityManager().find(User.class, service.getId_owner());
	}

	@Transactional
	@Override
	public Service getServiceById(int service_id) throws DataAccessException{
		return getEntityManager().find(Service.class, service_id);
	}

	@Transactional
	@Override
	public List<Service> getServiceByIdOwner(int id_owner) throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.id_owner=:id_owner")
				.setParameter("id_owner", id_owner);
		List<Service> s = q.getResultList();
		return s;
	}

	@Transactional
	@Override
	public List<Service> getServiceByIdOrg(int id_org) throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.id_org=:id_org")
				.setParameter("id_org", id_org);
		List<Service> s = q.getResultList();
		return s;
	}

	@Transactional
	@Override
	public List<Service> searchService(String token) throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.name LIKE :token AND S.state='PUBLISH'")
				.setParameter("token", "%"+token+"%");
		List<Service> s = q.getResultList();
		return s;
	}

	@Transactional
	@Override
	public List<Service> browseService(String category, String tags)
			throws DataAccessException {
		Query q = null;
		if(category!=null && tags!=null){
			q = getEntityManager().createQuery("FROM Service S WHERE S.category LIKE :category AND " +
				"S.tags LIKE :tags AND S.state='PUBLISH'")
				.setParameter("category", "%"+category+"%")
				.setParameter("tags", "%"+tags+"%");
		}
		else if(category==null && tags!=null){
			q = getEntityManager().createQuery("FROM Service S WHERE S.tags LIKE :tags AND S.state='PUBLISH'")
					.setParameter("tags", "%"+tags+"%");
		}
		else if(category!=null && tags==null){
			q = getEntityManager().createQuery("FROM Service S WHERE S.category LIKE :category AND S.state='PUBLISH'")
					.setParameter("category", "%"+category+"%");
		}
		List<Service> s = q.getResultList();
		return s;
	}

	

}
