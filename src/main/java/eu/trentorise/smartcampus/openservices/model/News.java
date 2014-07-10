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
 * Model of service history.
 * 
 * @author Giulia Canobbio
 *
 */
public class News {
	
	private String service;
	private String method;
	private String state;
	private Date date;
	
	private int serviceId;
	private int methodId;
	
	/**
	 * New instance of {@link News}.
	 * 
	 * @param service 
	 * 			: String, service name
	 * @param method 
	 * 			: String, method name
	 * @param state 
	 * 			: String
	 * @param date 
	 * 			: Date
	 * @param serviceId 
	 * 			: int
	 * @param methodId 
	 * 			: int
	 */
	public News(String service, String method, String state, Date date, int serviceId, int methodId) {
		this.service=service;
		this.method=method;
		this.state=state;
		this.date=date;
		this.serviceId=serviceId;
		this.methodId=methodId;
	}
	/**
	 * Get service name.
	 * 
	 * @return service name : String
	 */
	public String getService() {
		return service;
	}
	/**
	 * Set service name.
	 * 
	 * @param service 
	 * 			: String service name
	 */
	public void setService(String service) {
		this.service = service;
	}
	/**
	 * Get method name.
	 * 
	 * @return method name : String
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * Set method name.
	 * 
	 * @param method 
	 * 			: String method name
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * State value can be: Add, Modify, Delete, PUBLISH, UNPUBLISH, DEPRECATE.
	 * 
	 * @return state : String operation on method or service
	 */
	public String getState() {
		return state;
	}
	/**
	 * State value can be: Add, Modify, Delete, PUBLISH, UNPUBLISH, DEPRECATE.
	 * 
	 * @param state 
	 * 			: String
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * Get date.
	 * 
	 * @return date of operation on method or service : Date
	 */
	public Date getDate() {
		return date;
	}
	/**
	 * Set date.
	 * 
	 * @param date 
	 * 			: Date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	/**
	 * Get service id.
	 * 
	 * @return service id : int
	 */
	public int getServiceId() {
		return serviceId;
	}
	/**
	 * Set service id.
	 * 
	 * @param serviceId 
	 * 			: int
	 */
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	/**
	 * Get method id.
	 * 
	 * @return method id : int
	 */
	public int getMethodId() {
		return methodId;
	}
	/**
	 * Set method id.
	 * 
	 * @param methodId 
	 * 			: ints
	 */
	public void setMethodId(int methodId) {
		this.methodId = methodId;
	}

}
