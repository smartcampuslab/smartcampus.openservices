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
package eu.trentorise.smartcampus.openservices.entities;

import javax.persistence.*;

/**
 * Tag entity for table Tag:
 * id : primary key, auto increment, int(11);
 * id_service : not null, int(11);
 * name : not null, varchar(45).
 * 
 * @author Giulia Canobbio
 *
 */
@Entity
@Table(name="Tag")
public class Tag {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name="name", nullable=false)
	private String name;

	/**
	 * Get id of tag.
	 * 
	 * @return id : int
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Set id of tag.
	 * 
	 * @param id 
	 * 			: int
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Get name of tag.
	 * 
	 * @return name : String
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set name of tag.
	 * 
	 * @param name 
	 * 			: String
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
