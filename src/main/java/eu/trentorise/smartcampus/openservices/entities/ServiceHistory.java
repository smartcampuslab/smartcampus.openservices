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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Service History Entity for service history table:
 * id: primary key, not null, auto increment int(11);
 * operation: not null varchar(45);
 * id service: not null int(11);
 * id service method: int(11);
 * date: not null date date.
 * 
 * @author Giulia Canobbio
 *
 */
@Entity
@Table(name="ServiceHistory")
public class ServiceHistory {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name="operation")
	private String operation;
	@Column(name="id_service")
	private int id_service;
	@Column(name="id_serviceMethod")
	private int id_serviceMethod;
	@Column(name="\"date\"")
	private Date date;
	@Column(name="serviceName")
	private String serviceName;
	@Column(name="methodName")
	private String methodName;
	
	/**
	 * New {@link ServiceHistory} instance.
	 */
	public ServiceHistory(){
		
	}

	/**
	 * Get id of service history.
	 * 
	 * @return int id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set id of service history.
	 * 
	 * @param id 
	 * 			: int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get operation done on a specific service or method.
	 * Possible values are: ADD, MODIFY or DELETE.
	 * 
	 * @return String operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * Set operation done on a specific service or method.
	 * Possible values are: ADD, MODIFY or DELETE.
	 * 
	 * @param operation 
	 * 			: String
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * Get service id.
	 * 
	 * @return int service id 
	 */
	public int getId_service() {
		return id_service;
	}

	/**
	 * Set service id.
	 * 
	 * @param id_service 
	 * 			: int 
	 */
	public void setId_service(int id_service) {
		this.id_service = id_service;
	}

	/**
	 * Get method id of a service.
	 * 
	 * @return int service method id
	 */
	public int getId_serviceMethod() {
		return id_serviceMethod;
	}

	/**
	 * Set method id of a service.
	 * 
	 * @param id_serviceMethod 
	 * 			: int 
	 */
	public void setId_serviceMethod(int id_serviceMethod) {
		this.id_serviceMethod = id_serviceMethod;
	}

	/**
	 * Get date of operation.
	 * 
	 * @return Date date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Set date of operation.
	 * 
	 * @param date of creation 
	 * 			: Date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Get service name.
	 * 
	 * @return service name : String
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * Set service name.
	 * 
	 * @param serviceName 
	 * 			: String
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * Get method name.
	 * 
	 * @return method name : String
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Set method name.
	 * 
	 * @param methodName 
	 * 			: String
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	

}
