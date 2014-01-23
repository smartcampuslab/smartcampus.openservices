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
	public List<Service> showPublishedService() throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.state!='UNPUBLISH'");
		List<Service> s = q.getResultList();
		return s;
	}

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
		return getEntityManager().find(Organization.class, service.getOrganizationId());
	}

	@Transactional
	@Override
	public User getOwnerOfService(int service_id) throws DataAccessException{
		Service service = getEntityManager().find(Service.class, service_id);
		return getEntityManager().find(User.class, service.getCreatorId());
	}

	@Transactional
	@Override
	public Service getServiceById(int service_id) throws DataAccessException{
		return getEntityManager().find(Service.class, service_id);
	}

	@Transactional
	@Override
	public List<Service> getServiceByIdOwner(int id_owner) throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.creatorId=:id_owner")
				.setParameter("id_owner", id_owner);
		List<Service> s = q.getResultList();
		return s;
	}

	@Transactional
	@Override
	public List<Service> getServiceByIdOrg(int id_org) throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.organizationId=:id_org")
				.setParameter("id_org", id_org);
		List<Service> s = q.getResultList();
		return s;
	}

	@Transactional
	@Override
	public List<Service> searchService(String token) throws DataAccessException{
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.name LIKE :token AND S.state!='UNPUBLISH'")
				.setParameter("token", "%"+token+"%");
		List<Service> s = q.getResultList();
		return s;
	}

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

	@Transactional
	@Override
	public List<Service> findByCategory(int id) {
		Query q = getEntityManager().createQuery("FROM Service S WHERE S.category=:category")
				.setParameter("category", id);
		List<Service> s = q.getResultList();
		return s;
	}


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
