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

import javax.persistence.EntityNotFoundException;

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
	 */
	@RequestMapping(value = "/{org_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public Organization orgById(@PathVariable int org_id) {
		Organization org = organizationManager.getOrganizationById(org_id);
		logger.info("-- Retrieved organization -- name: "+org.getName());
		return org;
	}
	
	//User: manage my data - org
	/**
	 * Get my organization data
	 * @param user_id
	 * @return
	 */
	@RequestMapping(value = "/my", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListOrganization orgUser(){
		logger.info("-- View my organization --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		ListOrganization lorg = new ListOrganization();
		lorg.setOrgs(organizationManager.getUserOrganizations(username));
		return lorg;
	}
	
	//Organization - View organization data
	/**
	 * Show organizations
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListOrganization getOrganizations(){
		logger.info("-- View organization list --");
		ListOrganization lorgs = new ListOrganization();
		List<Organization> orgs = organizationManager.getOrganizations();
		lorgs.setOrgs(orgs);
		return lorgs;
	}
	
	//Organization - View organization activity history
	/**
	 * View organization history
	 * @param org_id
	 * @return
	 */
	@RequestMapping(value = "/activity/history/{org_id}", method = RequestMethod.GET, produces="application/json") 
	@ResponseBody
	public ListServiceHistory getOrgActivityHistory(@PathVariable int org_id){
		logger.info("-- View organization activity history --");
		//history of service in this organization
		ListServiceHistory lsh = new ListServiceHistory();
		lsh.setLserviceh(organizationManager.getHistory(org_id));
		return lsh;
	}
	
	//Organization - Manage organization data: create organization
	/**
	 * Add organization
	 * @param org
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public HttpStatus createOrganization(@RequestBody Organization org){
		logger.info("-- Create organization --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		organizationManager.createOrganization(username, org);
		return HttpStatus.CREATED;
	}
	
	//Organization - Manage Organization data: delete organization (if services are published then it delete them)
	/**
	 * Delete an organization
	 * User must be organization owner
	 * @param org
	 * @return
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE) 
	@ResponseBody
	public HttpStatus deleteOrganization(@PathVariable int id){
		logger.info("-- Delete organization --");
		//get user data
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		organizationManager.deleteOrganization(username, id);
		return HttpStatus.OK;
	}
	
	//Organization - Manage organization data: modify organization
	/**
	 * Modify organization data
	 * @param org
	 * @return
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.PUT, consumes="application/json") 
	@ResponseBody
	public HttpStatus modifyOrganization(@RequestBody Organization org){
		logger.info("-- Modify organization --");
		//get user data
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		organizationManager.updateOrganization(username, org);
		return HttpStatus.OK;
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
	 */
	@RequestMapping(value = "/manage/owner/add/{key}", method = RequestMethod.POST)
	@ResponseBody
	public HttpStatus orgManageAddOwnerData(@PathVariable String key){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			organizationManager.addOwner(username, key);
			return HttpStatus.OK;
		} catch (EntityNotFoundException e) {
			return HttpStatus.SERVICE_UNAVAILABLE;
		}
	}
	
	/**
	 * Delete owner from an organization
	 * User who deletes other owners must be organizaton owner
	 * @param org_id
	 * @param user_id
	 * @return
	 */
	@RequestMapping(value = "/manage/owner/delete/{org_id},{user_id}", method = RequestMethod.POST)
	@ResponseBody
	public HttpStatus orgManageDeleteOwnerData(@PathVariable int org_id, @PathVariable int user_id){
		//Delete connection between user and organization, where user has role ROLE_ORGOWNER
		organizationManager.deleteOrgUser(org_id, user_id);
		return HttpStatus.OK;
	}
}
