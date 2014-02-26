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
package eu.trentorise.smartcampus.openservice.test.controller;


import static junit.framework.Assert.*; 

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value= {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/spring-security.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
		"file:src/main/webapp/WEB-INF/spring/spring-embedded-db.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class UserControllerTest {
	
	//@Inject private CustomUserDetailsService customUserDetails;
	
	private static final String BASE_URL = "http://localhost:8080/openservice/api/user";
	private RestTemplate restTemplate;
	//Log
	private Logger log = LoggerFactory.getLogger(UserControllerTest.class);
	
	@Before
	public void setUp(){
		
		//customUserDetails = new CustomUserDetailsService();
		
		restTemplate = new RestTemplate();
		
		//set up my Authentication object
		/*Object principal = "sara";
		Object credentials = "sara";
		Authentication auth = new TestingAuthenticationToken(principal, credentials);
		SecurityContextHolder.getContext().setAuthentication(auth);
		*/
		/* version 2 - does not work because I have a CustomUserDetailsService
		Authentication authentication = new UsernamePasswordAuthenticationToken("sara","sara");
		SecurityContextHolder.getContext().setAuthentication(authentication);
		*/
	}
	
	@Test
	public void testAddNewUser() throws Exception{
		log.info("* Test User REST: /add - STARTING");
		User input = new User();
		input.setUsername("testEmailError");
		input.setPassword("provaTest");
		input.setEmail("provaTest@prova");
		try{
			ResponseObject response = restTemplate.postForObject(BASE_URL+"/add", input, ResponseObject.class);
			assertNull("No user here",response.getData());
		}catch(HttpClientErrorException e){
			assertTrue("Not 403", e.getStatusCode().toString().equalsIgnoreCase("403"));
		}
	}
	
	@Test
	public void testLogin() throws Exception{
		log.info("* Test User REST: /perform_login - STARTING");
		// Login - Authenticate user
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// Create the request body as a MultiValueMap
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("j_username", "giulia");
		body.add("j_password", "123456");
		// Note the body object as first parameter!
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body, headers);
		ResponseEntity<ResponseObject> response = restTemplate.exchange(
				"http://localhost:8080/openservice/perform_login",
				HttpMethod.POST, httpEntity, ResponseObject.class);

		log.info("## ResponseEntity Headers: " + response.getHeaders() + " ##");
		log.info("## ResponseEntity Body: " + response.getBody() + " ##");
		log.info("## Status code: " + response.getStatusCode());		

		assertTrue("Location contains welcome", response.getHeaders().getLocation().toString().contains("welcome"));
		assertTrue("Response status is not 302", response.getStatusCode()==HttpStatus.FOUND);

		//Get user data
		HttpEntity<?> httpEntity2 = new HttpEntity<Object>(null, response.getHeaders());
		ResponseEntity<ResponseObject> response2 = restTemplate.exchange(
				"http://localhost:8080/openservice/welcome",
				HttpMethod.GET, httpEntity2, ResponseObject.class);
		
		log.info("##2 ResponseEntity Headers: " + response2.getHeaders() + " ##");
		log.info("##2 ResponseEntity Body: " + response2.getBody() + " ##");
		log.info("##2 Status code: " + response2.getStatusCode());
	}
	
	@Test
	public void testLoginWrongCredentials() throws Exception{
		log.info("* Test User REST: /perform_login with bad credentials - STARTING");
		// Login - Authenticate user
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		// Create the request body as a MultiValueMap
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("j_username", "giulia");
		body.add("j_password", "123489");//wrong password
		// Note the body object as first parameter!
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body, headers);
		ResponseEntity<ResponseObject> response = restTemplate.exchange(
				"http://localhost:8080/openservice/perform_login",
				HttpMethod.POST, httpEntity, ResponseObject.class);

		log.info("## ResponseEntity Headers: "
				+ response.getHeaders().getLocation() + " ##");
		log.info("## ResponseEntity Body: " + response.getBody() + " ##");
		log.info("## Status code: " + response.getStatusCode());

		assertTrue("Location contains loginfailed", response.getHeaders().getLocation().toString().contains("loginfailed"));
	}

}
