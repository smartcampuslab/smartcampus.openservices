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

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RepositoryServiceSolrIndex implements ServiceSolrIndex{

	@Resource
	private SearchServiceDao repository;
	
	@Transactional
	@Override
	public void addToIndex(ServiceSolr service) {
		ServiceSolr s = ServiceSolr.getBuilder(service.getId(), service.getName())
				.creatorId(service.getCreatorId())
				.organizationId(service.getOrganizationId())
				.description(service.getDescription())
				.category(service.getCategory())
				.license(service.getLicense())
				.version(service.getVersion())
				.expiration(service.getExpiration())
				.documentation(service.getDocumentation())
				.state(service.getState())
				.accessInformation(service.getAccessInformation())
				.implementation(service.getImplementation())
				.tags(service.getTags())
				.build();
		repository.save(service);
	}

	@Transactional
	@Override
	public void deleteFromIndex(int serviceId) {
		repository.delete(serviceId);
	}
	
	@Transactional
	public List<ServiceSolr> search(String name, String description, String tag, String category){
		return repository.findByNameContainsOrDescriptionContainsOrTagsContainsOrCategoryContains(
				name, description, tag, category);
	}

}
