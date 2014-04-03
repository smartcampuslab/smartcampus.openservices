/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.openservices.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.trentorise.smartcampus.openservices.entities.Profile;
/**
 * Model of user data
 * @author Giulia Canobbio
 *
 */
public class User {
	
	private String username;
	private String email;
	private Profile profile;
	private String role;
	
	/**
	 * New instance of {@link User}
	 */
	public User() {
		super();
	}
	
	/**
	 * Cast from {@link eu.trentorise.smartcampus.openservices.entities.User} to {@link User} instance
	 * @param u : {@link eu.trentorise.smartcampus.openservices.entities.User}
	 */
	public User(eu.trentorise.smartcampus.openservices.entities.User u){
		setUsername(u.getUsername());
		setEmail(u.getEmail());
		setProfile(u.getProfile());
		setRole(u.getRole());
	}
	
	/**
	 * 
	 * @return username : String
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
	 * @return email : String
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
	 * 
	 * @return profile : {@link Profile} instance
	 */
	public Profile getProfile() {
		return profile;
	}
	/**
	 * 
	 * @param profile : {@link Profile} instance
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	/**
	 * User role can be: ROLE_ADMIN, ROLE_NORMAL
	 * @return role of user : String
	 */
	public String getRole() {
		return role;
	}
	/**
	 * User role can be: ROLE_ADMIN, ROLE_NORMAL
	 * @param role : String
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * Cast from a {@link User} to {@link eu.trentorise.smartcampus.openservices.entities.User} instance
	 * @return a {@link eu.trentorise.smartcampus.openservices.entities.User} instance
	 */
	public eu.trentorise.smartcampus.openservices.entities.User toUserEntity() {
		eu.trentorise.smartcampus.openservices.entities.User u = 
				new eu.trentorise.smartcampus.openservices.entities.User();
		u.setUsername(username);
		u.setEmail(email);
		u.setProfile(profile);
		u.setRole(role);
		return u;
	}
	
	/**
	 * Cast list from a {@link User} to {@link eu.trentorise.smartcampus.openservices.entities.User} instance
	 * @param users : collection of {@link eu.trentorise.smartcampus.openservices.entities.User} instances
	 * @return a list of {@link User}
	 */
	public static List<User> fromUserEntities(Collection<eu.trentorise.smartcampus.openservices.entities.User> users) {
		if (users != null) {
			List<User> res = new ArrayList<User>();
			for (eu.trentorise.smartcampus.openservices.entities.User u : users) {
				res.add(new User(u));
			} 
			return res;
		}
		return null;
	}
	
	/**
	 * Cast from a {@link eu.trentorise.smartcampus.openservices.entities.User} to {@link User} instance
	 * @param service : a {@link eu.trentorise.smartcampus.openservices.entities.User} instance
	 * @return a {@link User} instance
	 */
	public static User fromUserEntity(eu.trentorise.smartcampus.openservices.entities.User user) {
		if (user != null) return new User(user);
		return null;
	}

}
