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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.social.GoogleAuthHelper;

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
	
	//send authentication request to google
	@RequestMapping(value = "/auth", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject socialGooglePlus(HttpServletResponse response, HttpServletRequest request) {
		logger.info("****** Google auth ******");
		ResponseObject responseObject = new ResponseObject();
		
		//token - try to add in cookie
		String token = auth.getStateToken();
		Cookie c = new Cookie("state", token);
		logger.info("cookie - state: "+c.getValue());
		c.setPath("/openservice/");
		response.addCookie(c);
		
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
	@ResponseBody
	public ResponseObject confirmStateToken(HttpServletRequest request, HttpServletResponse response){
		ResponseObject responseObj = new ResponseObject();
		
		logger.info("****** Google callback ******");
		String code = request.getParameter("code");
		String token = request.getParameter("state");
		String session_token = request.getSession().getAttribute("state").toString();
		
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
		if( (code==null || token==null) &&!token.equals(session_token)){
			responseObj.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			responseObj.setError("You have to sign in!");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}else{
			try {
				String userInfo = auth.getUserInfoJson(request.getParameter("code"));
				logger.info("User Info: "+userInfo);
				responseObj.setData(userInfo);
				responseObj.setStatus(HttpServletResponse.SC_OK);
				response.setStatus(HttpServletResponse.SC_OK);
			} catch (IOException e) {
				logger.info("IOException ..");
				e.printStackTrace();
				responseObj.setError("Problem in reading user data");
				responseObj.setStatus(HttpServletResponse.SC_NOT_FOUND);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		}
		return responseObj;
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public void logout(HttpServletRequest request, HttpServletResponse response){
		//Delete state cookie 
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
		//Delete state from session
		request.getSession().removeAttribute("state");
		
		//TODO
		//link to https://accounts.google.com/logout
	}
}
