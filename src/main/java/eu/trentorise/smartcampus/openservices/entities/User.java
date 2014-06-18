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

import org.hibernate.annotations.Index;
import org.hibernate.annotations.IndexColumn;

/**
 * User Entity for user table
 * primary key, not null, auto increment int(11) id
 * not null, unique index varchar(45) username
 * not null varchar(45) password
 * not null, unique index varchar(145) password
 * not null int(11) enabled
 * mediumblob profile
 * not null varchar(45) role
 * 
 * @author Giulia Canobbio
 *
 */
@Entity
@Table(name="User")
public class User {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name="username", unique=true, nullable=false)
	private String username;
	@Column(name="password")
	private String password;
	@Column(name="email", unique=true, nullable=false)
	private String email;
	@Column(name="enabled", nullable=false)
	private int enabled;
	//adding an object profile - cast to Blob is doing by JPA
	@Column(name="profile")
	@Lob
	private Profile profile;
	@Column(name="role", nullable=false)
	private String role;
	
	/**
	 * New {@link User} instance
	 */
	public User(){
		
	}

	/**
	 * 
	 * @return int user id
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id : int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return String username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @param username : String
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * @return String password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password : String
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @return String email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email : String
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 0 - if user is not enabled
	 * 1- if user is enabled
	 * @return int enabled
	 */
	public int getEnabled() {
		return enabled;
	}

	/**
	 * 0 - if user is not enabled
	 * 1 - if user is enabled
	 * @param enabled : int
	 */
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * 
	 * @return {@link Profile} instance 
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * 
	 * @param profile : {@link Profile}
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	/**
	 * Role can be: normal user or admin
	 * @return String role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Role can be: normal user or admin
	 * @param role : String
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
}
