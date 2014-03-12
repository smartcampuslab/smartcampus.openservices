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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.entities.Service;

@Component
@Transactional
public class ManagerServiceSolr {
	
	@Autowired
	private RepositoryServiceSolrIndex repository;
	
	public void addService(Service s){
		ServiceSolr service = ServiceSolr.getBuilder(s.getId(), s.getName())
				.creatorId(s.getCreatorId())
				.organizationId(s.getOrganizationId())
				.description(s.getDescription())
				.category(s.getCategory())
				.license(s.getLicense())
				.version(s.getVersion())
				.expiration(s.getExpiration())
				.documentation(s.getDocumentation())
				.state(s.getState())
				.accessInformation(s.getAccessInformation())
				.implementation(s.getImplementation())
				.tags(s.getTags())
				.build();
		
		repository.addToIndex(service);
	}
	
	public void deleteService(Service s){
		repository.deleteFromIndex(s.getId());
	}
	
	public List<Service> search(String name, String description, String tag, String category){
		List<Service> list = new ArrayList<Service>();
		List<ServiceSolr> results = repository.search(name, description, tag, category);
		
		for(int i=0;i<results.size();i++){
			Service s = new Service(); 
			s.setId(results.get(i).getId());
			s.setCreatorId(results.get(i).getCreatorId());
			s.setOrganizationId(results.get(i).getOrganizationId());
			s.setDescription(results.get(i).getDescription());
			s.setCategory(results.get(i).getCategory());
			s.setLicense(results.get(i).getLicense());
			s.setVersion(results.get(i).getVersion());
			s.setExpiration(results.get(i).getExpiration());
			s.setDocumentation(results.get(i).getDocumentation());
			s.setState(results.get(i).getState());
			s.setAccessInformation(results.get(i).getAccessInformation());
			s.setImplementation(results.get(i).getImplementation());
			s.setTags(results.get(i).getTags());
			list.add(s);
		}
		
		return list;
	}

}
