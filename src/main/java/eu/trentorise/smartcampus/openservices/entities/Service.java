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

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Service Entity for service table: id: primary key not null, auto increment
 * int(11); name: not null, unique index varchar(45); description: varchar(45);
 * category: int(11); license: text; version: not null, varchar(45); access
 * information: mediumblob; documentation: varchar(45); expiration: bigint(20);
 * state: not null, varchar(10); creator id: not null, int(11); implementation:
 * longblob; organization id: not null, int(11).
 * 
 * @author Giulia Canobbio
 * 
 */
@Entity
@Table(name = "Service")
public class Service {

	@Id
	@GeneratedValue
	private int id;
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	@Column(name = "creator_id")
	private int creatorId;
	@Column(name = "organization_id")
	private int organizationId;

	private String owner;

	@Column(name = "owner_url")
	private String ownerUrl;

	@Column(name = "description", columnDefinition = "LONGTEXT")
	private String description;
	/*
	 * @Column(name="tags") private String tags;
	 */
	@Column(name = "category")
	private int category;

	@Column(name = "license_type")
	private String licenseType;

	@Column(name = "license", columnDefinition = "LONGTEXT")
	private String license;
	@Column(name = "version")
	private String version;
	@Column(name = "expiration")
	private long expiration;
	@Column(name = "documentation", columnDefinition = "LONGTEXT")
	private String documentation;
	@Column(name = "state")
	private String state;

	@Column(name = "accessInformation")
	@Lob
	private AccessInformation accessInformation;
	@Column(name = "implementation")
	@Lob
	private ImplementationInfo implementation;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE,
			CascadeType.MERGE }, fetch = FetchType.EAGER)
	// cascade = CascadeType.ALL
	@JoinColumn(name = "id_service", nullable = false)
	private List<Tag> tags;

	/**
	 * New {@link Service} instance.
	 */
	public Service() {

	}

	/**
	 * Get service id.
	 * 
	 * @return int id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set service id.
	 * 
	 * @param id
	 *            : int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get name of service.
	 * 
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name of service.
	 * 
	 * @param name
	 *            : String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get description of service.
	 * 
	 * @return String description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set description of service.
	 * 
	 * @param description
	 *            : String
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get category of service.
	 * 
	 * @return int category
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * Set category of service.
	 * 
	 * @param category
	 *            : int
	 */
	public void setCategory(int category) {
		this.category = category;
	}

	/**
	 * Get license of service.
	 * 
	 * @return String license
	 */
	public String getLicense() {
		return license;
	}

	/**
	 * Set license of service.
	 * 
	 * @param license
	 *            : String
	 */
	public void setLicense(String license) {
		this.license = license;
	}

	/**
	 * Get version of service.
	 * 
	 * @return String version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set version of service.
	 * 
	 * @param version
	 *            : String
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Get access information.
	 * 
	 * @return {@link AccessInformation} instance
	 */
	public AccessInformation getAccessInformation() {
		return accessInformation;
	}

	/**
	 * Set access information.
	 * 
	 * @param accessInformation
	 *            : {@link AccessInformation}
	 */
	public void setAccessInformation(AccessInformation accessInformation) {
		this.accessInformation = accessInformation;
	}

	/**
	 * Get documentation of service.
	 * 
	 * @return String documentation
	 */
	public String getDocumentation() {
		return documentation;
	}

	/**
	 * Set documentation of service.
	 * 
	 * @param documentation
	 *            : String
	 */
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}

	/**
	 * Get expiration.
	 * 
	 * @return long expiration
	 */
	public long getExpiration() {
		return expiration;
	}

	/**
	 * Set expiration.
	 * 
	 * @param expiration
	 *            : long
	 */
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	/**
	 * Get state of service. Possible values are: publish, unpublish or
	 * deprecate.
	 * 
	 * @return String state
	 */
	public String getState() {
		return state;
	}

	/**
	 * Set state of service. Possible values are: publish, unpublish or
	 * deprecate.
	 * 
	 * @param state
	 *            : String
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Get id of creator.
	 * 
	 * @return int creator id
	 */
	public int getCreatorId() {
		return creatorId;
	}

	/**
	 * Set id of creator.
	 * 
	 * @param creatorId
	 *            : int
	 */
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	/**
	 * Get id of organization that service belongs.
	 * 
	 * @return int organizaton id
	 */
	public int getOrganizationId() {
		return organizationId;
	}

	/**
	 * Set id of organization that service belongs.
	 * 
	 * @param organizationId
	 *            : int
	 */
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * Get implemantation info.
	 * 
	 * @return a {@link ImplementationInfo} instance
	 */
	public ImplementationInfo getImplementation() {
		return implementation;
	}

	/**
	 * Set implementation info.
	 * 
	 * @param implementation
	 *            : {@link ImplementationInfo}
	 */
	public void setImplementation(ImplementationInfo implementation) {
		this.implementation = implementation;
	}

	/**
	 * Get list of tags.
	 * 
	 * @return list of {@link Tag} instances
	 */
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 * Set list of tags.
	 * 
	 * @param tags
	 *            : list of {@link Tag} instances
	 */
	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerUrl() {
		return ownerUrl;
	}

	public void setOwnerUrl(String ownerUrl) {
		this.ownerUrl = ownerUrl;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

}
