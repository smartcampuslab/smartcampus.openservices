package com.openserviceproject.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.openserviceproject.entities.UserRole;

public interface UserRoleDao {
	
	/**
	 * Get a list of User_Role data by user id
	 * @param id_user
	 * @return
	 */
	public List<UserRole> getUserRoleByIdUser(int id_user) throws DataAccessException;
	
	/**
	 * Get a list of User_Role data by organization id
	 * @param id_org
	 * @return
	 */
	public List<UserRole> getUserRoleByIdOrg(int id_org) throws DataAccessException;
	
	/**
	 * Get list of role of a user in User_Role
	 * ROLE_ORGOWNER
	 * ROLE_SERVICEOWNER
	 * @param user_id
	 * @return
	 */
	public List<String> getRoleOfUser(int user_id) throws DataAccessException;
	
	/**
	 * Get role of a user in a given organization
	 * @param user_id
	 * @param org_id
	 * @return
	 */
	public UserRole getRoleOfUser(int user_id, int org_id) throws DataAccessException;
	
	/**
	 * Create a new User_Role data
	 */
	public void createUserRole(int user_id, int org_id, String role) throws DataAccessException;
	
	/**
	 * Delete a User_Role data
	 * @param ur
	 */
	public void deleteUserRole(UserRole ur) throws DataAccessException;
	
	/**
	 * Get a list of User_Role data where user has id and his/her role is role.
	 * @param user_id
	 * @param role
	 * @return
	 * @throws DataAccessException
	 */
	public List<UserRole> getUserRoleByIdRole(int user_id, String role) throws DataAccessException;
	

}
