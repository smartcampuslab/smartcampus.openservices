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

	@Transactional
	public Category getCategoryById(int id) throws DataAccessException {
		return categoryDao.getCategoryById(id);
	}
	
	
	@Transactional
	public Category getCategoryByName(String name) throws DataAccessException {
		return categoryDao.getCategoryByName(name);
	}

	@Transactional
	public List<Category> getCategories() throws DataAccessException {
		return categoryDao.getCategories();
	}
	
	@Transactional
	public Category addCategory(Category category) throws DataAccessException {
		if (categoryDao.getCategoryByName(category.getName()) != null) {
			throw new EntityExistsException();
		}
		categoryDao.addCategory(category);
		return category;
	} 
	
	@Transactional
	public Category modifyCategory(Category category) throws DataAccessException {
		Category old = categoryDao.getCategoryByName(category.getName());
		if (old != null && old.getId() != category.getId()) {
			throw new EntityExistsException();
		}
		categoryDao.modifyCategory(category);
		return category;
	}
	
	@Transactional
	public void deleteCategory(int id) throws DataAccessException {
		List<Organization> orgs = organizationDao.findByCategory(id);
		if (orgs != null && ! orgs.isEmpty()) throw new EntityExistsException();
		
		List<Service> services = serviceDao.findByCategory(id);
		if (services != null && ! services.isEmpty()) throw new EntityExistsException();
		
		categoryDao.deleteCategory(categoryDao.getCategoryById(id));
	}
}

