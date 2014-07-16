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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.entities.Tag;

/**
 * Delete tag data.
 * 
 * @author Giulia Canobbio
 *
 */
@Repository
public class TagDaoImpl implements TagDao{
	/**
	 * Instance of {@link EntityManager}
	 */
	@PersistenceContext(name="JpaPersistenceUnit")
	protected EntityManager entityManager;

	/**
	 * 
	 * @return entity manager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * This method delete Tag data from db.
	 */
	@Transactional
	@Override
	public void deleteTag(List<Tag> tag) throws DataAccessException {
		for(int i=0; i<tag.size();i++){
			getEntityManager().remove(getEntityManager().merge(tag.get(i)));
		}
	}
}
