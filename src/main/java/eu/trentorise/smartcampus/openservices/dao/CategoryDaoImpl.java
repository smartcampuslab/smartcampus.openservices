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

import eu.trentorise.smartcampus.openservices.entities.Category;
import eu.trentorise.smartcampus.openservices.entities.Service;

/**
 * Category Dao Implementation 
 * Retrieve, find, add, modify and delete category object
 * from database.
 * 
 * @author Giulia Canobbio
 *
 */
@Repository
public class CategoryDaoImpl implements CategoryDao {
	
	@PersistenceContext(unitName="JpaPersistenceUnit")
	protected EntityManager entityManager;
	
	/**
	 * 
	 * @return entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * Return {@Category} instance searching by category id
	 * @param int category id
	 * @return {@Category} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public Category getCategoryById(int id) throws DataAccessException {
		return getEntityManager().find(Category.class, id);
	}

	/**
	 * Return {@Category} instance searching by category name
	 * @param string name
	 * @return {@Category} instance
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public Category getCategoryByName(String name) throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Category M WHERE M.name=:name")
				.setParameter("name", name);
		List<Category> ms = q.getResultList();
		if(ms.size()==0){
			return null;
		}
		else return ms.get(0);
	}
	
	/**
	 * Return a list of all categories
	 * @return {@Category} instance list
	 * @throws DataAccessException
	 */
	@Override
	public List<Category> getCategories() throws DataAccessException {
		Query q = getEntityManager().createQuery("FROM Category");
		return q.getResultList();
	}

	/**
	 * Save a new category in database
	 * @param {@Category} category
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void addCategory(Category Category) throws DataAccessException {
		getEntityManager().persist(Category);
	}

	/**
	 * Modify an existing category
	 * @param {@Category} category
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void modifyCategory(Category Category) throws DataAccessException {
		getEntityManager().merge(Category);
		
	}

	/**
	 * Delete an existing category from database
	 * @param {@Category} category
	 * @throws DataAccessException
	 */
	@Transactional
	@Override
	public void deleteCategory(Category Category) throws DataAccessException {
		getEntityManager().remove(Category);
	}
}
