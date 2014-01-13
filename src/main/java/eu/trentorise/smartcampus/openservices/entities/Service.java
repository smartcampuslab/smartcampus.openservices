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
	@Column(name="name")
	private String name;
	@Column(name="namespace")
	private String namespace;
	@Column(name="id_owner")
	private int id_owner;
	@Column(name="id_org")
	private int id_org;
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
	@Column(name="accessInformation")
	@Lob
	private AccessInformation accessInformation;
	@Column(name="documentation")
	private String documentation;
	@Column(name="expiration")
	private Date expiration;
	@Column(name="implementationInfo")
	@Lob
	private ImplementationInfo implementationInfo;
	@Column(name="state")
	private String state;
	
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

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public int getId_owner() {
		return id_owner;
	}

	public void setId_owner(int id_owner) {
		this.id_owner = id_owner;
	}

	public int getId_org() {
		return id_org;
	}

	public void setId_org(int id_org) {
		this.id_org = id_org;
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

	public ImplementationInfo getImplementationInfo() {
		return implementationInfo;
	}

	public void setImplementationInfo(ImplementationInfo implementationInfo) {
		this.implementationInfo = implementationInfo;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
