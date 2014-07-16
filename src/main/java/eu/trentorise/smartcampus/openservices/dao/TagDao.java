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

import eu.trentorise.smartcampus.openservices.entities.Tag;

/**
 * 
 * Tag interface.
 * 
 * @author Giulia Canobbio
 *
 */
public interface TagDao {

	/**
	 * Deletes tag data found searching by service id.
	 * 
	 * @param tag
	 * 			: list of {@link Tag}
	 * @throws DataAccessException
	 */
	public void deleteTag(List<Tag> tag) throws DataAccessException;
}
