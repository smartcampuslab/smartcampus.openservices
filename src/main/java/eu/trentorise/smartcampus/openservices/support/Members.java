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
package eu.trentorise.smartcampus.openservices.support;
/**
 * Members class 
 * Having a few value of user data in order to avoid important data such as password.
 * @author Giulia Canobbio
 *
 */
public class Members {

	private int id;
	private String username;
	private String email;
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
	 * @return user id : int
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
	
	

}
