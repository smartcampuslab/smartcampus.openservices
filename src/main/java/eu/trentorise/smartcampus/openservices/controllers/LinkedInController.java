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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.scribe.OAuthServiceProvider;

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
	 * Start oauth authentication with Twitter with scribe
	 * @param request : instance of {@link HttpServletRequest}
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
	 * Retrieve user data after login and save them in db.
	 * @param request : instance of {@link HttpServletRequest}
	 * @return redirect to home page
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.GET, produces = "application/json")
	public String callbackTwitter( HttpServletRequest request){
		logger.info("LinkedIn Callback.. Starting ..");
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
		OAuthRequest oauthRequest = new OAuthRequest(Verb.GET, "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address,industry,headline)");
		service.signRequest(accessToken, oauthRequest);
		Response oauthResponse = oauthRequest.send();
		logger.info("Response .. {}",oauthResponse.getBody());
		
		//TODO save user data in db
		//TODO authenticate in spring security
		
		return "redirect:/";
	}
}
