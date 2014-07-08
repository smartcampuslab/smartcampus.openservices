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

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import eu.trentorise.smartcampus.openservices.Constants;
import eu.trentorise.smartcampus.openservices.entities.Profile;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.UserManager;
import eu.trentorise.smartcampus.openservices.scribe.OAuthServiceProvider;
import eu.trentorise.smartcampus.openservices.support.CookieUser;

/**
 * Twitter controller
 * that authenticates users and retrieves their data with scribe
 * @author Giulia Canobbio
 *
 */
@Controller
@RequestMapping(value = "/api/oauth/twitter")
public class TwitterController {
	
	private static final Logger logger = LoggerFactory.getLogger(TwitterController.class);
	/**
	 * instance of {@link OAuthServiceProvider}
	 */
	@Autowired
	@Qualifier("twitterServiceProvider")
	private OAuthServiceProvider twitterServiceProvider;
	/**
	 * Instance of {@link Environment} to get all variables in properties file
	 */
	@Autowired
	private Environment env;
	/**
	 * Instance of {@link UserManager} to retrieve and save user data.
	 */
	@Autowired
	private UserManager userManager;
	/**
	 * Instance of {@link UserDetailsService} to authenticate user in spring security
	 */
	@Autowired
	private UserDetailsService manager;
	
	/**
	 * Start oauth authentication with Twitter with scribe
	 * @param request : instance of {@link HttpServletRequest}
	 * @return {@link ResponseObject} with redirected url, status (OK) and error message if user is already logged in.
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject loginTwitter(HttpServletRequest request){
		logger.info("Twitter auth .. Starting ..");
		ResponseObject responseObj = new ResponseObject();
		
		logger.info("Request uri: {}", request.getRequestURI());

		// getting request and access token from session
		Token requestToken = (Token) request.getSession().getAttribute(eu.trentorise.smartcampus.openservices.Constants.OAUTH_REQUEST_TOKEN);
		Token accessToken = (Token) request.getSession().getAttribute(eu.trentorise.smartcampus.openservices.Constants.OAUTH_ACCESS_TOKEN);
		if(requestToken == null || accessToken == null) {
			logger.info("Generates a new request token..");
			// generate new request token
			OAuthService service = twitterServiceProvider.getService();
			logger.info("OAuthService for twitter: {}",service);
			requestToken = service.getRequestToken();
			logger.info("request token: {}",requestToken);
			request.getSession().setAttribute(eu.trentorise.smartcampus.openservices.Constants.OAUTH_REQUEST_TOKEN, requestToken);
			//set data in responseObj
			logger.info("Set data in response..");
			responseObj.setData(service.getAuthorizationUrl(requestToken));
			responseObj.setStatus(HttpServletResponse.SC_OK);
		}else{//already logged in
			responseObj.setData(env.getProperty("local.url"));
			responseObj.setStatus(HttpServletResponse.SC_OK);
			responseObj.setError("Already Logged!");
		}
		logger.info("Redirect to... {}",responseObj.getData());
		
		return responseObj;
	}
	
	/**
	 * Retrieve user data after login and save them in db.
	 * @param request : instance of {@link HttpServletRequest}
	 * @return redirect to home page
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.GET, produces = "application/json")
	public String callbackTwitter( HttpServletRequest request, HttpServletResponse response){
		logger.info("Twitter Callback.. Starting ..");
		ResponseObject responseObj = new ResponseObject();
		
		//request token
		OAuthService service = twitterServiceProvider.getService();
		Token requestToken = (Token) request.getSession().getAttribute(eu.trentorise.smartcampus.openservices.Constants.OAUTH_REQUEST_TOKEN);
		//access token
		String oauthVerifier = request.getParameter(eu.trentorise.smartcampus.openservices.Constants.OAUTH_VERIFIER);
		logger.info("String access token from request as parameter {}", oauthVerifier);
		Verifier verifier = new Verifier(oauthVerifier);
		Token accessToken = service.getAccessToken(requestToken, verifier);
		//store access token in session
		request.getSession().setAttribute(eu.trentorise.smartcampus.openservices.Constants.OAUTH_ACCESS_TOKEN, accessToken);
		
		logger.info("Retrieve user data");
		//get user data
		OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://api.twitter.com/1.1/account/verify_credentials.json");
		service.signRequest(accessToken, oauthRequest);
		Response oauthResponse = oauthRequest.send();
		String userData = oauthResponse.getBody();
		logger.info("Response .. {}",userData);
		
		//Check user data and if he/she does not exist in db, then save data
		try {
			
			//TwitterUser twuser = new ObjectMapper().readValue(userData, TwitterUser.class);
			//logger.info("User id {}", twuser.getId());
			
			JsonNode jsonNode = new ObjectMapper().readTree(userData);
			String username = jsonNode.get("screen_name").asText()+"@twitter";
			String email = "todo.twitter@twitter.todo";
			String name = jsonNode.get("name").asText().split(" ")[0];
			String surname = jsonNode.get("name").asText().split(" ")[1];
			String imgAvatar = jsonNode.get("profile_image_url").asText();
			
			logger.info("- User -");
			logger.info(" username:  {},",username);
			logger.info(" name {}, ",name);
			logger.info(" surname {}",surname);
			
			User userDb = userManager.getUserByUsername(username);
			if(userDb==null){
				logger.info("Save user data");
				//add to db
				User user = new User();
				user.setEmail(email);
				user.setEnabled(1);
				Profile p = new Profile();
				p.setName(name);
				p.setSurname(surname);
				p.setImgAvatar(imgAvatar);
				user.setProfile(p);
				user.setRole(Constants.ROLES.ROLE_NORMAL.toString());
				user.setUsername(username);
				try{
					userManager.createSocialUser(user);
				}catch(SecurityException s){
					logger.info("Different user with same username");
					//different email, same username
					responseObj.setError("Already exists a register user with this email address. Please try to login with correct provider.");
					responseObj.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					return "redirect:/";
				}
			}
			
			// authenticate in spring security
			logger.info("Set authentication security context holder");
			UserDetails userDetails = manager.loadUserByUsername(username);
			Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(), userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
				request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
				
				//check value and set it to true
				Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					for (int i = 0; i < cookies.length; i++) {
						if (cookies[i].getName().equalsIgnoreCase("value")) {
							cookies[i].setValue("true");
							cookies[i].setPath("/openservice/");
							response.addCookie(cookies[i]);
						}
					}
				}
				//user cookie
				CookieUser cu = new CookieUser();
				cu.setUsername(username);
				cu.setRole(Constants.ROLES.ROLE_NORMAL.toString());
				
				Gson gson = new Gson();
				String obj = gson.toJson(cu);
				
				Cookie userCookie = new Cookie("user", obj);
				userCookie.setPath("/openservice/");
				response.addCookie(userCookie);
			
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			responseObj.setError("Openservice server error. Problem in converting data. Retry later!");
			responseObj.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} catch (IOException e) {
			e.printStackTrace();
			responseObj.setError("Twitter server error. Problem in retrieving user data. Retry later!");
			responseObj.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		
		return "redirect:/";
	}

}
