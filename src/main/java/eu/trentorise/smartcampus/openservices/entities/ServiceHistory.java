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
 * Service History Entity for service history table
 * primary key, not null, auto increment int(11) id
 * not null varchar(45) operation
 * not null int(11) id service
 * int(11) id service method
 * not null date date
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
	@Column(name="date")
	private Date date;
	
	/**
	 * New {@Service History} instance
	 */
	public ServiceHistory(){
		
	}

	/**
	 * 
	 * @return int id of {@Service History} instance
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id in {@Service History} instance
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return String operation of {@Service History} instance
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * 
	 * @param operation in {@Service History} instance
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * 
	 * @return int service id of {@Service History} instance
	 */
	public int getId_service() {
		return id_service;
	}

	/**
	 * 
	 * @param id_service in {@Service History} instance
	 */
	public void setId_service(int id_service) {
		this.id_service = id_service;
	}

	/**
	 * 
	 * @return int service method id of {@Service History} instance
	 */
	public int getId_serviceMethod() {
		return id_serviceMethod;
	}

	/**
	 * 
	 * @param id_serviceMethod in {@Service History} instance
	 */
	public void setId_serviceMethod(int id_serviceMethod) {
		this.id_serviceMethod = id_serviceMethod;
	}

	/**
	 * 
	 * @return Date date of creation {@Service History} instance
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * 
	 * @param date of creation in {@Service History} instance
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}
