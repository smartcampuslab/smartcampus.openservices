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
package eu.trentorise.smartcampus.openservices.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.sun.mail.iap.Response;

import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.UserManager;
import eu.trentorise.smartcampus.openservices.support.ApplicationMailer;
import eu.trentorise.smartcampus.openservices.support.EmailValidator;

/**
 * 
 * User Controller
 * Restful web services for user data
 * mapping /api/user
 * 
 * @author Giulia Canobbio
 *
 */
@Controller
@RequestMapping(value="/api/user")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private ResponseObject responseObject;
	@Autowired
	private UserManager userManager;
	
	/*
	 * USER REST WEB SERVICE
	 */
	
	/**
	 * Retrieve User data by user id
	 * user id is a primary key
	 * @param id
	 * @param response
	 * @return {@link ResponseObject} with user data, status or error message.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject getUserById(@PathVariable int id, HttpServletResponse response){
		logger.info("-- User Data by Id --");
		User user = userManager.getUserById(id);
		responseObject = new ResponseObject();
		if(user == null){
			//response.getWriter().println("User does not exist");
			responseObject.setError("User does not exist");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}else{
			responseObject.setData(user);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	/**
	 * Retrieve User data by username
	 * username is unique
	 * @param username
	 * @param response
	 * @return {@link ResponseObject} with user data, status or error message. 
	 */
	@RequestMapping(value = "/my", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject getUserByUsername(HttpServletResponse response){
		logger.info("-- My User Data--");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userManager.getUserByUsername(username);
		responseObject = new ResponseObject();
		if(user == null){
			responseObject.setError("Connection Problem with database");
			responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}else{
			responseObject.setData(user);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	/**
	 * Get in input a User data and add it to User table.
	 * First check if username is already in use.
	 * Return saved user.
	 * @param user
	 * @param response
	 * @return {@link ResponseObject} with status or error message.
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public ResponseObject createUser(@RequestBody User user, HttpServletResponse response){
		logger.info("-- Add user data --");
		//Check username
		User userDB = userManager.getUserByUsername(user.getUsername());
		responseObject = new ResponseObject();
		if(userDB != null){
			responseObject.setError("Username already use");
			responseObject.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		}else{
			//Check email
			EmailValidator ev = new EmailValidator();
			if(!ev.validate(user.getEmail())){
				responseObject.setError("This is not a valid email address");
				responseObject.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}
			else{
				User newUser = userManager.createUser(user);
				if(newUser!=null){
					responseObject.setData(newUser);
					responseObject.setStatus(HttpServletResponse.SC_CREATED);
					response.setStatus(HttpServletResponse.SC_CREATED);
				}
				else{
					responseObject.setError("Connection problem with database");
					responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
					response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				}
			}
		}
		return responseObject;
	}
	
	/**
	 * Verify user email, sending an email with a link to 
	 * a rest service which enable user's account
	 * @param user
	 * @return {@link ResponseObject} with status or error message.
	 */
	@RequestMapping(value = "/add/verify", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public ResponseObject verifyEmail(@RequestBody User user){
		//TODO
		logger.info("-- User verify email --");
		ApplicationMailer mailer = new ApplicationMailer();
		mailer.sendMail(user.getEmail(), "[OpenService] Welcome!", "For activating your account goes to following link: "
				+ "<choseLink>");
		responseObject = new ResponseObject();
		responseObject.setStatus(HttpServletResponse.SC_OK);
		return responseObject;
	}
	
	/**
	 * Enable user account
	 * @return {@link ResponseObject} with status or error message.
	 */
	@RequestMapping(value = "/add/enable", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public ResponseObject enableUser(){
		logger.info("-- User enable --");
		responseObject = new ResponseObject();
		//TODO
		return responseObject;
	}
	
	/**
	 * Modify user account and update data in db
	 * @param user
	 * @param response
	 * @return {@link ResponseObject} with modified user data, status or error message.
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public ResponseObject modifyUserData(@RequestBody User user, HttpServletResponse response){
		logger.info("-- User modify --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User modifiedUser = userManager.modifyUserData(username, user);
		responseObject = new ResponseObject();
		if (modifiedUser != null) {
			responseObject.setData(modifiedUser);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		} else {
			responseObject.setError("Connection problem with database");
			responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
		return responseObject;
		
	}
	
	/**
	 * Disabled a user by his/her username.
	 * Therefore user cannot login.
	 * Then retrieve disabled user.
	 * @param username
	 * @param response
	 * @return {@link ResponseObject} with disabled user data, status or error message.
	 */
	@RequestMapping(value = "/disable/{username}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject disabledUser(@PathVariable String username, HttpServletResponse response){
		logger.info("-- User disable --");
		User user =  userManager.disabledUser(username);
		responseObject = new ResponseObject();
		if (user != null) {
			responseObject.setData(user);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		} else {
			responseObject.setError("Connection problem with database");
			responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
		return responseObject;
	}
	
}
