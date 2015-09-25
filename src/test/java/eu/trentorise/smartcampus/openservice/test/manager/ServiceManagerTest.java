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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
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
import eu.trentorise.smartcampus.openservices.entities.AccessInformation;
import eu.trentorise.smartcampus.openservices.entities.Category;
import eu.trentorise.smartcampus.openservices.entities.ExecutionProperties;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.managers.CategoryManager;
import eu.trentorise.smartcampus.openservices.managers.OrganizationManager;
import eu.trentorise.smartcampus.openservices.managers.ServiceManager;
import eu.trentorise.smartcampus.openservices.managers.UserManager;
import eu.trentorise.smartcampus.openservices.managers.WADLGenerator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {
		"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/spring-social.xml",
		"file:src/main/webapp/WEB-INF/spring/spring-security.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
		"file:src/test/resources/spring-embedded-db.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class ServiceManagerTest {

	// Log
	private Logger log = LoggerFactory.getLogger(ServiceManagerTest.class);
	@Autowired
	private ServiceManager serviceManager;
	@Autowired
	private ServiceDao serviceDao;
	@Autowired
	private MethodDao methodDao;

	@Autowired
	private UserManager userManager;

	@Autowired
	private CategoryManager catManager;

	@Autowired
	private OrganizationManager orgManager;

	@Autowired
	private WADLGenerator wadlGen;

	private String username;
	private Service service;
	private Method method;

	private int catId;

	private static final String USERNAME = "user1";

	@Before
	public void setUp() {

		createTestCats();
		createTestUsers();
		createTestOrgs();
		log.info("Service Manager Test - set up");
		username = "sara";
		service = new Service();
		service.setName("@TestService");
		service.setDescription("@Test Manager");
		service.setCategory(4);// @Test category
		service.setVersion("0.1");
		service.setCreatorId(1);// user sara
		service.setOrganizationId(1);// bla organization

		method = new Method();
		method.setName("@Test Method");
		method.setSynopsis("A test manager method");
		method.setDocumentation("Bla bla bla");

		/*
		 * TestBoxProperties prop = new TestBoxProperties();
		 * method.setTestboxProprieties(prop);
		 */

		log.info("Set up - END");
	}

	private void createTestUsers() {
		eu.trentorise.smartcampus.openservices.entities.User user1 = new eu.trentorise.smartcampus.openservices.entities.User();
		user1.setUsername(USERNAME);
		user1.setEmail("user1@aa.aa");
		user1.setRole(Constants.ROLES.ROLE_NORMAL.toString());
		userManager.createSocialUser(user1);
	}

	private void createTestCats() {
		Category cat = null;
		for (int i = 1; i < 5; i++) {
			cat = new Category();
			// cat.setId(i);
			cat.setName("cat" + i);
			cat.setDescription("desc");
			catId = catManager.addCategory(cat).getId();
		}
	}

	private void createTestOrgs() {
		Organization org = new Organization();
		org.setName("org1");
		orgManager.createOrganization(USERNAME, org);
	}

	@Test
	public void testCreateService() {
		log.info("TEST - Create new service ");
		boolean result = serviceManager.createService(username, service);
		assertEquals(true, result);
		log.info("Service added: " + service.getName());
		// retrieve id from database
		Service newService = serviceDao.useService(service.getName());
		assertNotNull("No new service", newService);
		service.setId(newService.getId());
		log.info("New service " + newService.getName() + " with id "
				+ newService.getId());
	}

	@Test
	public void testUpdateServiceData() {
		log.info("TEST - Update service data");
		// get id
		Service newService = serviceDao.useService(service.getName());
		service.setId(newService.getId());
		// do test
		service.setDescription("@Test update description");
		boolean result = serviceManager.updateService(username, service);
		assertEquals(true, result);
		log.info("Service update " + service.getName());
		// retrieve service from db
		Service modifiedService = serviceDao.useService(service.getName());
		assertNotNull("No modified service");
		assertEquals(service.getId(), modifiedService.getId());
		assertNotSame(newService.getDescription(),
				modifiedService.getDescription());
		log.info("Modified service " + modifiedService.getName() + " with id "
				+ modifiedService.getId());
	}

	@Test
	public void testChangeServiceState() {
		log.info("TEST - Change service stata");
		// get id
		Service newService = serviceDao.useService(service.getName());
		service.setId(newService.getId());
		// unpublish
		// not found in get services from catalog
		log.info("TEST Unpublish Service");
		List<Service> publishService = serviceDao.showPublishedService(0, 5,
				Constants.ORDER.id);
		for (int i = 0; i < publishService.size(); i++) {
			assertNotSame(service.getId(), publishService.get(i).getId());
		}
		log.info("My service is unpublished, so it is not in the list of published service");

		// publish OR deprecate
		log.info("TEST Change state: Publish Service");
		boolean result = serviceManager.changeState(username, service.getId(),
				Constants.SERVICE_STATE.PUBLISH.toString());
		assertEquals(true, result);
		// found in get services from catalog
		publishService.clear();
		publishService = serviceDao.showPublishedService(0, 5,
				Constants.ORDER.name);
		for (int i = 0; i < publishService.size(); i++) {
			if (publishService.get(i).getId() == service.getId()) {
				assertEquals(service.getId(), publishService.get(i).getId());
			}
		}
		log.info("My service is published, so it is in the list of published service");
	}

	@Test
	public void testAddMethod() {
		log.info("TEST Add new service method");
		// get id
		Service newService = serviceDao.useService(service.getName());
		method.setServiceId(newService.getId());
		boolean result = serviceManager.addMethod(username, method);
		assertTrue("No service method added", result == true);
	}

	@Test
	public void testDeleteMethod() {
		log.info("TEST Add new service method");
		// get method id
		Method newMethod = methodDao.getMethodByName(method.getName(),
				method.getServiceId());
		boolean result = serviceManager.deleteMethod(username,
				newMethod.getId());
		assertTrue("No service method added", result == true);
	}

	@Test
	public void testDeleteService() {
		log.info("TEST - Delete service");
		// get id
		Service newService = serviceDao.useService(service.getName());
		service.setId(newService.getId());
		boolean result = serviceManager
				.deleteService(username, service.getId());
		assertEquals(true, result);
		// find my service
		assertNull("Error: Your service exists",
				serviceManager.getServiceById(service.getId()));
		log.info("Service is deleted");
	}

	@Test
	public void testWADL() {
		service = new Service();
		service.setName("@TestService");
		service.setDescription("@Test Manager");
		service.setCategory(catId);
		service.setVersion("0.1");
		service.setCreatorId(1);// user sara
		service.setOrganizationId(1);// bla organization
		AccessInformation accessInfo = new AccessInformation();
		accessInfo
				.setEndpoint("https://os.smartcommunitylab.it/core.mobility/");
		accessInfo.setFormats("JSON,XML");
		accessInfo.setProtocols("REST,SOAP");

		service.setAccessInformation(accessInfo);

		serviceManager.createService(USERNAME, service);

		method = new Method();
		method.setName("@Test Method");
		method.setSynopsis("A test manager method");
		method.setDocumentation("Bla bla bla");
		method.setServiceId(service.getId());
		ExecutionProperties exec = new ExecutionProperties();
		exec.setHttpMethod("GET");
		exec.setRequestPathTemplate("/bikesharing/{cityname}/paper/{paperId}");
		method.setExecutionProperties(exec);
		serviceManager.addMethod(USERNAME, method);

		method = new Method();
		method.setName("@Test Method 1");
		method.setSynopsis("A test manager method");
		method.setDocumentation("Bla bla bla");
		method.setServiceId(service.getId());
		exec = new ExecutionProperties();
		exec.setHttpMethod("POST");
		exec.setRequestPathTemplate("/bikesharing/trento");
		exec.setRequestBodyTemplate("{\"location\":{\"coordinates\":[<lat>, <lng>],\"address\":\"<address string>\"},\"media\":[\"<img-url>\"],\"attribute\": {\"key\":\"value\"}}");
		method.setExecutionProperties(exec);
		serviceManager.addMethod(USERNAME, method);

		method = new Method();
		method.setName("@Test Method 2");
		method.setSynopsis("A test manager method");
		method.setDocumentation("Bla bla bla");
		method.setServiceId(service.getId());
		exec = new ExecutionProperties();
		exec.setHttpMethod("POST");
		exec.setRequestPathTemplate("/bikesharing/{city}?address={address}&distance={distance-in-km}&prettyOutput={true-false}&rows={number}&start={start-resutl}");
		method.setExecutionProperties(exec);
		serviceManager.addMethod(USERNAME, method);

		System.out.println(serviceManager.getWADL(service.getId()));
	}

	@Test
	public void validWADL() {
		String sample = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
				+ "<application xmlns=\"http://wadl.dev.java.net/2009/02\">\n"
				+ "    <resources base=\"https://os.smartcommunitylab.it/core.mobility/\">\n"
				+ "        <resource path=\"/bikesharing/{cityname}/paper/{paperId}\" id=\"@Test Method\">\n"
				+ "            <param required=\"true\" style=\"template\" name=\"cityname\"/>\n"
				+ "            <param required=\"true\" style=\"template\" name=\"paperId\"/>\n"
				+ "            <method name=\"GET\">\n"
				+ "                <request>\n"
				+ "                    <representation mediaType=\"application/json\"/>\n"
				+ "                    <representation mediaType=\"application/xml\"/>\n"
				+ "                </request>\n"
				+ "                <response>\n"
				+ "                    <representation mediaType=\"application/json\"/>\n"
				+ "                    <representation mediaType=\"application/xml\"/>\n"
				+ "                </response>\n"
				+ "            </method>\n"
				+ "        </resource>\n"
				+ "        <resource path=\"/bikesharing/trento\" id=\"@Test Method 1\">\n"
				+ "            <method name=\"POST\">\n"
				+ "                <request>\n"
				+ "                    <representation mediaType=\"application/json\"/>\n"
				+ "                    <representation mediaType=\"application/xml\"/>\n"
				+ "                </request>\n"
				+ "                <response>\n"
				+ "                    <representation mediaType=\"application/json\"/>\n"
				+ "                    <representation mediaType=\"application/xml\"/>\n"
				+ "                </response>\n"
				+ "            </method>\n"
				+ "        </resource>\n"
				+ "        <resource path=\"/bikesharing/{city}?address={address}&amp;distance={distance-in-km}&amp;prettyOutput={true-false}&amp;rows={number}&amp;start={start-resutl}\" id=\"@Test Method 2\">\n"
				+ "            <param required=\"true\" style=\"template\" name=\"city\"/>\n"
				+ "            <param style=\"query\" name=\"address\"/>\n"
				+ "            <param style=\"query\" name=\"distance\"/>\n"
				+ "            <param style=\"query\" name=\"prettyOutput\"/>\n"
				+ "            <param style=\"query\" name=\"rows\"/>\n"
				+ "            <param style=\"query\" name=\"start\"/>\n"
				+ "            <method name=\"POST\">\n"
				+ "                <request>\n"
				+ "                    <representation mediaType=\"application/json\"/>\n"
				+ "                    <representation mediaType=\"application/xml\"/>\n"
				+ "                </request>\n"
				+ "                <response>\n"
				+ "                    <representation mediaType=\"application/json\"/>\n"
				+ "                    <representation mediaType=\"application/xml\"/>\n"
				+ "                </response>\n"
				+ "            </method>\n"
				+ "        </resource>\n"
				+ "    </resources>\n"
				+ "</application>\n";

		String wrongSample = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

		Assert.assertTrue(wadlGen.isValidWADL(sample));
		Assert.assertFalse(wadlGen.isValidWADL(wrongSample));
	}
}
