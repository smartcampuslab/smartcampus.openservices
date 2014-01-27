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

import java.util.Arrays;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;

import eu.trentorise.smartcampus.openservices.controllers.ServiceController;
import eu.trentorise.smartcampus.openservices.dao.ServiceDao;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.securitymodel.CustomUserDetailsService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import static junit.framework.Assert.assertEquals;  
import static junit.framework.Assert.assertNotNull;  
import static junit.framework.Assert.assertNull;  
import static junit.framework.Assert.assertTrue; 

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value= {"file:src/main/webapp/WEB-INF/spring/root-context.xml","file:src/main/webapp/WEB-INF/spring/spring-security.xml","file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class ServiceControllerTest {
	/*
	private MockHttpServletRequest requestMock;
	private MockHttpServletResponse responseMock;
	private AnnotationMethodHandlerAdapter handlerAdapter;
	private ObjectMapper mapper;
	
	@Before
	public void setUp(){
		requestMock = new MockHttpServletRequest();
		requestMock.setContentType(MediaType.APPLICATION_JSON_VALUE);
		requestMock.setAuthType(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING);
		
		responseMock = new MockHttpServletResponse();
		
		handlerAdapter = new AnnotationMethodHandlerAdapter();
		HttpMessageConverter[] messageConverters = {new MappingJacksonHttpMessageConverter()};
		handlerAdapter.setMessageConverters(messageConverters);
		
		mapper = new ObjectMapper();
		
	}
	
	@Test
	public void testFindAllServicesInDB() throws Exception{
		requestMock.setMethod("GET");
		requestMock.setRequestURI("/api/service/view");
		//handlerAdapter.handle(requestMock, responseMock, ServiceController);
		
		
	}
	
	@Test
	public void testFindOneService() throws Exception{
		
		
	}
	*/
	
	//version 2 of test controller
	private static final String BASE_URL = "http://localhost:8080/openservice/api/service";
	private RestTemplate restTemplate;
	//Log
	private Logger log = LoggerFactory.getLogger(ServiceControllerTest.class);
	
	@Before
	public void setUp(){
		restTemplate = new RestTemplate();
		
		//need log in
	}
	
	@Test
	public void testAddNewService() throws Exception{
		log.info("* Test Service REST: /add - STARTING");
		//need log in
	}
	
	@Test
	public void testFindOneService() throws Exception{
		log.info("** Test Service REST: /view/description/1 - STARTING ...");
		//Get service with id 1
		ResponseObject service = restTemplate.getForObject(BASE_URL+"/view/description/1", ResponseObject.class, new Object[]{});
		assertNotNull("no service with this id", service);
		//check if result is the expected one
		Service expected = new Service();
		expected.setId(1);
		expected.setName("book seller");
		expected.setCreatorId(1);
		expected.setOrganizationId(2);
		expected.setDescription("Selling book");
		//expected.setCategory("book");
		expected.setVersion("1");
		expected.setExpiration(0);
		expected.setState("publish");
		
		assertTrue("Same id", service.getData().toString().contains("id="+expected.getId()));
	}

}
