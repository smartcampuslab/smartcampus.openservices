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
import eu.trentorise.smartcampus.openservices.entities.Method;

/**
 * Method dao interface
 * 
 * @author Giulia Canobbio
 *
 */
public interface MethodDao {
	
	/**
	 * Get Method data by id (primary key)
	 * @param id
	 * @return a {@link Method} instance
	 * @throws DataAccessException
	 */
	public Method getMethodById(int id) throws DataAccessException;
	
	/**
	 * Get Method data by name
	 * name is unique
	 * @param name
	 * @return a {@link Method} instance
	 * @throws DataAccessException
	 */
	public Method getMethodByName(String name) throws DataAccessException;
	
	/**
	 * Get a list of method for a given service
	 * @param service_id
	 * @return a {@link Method} instance
	 * @throws DataAccessException
	 */
	public List<Method> getMethodByServiceId(int service_id) throws DataAccessException;
	
	/**
	 * Add new method
	 * @param method : a {@link Method} instance
	 * @throws DataAccessException
	 */
	public void addMethod(Method method) throws DataAccessException;
	
	/**
	 * Modify a method
	 * @param method : a {@link Method} instance
	 * @throws DataAccessException
	 */
	public void modifyMethod(Method method) throws DataAccessException;
	
	/**
	 * Delete a method
	 * @param method : a {@link Method} instance
	 * @throws DataAccessException
	 */
	public void deleteMethod(Method method) throws DataAccessException;

}
