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
 * Model of service.
 * 
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

	/**
	 * New instance of {@link Service}.
	 */
	public Service() {
		super();
	}
	/**
	 * New instance of {@link Service} from an instance of 
	 * {@link eu.trentorise.smartcampus.openservices.entities.Service}.
	 * 
	 * @param s 
	 * 			: a {@link eu.trentorise.smartcampus.openservices.entities.Service} instance
	 */
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
	
	/**
	 * Get service id.
	 * 
	 * @return service id : int
	 */
	public int getId() {
		return id;
	}
	/**
	 * Set service id.
	 * 
	 * @param id 
	 * 			: int
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * Get service name.
	 * 
	 * @return service name : String
	 */
	public String getName() {
		return name;
	}
	/**
	 * Set service name.
	 * 
	 * @param name 
	 * 			: String
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Get creator id.
	 * 
	 * @return id of creator : int 
	 */
	public int getCreatorId() {
		return creatorId;
	}
	/**
	 * Set creator id.
	 * 
	 * @param creatorId 
	 * 			: int
	 */
	public void setCreatorId(int creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * Get organization id.
	 * 
	 * @return organization id : int
	 */
	public int getOrganizationId() {
		return organizationId;
	}
	/**
	 * Set organization id.
	 * 
	 * @param organizationId 
	 * 			: int
	 */
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	/**
	 * Get description.
	 * 
	 * @return description : String
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Set description.
	 * 
	 * @param description 
	 * 				: String
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Get category id.
	 * 
	 * @return category id : int
	 */
	public int getCategory() {
		return category;
	}
	/**
	 * Set category id.
	 * 
	 * @param category 
	 * 			: int 
	 */
	public void setCategory(int category) {
		this.category = category;
	}
	/**
	 * Get license.
	 * 
	 * @return license : String
	 */
	public String getLicense() {
		return license;
	}
	/**
	 * Set license.
	 * 
	 * @param license 
	 * 			: String
	 */
	public void setLicense(String license) {
		this.license = license;
	}
	/**
	 * Get version.
	 * 
	 * @return version : String
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * Set version.
	 * 
	 * @param version 
	 * 			: String
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * Get expiration.
	 * 
	 * @return expiration : long
	 */
	public long getExpiration() {
		return expiration;
	}
	/**
	 * Set expiration.
	 * 
	 * @param expiration 
	 * 				: long
	 */
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}
	/**
	 * Get documentation.
	 * 
	 * @return documentation : String
	 */
	public String getDocumentation() {
		return documentation;
	}
	/**
	 * Set documentation.
	 * 
	 * @param documentation 
	 * 			: String 
	 */
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	/**
	 * State value can be: PUBLISH, UNPUBLISH, DEPRECATE.
	 * 
	 * @return state of service : String
	 */
	public String getState() {
		return state;
	}
	/**
	 * State value can be: PUBLISH, UNPUBLISH, DEPRECATE.
	 * 
	 * @param state 
	 * 			: String
	 */
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * Get access information.
	 * 
	 * @return a {@link AccessInformation} instance 
	 */
	public AccessInformation getAccessInformation() {
		return accessInformation;
	}
	/**
	 * Set access information.
	 * 
	 * @param accessInformation 
	 * 			: a {@link AccessInformation} instance 
	 */
	public void setAccessInformation(AccessInformation accessInformation) {
		this.accessInformation = accessInformation;
	}
	/**
	 * Get implementation info.
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
	 * 			: a {@link ImplementationInfo} instance 
	 */
	public void setImplementation(ImplementationInfo implementation) {
		this.implementation = implementation;
	}
	/**
	 * Get list of tags.
	 * 
	 * @return a list of tag
	 */
	public List<String> getTags() {
		return tags;
	}
	/**
	 * Set list of tags.
	 * 
	 * @param tags 
	 * 			: String list of tag
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	/**
	 * Cast from a {@link Service} to {@link eu.trentorise.smartcampus.openservices.entities.Service} instance.
	 * 
	 * @return a {@link eu.trentorise.smartcampus.openservices.entities.Service} instance
	 */
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
	
	/**
	 * Cast list from a {@link Service} to {@link eu.trentorise.smartcampus.openservices.entities.Service} instance.
	 * 
	 * @param services 
	 * 			: collection of {@link eu.trentorise.smartcampus.openservices.entities.Service} instances
	 * @return a list of {@link Service}
	 */
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
	
	/**
	 * Cast from a {@link eu.trentorise.smartcampus.openservices.entities.Service} to {@link Service} instance.
	 * 
	 * @param service 
	 * 			: a {@link eu.trentorise.smartcampus.openservices.entities.Service} instance
	 * @return a {@link Service} instance
	 */
	public static Service fromServiceEntity(eu.trentorise.smartcampus.openservices.entities.Service service) {
		if (service != null) return new Service(service);
		return null;
	}
}
