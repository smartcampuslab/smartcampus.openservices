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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.*;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Home view
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletResponse response) {
		logger.info("-- Welcome home! --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		response.setHeader("User", username);
		logger.info("-- Welcome home! User: "+username+" --");
		String roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
		response.setHeader("Roles", roles);
		logger.info("-- Welcome home! Roles: "+roles+" --");
		return "index";
	}
	
	/**
	 * Welcome which is always home view
	 * It is just for login problem
	 * @return
	 */
	@RequestMapping(value="/welcome", method = RequestMethod.GET)
	public String printWelcome() {
		return "index";
	}
	
	/**
	 * If url is wrong, then return index page.
	 */
	@RequestMapping()
	public String error(HttpServletRequest request) {
		logger.info("-- Error mapping! --"+ request.getContextPath());
		return "index";
	}
	
	//try for a strange behavior of spring security
	/**
	 * Login page
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(){
		logger.info("-- Login --");
		return "index";
	}
	
	/**
	 * Login failed
	 * Show login with error true
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginfailed(ModelMap model){
		model.addAttribute("error","true");
		return "index";
	}
	
	//User - logout
	/**
	 * Logout returns to login page
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("-- Logout "+username+" --"+SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
		String sessionId = ((WebAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getSessionId();
		logger.info("-- JSessionID: --"+sessionId);
		return "index";
	}
	
}
