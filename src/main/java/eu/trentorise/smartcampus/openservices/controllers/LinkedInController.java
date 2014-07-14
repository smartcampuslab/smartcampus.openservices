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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;

import eu.trentorise.smartcampus.openservices.Constants;
import eu.trentorise.smartcampus.openservices.entities.Profile;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.UserManager;
import eu.trentorise.smartcampus.openservices.scribe.OAuthServiceProvider;
import eu.trentorise.smartcampus.openservices.social.LinkedInUser;
import eu.trentorise.smartcampus.openservices.support.CookieUser;

/**
 * LinkedIn controller uses scribe to connect user with their linkedIn profile.
 * It retrieves user data and saved in db, if possible.
 * 
 * @author Giulia Canobbio
 *
 */
@Controller
@RequestMapping(value = "/api/oauth/linkedin")
public class LinkedInController {
	
	private static final Logger logger = LoggerFactory.getLogger(LinkedInController.class);
	/**
	 * instance of {@link OAuthServiceProvider}
	 */
	@Autowired
	@Qualifier("linkedInSProvider")
	private OAuthServiceProvider linServiceProvider;
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
	 * LoginTwitter starts oauth authentication with Twitter.
	 * First it searches in session for access and request token, because if they are not null then user
	 * is already logged in, otherwise it generates new request token.
	 * Then request token is saved in session and authorization url for login is created.
	 * 
	 * @param request 
	 * 			: instance of {@link HttpServletRequest}
	 * @return {@link ResponseObject} with redirected url, status (OK) and error message if user is already logged in.
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject loginTwitter(HttpServletRequest request){
		logger.info("LinkedIn auth .. Starting ..");
		ResponseObject responseObj = new ResponseObject();
		
		logger.info("Request uri: {}", request.getRequestURI());

		// getting request and access token from session
		Token requestToken = (Token) request.getSession().getAttribute(eu.trentorise.smartcampus.openservices.Constants.OAUTH_REQUEST_TOKEN);
		Token accessToken = (Token) request.getSession().getAttribute(eu.trentorise.smartcampus.openservices.Constants.OAUTH_ACCESS_TOKEN);
		if(requestToken == null || accessToken == null) {
			logger.info("Generates a new request token..");
			// generate new request token
			OAuthService service = linServiceProvider.getService();
			logger.info("OAuthService for linkedIn: {}",service);
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
	 * CallbackTwitter is the url called back from LinkedIn after successful login from user.
	 * It gets request token from session. 
	 * Then it gets access token from request token and a verifier, and stores it in session.
	 * Using this tokens, it is possible to retrieve user data.
	 * After that data is unmarshalled with XStream (xml format) and saved in db.
	 * If email is already in use, a security warning is thrown and user is redirected to home page 
	 * without authentication and response status UNAUTHORIZED.
	 * Otherwise user is authenticated and cookie user is added, then user is redirected to home page.
	 * 
	 * @param request 
	 * 			: instance of {@link HttpServletRequest}
	 * @param response
	 * 			: response of {@link HttpServletResponse}
	 * @return redirect to home page
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.GET, produces = "application/json")
	public String callbackTwitter( HttpServletRequest request,  HttpServletResponse response){
		logger.info("LinkedIn Callback.. Starting ..");
		//ResponseObject responseObj = new ResponseObject();
		
		//request token
		OAuthService service = linServiceProvider.getService();
		Token requestToken = (Token) request.getSession().getAttribute(eu.trentorise.smartcampus.openservices.Constants.OAUTH_REQUEST_TOKEN);
		//access token
		String oauthVerifier = request.getParameter(eu.trentorise.smartcampus.openservices.Constants.OAUTH_VERIFIER);
		logger.info("String access token from request as parameter {}", oauthVerifier);
		Verifier verifier = new Verifier(oauthVerifier);
		logger.info("Verifier: {}",verifier);
		Token accessToken = service.getAccessToken(requestToken, verifier);
		//store access token in session
		request.getSession().setAttribute(eu.trentorise.smartcampus.openservices.Constants.OAUTH_ACCESS_TOKEN, accessToken);
		
		logger.info("Retrieve user data");
		//get user data
		OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address)");
		service.signRequest(accessToken, oauthRequest);
		Response oauthResponse = oauthRequest.send();
		String responseData = oauthResponse.getBody();
		logger.info("Response .. {}",responseData);
		
		//Unmarshalling data
		XStream xstream = new XStream();
		xstream.alias("person", LinkedInUser.class);
		xstream.aliasField("first-name", LinkedInUser.class, "first_name");
		xstream.aliasField("last-name", LinkedInUser.class, "last_name");
		xstream.aliasField("email-address", LinkedInUser.class, "email_address");
		LinkedInUser lInUser = (LinkedInUser) xstream.fromXML(responseData);
		
		logger.info("- User -");
		logger.info(" id:  {},",lInUser.getId());
		logger.info(" name {}, ",lInUser.getFirst_name());
		logger.info(" surname {}",lInUser.getLast_name());
		
		String username = lInUser.getId()+"@linkedIn";
		
		//save user data in db
		User userDb = userManager.getUserByUsername(username);
		if(userDb==null){
			logger.info("Save user data");
			//add to db
			User user = new User();
			user.setEmail(lInUser.getEmail_address());
			user.setEnabled(1);
			Profile p = new Profile();
			p.setName(lInUser.getFirst_name());
			p.setSurname(lInUser.getLast_name());
			//p.setImgAvatar(imgAvatar);
			user.setProfile(p);
			user.setRole(Constants.ROLES.ROLE_NORMAL.toString());
			user.setUsername(username);
			try{
				userManager.createSocialUser(user);
			}catch(SecurityException s){
				logger.info("Error: Already exists a register user with this email address. Please try to login with correct provider.");
				//different email, same username
				//responseObj.setError("Already exists a register user with this email address. Please try to login with correct provider.");
				//responseObj.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				return "redirect:/";
			}
		}
		
		//authenticate in spring security
		logger.info("Set authentication security context holder");
		UserDetails userDetails = manager.loadUserByUsername(username);
		Authentication auth = new UsernamePasswordAuthenticationToken(
				userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		request.getSession()
				.setAttribute(
						HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
						SecurityContextHolder.getContext());

		// check value and set it to true
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
		// user cookie
		CookieUser cu = new CookieUser();
		cu.setUsername(username);
		cu.setRole(Constants.ROLES.ROLE_NORMAL.toString());

		Gson gson = new Gson();
		String obj = gson.toJson(cu);

		Cookie userCookie = new Cookie("user", obj);
		userCookie.setPath("/openservice/");
		response.addCookie(userCookie);
		
		return "redirect:/";
	}
}
