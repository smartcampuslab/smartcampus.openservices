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

import org.springframework.dao.DataAccessException;

import eu.trentorise.smartcampus.openservices.entities.Category;

public interface CategoryDao {
	
	/**
	 * Get {@link Category} data by id (primary key)
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public Category getCategoryById(int id) throws DataAccessException;
	
	/**
	 * Get Category data by name
	 * name is unique
	 * @param name
	 * @return
	 * @throws DataAccessException
	 */
	public Category getCategoryByName(String name) throws DataAccessException;
	
	/**
	 * @return list of all the {@link Category} instances
	 * @throws DataAccessException
	 */
	public List<Category> getCategories() throws DataAccessException;
	
	/**
	 * Add new Category
	 * @param Category
	 * @throws DataAccessException
	 */
	public void addCategory(Category Category) throws DataAccessException;
	
	/**
	 * Modify a Category
	 * @param Category
	 * @throws DataAccessException
	 */
	public void modifyCategory(Category Category) throws DataAccessException;
	
	/**
	 * Delete a Category
	 * @param Category
	 * @throws DataAccessException
	 */
	public void deleteCategory(Category Category) throws DataAccessException;

}
