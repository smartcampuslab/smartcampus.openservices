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
 * Service Entity for service table
 * primary key, not null, auto increment int(11) id
 * not null, unique index varchar(45) name
 * varchar(45) description
 * varchar(45) tags
 * int(11) category
 * text license
 * not null varchar(45) version
 * mediumblob access information
 * varchar(45) documentation
 * bigint(20) expiration
 * not null varchar(10) state
 * not null int(11) creator id
 * longblob implementation
 * not null int(11) organization id
 * 
 * @author Giulia Canobbio
 *
 */
@Entity
@Table(name="Service")
public class Service {

	@Id
	@GeneratedValue
	private int id;
	@Column(name="name", unique=true, nullable=false)
	private String name;
	@Column(name="creator_id")
	private int creatorId;
	@Column(name="organization_id")
	private int organizationId;
	@Column(name="description")
	private String description;
	@Column(name="tags")
	private String tags;
	@Column(name="category")
	private int category;
	@Column(name="license")
	private String license;
	@Column(name="version")
	private String version;
	@Column(name="expiration")
	private long expiration;
	@Column(name="documentation")
	private String documentation;
	@Column(name="state")
	private String state;
	
	@Column(name="accessInformation")
	@Lob
	private AccessInformation accessInformation;
	@Column(name="implementation")
	@Lob
	private ImplementationInfo implementation;
	
	/**
	 * New {@Service} instance
	 */
	public Service(){
		
	}

	/**
	 * 
	 * @return int id of {@Service} instance
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id in {@Service} instance
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return String name of {@Service} instance
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name in {@Service} instance
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return String description of {@Service} instance
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description in {@Service} instance
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return String tags of {@Service} instance
	 */
	public String getTags() {
		return tags;
	}

	/**
	 * 
	 * @param tags in {@Service} instance
	 */
	public void setTags(String tags) {
		this.tags = tags;
	}

	/**
	 * 
	 * @return int category of {@Service} instance
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * 
	 * @param category in {@Service} instance
	 */
	public void setCategory(int category) {
		this.category = category;
	}

	/**
	 * 
	 * @return String license of {@Service} instance
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * 
	 * @param license in {@Service} instance
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * 
	 * @return String version of {@Service} instance
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 
	 * @param version in {@Service} instance
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 
	 * @return {@Access Information} instance of {@Service} instance
	 */
	public AccessInformation getAccessInformation() {
		return accessInformation;
	}

	/**
	 * 
	 * @param accessInformation in {@Service} instance
	 */
	public void setAccessInformation(AccessInformation accessInformation) {
		this.accessInformation = accessInformation;
	}

	/**
	 * 
	 * @return String documentation of {@Service} instance
	 */
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * 
	 * @param documentation in {@Service} instance
	 */
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * 
	 * @return long expiration of {@Service} instance
	 */
	public long getExpiration() {
		return expiration;
	}

	/**
	 * 
	 * @param expiration in {@Service} instance
	 */
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	/**
	 * 
	 * @return String state of {@Service} instance
	 */
	public String getState() {
		return state;
	}

	/**
	 * 
	 * @param state in {@Service} instance
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 
	 * @return int creator id of {@Service} instance
	 */
	public int getCreatorId() {
		return creatorId;
	}

	/**
	 * 
	 * @param creatorId in {@Service} instance
	 */
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	/**
	 * 
	 * @return int organizaton id of {@Service} instance
	 */
	public int getOrganizationId() {
		return organizationId;
	}

	/**
	 * 
	 * @param organizationId in {@Service} instance
	 */
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * 
	 * @return {@Implementation Info} instance of {@Service} instance
	 */
	public ImplementationInfo getImplementation() {
		return implementation;
	}

	/**
	 * 
	 * @param implementation in {@Service} instance
	 */
	public void setImplementation(ImplementationInfo implementation) {
		this.implementation = implementation;
	}
}
