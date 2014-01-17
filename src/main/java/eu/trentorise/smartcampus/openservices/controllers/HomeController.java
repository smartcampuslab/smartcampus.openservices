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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.runner.Request;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.UserManager;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	private UserManager userManager;
	
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
		
		//return cookie not Http Only with value true if user is authenticated o.w. false
		String value = "false";
		if(!username.equalsIgnoreCase("anonymousUser")){
			value = SecurityContextHolder.getContext().getAuthentication().isAuthenticated()+"";
		}
		logger.info("-- Welcome home! Authenticated: "+value+" --");
		Cookie cookies = new Cookie("value", value);
		cookies.setPath("/openservice");
		response.addCookie(cookies);
		
		return "index";
	}
	
	/**
	 * Welcome which is always home view
	 * It is just for login problem
	 * @return
	 */
	@RequestMapping(value="/welcome", method = RequestMethod.GET)
	@ResponseBody
	public User printWelcome(HttpServletRequest request, HttpServletResponse response) {
		logger.info("-- Welcome after login --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userManager.getUserByUsername(username);
		user.setPassword(null);
		logger.info("-- User "+username+" --");
		
		//check if cookie value exists and set it
		String value = SecurityContextHolder.getContext().getAuthentication().isAuthenticated()+"";
		Cookie[] cookies = request.getCookies();
		String name;
		if(cookies!=null){
			for (int i = 0; i < cookies.length; i++) {
				name = cookies[i].getName();
				if(name.equalsIgnoreCase("value")){
				cookies[i].setValue(value+"");
				cookies[i].setPath("/openservice");
				}
				response.addCookie(cookies[i]);
			}
		}

		return user;
	}
	
	/**
	 * If url is wrong, then return index page.
	 */
	@RequestMapping()
	public String error(HttpServletRequest request, HttpServletResponse response) {
		logger.info("-- Error mapping! --");
		return home(response);
	}
	
	//try for a strange behavior of spring security
	/**
	 * Login page
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	//@ResponseBody
	public String login(HttpServletRequest request, HttpServletResponse response) throws IOException{
		logger.info("-- Perform Login --");
		/*String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userManager.getUserByUsername(username);
		if(user!=null){
			user.setPassword(null);
			Cookie[] cookies = request.getCookies();
			String name;
			if(cookies!=null){
				for (int i = 0; i < cookies.length; i++) {
					name = cookies[i].getName();
					if(name.equalsIgnoreCase("value")){
					cookies[i].setValue("true");
					}
					response.addCookie(cookies[i]);
				}
			}
			return user;
		}
		else{
			//response.sendError(401);
			return null;
		}*/
		return "index";
	}
	
	/**
	 * Login failed
	 * Show login with error true
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	public String loginfailed(HttpServletRequest request, HttpServletResponse response) throws IOException{
		logger.info("-- Login failed --");
		return "index";
	}
	
	//User - logout
	/**
	 * Logout returns to login page
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response){
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		logger.info("-- Logout "+username+" --"+SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
		String sessionId = ((WebAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails()).getSessionId();
		logger.info("-- JSessionID: --"+sessionId);
		SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
		
		//check if cookie value exists and set it
		String value = SecurityContextHolder.getContext().getAuthentication().isAuthenticated()+"";
		Cookie[] cookies = request.getCookies();
		String name;
		if(cookies!=null){
			for (int i = 0; i < cookies.length; i++) {
				name = cookies[i].getName();
				if(name.equalsIgnoreCase("value")){
					cookies[i].setValue(value+"");
					cookies[i].setPath("/openservice");
				}
				response.addCookie(cookies[i]);
			}
		}
		return "index";
	}
	
}
