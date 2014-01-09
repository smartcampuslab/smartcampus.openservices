package com.openserviceproject.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.openserviceproject.entities.*;

public interface UserDao {
	
	/**
	 * Get a list of users
	 * @return
	 */
	public List<User> getUsers() throws DataAccessException;
	
	/**
	 * Get a user by id
	 * @param id
	 * @return
	 */
	public User getUserById(int id) throws DataAccessException;
	
	/**
	 * Get a user by username, which is not null and unique
	 * @param username
	 * @return
	 */
	public User getUserByUsername(String username) throws DataAccessException;
	
	/**
	 * Modify User data
	 * @param user
	 * @throws DataAccessException
	 */
	public void modifyUser(User user) throws DataAccessException;
	
	/**
	 * Add a new user
	 * @param user
	 */
	public void addUser(User user) throws DataAccessException;
	
	/**
	 * disable a user
	 * @param id
	 */
	public void disableUser(User user) throws DataAccessException;

}
