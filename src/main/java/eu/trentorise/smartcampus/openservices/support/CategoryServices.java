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

package eu.trentorise.smartcampus.openservices.support;

import java.util.List;

import eu.trentorise.smartcampus.openservices.entities.Category;

/**
 * Class that associates category and services.
 * 
 * @author raman
 *
 */
public class CategoryServices {

	private List<Category> categories;
	private List<Integer> services;
	/**
	 * Get list of categories.
	 * 
	 * @return list of {@link Category}
	 */
	public List<Category> getCategories() {
		return categories;
	}
	/**
	 * Set list of categories.
	 * 
	 * @param categories 
	 * 			: instance of {@link Category}
	 */
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	/**
	 * Get list of services.
	 * 
	 * @return list of services id
	 */
	public List<Integer> getServices() {
		return services;
	}
	/**
	 * Set list of services.
	 * 
	 * @param services 
	 * 			: list of services id
	 */
	public void setServices(List<Integer> services) {
		this.services = services;
	}
	
}
