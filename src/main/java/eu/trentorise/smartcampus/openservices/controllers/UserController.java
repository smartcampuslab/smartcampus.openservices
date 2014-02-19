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

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.UserManager;
import eu.trentorise.smartcampus.openservices.support.ApplicationMailer;
import eu.trentorise.smartcampus.openservices.support.EmailValidator;

/**
 * Controller that retrieves and modify user data for logged user and
 * allows enable and disable operations for admin user.
 * 
 * @author Giulia Canobbio
 *
 */
@Controller
@RequestMapping(value="/api/user")
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	/**
	 * {@link ResponseObject} Response object contains requested data, 
	 * status of response and if necessary a custom error message.
	 */
	private ResponseObject responseObject;
	/**
	 * Instance of {@link UserManager} to retrieve data using Dao classes.
	 */
	@Autowired
	private UserManager userManager;
	/**
	 * Instance of {@link ApplicationMailer} to send email.
	 */
	@Autowired
	private ApplicationMailer mailer;
	/**
	 * Instance of {@link Environment} to get all variables in properties file
	 */
	@Autowired
	private Environment env;
	
	/*
	 * USER REST WEB SERVICE
	 */
	
	/**
	 * Retrieve User data by user id.
	 * User id is a primary key.
	 * This operation is for admin user.
	 * @param id : int user id
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with user data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
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
	 * Retrieves user data for logged user.
	 * This operation is for logged user.
	 * @param response {@link HttpServletResponse} which returns status of response OK or SERVICE UNAVAILABLE
	 * @return {@link ResponseObject} with user data, status (OK or SERVICE UNAVAILABLE) and 
	 * error message (if status is SERVICE UNAVAILABLE).
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
	 * @param user : {@link User} instance
	 * @param response : {@link HttpServletResponse} which returns status of response CREATED, FORBIDDEN or 
	 * SERVICE UNAVAILABLE
	 * @return {@link ResponseObject} with new user data, status (CREATED, FORBIDDEN or SERVICE UNAVAILABLE) and 
	 * error message (if status is FORBIDDEN or SERVICE UNAVAILABLE).
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
					//verify email
					verifyEmail(newUser);
					response.setStatus(HttpServletResponse.SC_CREATED);
				}
				else{
					responseObject.setError("Connection problem with database or duplicate email");
					responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
					response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				}
			}
		}
		return responseObject;
	}
	
	/**
	 * Verify user email, sending an email with a link to 
	 * a rest service which enable user's account.
	 * @param user : {@link User} instance that wants to enable account.
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE or UNAUTHORIZED) and 
	 * error message (if status is SERIVCE UNAVAILABLE or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/add/verify", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public ResponseObject verifyEmail(@RequestBody User user){
		logger.info("-- User verify email --");
		responseObject = new ResponseObject();
		try {
			String s = userManager.addKeyVerifyEmail(user.getUsername());
			if(s!=null){
			// return link
			String host = env.getProperty("host");
			String link = host+"api/user/add/enable/"+user.getUsername()+"/"+ s;
			// send it via email to user
			mailer.sendMail(env.getProperty("email.username"),
					user.getEmail(),
					env.getProperty("user.message.object")+" "+user.getUsername(), 
					env.getProperty("user.message.body")+" "+link);
			responseObject.setStatus(HttpServletResponse.SC_OK);
			}
			else{
				responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				responseObject.setError("Verification of user email not successful - Service Unavailable.");
			}
		} catch (SecurityException s) {
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			responseObject.setError("Verification of user email not successful - Your account is already enabled.");
		}
		responseObject.setStatus(HttpServletResponse.SC_OK);
		return responseObject;
	}
	
	/**
	 * Enables user account verifying username and key.
	 * @param username : String username, user who wants to enable his/her account
	 * @param key : String key sent by email to user
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE or NOT FOUND) and 
	 * error message (if status is SERIVCE UNAVAILABLE or NOT FOUND).
	 */
	@RequestMapping(value = "/add/enable/{username}/{key}", method = RequestMethod.GET) 
	@ResponseBody
	public ResponseObject enableUser(@PathVariable String username, @PathVariable String key){
		logger.info("-- User enable --");
		responseObject = new ResponseObject();
		try{
			User enabledUser = userManager.enableUserAfterVerification(username, key);
			if(enabledUser!=null){
				enabledUser.setPassword(null);
				responseObject.setData(enabledUser);
				responseObject.setStatus(HttpServletResponse.SC_OK);
			}else{
				responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				responseObject.setError("Connection problem with database.");
			}
		}catch(EntityNotFoundException e){
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("Wrong key.");
		}
		return responseObject;
	}
	
	/**
	 * Modify user account and update data in db.
	 * This operation is only for logged user.
	 * @param user : {@link User} instance
	 * @param response : {@link HttpServletResponse} which returns status of response OK or 
	 * SERVICE UNAVAILABLE
	 * @return {@link ResponseObject} with modified user data, status (OK or SERVICE UNAVAILABLE) and 
	 * error message (if status is SERVICE UNAVAILABLE).
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
	 * This operation is only for admin user.
	 * @param username : String username of user that admin wants to disable
	 * @param response : {@link HttpServletResponse} which returns status of response OK or 
	 * SERVICE UNAVAILABLE
	 * @return {@link ResponseObject} with disabled user data, status (OK or SERVICE UNAVAILABLE) and 
	 * error message (if status is SERVICE UNAVAILABLE).
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
