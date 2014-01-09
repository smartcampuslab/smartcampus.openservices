package com.openserviceproject.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.openserviceproject.entities.*;

public interface ServiceDao {

	/**
	 * Show all service
	 * @return
	 */
	public List<Service> showService() throws DataAccessException;
	
	/**
	 * Show all service of a given user
	 * @param username
	 * @return
	 */
	public List<Service> showMyService(String username) throws DataAccessException;
	
	/**
	 * User can interact and use a chosen service
	 * @param service_name
	 */
	public Service useService(String service_name) throws DataAccessException;
	
	/**
	 * Create a service to an organization.
	 * Set username as owner_id
	 */
	public void createService(Service service) throws DataAccessException;
	
	/**
	 * Modify a service 
	 */
	public void modifyService(Service service) throws DataAccessException;
	
	/**
	 * Delete a service
	 */
	public void deleteService(Service service) throws DataAccessException;
	
	/**
	 * Get organization of service
	 * @param service_id
	 * @return
	 */
	public Organization getOrganizationofService(int service_id) throws DataAccessException;
	
	/**
	 * Get owner of service
	 * @param service_id
	 * @return
	 */
	public User getOwnerOfService(int service_id) throws DataAccessException;
	
	/**
	 * Get service by id
	 * @param service_id
	 * @return
	 */
	public Service getServiceById(int service_id) throws DataAccessException;
	
	/**
	 * Get service by owner id
	 * @param id_owner
	 * @return
	 */
	public List<Service> getServiceByIdOwner(int id_owner) throws DataAccessException;
	
	/**
	 * Get service by organization id
	 * @param id_org
	 * @return
	 */
	public List<Service> getServiceByIdOrg(int id_org) throws DataAccessException;
	
	/**
	 * Find services having in name the token in input
	 * @param token
	 * @return
	 */
	public List<Service> searchService(String token) throws DataAccessException;
	
	/**
	 * Browse services having input category and tags
	 * @param category
	 * @param tags
	 * @return
	 * @throws DataAccessException
	 */
	public List<Service> browseService(String category, String tags) throws DataAccessException;
	
}
