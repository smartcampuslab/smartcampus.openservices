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

import java.util.*;

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

import eu.trentorise.smartcampus.openservices.dao.*;
import eu.trentorise.smartcampus.openservices.entities.*;
import eu.trentorise.smartcampus.openservices.support.ApplicationMailer;
import eu.trentorise.smartcampus.openservices.support.GenerateKey;
import eu.trentorise.smartcampus.openservices.support.ListOrganization;
import eu.trentorise.smartcampus.openservices.support.ListServiceHistory;

@Controller
@RequestMapping(value="/api/org")
public class OrganizationController {

	private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);
	
	@Autowired
	private OrganizationDao orgDao;
	@Autowired
	private UserRoleDao urDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ServiceHistoryDao shDao;
	@Autowired
	private TemporaryLinkDao tlDao;
	
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
	public Organization orgByName(@PathVariable int org_id) {
		Organization org = orgDao.getOrganizationById(org_id);
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
		User user = userDao.getUserByUsername(username);
		ListOrganization lorg = new ListOrganization();
		List<Organization> orgs = orgDao.showMyOrganizations(user.getId());
		lorg.setOrgs(orgs);
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
		List<Organization> orgs = orgDao.showOrganizations();
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
		List<ServiceHistory> histories =  shDao.getServiceHistoryByOrgId(org_id);
		ListServiceHistory lsh = new ListServiceHistory();
		lsh.setLserviceh(histories);
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
		User user = userDao.getUserByUsername(username);
		org.setCreatorId(user.getId());
		orgDao.createOrganization(org);
		//get org id
		Organization orgSaved = orgDao.getOrganizationByName(org.getName());
		//add UserRole
		urDao.createUserRole(orgSaved.getCreatorId(), orgSaved.getId(), "ROLE_ORGOWNER");
		return HttpStatus.CREATED;
	}
	
	//Organization - Manage Organization data: delete organization (if services are published then it delete them)
	/**
	 * Delete an organization
	 * User must be organization owner
	 * @param org
	 * @return
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public HttpStatus deleteOrganization(@RequestBody Organization org){
		logger.info("-- Delete organization --");
		//get user data
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userDao.getUserByUsername(username);
		//check user role
		UserRole ur = urDao.getRoleOfUser(user.getId(), org.getId());
		if(ur.getRole().equalsIgnoreCase("ROLE_ORGOWNER")){
			// delete org
			orgDao.deleteOrganization(org);
			// TODO delete service
			// TODO delete user role for organization
			return HttpStatus.OK;
		}
		else return HttpStatus.UNAUTHORIZED;
	}
	
	//Organization - Manage organization data: modify organization
	/**
	 * Modify organization data
	 * @param org
	 * @return
	 */
	@RequestMapping(value = "/modify", method = RequestMethod.POST, consumes="application/json") 
	@ResponseBody
	public HttpStatus modifyOrganization(@RequestBody Organization org){
		logger.info("-- Modify organization --");
		Organization o = orgDao.getOrganizationById(org.getId());
		//get user data
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userDao.getUserByUsername(username);
		//check user role
		UserRole ur = urDao.getRoleOfUser(user.getId(), org.getId());
		if(ur.getRole().equalsIgnoreCase("ROLE_ORGOWNER")){
			//TODO which values can be modified by user?
			o.setDescription(org.getDescription());
			o.setCategory(org.getCategory());
			o.setContacts(org.getContacts());
			orgDao.modifyOrganization(o);
			return HttpStatus.OK;
		}
		else return HttpStatus.UNAUTHORIZED;
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
	@RequestMapping(value = "/manage/owner/{org_id},{email}", method = RequestMethod.POST)
	@ResponseBody
	public String orgManageOwnerData(@PathVariable int org_id, @PathVariable String email){
		logger.info("-- Manage Organization Owner --");
		//Get username
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userDao.getUserByUsername(username);
		//check user role
		Organization org = orgDao.getOrganizationById(org_id);
		UserRole ur = urDao.getRoleOfUser(user.getId(), org.getId());
		if(ur.getRole().equalsIgnoreCase("ROLE_ORGOWNER")){
		
		//Generate a key
		GenerateKey g = new GenerateKey();
		String s = g.getPriv().toString().split("@")[1];
		
		//saved in a temporary table
		TemporaryLink entity = new TemporaryLink();	
		entity.setKey(s);
		entity.setId_org(org_id);
		entity.setEmail(email);
		tlDao.save(entity);
		
		//return link
		String link = "<host>/org/manage/owner/add/"+s;
		System.out.println("Link: "+link);
		
		//send it via email to user
		ApplicationMailer mailer = new ApplicationMailer();
		mailer.sendMail(email, "[OpenService] Invitation to organization", 
				username+" has invited you to become an organization owner of "+
				org.getName()+". If you are not a user of OpenService, please sign up and become part of " +
				"our community. Please check the following link to accept, if you are already a user: "+link);
		
		return link;
		}
		else return "not allowed";
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
		User user = userDao.getUserByUsername(username);
		//Check in table TemporaryLink if this key is saved and if user is correct
		TemporaryLink tl = tlDao.getTLByKey(key);
		if (tl != null) {
			// delete it if it is all ok
			if (tl.getEmail() == user.getEmail()) {
				// add a UserRole data in table: user_id, org_id, role ORG_OWNER
				urDao.createUserRole(user.getId(), tl.getId_org(),
						"ROLE_ORGOWNER");
				// delete temporary link
				tlDao.delete(key);
				return HttpStatus.OK;
			} else
				return HttpStatus.UNAUTHORIZED;
		}
		else return HttpStatus.SERVICE_UNAVAILABLE;
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
		UserRole ur = new UserRole(user_id, org_id, "ROLE_ORGOWNER");
		urDao.deleteUserRole(ur);
		return HttpStatus.OK;
	}
	
}
