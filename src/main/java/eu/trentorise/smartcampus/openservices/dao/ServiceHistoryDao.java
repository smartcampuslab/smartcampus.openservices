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

import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;

/**
 * Service History Dao Interface
 * 
 * @author Giulia Canobbio
 *
 */
public interface ServiceHistoryDao {
	
	/**
	 * Get ServiceHistory data by id
	 * id primary key
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public ServiceHistory getServiceHistoryById(int id) throws DataAccessException;
	
	/**
	 * Get a list of ServiceHistory data by service_id
	 * @param service_id
	 * @return
	 * @throws DataAccessException
	 */
	public List<ServiceHistory> getServiceHistoryByServiceId(int service_id) throws DataAccessException;
	
	/**
	 * Add a new ServiceHistory data
	 * @param serviceHistory
	 * @throws DataAccessException
	 */
	public void addServiceHistory(ServiceHistory serviceHistory) throws DataAccessException;
	
	/**
	 * Modify a ServiceHistory data
	 * @param serviceHistory
	 * @throws DataAccessException
	 */
	public void modifyServiceHistory(ServiceHistory serviceHistory) throws DataAccessException;
	
	/**
	 * Delete a ServiceHistory data
	 * @param serviceHistory
	 * @throws DataAccessException
	 */
	public void deleteServiceHistory(ServiceHistory serviceHistory) throws DataAccessException;
	
	/**
	 * Get a list of ServiceHistory data by organization id
	 * @param org_id
	 * @return
	 * @throws DataAccessException
	 */
	public List<ServiceHistory> getServiceHistoryByOrgId(int org_id) throws DataAccessException;

}
