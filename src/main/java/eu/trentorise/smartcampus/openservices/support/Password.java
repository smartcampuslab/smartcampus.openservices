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
package eu.trentorise.smartcampus.openservices.support;
/**
 * Support class for input parameters of user controller.
 * 
 * @author Giulia Canobbio
 *
 */
public class Password {
	
	private String oldP;
	private String newP;
	/**
	 * Get old password.
	 * 
	 * @return old password : String
	 */
	public String getOldP() {
		return oldP;
	}
	/**
	 * Set old password.
	 * 
	 * @param oldP 
	 * 			: String old password
	 */
	public void setOldP(String oldP) {
		this.oldP = oldP;
	}
	/**
	 * Get new password.
	 * 
	 * @return new password : String
	 */
	public String getNewP() {
		return newP;
	}
	/**
	 * Set new password.
	 * 
	 * @param newP 
	 * 			: String new password
	 */
	public void setNewP(String newP) {
		this.newP = newP;
	}
}
