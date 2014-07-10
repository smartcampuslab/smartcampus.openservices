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
 * Temporary Link Entity for temporary link table:
 * id : primary key, not null, auto increment int(11);
 * id_org : not null int(11);
 * key: not null, unique index varchar(50);
 * email: not null varchar(45);
 * role: varchar(255).
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
	@GeneratedValue
	private int id;
	@Column(name="id_org")
	private int id_org;
	@Column(name="\"key\"",columnDefinition="LONGTEXT")
	private String key;
	@Column(name="email")
	private String email;
	@Column(name="role")
	private String role;
	
	/**
	 * New {@link TemporaryLink} instance.
	 */
	public TemporaryLink(){
	}

	/**
	 * Get id.
	 * 
	 * @return int id 
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set id.
	 * 
	 * @param id 
	 * 			: int
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Get organization id.
	 * 
	 * @return int organization id 
	 */
	public int getId_org() {
		return id_org;
	}

	/**
	 * Set organization id.
	 * 
	 * @param id_org 
	 * 			: int 
	 */
	public void setId_org(int id_org) {
		this.id_org = id_org;
	}
	
	/**
	 * Get key.
	 * 
	 * @return String key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Set key.
	 * 
	 * @param key 
	 * 			: String
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * Get email.
	 * 
	 * @return String email 
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set email.
	 * 
	 * @param email 
	 * 			: String
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get role.
	 * 
	 * @return String role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Set role.
	 * 
	 * @param role 
	 * 			: String
	 */
	public void setRole(String role) {
		this.role = role;
	}

}
