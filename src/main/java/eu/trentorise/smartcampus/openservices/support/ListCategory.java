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
package eu.trentorise.smartcampus.openservices.support;

import java.util.List;

import eu.trentorise.smartcampus.openservices.entities.Category;
/**
 * Simple class just to return a list of categories
 * REST JSON use
 * @author Giulia Canobbio
 *
 */
public class ListCategory{

	private List<Category> categories;
	
	/**
	 * New {@ListCategory} instance
	 */
	public ListCategory(){
		
	}
	/**
	 * 
	 * @return categories list
	 */
	public List<Category> getCategories() {
		return categories;
	}

	/**
	 * 
	 * @param categories list
	 */
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

}
