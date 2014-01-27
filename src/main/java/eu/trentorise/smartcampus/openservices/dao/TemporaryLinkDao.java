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

import org.springframework.dao.DataAccessException;

//import org.springframework.data.repository.Repository;

import eu.trentorise.smartcampus.openservices.entities.*;

/**
 * Temporary Link Dao Interface
 * @author Giulia Canobbio
 *
 */
public interface TemporaryLinkDao /*extends Repository<TemporaryLink, Long>*/{
	
	/**
	 * Find a TemporaryLink data by key 
	 * key is primary key of table TemporaryLink
	 * @param key
	 * @return
	 */
	public TemporaryLink getTLByKey(String key) throws DataAccessException;
	
	/**
	 * Save a TemporaryLink data in db
	 * @param tl
	 */
	public void save(TemporaryLink tl) throws DataAccessException;
	
	/**
	 * Delete TemporaryLink by key
	 * @param key
	 */
	public void delete(String key) throws DataAccessException;
	
}
