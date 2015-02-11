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
 * Organization Entity for organization table: id: primary key, not null, auto
 * increment int(11); name: not null, unique index varchar(45); description:
 * varchar(255); activity area: varchar(45); category: int(11); contacts:
 * MediumBlob; creator id: int(11); logo: varchar(255).
 * 
 * @author Giulia Canobbio
 * 
 */
@Entity
@Table(name = "Organization")
public class Organization {

	@Id
	@GeneratedValue
	private int id;
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	@Column(name = "description", columnDefinition="LONGTEXT")
	private String description;
	@Column(name = "activityArea")
	private String activityArea;
	@Column(name = "category")
	private int category;
	@Column(name = "contacts")
	@Lob
	private Contact contacts;
	@Column(name = "creator_id")
	private int creatorId;

	@Column(name = "logo")
	private String logo;

	/**
	 * New {@link Organization} instance.
	 */
	public Organization() {

	}

	/**
	 * Get id.
	 * 
	 * @return int id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set id.
	 * 
	 * @param id
	 *            : int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Get name.
	 * 
	 * @return String name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set name.
	 * 
	 * @param name
	 *            : String
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get activity area.
	 * 
	 * @return String activity area
	 */
	public String getActivityArea() {
		return activityArea;
	}

	/**
	 * Set activity area.
	 * 
	 * @param activityArea
	 *            : String
	 */
	public void setActivityArea(String activityArea) {
		this.activityArea = activityArea;
	}

	/**
	 * Get category.
	 * 
	 * @return int category
	 */
	public int getCategory() {
		return category;
	}

	/**
	 * Set category.
	 * 
	 * @param category
	 *            : int
	 */
	public void setCategory(int category) {
		this.category = category;
	}

	/**
	 * Get {@link Contact} instance.
	 * 
	 * @return {@link Contact} instance
	 */
	public Contact getContacts() {
		return contacts;
	}

	/**
	 * Set {@link Contact} instance.
	 * 
	 * @param contacts
	 *            : {@link Contact}
	 */
	public void setContacts(Contact contacts) {
		this.contacts = contacts;
	}

	/**
	 * Get description.
	 * 
	 * @return the description, String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set description.
	 * 
	 * @param description
	 *            : String, the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get creator id of organization.
	 * 
	 * @return the creatorId, int
	 */
	public int getCreatorId() {
		return creatorId;
	}

	/**
	 * Set creator id.
	 * 
	 * @param creatorId
	 *            : int, the creatorId to set
	 */
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}

	/**
	 * Get logo of organization.
	 * 
	 * @return url of logo, String
	 */
	public String getLogo() {
		return logo;
	}

	/**
	 * Set logo.
	 * 
	 * @param logo
	 *            : String, the logo to set
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}
}
