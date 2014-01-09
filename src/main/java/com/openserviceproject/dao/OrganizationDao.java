package com.openserviceproject.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.openserviceproject.entities.Organization;

public interface OrganizationDao {
	
	/**
	 * Show all organization in database
	 * @return
	 */
	public List<Organization> showOrganizations() throws DataAccessException;
	
	/**
	 * Show my organizations
	 * Using user id
	 * @param id_user
	 * @return
	 */
	public List<Organization> showMyOrganizations(int id_user) throws DataAccessException;
	
	/**
	 * Get organization by id
	 * @param org_id
	 * @return
	 * @throws DataAccessException
	 */
	public Organization getOrganizationById(int org_id) throws DataAccessException;
	
	/**
	 * Get organization data by name
	 * @param org_name
	 * @return
	 * @throws DataAccessException
	 */
	public Organization getOrganizationByName(String org_name) throws DataAccessException;
	
	/**
	 * Create an organization
	 * @param username
	 */
	public void createOrganization(Organization org) throws DataAccessException;
	
	/**
	 * Modify Organization
	 * @param username
	 * @param org_name
	 */
	public void modifyOrganization(Organization org) throws DataAccessException;
	
	/**
	 * Delete an organization
	 * @param username
	 * @param org_name
	 */
	public void deleteOrganization(Organization org) throws DataAccessException;
	
	
	/**
	 * Delete service_owner of an organization
	 * @param username
	 * @param org_name
	 */
	//public void deleteServiceOwner(String username, String org_name) throws DataAccessException;
	
	/**
	 * Add an organization owner to an organization
	 * @param username
	 * @param org_owner
	 * @param org_name
	 */
	//public void addOrganizationOwner(String username, String org_name) throws DataAccessException;
	
	/**
	 * Delete organization owner
	 * @param username
	 * @param org_owner
	 * @param org_name
	 */
	//public void deleteOrganizationOwner(String username, String org_name) throws DataAccessException;
	
	/**
	 * Get a list of organizations where user is owner
	 * @param owner_id
	 * @return
	 */
	public List<Organization> getOrganizationByOwnerId(int owner_id) throws DataAccessException;
	
	/**
	 * Search Organization given a token
	 * For now I suppose that token is part of organization name (LIKE)
	 * @param token
	 * @return
	 * @throws DataAccessException
	 */
	public List<Organization> searchOrganization(String token) throws DataAccessException;
	
	/**
	 * Browse Organization using filters
	 * category (LIKE)
	 * geography - for now I suppose that this is a string (LIKE)
	 * @param category
	 * @param geography
	 * @return
	 * @throws DataAccessException
	 */
	public List<Organization> browseOrganization(String category, String geography) throws DataAccessException;
	
}
