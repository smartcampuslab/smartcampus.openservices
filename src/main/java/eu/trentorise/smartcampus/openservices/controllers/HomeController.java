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
import java.net.ConnectException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.UserManager;

/**
 * A controller which handles requests for the application home page.
 * @author Giulia Canobbio
 *
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	/**
	 * Instance of {@link UserManager} to retrieve data using Dao classes.
	 */
	@Autowired
	private UserManager userManager;
	/**
	 * {@link ResponseObject} Response object contains requested data, 
	 * status of response and if necessary a custom error message.
	 */
	private ResponseObject responseObjetc;
	
	/**
	 * Home view rendering home jsp and save or modified a cookie called value.
	 * This cookie is true if user is logged in, otherwise it is false.
	 * @param request : {@link HttpServletRequest} which is needed to find out if a specific cookie exists.
	 * @param response : {@link HttpServletResponse} which returns a new cookie or if it already exists, modified it.
	 * @return home jsp
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request, HttpServletResponse response) {
		logger.info("-- Welcome home! --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		response.setHeader("User", username);
		logger.info("-- Welcome home! User: "+username+" --");
		String roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
		response.setHeader("Roles", roles);
		logger.info("-- Welcome home! Roles: "+roles+" --");
		
		//Principal and credentials
		logger.info("-- Principal: "+SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()+" --"+
		"Credentials: "+SecurityContextHolder.getContext().getAuthentication().getCredentials()+" --");
		
		//return cookie not Http Only with value true if user is authenticated o.w. false
		String value = "false";
		if(!username.equalsIgnoreCase("anonymousUser")){
			value = SecurityContextHolder.getContext().getAuthentication().isAuthenticated()+"";
		}
		logger.info("-- Welcome home! Authenticated: "+value+" --");
		Cookie cookie = new Cookie("value", value);
		cookie.setPath("/openservice/");
		
		boolean found = false;
		Cookie[] cookies = request.getCookies();
		String name;
		if(cookies!=null){
			for (int i = 0; i < cookies.length; i++) {
				name = cookies[i].getName();
				System.out.println("Found cookies: "+i+", name: "+name);
				if(name.equalsIgnoreCase("value")){
					cookies[i].setValue(value);
					cookies[i].setPath("/openservice/");
					//cookies[i].setMaxAge(0);
					found = true;
					response.addCookie(cookies[i]);
				}
			}
		}
		else{
			System.out.println("No cookie in response");
			response.addCookie(cookie);
		}
		if(cookies!=null && !found){
			System.out.println("No cookie value in this request");
			response.addCookie(cookie);
		}
		
		return "index";
	}
	
	/**
	 * Retrieve user data, it is called after login in.
	 * It returns user data and set cookie value to true.
	 * @param request : {@link HttpServletRequest} which is needed to find out if a specific cookie exists.
	 * @param response : {@link HttpServletResponse} which returns a new cookie or if it already exists, modified it.
	 * @return {@link ResponseObject} with user data, status (OK or NOT FOUND) and 
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/welcome", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject printWelcome(HttpServletRequest request, HttpServletResponse response) {
		logger.info("-- Welcome after login --");
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userManager.getUserByUsername(username);
		responseObjetc = new ResponseObject();
		if(user!=null){
			user.setPassword(null);
			responseObjetc.setData(user);
			responseObjetc.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObjetc.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObjetc.setError("User does not exist or database problem");
		}
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
				cookies[i].setPath("/openservice/");
				response.addCookie(cookies[i]);
				}
				
			}
		}

		return responseObjetc;
	}
	
	/**
	 * Return to home jsp with an error code of NOT FOUND.
	 * It is called when user request wrong urls.
	 * @param value : {@link Cookie} object, it contains value cookie set to false or true.
	 * @param user : {@link Cookie} object, it contains user data such as username and roles.
	 * @param request : {@link HttpServletRequest} DO NOTHING NOW
	 * @param response : {@link HttpServletResponse} which returns an error response (404, NOT FOUND).
	 * @return home jsp
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	
	@RequestMapping()
	public String error(@CookieValue(value="value", required=false) String value, 
			@CookieValue(value="user", required=false) String user, HttpServletRequest request, HttpServletResponse response) 
					throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		logger.info("-- Error mapping! --");
	/*	String username = null;
		String password = null;
		String role = null;
		if(user!=null){
			String sub = (String) user.subSequence(1, user.indexOf(","));//"username":"sara"
			logger.info("*****"+sub);
			username = (String)sub.subSequence(sub.indexOf(":\"")+2, sub.lastIndexOf("\""));
			logger.info("++++++"+username);
			//get user data
			User userDetails = userManager.getUserByUsername(username);
			password = userDetails.getPassword();
			logger.info("++++++"+password);
			role = userDetails.getRole();
			logger.info("++++++"+role);
		}
		
		if(value!=null){
			if(value.equalsIgnoreCase("true")){
				if(user!=null && username!=null && password!=null){
					//try manual authentication
					CustomUserDetailsService udService = new CustomUserDetailsService();
					logger.info("Print user before load it: "+username);
					UserDetails userDetails = udService.loadUserByUsername(username);
					Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
							userDetails.getPassword(),userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
		}*/
		response.setStatus(404);
		return home(request, response);
	}
	
	/**
	 * Login rest, checking if cookie already exists and change their value.
	 * @param request : {@link HttpServletRequest}
	 * @param response : {@link HttpServletResponse} 
	 * @return a {@link ResponseObject} with status (UNAUTHORIZED) and error (You have to sign in)
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject login(HttpServletRequest request, HttpServletResponse response){
		logger.info("-- Perform Login --");
		
		//Check if cookies exist and change it
		Cookie cookie = new Cookie("value", "false");
		cookie.setPath("/openservice/");
		
		Cookie[] cookies = request.getCookies();
		String name;
		if(cookies!=null){
			for (int i = 0; i < cookies.length; i++) {
				name = cookies[i].getName();
				System.out.println("Found cookies: "+i+", name: "+name);
				if(name.equalsIgnoreCase("value")){
					cookies[i].setValue("false");
					cookies[i].setPath("/openservice/");
					response.addCookie(cookies[i]);
				}
				if(name.equalsIgnoreCase("user")){
					cookies[i].setValue(null);
					response.addCookie(cookies[i]);
				}
			}
		}
		
		responseObjetc = new ResponseObject();
		responseObjetc.setError("You have to sign in");
		responseObjetc.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return responseObjetc;
	}
	
	/**
	 * Login failed.
	 * Returns an error message, because user does not exists or insert wrong credentials.
	 * @param response : {@link HttpServletResponse} which returns an error response (404, NOT FOUND).
	 * @return {@link ResponseObject} with status (401, UNAUTHORIZED) and error message.
	 * @throws IOException
	 */
	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject loginfailed(HttpServletResponse response){
		logger.info("-- Login failed --");
		responseObjetc = new ResponseObject();
		responseObjetc.setStatus(401);
		responseObjetc.setError("Invalid username or password");
		response.setStatus(401);
		return responseObjetc;
	}
	
	//User - logout
	/**
	 * Logout, it set authentication to false and set cookie value to false.
	 * Returns index jsp
	 * @param request : {@link HttpServletRequest} which is needed to find out if a specific cookie exists.
	 * @param response : {@link HttpServletResponse} which returns a new cookie or if it already exists, modified it.
	 * @return home jsp
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
					cookies[i].setPath("/openservice/");
					response.addCookie(cookies[i]);
				}
				
			}
		}
		return "index";
	}
	/*
	@ExceptionHandler(ConnectException.class)
	@ResponseBody
	public ResponseObject catchException(HttpServletResponse response){
		responseObjetc = new ResponseObject();
		responseObjetc.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		responseObjetc.setError("We are sorry but now server is down.");
		response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		return responseObjetc;
	}
	*/
}
