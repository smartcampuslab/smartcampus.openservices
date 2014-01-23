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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import eu.trentorise.smartcampus.openservices.entities.*;
import eu.trentorise.smartcampus.openservices.support.*;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import static junit.framework.Assert.assertEquals;  
import static junit.framework.Assert.assertNotNull;  
import static junit.framework.Assert.assertNull;  
import static junit.framework.Assert.assertTrue; 

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value= {"file:src/main/webapp/WEB-INF/spring/root-context.xml","file:src/main/webapp/WEB-INF/spring/spring-security.xml","file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class CatalagoControllerTest {
	
	private static final String BASE_URL = "http://localhost:8080/openservice/api/catalog";
	private RestTemplate restTemplate;
	//Log
	private Logger log = LoggerFactory.getLogger(ServiceControllerTest.class);
	
	@Before
	public void setUp(){
		restTemplate = new RestTemplate();		
	}
	
	@Test
	public void testServicesPublishDepracate() throws Exception{
		log.info("* Test Catalog REST: /service - STARTING");
		ListService searchService = restTemplate.getForObject(BASE_URL+"/service", ListService.class, new Object[]{});
		assertNotNull("No service for this simple search", searchService);
		assertEquals(1, searchService.getServices().get(0).getId());
		assertTrue("Not empty", searchService.getServices().size()>0);
	}
	
	@Test
	public void testServiceData() throws Exception{
		log.info("* Test Catalog REST: /service/{service_id} - STARTING");
		//publish service
		Service searchService = restTemplate.getForObject(BASE_URL+"/service/1", Service.class, new Object[]{});
		assertNotNull("No service data", searchService);
		assertTrue("Not empty", searchService.getId()==1);
		
		//unpublish service
		Service unpublService = restTemplate.getForObject(BASE_URL+"/service/4", Service.class, new Object[]{});
		assertNull("Service is not unpublish", unpublService);
		
	}
	
	@Test
	public void testServiceMethods() throws Exception{
		log.info("* Test Catalog REST: /service/methods/{service_id} - STARTING");
		ListMethod searchService = restTemplate.getForObject(BASE_URL+"/service/methods/1", ListMethod.class, new Object[]{});
		assertNotNull("No service for this simple search", searchService);
		assertTrue("Empty", searchService.getMethods().size()==0);
	}
	
	@Test
	public void testServiceHistory() throws Exception{
		log.info("* Test Catalog REST: /service/history/{service_id} - STARTING");
		ListServiceHistory searchService = restTemplate.getForObject(BASE_URL+"/service/history/1", ListServiceHistory.class, new Object[]{});
		assertNotNull("No service for this simple search", searchService);
		assertTrue("Empty", searchService.getLserviceh().size()==0);
	}
	
	@Test
	public void testServiceSimpleSearch() throws Exception{
		log.info("* Test Catalog REST: /service/search/{token} - STARTING");
		String token = "book"; 
		ListService searchService = restTemplate.getForObject(BASE_URL+"/service/search/"+token, ListService.class, new Object[]{});
		assertNotNull("No service for this simple search", searchService);
	}
	
	@Test
	public void testServiceSearchByCategory() throws Exception{
		log.info("** Test Catalog REST: /service/browse/category/{category} - STARTING ...");
		ListService searchService = restTemplate.getForObject(BASE_URL+"/service/browse/category/1", ListService.class, new Object[]{});
		assertNotNull("No service for this search by category", searchService);
		//assertTrue("Service category contains book", searchService.getServices().get(0).getCategory().contains("book"));
	}
	
	@Test
	public void testServiceSearchByTags() throws Exception{
		log.info("** Test Catalog REST: /service/browse/tags/{tags} - STARTING ...");
		String token = "book"; 
		ListService searchService = restTemplate.getForObject(BASE_URL+"/service/browse/tags/"+token, ListService.class, new Object[]{});
		assertNotNull("No service for this search by tags", searchService);
		assertTrue("List is empty", searchService.getServices().size()==0);
	}
	
	@Test
	public void testOrg() throws Exception{
		log.info("** Test Catalog REST: /org - STARTING ...");
		String token = "amazon"; 
		ListOrganization searchOrg = restTemplate.getForObject(BASE_URL+"/org", ListOrganization.class, new Object[]{});
		assertNotNull("No organization for this simple search", searchOrg);
		assertTrue("List is not empty", searchOrg.getOrgs().size()>0);

	}
	
	@Test
	public void testOrgSimpleSearch() throws Exception{
		log.info("** Test Catalog REST: /org/search/{token} - STARTING ...");
		String token = "amazon"; 
		ListOrganization searchOrg = restTemplate.getForObject(BASE_URL+"/org/search/"+token, ListOrganization.class, new Object[]{});
		assertNotNull("No organization for this simple search", searchOrg);
		assertTrue("List is not empty", searchOrg.getOrgs().size()>0);

	}

	@Test
	public void testOrgSearchByCategory() throws Exception{
		log.info("** Test Catalog REST: /org/browse/category/{category} - STARTING ...");
		ListOrganization searchOrg = restTemplate.getForObject(BASE_URL+"/org/browse/category/2", ListOrganization.class, new Object[]{});
		assertNotNull("No service for this search by category", searchOrg);
		assertTrue("Organization name is not Amazon1", searchOrg.getOrgs().get(0).getName().equalsIgnoreCase("Amazon1"));
		assertTrue("Organization category contains", searchOrg.getOrgs().size()>0);
	}

}
