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

import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.openservices.Constants.ROLES;
import eu.trentorise.smartcampus.openservices.Utils;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.OrganizationManager;
import eu.trentorise.smartcampus.openservices.support.ApplicationMailer;
import eu.trentorise.smartcampus.openservices.support.EmailValidator;
import eu.trentorise.smartcampus.openservices.support.Members;
import eu.trentorise.smartcampus.openservices.support.UserInvitation;

/**
 * Controller that retrieves, adds, modifies and deletes organization data for authenticated users.
 * 
 * @author Giulia Canobbio
 *
 */
@Controller
@RequestMapping(value="/api/org")
public class OrganizationController {

	private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);
	/**
	 * {@link ResponseObject} Response object contains requested data, 
	 * status of response and if necessary a custom error message.
	 */
	private ResponseObject responseObject;
	/**
	 * Instance of {@link OrganizationManager} to retrieve data using Dao classes.
	 */
	@Autowired
	private OrganizationManager organizationManager;
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
	 * Rest web service for Organization
	 */

	/**
	 * Retrieves data of organization searching by its id.
	 * @param org_id : int organization id
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with organization data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/{org_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject orgById(@PathVariable int org_id, HttpServletResponse response){
		logger.info("-- Retrieved organization -- ");
		Organization org = organizationManager.getOrganizationById(org_id);
		responseObject = new ResponseObject();
		if(org==null){
			//response.getWriter().println("No organization for this id");
			responseObject.setError("No organization for this id");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}else{
			responseObject.setData(org);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	//User: manage my data - org
	/**
	 * Retrieves organization data of user having role organization owner.
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with list of organization data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/my", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject orgUser(HttpServletResponse response){
		logger.info("-- View my organization --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Organization> orgs = organizationManager.getUserOrganizations(username);
		responseObject = new ResponseObject();
		if(orgs==null || orgs.size()==0){
			responseObject.setError("No organization for this user");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(orgs);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	//Organization - View organization data
	/**
	 * Retrieves all organization data.
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with list of organization data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject getOrganizations(HttpServletResponse response){
		logger.info("-- View organization list --");
		List<Organization> orgs = organizationManager.getOrganizations(0, 100000000, "name");
		responseObject = new ResponseObject();
		if(orgs==null || orgs.size()==0){
			responseObject.setError("No organization in database");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(orgs);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	//Organization - View organization activity history
	/**
	 * Retrieves history of services belonging to an organization.
	 * Search by organization id.
	 * @param org_id : int organization id
	 * @param response : {@link HttpServletResponse} which returns status of response OK or NOT FOUND
	 * @return {@link ResponseObject} with list of service history data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/activity/history/{org_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ResponseObject getOrgActivityHistory(@PathVariable int org_id, HttpServletResponse response){
		logger.info("-- View organization activity history --");
		List<ServiceHistory> history = organizationManager.getHistory(org_id);
		responseObject = new ResponseObject();
		if(history==null || history.size()==0){
			responseObject.setError("No history found for this organization");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		else{
			responseObject.setData(history);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	//Organization - Manage organization data: create organization
	/**
	 * Add a new Organization in database.
	 * In this way, user becomes organization owner of this organization.
	 * @param org : {@link Organization} organization object.
	 * @param response : {@link HttpServletResponse} which returns status of response CREATED or BAD REQUEST
	 * @return {@link ResponseObject} with status (CREATED or BAD REQUEST) and error message (if status is BAD REQUEST).
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public ResponseObject createOrganization(@RequestBody Organization org, HttpServletResponse response){
		logger.info("-- Create organization --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean result = organizationManager.createOrganization(username, org);
		responseObject = new ResponseObject();
		if(result){
			responseObject.setStatus(HttpServletResponse.SC_CREATED);
			response.setStatus(HttpServletResponse.SC_CREATED);
		}
		else{
			responseObject.setError("Already existing organization, change name");
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			//response.setHeader("Error", "Wrong fields value or duplicate entries");
		}
		return responseObject;
	}
	
	//Organization - Manage Organization data: delete organization (if services are published then it delete them)
	/**
	 * User can delete an organization from database, only if he/she has role 'organization owner' 
	 * for this organization.
	 * Delete operation causes delete of all services, methods and service histories which belogns 
	 * to this organization.
	 * @param id : int organization id
	 * @param response : {@link HttpServletResponse} which returns status of response OK or UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK or UNAUTHORIZED) and error message (if status is UNAUTHORIZED).
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE) 
	@ResponseBody
	public ResponseObject deleteOrganization(@PathVariable int id, HttpServletResponse response){
		logger.info("-- Delete organization --");
		//get user data
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			boolean result = organizationManager.deleteOrganization(username,
					id);
			responseObject = new ResponseObject();
			if (result) {
				responseObject.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseObject.setError("Problem in deleting organization");
				responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (SecurityException s) {
			responseObject.setError("You cannot delete this organization");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return responseObject;
	}
	
	//Organization - Manage organization data: modify organization
	/**
	 * Modify organization data in database. User must have role 'organization owner' for this organization.
	 * He/she can modify only the following field: description, category and contacts.
	 * @param org : {@link Organization} organization object
	 * @param response : {@link HttpServletResponse} which returns status of response OK, BAD REQUEST or UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK or UNAUTHORIZED) and 
	 * error message (if status is BAD REQUEST or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public ResponseObject modifyOrganization(@RequestBody Organization org, HttpServletResponse response){
		logger.info("-- Modify organization --");
		//get user data
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		try{
		boolean result = organizationManager.updateOrganization(username, org);
		responseObject = new ResponseObject();
		if(result){
			responseObject.setStatus(HttpServletResponse.SC_OK);
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else{
			responseObject.setError("Error in  modifying organization data");
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		}catch(SecurityException s){
			responseObject.setError("You cannot modify this organization");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return responseObject;
	}
	
	//Organization - Manage organization data: add/remove organization owner (send a link)
	//Link: rest web service address/key
	/**
	 * Add organization owner to an organization.
	 * User who send invitation must be an organization owner.
	 * @param org_id : int organization id
	 * @param email : String new user email
	 * @return {@link ResponseObject} with status (OK or UNAUTHORIZED) and 
	 * error message (if status is UNAUTHORIZED).
	 */
	@RequestMapping(value = "/manage/owner", method = RequestMethod.POST)//, consumes="application/json")
	@ResponseBody
	public ResponseObject orgManageOwnerData(@RequestBody UserInvitation data,
			HttpServletRequest req){
		logger.info("-- Manage Organization Owner --");
		String email = data.getEmail();
		int org_id = data.getOrg_id();
		logger.info("Data: "+email+", "+org_id);
		
		if(org_id!=0 && (!email.equalsIgnoreCase("") || email!=null) ){
			// Get username
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			responseObject = new ResponseObject();
			// validate email
			EmailValidator ev = new EmailValidator();
			if (ev.validate(email)) {
				try {
					String s = organizationManager.createInvitation(username,
							org_id, ROLES.ROLE_ORGOWNER.toString(), email);
					// return link
					String host = Utils.getAppURL(req); // env.getProperty("host");
					String link = host + "org/enable/" + s;// api/org/manage/owner/add/
					// send it via email to user
					mailer.sendMail(
							env.getProperty("email.username"),
							email,
							env.getProperty("org.message.object")
									+ " "
									+ organizationManager.getOrganizationById(
											org_id).getName(), username + " "
									+ env.getProperty("org.message.body") + " "
									+ link);
					responseObject.setStatus(HttpServletResponse.SC_OK);
				} catch (SecurityException s) {
					responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					responseObject.setError("User cannot invite other users to an organization");
				}
			} else {
				// wrong email address - not valid
				responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				responseObject.setError("Not valid email address");
			}
		}else{
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			responseObject.setError("Wrong input data: missing organization id or email");
		}
		
		return responseObject;
	}
	
	/**
	 * Complete registration of user as 'organization owner' for a specific organization.
	 * Invited user must accept sending key, received by email, to this rest service.
	 * Key is deleted from database and user has role 'organizaiton owner'.
	 * If user is not invited one or there are problems with database, then an error is sent.
	 * @param key : String key, a private key saved in database
	 * @param response : {@link HttpServletResponse} which returns status of response OK, SERVICE UNAVAILABLE,
	 * NOT FOUND or UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE, NOT FOUND or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE, NOT FOUND or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/manage/owner/add/{key}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject orgManageAddOwnerData(@PathVariable String key, HttpServletResponse response){
		logger.info("-- Add organization members by key --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		responseObject = new ResponseObject();
		try {
			boolean result = organizationManager.addOwner(username, key);
			if(result){
				responseObject.setStatus(HttpServletResponse.SC_OK);
			}else{
				responseObject.setError("Connection problem with database");
				responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
			}
		} catch (EntityNotFoundException e) {
			responseObject.setError("Wrong or unexisting key");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} catch(SecurityException s){
			responseObject.setError("Unauthorized user");
			responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		return responseObject;
	}
	
	/**
	 * Delete owner from an organization.
	 * User who deletes other owners must be organization owner.
	 * @param org_id : int organization id
	 * @param user_id : int user id
	 * @param response : : {@link HttpServletResponse} which returns status of response OK, SERVICE UNAVAILABLE 
	 * or UNAUTHORIZED
	 * @return {@link ResponseObject} with status (OK, SERVICE UNAVAILABLE, BAD REQUEST or UNAUTHORIZED) and 
	 * error message (if status is SERVICE UNAVAILABLE, BAD REQUEST or UNAUTHORIZED).
	 */
	@RequestMapping(value = "/manage/owner/delete", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public ResponseObject orgManageDeleteOwnerData(@RequestBody UserInvitation data, 
			HttpServletResponse response){
		logger.info("-- Delete organization members --");
		int org_id = data.getOrg_id();
		int user_id  = data.getUser_id();
		
		if(user_id!=0 && org_id!=0){
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			// Delete connection between user and organization, where user has role ROLE_ORGOWNER
			try {
				boolean result = organizationManager.deleteOrgUser(username,
						org_id, user_id);
				responseObject = new ResponseObject();
				if (result) {
					responseObject.setStatus(HttpServletResponse.SC_OK);
					response.setStatus(HttpServletResponse.SC_OK);
				} else {
					responseObject.setError("Connection problem with database");
					responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
					response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
				}
			} catch (SecurityException s) {
				responseObject.setError("User cannot delete another owner from organization");
				responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			} catch (UnsupportedOperationException u) {
				responseObject.setError("User cannot delete herself/himself");
				responseObject.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}else{
			responseObject.setError("Wrong data input: Missing organization id or user id");
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
		return responseObject;
	}
	
	/**
	 * Retrieves members of organization
	 * @param organization_id : int 
	 * @return {@link ResponseObject} with status (OK or INTERNAL SERVER ERROR) and 
	 * error message (if status is INTERNAL SERVER ERROR).
	 */
	@RequestMapping(value = "/members/{organization_id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject orgMembers(@PathVariable int organization_id){
		logger.info("-- Retrieve organization members --");
		responseObject = new ResponseObject();
		List<Members> members = organizationManager.organizationMembers(organization_id);
		if(members!=null && !members.isEmpty()){
			responseObject.setData(members);
			responseObject.setStatus(HttpServletResponse.SC_OK);
			
		}else{
			responseObject.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			responseObject.setError("Connection problem");
		}
		return responseObject;
	}
}
