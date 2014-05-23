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
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
/**
 * Social controller for
 * Facebook
 * Google Plus
 * Twitter
 * Linkedin
 * @author Giulia Canobbio
 *
 */
@Controller
@RequestMapping(value = "/api/social")
public class SocialController {

	private static final Logger logger = LoggerFactory.getLogger(SocialController.class);
	private RestTemplate temp;
	
	
	@RequestMapping(value = "/fb", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject socialFb(HttpServletResponse response) {
		ResponseObject responseObject = new ResponseObject();
		temp = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("scope", "email,user_likes,friends_likes,publish_stream");
		
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body,headers);
		
		ResponseEntity<Object> r =temp.exchange("http://localhost:8080/openservice/signin/facebook", HttpMethod.POST, httpEntity, Object.class);
		
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
			responseObject.setError("Facebook unavailable. Retry Later.");
		}
		
		return responseObject;
	}
	
	@RequestMapping(value = "/google", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject socialGooglePlus(HttpServletResponse response) {
		ResponseObject responseObject = new ResponseObject();
		temp = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("redirect_uri","http://localhost:8080/openservice/"/*URLEncoder.encode("http://localhost:8080/openservice")*/);
		body.add("scope", "https://www.googleapis.com/auth/plus.me");
		
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body,headers);
		
		ResponseEntity<Object> r =temp.exchange("http://localhost:8080/openservice/signin/google", HttpMethod.POST, httpEntity, Object.class);
		
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
			responseObject.setError("Problem with Google Plus. Retry Later.");
		}
		
		return responseObject;
	}
	
	@RequestMapping(value = "/twitter", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject socialTwitter(HttpServletResponse response) {
		ResponseObject responseObject = new ResponseObject();
		temp = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		//body.add("scope", "email,user_likes,friends_likes,publish_stream");
		
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body,headers);
		
		ResponseEntity<Object> r =temp.exchange("http://localhost:8080/openservice/signin/twitter", HttpMethod.POST, httpEntity, Object.class);
		
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
			responseObject.setError("Twitter unavailable. Retry Later.");
		}
		
		return responseObject;
	}
	
	@RequestMapping(value = "/linkedin", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject socialLinkedin(HttpServletResponse response) {
		ResponseObject responseObject = new ResponseObject();
		temp = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("scope", "r_basicprofile r_emailaddress");
		
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body,headers);
		
		ResponseEntity<Object> r =temp.exchange("http://localhost:8080/openservice/signin/linkedin", HttpMethod.POST, httpEntity, Object.class);
		
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
			responseObject.setError("Linkedin unavailable. Retry Later.");
		}
		
		return responseObject;
	}
}
