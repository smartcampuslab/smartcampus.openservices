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
package eu.trentorise.smartcampus.openservices.solr;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;

import eu.trentorise.smartcampus.openservices.entities.AccessInformation;
import eu.trentorise.smartcampus.openservices.entities.ImplementationInfo;
import eu.trentorise.smartcampus.openservices.entities.Tag;

public class ServiceSolr {

	@Id
	@Field
	private int id;
	@Field
	private String name;
	@Field
	private int creatorId;
	@Field
	private int organizationId;
	@Field
	private String description;
	@Field
	private int category;
	@Field
	private String license;
	@Field
	private String version;
	@Field
	private long expiration;
	@Field
	private String documentation;
	@Field
	private String state;
	@Field
	private AccessInformation accessInformation;
	@Field
	private ImplementationInfo implementation;
	@Field
	private List<Tag> tags;
	
	public ServiceSolr(){
		
	}
	
	public int getId() {
		return id;
	}



	public String getName() {
		return name;
	}



	public int getCreatorId() {
		return creatorId;
	}



	public int getOrganizationId() {
		return organizationId;
	}



	public String getDescription() {
		return description;
	}



	public int getCategory() {
		return category;
	}



	public String getLicense() {
		return license;
	}



	public String getVersion() {
		return version;
	}



	public long getExpiration() {
		return expiration;
	}



	public String getDocumentation() {
		return documentation;
	}



	public String getState() {
		return state;
	}



	public AccessInformation getAccessInformation() {
		return accessInformation;
	}



	public ImplementationInfo getImplementation() {
		return implementation;
	}



	public List<Tag> getTags() {
		return tags;
	}

	public static Builder getBuilder(int id, String name){
		return new Builder(id,name);
	}

	public static class Builder{
		private ServiceSolr build;
		
		public Builder(int id, String name){
			build = new ServiceSolr();
			build.id=id;
			build.name=name;
		}
		
		public Builder creatorId(int creatorId){
			build.creatorId=creatorId;
			return this;
		}
		
		public Builder organizationId(int organizationId){
			build.organizationId=organizationId;
			return this;
		}
		
		public Builder description(String description){
			build.description=description;
			return this;
		}
		
		public Builder category(int category){
			build.category=category;
			return this;
		}
		
		public Builder license(String license){
			build.license=license;
			return this;
		}
		
		public Builder version(String version){
			build.version=version;
			return this;
		}
		
		public Builder expiration(long expiration){
			build.expiration=expiration;
			return this;
		}
		
		public Builder documentation(String documentation){
			build.documentation=documentation;
			return this;
		}
		
		public Builder state(String state){
			build.state=state;
			return this;
		}
		
		public Builder accessInformation(AccessInformation accessInformation){
			build.accessInformation=accessInformation;
			return this;
		}
		
		public Builder implementation(ImplementationInfo implementation){
			build.implementation=implementation;
			return this;
		}
		
		public Builder tags(List<Tag> tags){
			build.tags=tags;
			return this;
		}		
		
		public ServiceSolr build(){
			return build;
		}
		
	}

}
