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

import javax.persistence.*;

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
	private String category;
	@Column(name="license")
	private String license;
	@Column(name="version")
	private String version;
	@Column(name="expiration")
	private Date expiration;
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
	
	public Service(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public AccessInformation getAccessInformation() {
		return accessInformation;
	}

	public void setAccessInformation(AccessInformation accessInformation) {
		this.accessInformation = accessInformation;
	}

	public String getDocumentation() {
		return documentation;
	}

	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public ImplementationInfo getImplementation() {
		return implementation;
	}

	public void setImplementation(ImplementationInfo implementation) {
		this.implementation = implementation;
	}
}
