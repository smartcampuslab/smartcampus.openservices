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
	public String callbackTwitter( HttpServletRequest request){
		logger.info("Twitter Callback.. Starting ..");
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
		logger.info("Response .. {}",oauthResponse.getBody());
		//TODO save user data in db
		//TODO security
		
		return "redirect:/";
	}

}
