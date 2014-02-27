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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

/**
 * A controller which retrieves category data for all users and 
 * adds, deletes category data for admin user. 
 * 
 * @author raman
 *
 */
@Controller
@RequestMapping(value="/api/category")
public class CategoryController {
	
	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
	/**
	 * {@link ResponseObject} Response object contains requested data, 
	 * status of response and if necessary a custom error message.
	 */
	private ResponseObject responseObject;
	/**
	 * Instance of {@link CategoryManager} to retrieve data using Dao classes.
	 */
	@Autowired
	private CategoryManager categoryManager;
	
	/**
	 * Retrieve category data searching by category id
	 * @param category : int category id
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with category data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/{category}", method=RequestMethod.GET)
	public @ResponseBody ResponseObject getCategoryById(@PathVariable int category, HttpServletResponse response) {
		logger.info("-- Cateogry by id --");
		responseObject = new ResponseObject();
		Category cat = categoryManager.getCategoryById(category);
		if(cat!=null){
			responseObject.setData(cat);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setError("No category found with this id");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}
	
	/**
	 * Retrieve all categories data.
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with category data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody ResponseObject getCategories(HttpServletResponse response) {
		logger.info("-- Cateogry List --");
		responseObject = new ResponseObject();
		List<Category> cat = categoryManager.getCategories();
		if(cat!=null){
			responseObject.setData(cat);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setError("There is no category");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}

	/**
	 * Add a new category to database.
	 * @param category : {@link Category} object
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with new added category data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody ResponseObject createCategory(@RequestBody Category category, HttpServletResponse response) {
		logger.info("-- New Cateogry --");
		responseObject = new ResponseObject();
		Category cat = categoryManager.addCategory(category);
		if(cat!=null){
			responseObject.setData(cat);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setError("Problem in adding a new category.");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}
	
	/**
	 * Modify a category.
	 * @param category : {@link Category} object
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with modified category data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/modify", method=RequestMethod.PUT, consumes="application/json")
	public @ResponseBody ResponseObject modifyCategory(@RequestBody Category category, HttpServletResponse response) {
		logger.info("-- Modify Category --");
		responseObject = new ResponseObject();
		Category cat = categoryManager.modifyCategory(category);
		if(cat!=null){
			responseObject.setData(cat);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setError("The category was not modified.");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}

	/**
	 * Delete a category by its id.
	 * @param category : int category id
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with true/false value result of delete operation, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/delete/{id}", method=RequestMethod.DELETE)
	public ResponseObject deleteCategory(@PathVariable int category, HttpServletResponse response) {
		logger.info("-- Delete Cateogry by id --");
		responseObject = new ResponseObject();
		boolean cat = categoryManager.deleteCategory(category);
		if(cat){
			responseObject.setData(cat);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setError("Category was not deleted.");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}

}
