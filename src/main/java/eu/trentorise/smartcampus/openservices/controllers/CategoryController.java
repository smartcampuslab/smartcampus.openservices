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

package eu.trentorise.smartcampus.openservices.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.openservices.entities.Category;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.managers.CategoryManager;
import eu.trentorise.smartcampus.openservices.support.ListCategory;

/**
 * Category Controller
 * Retrieve, add, delete category data
 * mapping /api/category
 * 
 * @author raman
 *
 */
@Controller
@RequestMapping(value="/api/category")
public class CategoryController {

	private ResponseObject responseObject;
	
	@Autowired
	private CategoryManager categoryManager;
	
	/**
	 * Retrieve category data searching by category id
	 * @param category
	 * @param response
	 * @return {@link ResponseObject} with category data, status or error message.
	 */
	@RequestMapping(value="/{category}", method=RequestMethod.GET)
	public @ResponseBody ResponseObject getCategoryById(@PathVariable int category, HttpServletResponse response) {
		responseObject = new ResponseObject();
		Category cat = categoryManager.getCategoryById(category);
		if(cat!=null){
			responseObject.setData(cat);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setError("Connection problem or no category found");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}
	
	/**
	 * Retrieve all categories data
	 * @param response
	 * @return {@link ResponseObject} with category data, status or error message.
	 */
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody ResponseObject getCategories(HttpServletResponse response) {
		responseObject = new ResponseObject();
		List<Category> cat = categoryManager.getCategories();
		if(cat!=null){
			responseObject.setData(cat);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setError("Connection problem or no categories found");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}

	/**
	 * Add a new category to database
	 * @param category
	 * @param response
	 * @return {@link ResponseObject} with category data, status or error message.
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody ResponseObject createCategory(@RequestBody Category category, HttpServletResponse response) {
		responseObject = new ResponseObject();
		Category cat = categoryManager.addCategory(category);
		if(cat!=null){
			responseObject.setData(cat);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setError("Connection problem or no added category found");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}
	
	/**
	 * Modify a category
	 * @param category
	 * @param response
	 * @return {@link ResponseObject} with category data, status or error message.
	 */
	@RequestMapping(value="/modify", method=RequestMethod.PUT, consumes="application/json")
	public @ResponseBody ResponseObject modifyCategory(@RequestBody Category category, HttpServletResponse response) {
		responseObject = new ResponseObject();
		Category cat = categoryManager.modifyCategory(category);
		if(cat!=null){
			responseObject.setData(cat);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setError("Connection problem or no modified category found");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}

	/**
	 * Delete a category
	 * @param category
	 * @param response
	 * @return {@link ResponseObject} with category data, status or error message.
	 */
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public ResponseObject deleteCategory(@PathVariable int category, HttpServletResponse response) {
		responseObject = new ResponseObject();
		boolean cat = categoryManager.deleteCategory(category);
		if(cat){
			responseObject.setData(cat);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setError("Connection problem");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}

}
