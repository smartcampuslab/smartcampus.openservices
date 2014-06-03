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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.openservices.Constants;
import eu.trentorise.smartcampus.openservices.entities.Profile;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.UserManager;
import eu.trentorise.smartcampus.openservices.social.GoogleAuthHelper;
import eu.trentorise.smartcampus.openservices.social.GoogleUser;

/**
 * Google controller
 * Connect to google and retrieve user data
 * 
 * @author Giulia Canobbio
 *
 */
@Controller
@RequestMapping(value = "/api/oauth/google")
public class GoogleController {
	
	private static final Logger logger = LoggerFactory.getLogger(GoogleController.class);
	private GoogleAuthHelper auth = new GoogleAuthHelper();
	
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private UserDetailsService manager;
	
	//send authentication request to google
	@RequestMapping(value = "/auth", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject socialGooglePlus(HttpServletResponse response, HttpServletRequest request) {
		logger.info("****** Google auth ******");
		ResponseObject responseObject = new ResponseObject();
		
		String token = auth.getStateToken();
		boolean found = false;
		/*Search cookie state
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equalsIgnoreCase("state")) {
					logger.info("cookie found - state: "+cookies[i].getValue());
					cookies[i].setValue(token);
					found = true;
				}
			}
		}*/
		//token - try to add in cookie
		//if(!found){
			Cookie c = new Cookie("state", token);
			logger.info("cookie - state: "+c.getValue());
			c.setPath("/openservice/");
			response.addCookie(c);
		//}
		
		responseObject.setData(auth.buildLoginUrl());
		responseObject.setStatus(HttpServletResponse.SC_OK);
		//save in session
		request.getSession().setAttribute("state",token);
		response.setStatus(HttpServletResponse.SC_OK);
		
		return responseObject;
	}
	
	//callback
	//confirm anti-forgery state token
	@RequestMapping(value = "/callback", method = RequestMethod.GET, produces = "application/json")
	//@ResponseBody
	public String confirmStateToken(HttpServletRequest request, HttpServletResponse response){
		ResponseObject responseObj = new ResponseObject();
		
		logger.info("****** Google callback ******");
		String code = request.getParameter("code");
		String token = request.getParameter("state");
		String session_token = "";
		if(request.getSession().getAttribute("state")!=null){
			session_token = request.getSession().getAttribute("state").toString();
		}
		
		logger.info("request code: "+code);
		logger.info("request token: "+token);
		logger.info("request session token: "+session_token);
		
		//Search cookie state
		Cookie[] cookies = request.getCookies();
		String cookie_token="";
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equalsIgnoreCase("state")) {
					cookie_token = cookies[i].getValue();
				}
			}
		}
		logger.info("cookie token: "+cookie_token);
		
		//compare state token in session and state token in response of google
		//if equals return to home
		//if not error page
		if( (code==null || token==null) && (!token.equals(session_token) || !token.equals(cookie_token))){
			logger.info("Error");
			responseObj.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			responseObj.setError("You have to sign in!");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}else{
			try {
				GoogleUser userInfo = auth.getUserInfoJson(code);
				logger.info("User Info: "+userInfo);
				responseObj.setData(userInfo);
				responseObj.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
				logger.info("Check user data");
				
				//check if user is already in
				if(userManager.getUserByUsername(userInfo.getName())==null){
					logger.info("Save user data");
					//add to db
					User user = new User();
					user.setEmail(userInfo.getEmail());
					user.setEnabled(1);
					Profile p = new Profile();
					p.setName(userInfo.getGiven_name());
					p.setSurname(userInfo.getFamily_name());
					p.setImgAvatar(userInfo.getPicture());
					user.setProfile(p);
					user.setRole(Constants.ROLES.ROLE_NORMAL.toString());
					user.setUsername(userInfo.getName());
					userManager.createSocialUser(user);
				}
				// authenticate in spring security
				logger.info("Set authentication security context holder");
				UserDetails userDetails = manager.loadUserByUsername(userInfo.getName());
				Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),userDetails.getPassword(), 
						userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
				
			} catch (IOException e) {
				logger.info("IOException ..");
				e.printStackTrace();
				responseObj.setError("Problem in reading user data");
				responseObj.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		}
		
		return "redirect:index";
		//return responseObj;
	}
}
