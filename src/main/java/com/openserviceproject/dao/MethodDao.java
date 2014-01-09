package com.openserviceproject.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.openserviceproject.entities.Method;

public interface MethodDao {
	
	/**
	 * Get Method data by id (primary key)
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	public Method getMethodById(int id) throws DataAccessException;
	
	/**
	 * Get Method data by name
	 * name is unique
	 * @param name
	 * @return
	 * @throws DataAccessException
	 */
	public Method getMethodByName(String name) throws DataAccessException;
	
	/**
	 * Get a list of method for a given service
	 * @param service_id
	 * @return
	 * @throws DataAccessException
	 */
	public List<Method> getMethodByServiceId(int service_id) throws DataAccessException;
	
	/**
	 * Add new method
	 * @param method
	 * @throws DataAccessException
	 */
	public void addMethod(Method method) throws DataAccessException;
	
	/**
	 * Modify a method
	 * @param method
	 * @throws DataAccessException
	 */
	public void modifyMethod(Method method) throws DataAccessException;
	
	/**
	 * Delete a method
	 * @param method
	 * @throws DataAccessException
	 */
	public void deleteMethod(Method method) throws DataAccessException;

}
