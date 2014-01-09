package com.openserviceproject.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.openserviceproject.entities.ServiceHistory;

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
