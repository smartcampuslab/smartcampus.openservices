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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Category Entity for table Category
 * primary key, not null, auto increment int(11) id
 * not null, unique index varchar(45) name
 * varchar(100) description
 * 
 * @author Giulia Canobbio
 *
 */
@Entity
@Table(name="Category")
public class Category {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name="name", unique=true)
	private String name;
	@Column(name="description")
	private String description;
	
	/**
	 * New instance of {@Category}
	 */
	public Category(){
		
	}

	/**
	 * Get id of {@Category}
	 * @return {@int} id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set id of {@Category}
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get name of {@Category}
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of {@Category}
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get description of {@Category}
	 * @return String description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set description of {@Category}
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
