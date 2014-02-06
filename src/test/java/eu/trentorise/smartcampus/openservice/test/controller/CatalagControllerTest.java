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

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import eu.trentorise.smartcampus.openservices.entities.*;
import eu.trentorise.smartcampus.openservices.managers.CatalogManager;
import eu.trentorise.smartcampus.openservices.support.*;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import static junit.framework.Assert.assertEquals;  
import static junit.framework.Assert.assertNotNull;  
import static junit.framework.Assert.assertNull;  
import static junit.framework.Assert.assertTrue; 

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value= {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/spring-security.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
		"file:src/main/webapp/WEB-INF/spring/spring-embedded-db.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class CatalagControllerTest {
	
	private static final String BASE_URL = "http://localhost:8080/openservice/api/catalog";
	private RestTemplate restTemplate;
	//Log
	private Logger log = LoggerFactory.getLogger(CatalagControllerTest.class);
	
	@Autowired
	private CatalogManager catalog;
	
	@Before
	public void setUp(){
		restTemplate = new RestTemplate();		
	}
	
	@Test
	public void testServicesPublishDepracate() throws Exception{
		log.info("* Test Catalog REST: /service - STARTING");
		//ListService searchService 
		ResponseObject searchService= restTemplate.getForObject(BASE_URL+"/service", ResponseObject.class, new Object[]{});
		System.out.println("RESPONSEOBJECT: "+searchService.getData()+", "+searchService.getStatus()+", "
				+searchService.getError());
		assertNotNull("No service for this simple search", searchService);
		assertTrue("Not found service with id = 1", searchService.getData().toString().contains("id=1"));
		assertTrue("Not status 200", searchService.getStatus()==200);
	}
	
	@Test
	public void testServiceData() throws Exception{
		log.info("* Test Catalog REST: /service/{service_id} - STARTING");
		//publish service
		log.info("Find published service data..");
		//found publish or deprecate services
		List<Service> services = catalog.catalogServices();
		
		if(services!=null && services.size()>0){
			ResponseObject searchService = restTemplate.getForObject(BASE_URL+"/service/"+services.get(0).getId(), 
					ResponseObject.class, new Object[]{});
			assertNotNull("No service data", searchService);
			assertTrue("Not empty", searchService.getData().toString().contains("id=1"));
			assertTrue("Not status 200", searchService.getStatus()==200);
		}
		
		//unpublish service
		log.info("Not find unpublished service data..");
		//search in published and deprecated services - found some missing number. - TODO
		try{
			ResponseObject unpublService = restTemplate.getForObject(BASE_URL+"/service/4", ResponseObject.class, new Object[]{});
			assertNull("Service is not unpublish", unpublService.getData());
		}catch(HttpClientErrorException e){
			log.info("Status code "+e.getStatusCode());
			assertTrue("Not 404", e.getStatusCode().toString().equalsIgnoreCase("404"));
		}
	}
	
	@Test
	public void testServiceMethods() throws Exception{
		log.info("* Test Catalog REST: /service/methods/{service_id} - STARTING");
		try{
			ResponseObject searchService = restTemplate.getForObject(BASE_URL+"/service/methods/1", ResponseObject.class, new Object[]{});
			assertNotNull("No service for this simple search", searchService);
			assertTrue("Empty", searchService.getData()!=null);
		}catch(HttpClientErrorException e){
			assertTrue("Not 404", e.getStatusCode().toString().equalsIgnoreCase("404"));
		}
	}
	
	@Test
	public void testServiceHistory() throws Exception{
		log.info("* Test Catalog REST: /service/history/{service_id} - STARTING");
		try{
			ResponseObject searchService = restTemplate.getForObject(BASE_URL+"/service/history/1", ResponseObject.class, new Object[]{});
			assertNotNull("No service for this simple search", searchService);
			log.info("Data "+searchService.getData());
			//assertTrue("Empty", searchService.getData()==null);
		}catch(HttpClientErrorException e){
			assertTrue("Not 404", e.getStatusCode().toString().equalsIgnoreCase("404"));
		}
	}
	
	@Test
	public void testServiceSimpleSearch() throws Exception{
		log.info("* Test Catalog REST: /service/search/{token} - STARTING");
		String token = "b"; 
		ResponseObject searchService = restTemplate.getForObject(BASE_URL+"/service/search/"+token, ResponseObject.class, new Object[]{});
		assertNotNull("No service for this simple search", searchService.getData());
	}
	
	@Test
	public void testServiceSearchByCategory() throws Exception{
		log.info("** Test Catalog REST: /service/browse/category/{category} - STARTING ...");
		//get categories data
		CategoryServices categories = catalog.getCategoryServices();
		if(categories!=null && categories.getCategories().size()>0){
			List<Category> category = categories.getCategories();
		
			ResponseObject searchService = restTemplate.getForObject(BASE_URL+"/service/browse/category/"
				+category.get(0).getId(), ResponseObject.class, new Object[]{});
			assertNotNull("No service for this search by category", searchService.getData());
		}
	}
	
	@Test
	public void testServiceSearchByTags() throws Exception{
		log.info("** Test Catalog REST: /service/browse/tags/{tags} - STARTING ...");
		String token = "b"; 
		try{
			ResponseObject searchService = restTemplate.getForObject(BASE_URL+"/service/browse/tags/"+token, ResponseObject.class, new Object[]{});
			assertNotNull("No service for this search by tags", searchService.getData());
			assertTrue("List is empty", searchService.getData()!=null);
		}catch(HttpClientErrorException e){
			assertTrue("Not 404", e.getStatusCode().toString().equalsIgnoreCase("404"));
		}
	}
	
	@Test
	public void testOrg() throws Exception{
		log.info("** Test Catalog REST: /org - STARTING ...");
		String token = "a"; 
		ResponseObject searchOrg = restTemplate.getForObject(BASE_URL+"/org", ResponseObject.class, new Object[]{});
		assertNotNull("No organization for this simple search", searchOrg.getData());

	}
	
	@Test
	public void testOrgSimpleSearch() throws Exception{
		log.info("** Test Catalog REST: /org/search/{token} - STARTING ...");
		String token = "a"; 
		ResponseObject searchOrg = restTemplate.getForObject(BASE_URL+"/org/search/"+token, ResponseObject.class, new Object[]{});
		assertNotNull("No organization for this simple search", searchOrg.getData());
	}

	@Test
	public void testOrgSearchByCategory() throws Exception{
		log.info("** Test Catalog REST: /org/browse/category/{category} - STARTING ...");
		//get categories data
		CategoryServices categories = catalog.getCategoryServices();
		if(categories!=null && categories.getCategories().size()>0){
			List<Category> category = categories.getCategories();
			try{
				ResponseObject searchOrg = restTemplate.getForObject(BASE_URL+"/org/browse/category/"+category.get(0).getId(), 
						ResponseObject.class, new Object[]{});
				assertNotNull("No service for this search by category", searchOrg);
				assertTrue("Organization name is not Amazon1", searchOrg.getData().toString().contains("Amazon1"));
				assertTrue("Organization category contains", searchOrg.getData()!=null);
			}catch(HttpClientErrorException e){
				assertTrue("Not 404", e.getStatusCode().toString().equalsIgnoreCase("404"));
			}
		}
	}

}
