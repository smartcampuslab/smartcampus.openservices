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

import java.util.Date;

/**
 * Model of service history
 * @author Giulia Canobbio
 *
 */
public class News {
	
	private String service;
	private String method;
	private String state;
	private Date date;
	
	/**
	 * New instance of {@link News}
	 * @param service : String
	 * @param method : String
	 * @param state : String
	 * @param date : Date
	 */
	public News(String service, String method, String state, Date date) {
		this.service=service;
		this.method=method;
		this.state=state;
		this.date=date;
	}
	/**
	 * 
	 * @return service name : String
	 */
	public String getService() {
		return service;
	}
	/**
	 * 
	 * @param service : String service name
	 */
	public void setService(String service) {
		this.service = service;
	}
	/**
	 * 
	 * @return method name : String
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * 
	 * @param method : String method name
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * State value can be: Add, Modify, Delete, PUBLISH, UNPUBLISH, DEPRECATE
	 * @return state : String operation on method or service
	 */
	public String getState() {
		return state;
	}
	/**
	 * State value can be: Add, Modify, Delete, PUBLISH, UNPUBLISH, DEPRECATE
	 * @param state : String
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * 
	 * @return date of operation on method or service : Date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * 
	 * @param date : Date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
