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
 * UserRole Entity for user role table
 * primary key, not null, auto increment int(11) id
 * not null int(11) user id
 * not null int(11) organization id
 * not null varchar(45) role
 * 
 * Relation between organizationa and user is saved in this table.
 * 
 * @author Giulia Canobbio
 *
 */
@Entity
@Table(name="UserRole")
public class UserRole {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name="id_user")
	private int id_user;
	@Column(name="id_org")
	private int id_org;
	@Column(name="role")
	private String role;
	
	/**
	 * New {@UserRole} instance
	 */
	public UserRole(){
		
	}
	
	/**
	 * New {@UserRole} instance
	 * @param id_user
	 * @param id_org
	 * @param role
	 */
	public UserRole(int id_user, int id_org, String role){
		this.id_user=id_user;
		this.id_org=id_org;
		this.role=role;
	}

	/**
	 * 
	 * @return int id of {@UserRole} instance
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id in {@UserRole} instance
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return int user id of {@UserRole} instance
	 */
	public int getId_user() {
		return id_user;
	}

	/**
	 * 
	 * @param id_user in {@UserRole} instance
	 */
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	/**
	 * 
	 * @return int organization id of {@UserRole} instance
	 */
	public int getId_org() {
		return id_org;
	}

	/**
	 * 
	 * @param id_org of {@UserRole} instance
	 */
	public void setId_org(int id_org) {
		this.id_org = id_org;
	}

	/**
	 * 
	 * @return String role of {@UserRole} instance
	 */
	public String getRole() {
		return role;
	}

	/**
	 * 
	 * @param role in {@UserRole} instance
	 */
	public void setRole(String role) {
		this.role = role;
	}

}
