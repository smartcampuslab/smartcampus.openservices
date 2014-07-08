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
package eu.trentorise.smartcampus.openservices.social;

/**
 * Model for linkedIn user data
 * @author Giulia Canobbio
 *
 */
public class LinkedInUser {

	private String id;
	private String first_name;
	private String last_name;
	private String email_address;
	private String headline;
	/**
	 * 
	 * @return String id
	 */
	public String getId() {
		return id;
	}
	/**
	 * 
	 * @param id : String
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 
	 * @return String first name
	 */
	public String getFirst_name() {
		return first_name;
	}
	/**
	 * 
	 * @param first_name : String
	 */
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	/**
	 * 
	 * @return String last name
	 */
	public String getLast_name() {
		return last_name;
	}
	/**
	 * 
	 * @param last_name : String
	 */
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	/**
	 * 
	 * @return String email address
	 */
	public String getEmail_address() {
		return email_address;
	}
	/**
	 * 
	 * @param email_address : String
	 */
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	/**
	 * 
	 * @return String headline
	 */
	public String getHeadline() {
		return headline;
	}
	/**
	 * 
	 * @param headline : String
	 */
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
	
	
}
