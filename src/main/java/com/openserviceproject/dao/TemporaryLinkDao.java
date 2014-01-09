package com.openserviceproject.dao;

import org.springframework.dao.DataAccessException;

//import org.springframework.data.repository.Repository;
import com.openserviceproject.entities.*;

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
