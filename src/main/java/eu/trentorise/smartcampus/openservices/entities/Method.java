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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Method Entity for Method table
 * primary key, not null, auto increment int(11) id
 * not null, unique index varchar(45) name
 * varchar(45) synopsis
 * varchar(45) documentation
 * int(11) service id
 * longBlob testbox proprieties
 * @author Giulia Canobbio
 *
 */
@Entity
@Table(name="Method")
public class Method {
	
	@Id
	@GeneratedValue
	private int id;
	@Column(name="name")
	private String name;
	@Column(name="synopsis")
	private String synopsis;
	@Column(name="documentation")
	private String documentation;
	@Column(name="service_id")
	private int serviceId;
	@Column(name="testbox_properties")
	@Lob
	private TestBoxProperties testboxProperties;
	
	/**
	 * New instance of {@Method}
	 */
	public Method(){
		
	}

	/**
	 * Get id of {@Method} instance
	 * @return int id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set id in {@Method} instance
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get name of {@Method} instance
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name in {@Method} instance
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get synopsis of {@Method} instance
	 * @return String synopsis
	 */
	public String getSynopsis() {
		return synopsis;
	}

	/**
	 * Set synopsis in {@Method} instance
	 * @param synopsis
	 */
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	/**
	 * Get documentation of {@Method} instance 
	 * @return String documentation
	 */
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * Set documentation in {@Method} instance
	 * @param documentation
	 */
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * Get service id of {@Method} instance
	 * @return int service id
	 */
	public int getServiceId() {
		return serviceId;
	}

	/**
	 * Set service id in {@Method} instance 
	 * @param serviceId
	 */
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * Get {@TestBoxProperties} instance of {@Method} instance
	 * @return {@TestBoxProperties} instance
	 */
	public TestBoxProperties getTestboxProperties() {
		return testboxProperties;
	}

	/**
	 * Set {@TestBoxProperties} instance in {@Method} instance
	 * @param testboxProperties
	 */
	public void setTestboxProprieties(TestBoxProperties testboxProperties) {
		this.testboxProperties = testboxProperties;
	}

}
