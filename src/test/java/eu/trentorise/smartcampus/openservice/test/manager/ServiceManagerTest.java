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
package eu.trentorise.smartcampus.openservice.test.manager;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import eu.trentorise.smartcampus.openservices.Constants;
import eu.trentorise.smartcampus.openservices.dao.MethodDao;
import eu.trentorise.smartcampus.openservices.dao.ServiceDao;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.TestBoxProperties;
import eu.trentorise.smartcampus.openservices.entities.TestInfo;
import eu.trentorise.smartcampus.openservices.managers.ServiceManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value= {"file:src/main/webapp/WEB-INF/spring/root-context.xml","file:src/main/webapp/WEB-INF/spring/spring-security.xml","file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class ServiceManagerTest {
	
	// Log
	private Logger log = LoggerFactory.getLogger(ServiceManagerTest.class);
	@Autowired
	private ServiceManager serviceManager;
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private MethodDao methodDao;

	private String username;
	private Service service;
	private Method method;

	@Before
	public void setUp() {
		log.info("Service Manager Test - set up");
		username = "sara";
		service = new Service();
		service.setName("@TestService");
		service.setDescription("@Test Manager");
		service.setCategory(4);//@Test category
		service.setVersion("0.1");
		service.setCreatorId(1);//user sara
		service.setOrganizationId(6);//bla organization
		
		method = new Method();
		method.setName("@Test Method");
		method.setSynopsis("A test manager method");
		method.setDocumentation("Bla bla bla");
		
		TestBoxProperties prop = new TestBoxProperties();
		method.setTestboxProprieties(prop);
		
		log.info("Set up - END");
	}
	
	@Test
	public void testCreateService(){
		log.info("TEST - Create new service ");
		boolean result = serviceManager.createService(username, service);
		assertEquals(true,result);
		//retrieve id from database
		Service newService = serviceDao.useService(service.getName());
		assertNotNull("No new service", newService);
		service.setId(newService.getId());
		log.info("New service "+newService.getName()+" with id "+newService.getId());
	}
	
	@Test
	public void testUpdateServiceData(){
		log.info("TEST - Update service data");
		//get id
		Service newService = serviceDao.useService(service.getName());
		service.setId(newService.getId());
		//do test
		service.setDescription("@Test update description");
		boolean result = serviceManager.updateService(username, service);
		assertEquals(true, result);
		//retrieve service from db
		Service modifiedService = serviceDao.useService(service.getName());
		assertNotNull("No modified service");
		assertEquals(service.getId(),modifiedService.getId());
		assertNotSame(service.getDescription(), modifiedService.getDescription());
		log.info("Modified service "+modifiedService.getName()+" with id "+modifiedService.getId());
	}
	
	@Test
	public void testChangeServiceState(){
		log.info("TEST - Change service stata");
		//get id
		Service newService = serviceDao.useService(service.getName());
		service.setId(newService.getId());
		//unpublish
		//not found in get services from catalog
		log.info("TEST Unpublish Service");
		List<Service> publishService = serviceDao.showPublishedService();
		for(int i=0;i<publishService.size();i++){
			assertNotSame(service.getId(),publishService.get(i).getId());
		}
		log.info("My service is unpublished, so it is not in the list of published service");
		
		//publish OR deprecate
		log.info("TEST Change state: Publish Service");
		boolean result = serviceManager.changeState(username, service.getId(), Constants.SERVICE_STATE.PUBLISH.toString());
		assertEquals(true, result);
		//found in get services from catalog
		publishService.clear();
		publishService = serviceDao.showPublishedService();
		for(int i=0;i<publishService.size();i++){
			if(publishService.get(i).getId()==service.getId()){
				assertEquals(service.getId(),publishService.get(i).getId());
			}
		}
		log.info("My service is published, so it is in the list of published service");
	}
	
	@Test
	public void testAddMethod(){
		log.info("TEST Add new service method");
		//get id
		Service newService = serviceDao.useService(service.getName());
		method.setServiceId(newService.getId());
		boolean result = serviceManager.addMethod(username, method);
		assertTrue("No service method added",result==true);
	}
	
	@Test
	public void testUpdateMethod(){
		log.info("TEST Add new service method");
		//get id
		Service newService = serviceDao.useService(service.getName());
		method.setServiceId(newService.getId());
		method.setSynopsis("Modify test");
		//get method id
		Method newMethod = methodDao.getMethodByName(method.getName());
		method.setId(newMethod.getId());
		boolean result = serviceManager.updateMethod(username, method);
		assertTrue("No service method added",result==true);
		
		//add test
		log.info("TEST Add new test for service method");
		TestInfo testInfo = new TestInfo();
		testInfo.setName("Prova");
		testInfo.setRequestPath("/openservice/test");
		result = serviceManager.addTest(username, method.getId(), testInfo);
		assertTrue("No test for service method added",result==true);
		
		//modify test
		log.info("TEST Modify test for service method");
		testInfo.setRequestPath("/openservice/test1");
		result = serviceManager.modifyTest(username, method.getId(),0, testInfo);
		assertTrue("No test for service method modified",result==true);
		
		//delete test
		log.info("TEST Delete test for service method");
		result = serviceManager.deleteTest(username, method.getId(), 0);
		assertTrue("No test for service method deleted",result==true);
	}
	
	@Test
	public void testDeleteMethod(){
		log.info("TEST Add new service method");
		//get method id
		Method newMethod = methodDao.getMethodByName(method.getName());
		boolean result = serviceManager.deleteMethod(username, newMethod.getId());
		assertTrue("No service method added",result==true);
	}
	
	@Test
	public void testDeleteService(){
		log.info("TEST - Delete service");
		//get id
		Service newService = serviceDao.useService(service.getName());
		service.setId(newService.getId());
		boolean result = serviceManager.deleteService(username, service.getId());
		assertEquals(true, result);
		//find my service
		assertNull("Error: Your service exists",serviceManager.getServiceById(service.getId()));
		log.info("Service is deleted");
	}
	

}
