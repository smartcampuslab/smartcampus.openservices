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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.web.client.RestTemplate;

import eu.trentorise.smartcampus.openservices.entities.ResponseObject;

import static junit.framework.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value= {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/spring-security.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
		"file:src/test/java/spring-embedded-db.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class CategoryControllerTest {
	
	private static final String BASE_URL = "http://localhost:8080/openservice/api/category";
	private RestTemplate restTemplate;
	//Log
	private Logger log = LoggerFactory.getLogger(CategoryControllerTest.class);
	
	@Before
	public void setUp(){
		restTemplate = new RestTemplate();		
	}
	
	@Test
	public void testCategoryData() throws Exception{
		log.info("* Test Catalog REST: /service/id - STARTING");
		ResponseObject category = restTemplate.getForObject(BASE_URL+"/1", ResponseObject.class, new Object[]{});
		assertNotNull("A category found", category.getData());
		assertTrue("Category id", category.getData().toString().contains("id=1"));
		assertTrue("Category name", category.getData().toString().contains("name=book"));
	}
	
	@Test
	public void testCategoryList() throws Exception{
		log.info("* Test Catalog REST: /service - STARTING");
		ResponseObject category = restTemplate.getForObject(BASE_URL, ResponseObject.class, new Object[]{});
		assertNotNull("A category found", category.getData());
		assertTrue("Category id", category.getData().toString().contains("id=1"));
		assertTrue("Category name", category.getData().toString().contains("name=book"));
	}

}
