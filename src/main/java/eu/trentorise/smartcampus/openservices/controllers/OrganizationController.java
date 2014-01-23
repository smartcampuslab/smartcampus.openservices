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
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.ServiceHistory;
import eu.trentorise.smartcampus.openservices.managers.OrganizationManager;
import eu.trentorise.smartcampus.openservices.support.ApplicationMailer;
import eu.trentorise.smartcampus.openservices.support.ListOrganization;
import eu.trentorise.smartcampus.openservices.support.ListServiceHistory;

@Controller
@RequestMapping(value="/api/org")
public class OrganizationController {

	private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);
	
	@Autowired
	private OrganizationManager organizationManager;
	
	/*
	 * Rest web service for Organization
	 */

	/**
	 * Retrieve data of an organization
	 * @param org_id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/{org_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public Organization orgById(@PathVariable int org_id, HttpServletResponse response) throws IOException {
		logger.info("-- Retrieved organization -- ");
		Organization org = organizationManager.getOrganizationById(org_id);
		if(org==null){
			response.getWriter().println("No organization for this id");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return org;
	}
	
	//User: manage my data - org
	/**
	 * Get my organization data
	 * @param user_id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/my", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListOrganization orgUser(HttpServletResponse response) throws IOException{
		logger.info("-- View my organization --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		ListOrganization lorg = new ListOrganization();
		List<Organization> orgs = organizationManager.getUserOrganizations(username);
		lorg.setOrgs(orgs);
		if(orgs==null || orgs.size()==0){
			response.getWriter().println("No organization for this user");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return lorg;
	}
	
	//Organization - View organization data
	/**
	 * Show organizations
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListOrganization getOrganizations(HttpServletResponse response) throws IOException{
		logger.info("-- View organization list --");
		ListOrganization lorgs = new ListOrganization();
		List<Organization> orgs = organizationManager.getOrganizations();
		lorgs.setOrgs(orgs);
		if(orgs==null || orgs.size()==0){
			response.getWriter().println("No organization in database");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return lorgs;
	}
	
	//Organization - View organization activity history
	/**
	 * View organization history
	 * @param org_id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/activity/history/{org_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListServiceHistory getOrgActivityHistory(@PathVariable int org_id, HttpServletResponse response) throws IOException{
		logger.info("-- View organization activity history --");
		//history of service in this organization
		ListServiceHistory lsh = new ListServiceHistory();
		List<ServiceHistory> history = organizationManager.getHistory(org_id);
		lsh.setLserviceh(history);
		if(history==null || history.size()==0){
			response.getWriter().println("No history found for this organization");
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return lsh;
	}
	
	//Organization - Manage organization data: create organization
	/**
	 * Add organization
	 * @param org
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public void createOrganization(@RequestBody Organization org, HttpServletResponse response) throws IOException{
		logger.info("-- Create organization --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean result = organizationManager.createOrganization(username, org);
		if(result){
			response.setStatus(HttpServletResponse.SC_CREATED);
		}
		else{
			response.getWriter().println("Already existing organization, change name");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			//response.setHeader("Error", "Wrong fields value or duplicate entries");
		}
	}
	
	//Organization - Manage Organization data: delete organization (if services are published then it delete them)
	/**
	 * Delete an organization
	 * User must be organization owner
	 * @param org
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE) 
	@ResponseBody
	public void deleteOrganization(@PathVariable int id, HttpServletResponse response) throws IOException{
		logger.info("-- Delete organization --");
		//get user data
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean result = organizationManager.deleteOrganization(username, id);
		if(result){
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else{
			response.getWriter().println("You cannot delete this organization");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	//Organization - Manage organization data: modify organization
	/**
	 * Modify organization data
	 * @param org
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public void modifyOrganization(@RequestBody Organization org, HttpServletResponse response) throws IOException{
		logger.info("-- Modify organization --");
		//get user data
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		boolean result = organizationManager.updateOrganization(username, org);
		if(result){
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else{
			response.getWriter().println("You cannot modify this organization");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	//Organization - Manage organization data: add/remove organization owner (send a link)
	//Link: rest web service address/key
	/**
	 * Add organization owner to an organization
	 * User who invites another user must be an organization owner
	 * @param org_id
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/manage/owner/{org_id}/{role}/{email}", method = RequestMethod.POST)
	@ResponseBody
	public String orgManageOwnerData(@PathVariable int org_id, @PathVariable String role, @PathVariable String email){
		logger.info("-- Manage Organization Owner --");
		//Get username
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		String s = organizationManager.createInvitation(username, org_id, role, email);
		//return link
		String link = "<host>/org/manage/owner/add/"+s;

		// TODO: generalize template
		//send it via email to user
		ApplicationMailer mailer = new ApplicationMailer();
		mailer.sendMail(email, "[OpenService] Invitation to organization", 
				username+" has invited you to become an organization owner of "+
				organizationManager.getOrganizationById(org_id).getName()+". If you are not a user of OpenService, please sign up and become part of " +
				"our community. Please check the following link to accept, if you are already a user: "+link);
		
		return link;
	}
	
	/**
	 * Add new role to user having the invitation link
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/manage/owner/add/{key}", method = RequestMethod.POST)
	@ResponseBody
	public void orgManageAddOwnerData(@PathVariable String key, HttpServletResponse response) throws IOException{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			organizationManager.addOwner(username, key);
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (EntityNotFoundException e) {
			response.getWriter().println("Wrong or unexisting key");
			response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
	}
	
	/**
	 * Delete owner from an organization
	 * User who deletes other owners must be organizaton owner
	 * @param org_id
	 * @param user_id
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/manage/owner/delete/{org_id},{user_id}", method = RequestMethod.POST)
	@ResponseBody
	public void orgManageDeleteOwnerData(@PathVariable int org_id, @PathVariable int user_id, HttpServletResponse response) throws IOException{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		//Delete connection between user and organization, where user has role ROLE_ORGOWNER
		boolean result = organizationManager.deleteOrgUser(username,org_id, user_id);
		if(result){
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else{
			response.getWriter().println("User cannot delete another owner from organization");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}
