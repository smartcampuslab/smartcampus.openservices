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

import javax.servlet.http.HttpServletResponse;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
/**
 * Social controller for
 * Facebook
 * @author Giulia Canobbio
 *
 */
@Controller
@RequestMapping(value = "/api/social")
public class SocialController {

	private static final Logger logger = LoggerFactory.getLogger(SocialController.class);
	/**
	 * Instance of {@link RestTemplate}
	 */
	private RestTemplate temp;
	/**
	 * Instance of {@link Environment}
	 */
	@Autowired
	private Environment env;
	
	/**
	 * Login with Facebook.
	 * Rest service called for handling redirect url.
	 * 
	 * @param response 
	 * 			: {@link HttpServletResponse} which returns status of response 
	 *            OK or NOT FOUND
	 * @return {@link ResponseObject} with link to Facebook login page, status 
	 * 			(OK or NOT FOUND) and error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/fb", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject socialFb(HttpServletResponse response) {
		ResponseObject responseObject = new ResponseObject();
		temp = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("redirect_uri", env.getProperty("application.url"));
		body.add("scope", "email,user_likes,friends_likes,publish_stream");
		
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body,headers);
		
		ResponseEntity<Object> r =temp.exchange(env.getProperty("application.url")+"signin/facebook", HttpMethod.POST, httpEntity, Object.class);
		
		logger.info("## ResponseEntity Headers: {}  ##",r.getHeaders().getLocation());
		logger.info("## ResponseEntity Body: {} ##",r.getBody());
		logger.info("## Status code: {} ", r.getStatusCode());
		
		if(r.getHeaders().getLocation()!=null){
			logger.info("## Get location");
			responseObject.setData(r.getHeaders().getLocation());
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		else{
			logger.info("## Error");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("Facebook unavailable. Retry Later.");
		}
		
		return responseObject;
	}
	
	/**
	 * Logout with Facebook.
	 * 
	 * @param response 
	 * 			: {@link HttpServletResponse} which returns status of response 
	 *            OK or NOT FOUND
	 * @return {@link ResponseObject} with link to Facebook login page, status 
	 * 			(OK or NOT FOUND) and error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/fb", method = RequestMethod.DELETE, produces = "application/json")
	@ResponseBody
	public ResponseObject socialFbLogout(HttpServletResponse response) {
		ResponseObject responseObject = new ResponseObject();
		temp = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body,headers);
		
		ResponseEntity<Object> r =temp.exchange(env.getProperty("application.url")+"signin/facebook", HttpMethod.DELETE, httpEntity, Object.class);
		
		logger.info("## ResponseEntity Headers: {}  ##",r.getHeaders().getLocation());
		logger.info("## ResponseEntity Body: {} ##",r.getBody());
		logger.info("## Status code: {} ", r.getStatusCode());
		
		if(r.getHeaders().getLocation()!=null){
			responseObject.setData(r.getHeaders().getLocation());
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		else{
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("Failure to logout. Facebook unavailable. Retry Later.");
		}
		
		return responseObject;
	}
}
