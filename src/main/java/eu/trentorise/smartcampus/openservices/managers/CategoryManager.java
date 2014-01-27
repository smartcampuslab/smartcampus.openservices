/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/

package eu.trentorise.smartcampus.openservices.managers;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.dao.CategoryDao;
import eu.trentorise.smartcampus.openservices.dao.OrganizationDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceDao;
import eu.trentorise.smartcampus.openservices.entities.Category;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.Service;

/**
 * Category Manager Interfaces with dao
 * Retrieve, add, modify and delete category from database
 * 
 * @author raman
 *
 */
@Component
public class CategoryManager {

	@Autowired
	private CategoryDao categoryDao;
	@Autowired 
	private ServiceDao serviceDao;
	@Autowired
	private OrganizationDao organizationDao;

	/**
	 * Retrieve category data from database
	 * searching by id
	 * @param id: category id
	 * @return {@link Category} instance
	 * @throws DataAccessException
	 */
	@Transactional
	public Category getCategoryById(int id) throws DataAccessException {
		try{
			return categoryDao.getCategoryById(id);
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Retrieve category data from database
	 * Searching by category name
	 * @param name: category name
	 * @return {@link Category} instance
	 * @throws DataAccessException
	 */
	@Transactional
	public Category getCategoryByName(String name) throws DataAccessException {
		try{
			return categoryDao.getCategoryByName(name);
		}catch(DataAccessException d){
			return null;
		}
	}

	/**
	 * Retrieve all category data from database
	 * @return all {@link Category} instances
	 * @throws DataAccessException
	 */
	@Transactional
	public List<Category> getCategories() throws DataAccessException {
		try{
			return categoryDao.getCategories();
		}catch(DataAccessException d){
			return null;
		}
	}
	
	/**
	 * Add a new category in database
	 * @param category: Category data
	 * @return {@link Category} instance
	 * @throws DataAccessException
	 */
	@Transactional
	public Category addCategory(Category category) throws DataAccessException {
		try {
			if (categoryDao.getCategoryByName(category.getName()) != null) {
				throw new EntityExistsException();
			}
			categoryDao.addCategory(category);
			return category;
		} catch (DataAccessException d) {
			return null;
		}
	} 
	
	/**
	 * Modify category data from database
	 * @param category: Category data
	 * @return {@link Category} instance
	 * @throws DataAccessException
	 */
	@Transactional
	public Category modifyCategory(Category category) throws DataAccessException {
		try {
			Category old = categoryDao.getCategoryByName(category.getName());
			if (old != null && old.getId() != category.getId()) {
				throw new EntityExistsException();
			}
			categoryDao.modifyCategory(category);
			return category;
		} catch (DataAccessException d) {
			return null;
		}
	}
	
	/**
	 * Delete category data from database
	 * @param id: category id
	 * @return boolean: true if it is ok else false
	 * @throw EntityExistsException if delete is not successful
	 * @throws DataAccessException
	 */
	@Transactional
	public boolean deleteCategory(int id) throws DataAccessException {
		try {
			List<Organization> orgs = organizationDao.findByCategory(id);
			if (orgs != null && !orgs.isEmpty())
				throw new EntityExistsException();

			List<Service> services = serviceDao.findByCategory(id);
			if (services != null && !services.isEmpty())
				throw new EntityExistsException();

			categoryDao.deleteCategory(categoryDao.getCategoryById(id));
			return true;
		} catch (DataAccessException d) {
			return false;
		}
	}
}

