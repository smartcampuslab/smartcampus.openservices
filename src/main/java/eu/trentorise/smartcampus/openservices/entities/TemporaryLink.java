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
 * Temporary Link Entity for temporary link table
 * primary key, not null, auto increment int(11) id
 * not null int(11) id organization
 * not null, unique index varchar(50) key
 * not null varchar(45)
 * varchar(255) role
 * 
 * Used to invite user in an organization by a key
 * 
 * @author Giulia Canobbio
 *
 */
@Entity
@Table(name="TemporaryLink")
public class TemporaryLink {
	
	@Id
	private int id;
	@Column(name="id_org")
	private int id_org;
	@Column(name="\"key\"")
	private String key;
	@Column(name="email")
	private String email;
	@Column(name="role")
	private String role;
	
	/**
	 * New {@Temporary Link} instance
	 */
	public TemporaryLink(){
	}

	/**
	 * 
	 * @return int id of {@Temporary Link} instance
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id in {@Temporary Link} instance
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return int organization id of {@Temporary Link} instance
	 */
	public int getId_org() {
		return id_org;
	}

	/**
	 * 
	 * @param id_org in {@Temporary Link} instance
	 */
	public void setId_org(int id_org) {
		this.id_org = id_org;
	}
	
	/**
	 * 
	 * @return String key of {@Temporary Link} instance
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 
	 * @param key in {@Temporary Link} instance
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 
	 * @return String email of {@Temporary Link} instance
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email in {@Temporary Link} instance
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return String role of {@Temporary Link} instance
	 */
	public String getRole() {
		return role;
	}

	/**
	 * 
	 * @param role in {@Temporary Link} instance
	 */
	public void setRole(String role) {
		this.role = role;
	}

}
