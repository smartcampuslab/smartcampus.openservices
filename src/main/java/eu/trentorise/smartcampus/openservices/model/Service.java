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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.trentorise.smartcampus.openservices.entities.AccessInformation;
import eu.trentorise.smartcampus.openservices.entities.ImplementationInfo;
import eu.trentorise.smartcampus.openservices.entities.Tag;

/**
 * @author raman
 *
 */
public class Service {

	private int id;
	private String name;
	private int creatorId;
	private int organizationId;
	private String description;
	private int category;
	private String license;
	private String version;
	private long expiration;
	private String documentation;
	private String state;
	private AccessInformation accessInformation;
	private ImplementationInfo implementation;
	private List<String> tags;

	public Service() {
		super();
	}
	
	private Service(eu.trentorise.smartcampus.openservices.entities.Service s) {
		setAccessInformation(s.getAccessInformation());
		setCategory(s.getCategory());
		setCreatorId(s.getCreatorId());
		setDescription(s.getDescription());
		setDocumentation(s.getDocumentation());
		setExpiration(s.getExpiration());
		setId(s.getId());
		setImplementation(s.getImplementation());
		setLicense(s.getLicense());
		setName(s.getName());
		setOrganizationId(s.getOrganizationId());
		setState(s.getState());
		if (s.getTags() != null) {
			setTags(new ArrayList<String>());
			for (Tag t : s.getTags()){
				getTags().add(t.getName());
			}
		}
		setVersion(s.getVersion());
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
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
	public long getExpiration() {
		return expiration;
	}
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}
	public String getDocumentation() {
		return documentation;
	}
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public AccessInformation getAccessInformation() {
		return accessInformation;
	}
	public void setAccessInformation(AccessInformation accessInformation) {
		this.accessInformation = accessInformation;
	}
	public ImplementationInfo getImplementation() {
		return implementation;
	}
	public void setImplementation(ImplementationInfo implementation) {
		this.implementation = implementation;
	}
	
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public eu.trentorise.smartcampus.openservices.entities.Service toServiceEntity() {
		eu.trentorise.smartcampus.openservices.entities.Service s = new eu.trentorise.smartcampus.openservices.entities.Service();
		s.setAccessInformation(accessInformation);
		s.setCategory(category);
		s.setCreatorId(creatorId);
		s.setDescription(description);
		s.setDocumentation(documentation);
		s.setExpiration(expiration);
		s.setId(id);
		s.setImplementation(implementation);
		s.setLicense(license);
		s.setName(name);
		s.setOrganizationId(organizationId);
		s.setState(state);
		if (tags != null) {
			s.setTags(new ArrayList<Tag>());
			for (String t : tags){
				Tag tag = new Tag();
				tag.setName(t);
				s.getTags().add(tag);
			}
		}
		s.setVersion(version);
		return s;
	}
	
	public static List<Service> fromServiceEntities(Collection<eu.trentorise.smartcampus.openservices.entities.Service> services) {
		if (services != null) {
			List<Service> res = new ArrayList<Service>();
			for (eu.trentorise.smartcampus.openservices.entities.Service s : services) {
				res.add(new Service(s));
			} 
			return res;
		}
		return null;
	}
	
	public static Service fromServiceEntity(eu.trentorise.smartcampus.openservices.entities.Service service) {
		if (service != null) return new Service(service);
		return null;
	}
}
