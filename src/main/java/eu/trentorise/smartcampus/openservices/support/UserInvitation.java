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

public class UserInvitation {

	private int org_id;
	private int user_id;
	private String email;
	/**
	 * 
	 * @return organization id : int
	 */
	public int getOrg_id() {
		return org_id;
	}
	/**
	 * 
	 * @param org_id : int
	 */
	public void setOrg_id(int org_id) {
		this.org_id = org_id;
	}
	/**
	 * 
	 * @return user id : int
	 */
	public int getUser_id() {
		return user_id;
	}
	/**
	 * 
	 * @param user_id : int
	 */
	public void setUser_id(int user_id) {
		this.user_id = user_id;
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
	
	
}
