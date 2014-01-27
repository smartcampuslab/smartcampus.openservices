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
	@Column(name="username")
	private String username;
	@Column(name="password")
	private String password;
	@Column(name="email")
	private String email;
	@Column(name="enabled")
	private int enabled;
	//adding an object profile - cast to Blob is doing by JPA
	@Column(name="profile")
	@Lob
	private Profile profile;
	@Column(name="role")
	private String role;
	
	/**
	 * New {@User} instance
	 */
	public User(){
		
	}
	
	/**
	 * New {@User} instance
	 * @param id
	 * @param username
	 * @param password
	 * @param email
	 * @param enabled
	 * @param role
	 */
	public User(int id, String username, String password, String email, int enabled, String role){
		this.id=id;
		this.username=username;
		this.password=password;
		this.email=email;
		this.enabled=enabled;
		this.role = role;
	}

	/**
	 * 
	 * @return int user id of {@User} instance
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id in {@User} instance
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return String username of {@User} instance
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 
	 * @param username in {@User} instance
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 
	 * @return String password of {@User} instance
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password in {@User} instance
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 
	 * @return String email of {@User} instance
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email in {@User} instance
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 0 - if user is not enabled
	 * 1- if user is enabled
	 * @return int enabled of {@User} instance
	 */
	public int getEnabled() {
		return enabled;
	}

	/**
	 * 0 - if user is not enabled
	 * 1 - if user is enabled
	 * @param enabled in {@User} instance
	 */
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * 
	 * @return {@Profile} profile of {@User} instance
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * 
	 * @param profile in {@User} instance
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	/**
	 * Role can be: normal user or admin
	 * @return String role of {@User} instance
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Role can be: normal user or admin
	 * @param role in {@User} instance
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
}
