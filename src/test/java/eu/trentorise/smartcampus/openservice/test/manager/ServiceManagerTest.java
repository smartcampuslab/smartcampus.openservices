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

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import eu.trentorise.smartcampus.openservices.AuthProtocol;
import eu.trentorise.smartcampus.openservices.OrderBy;
import eu.trentorise.smartcampus.openservices.ServiceState;
import eu.trentorise.smartcampus.openservices.UserRoles;
import eu.trentorise.smartcampus.openservices.dao.ServiceDao;
import eu.trentorise.smartcampus.openservices.entities.AccessInformation;
import eu.trentorise.smartcampus.openservices.entities.Authentication;
import eu.trentorise.smartcampus.openservices.entities.Category;
import eu.trentorise.smartcampus.openservices.entities.ExecutionProperties;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.Organization;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.entities.Tag;
import eu.trentorise.smartcampus.openservices.entities.User;
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

	@Autowired
	private ServiceManager serviceManager;
	@Autowired
	private ServiceDao serviceDao;

	@Autowired
	private UserManager userManager;

	@Autowired
	private CategoryManager catManager;

	@Autowired
	private OrganizationManager orgManager;

	@Autowired
	private WADLGenerator wadlGen;

	private Service service;
	private Method method;

	private int catId;
	private User user;
	private Organization organization;

	private static final String USERNAME = "user1";

	@Before
	public void setUp() {
		createTestCats();
		createTestUsers();
		createTestOrgs();
	}

	@After
	public void clean() {
		for (Service s : serviceManager.getServices()) {
			serviceManager.deleteService(USERNAME, s.getId());
		}

		for (Category cat : catManager.getCategories()) {
			catManager.deleteCategory(cat.getId());
		}

		for (Organization o : orgManager.getOrganizations(0, 1000,
				OrderBy.id.toString())) {
			orgManager.deleteOrganization(USERNAME, o.getId());
		}

	}

	private void createTestUsers() {
		if ((user = userManager.getUserByUsername(USERNAME)) == null) {
			eu.trentorise.smartcampus.openservices.entities.User user1 = new eu.trentorise.smartcampus.openservices.entities.User();
			user1.setUsername(USERNAME);
			user1.setEmail("user1@aa.aa");
			user1.setRole(UserRoles.ROLE_NORMAL.toString());
			user = userManager.createSocialUser(user1);
		}
	}

	private void createTestCats() {
		Category cat = null;
		for (int i = 1; i < 5; i++) {
			cat = new Category();
			cat.setName("cat" + i);
			cat.setDescription("desc");
			catId = catManager.addCategory(cat).getId();
		}
	}

	private void createTestOrgs() {
		Organization org = new Organization();
		org.setName("org1");
		organization = orgManager.createOrganization(USERNAME, org);
	}

	@Test
	public void testCreateService() {
		Service service = new Service();
		service.setName("creation");
		service.setCategory(catId);
		service.setVersion("1.0-FINAL");
		service.setCreatorId(user.getId());
		service.setOrganizationId(1);
		int numSrv = serviceManager.getServices().size();
		Assert.assertTrue(serviceManager.createService(USERNAME, service));
		Assert.assertEquals(numSrv + 1, serviceManager.getServices().size());
	}

	@Test
	public void testUpdateServiceData() {
		Service service = new Service();
		service.setName("creation");
		service.setCategory(catId);
		service.setVersion("1.0-FINAL");
		service.setCreatorId(user.getId());
		service.setOrganizationId(organization.getId());
		Assert.assertTrue(serviceManager.createService(USERNAME, service));
		String desc = "My new description";
		Service loaded = serviceDao.useService("creation");
		Assert.assertNotNull(loaded);
		loaded.setDescription(desc);
		Assert.assertTrue(serviceManager.updateService(USERNAME, loaded));

		loaded = serviceDao.useService("creation");
		Assert.assertEquals(desc, loaded.getDescription());
	}

	@Test
	public void testChangeServiceState() {
		Service service = new Service();
		service.setName("creation");
		service.setCategory(catId);
		service.setVersion("1.0-FINAL");
		service.setCreatorId(user.getId());
		service.setOrganizationId(organization.getId());
		Assert.assertTrue(serviceManager.createService(USERNAME, service));

		List<Service> services = serviceDao.showPublishedService(0, 1000,
				OrderBy.id);

		Service loaded = serviceDao.useService("creation");
		int numSrv = services.size();
		Assert.assertTrue(serviceManager.changeState(USERNAME, loaded.getId(),
				ServiceState.PUBLISH.toString()));

		services = serviceDao.showPublishedService(0, 1000, OrderBy.id);

		Assert.assertEquals(numSrv + 1, services.size());

		Assert.assertTrue(serviceManager.changeState(USERNAME, loaded.getId(),
				ServiceState.UNPUBLISH.toString()));

		services = serviceDao.showPublishedService(0, 1000, OrderBy.id);

		Assert.assertEquals(numSrv, services.size());
	}

	@Test
	public void testAddRemoveMethod() {
		Service service = new Service();
		service.setName("creation");
		service.setCategory(catId);
		service.setVersion("1.0-FINAL");
		service.setCreatorId(user.getId());
		service.setOrganizationId(organization.getId());
		Assert.assertTrue(serviceManager.createService(USERNAME, service));
		Service loaded = serviceDao.useService("creation");

		int numMethods = serviceManager.getServiceMethodsByServiceId(
				loaded.getId()).size();

		Method newOne = new Method();
		newOne.setName("method ONE");
		newOne.setServiceId(loaded.getId());

		Assert.assertTrue(serviceManager.addMethod(USERNAME, newOne));

		List<Method> methods = serviceManager
				.getServiceMethodsByServiceId(loaded.getId());
		Assert.assertEquals(numMethods + 1, methods.size());

		Assert.assertTrue(serviceManager.deleteMethod(USERNAME, methods.get(0)
				.getId()));

		Assert.assertEquals(numMethods, serviceManager
				.getServiceMethodsByServiceId(loaded.getId()).size());

	}

	@Test
	public void testDeleteService() {
		Service service = new Service();
		service.setName("creation");
		service.setCategory(catId);
		service.setVersion("1.0-FINAL");
		service.setCreatorId(user.getId());
		service.setOrganizationId(organization.getId());
		Assert.assertTrue(serviceManager.createService(USERNAME, service));
		Service loaded = serviceDao.useService("creation");
		Assert.assertNotNull(loaded);
		Assert.assertTrue(serviceManager.deleteService(USERNAME, loaded.getId()));
		Assert.assertNull(serviceDao.useService("creation"));

	}

	@Test
	public void testExportation() {
		service = new Service();
		service.setName("@TestService");
		service.setDescription("@Test Manager");
		service.setCategory(catId);
		service.setVersion("0.1");
		service.setCreatorId(user.getId());
		service.setOwner("Smartcommunity, comune di trento, rovereto");
		service.setOwnerUrl("http://smartnation.it");
		service.setLicense("MIT");
		service.setOrganizationId(organization.getId());
		List<Tag> tags = new ArrayList<Tag>();
		Tag t = new Tag();
		t.setName("dev");
		tags.add(t);
		t = new Tag();
		t.setName("test");
		tags.add(t);
		service.setTags(tags);
		AccessInformation accessInfo = new AccessInformation();
		accessInfo
				.setEndpoint("https://os.smartcommunitylab.it/core.mobility/");
		accessInfo.setFormats("JSON,XML");
		accessInfo.setProtocols("REST,SOAP");
		Authentication auth = new Authentication();
		auth.setAccessProtocol(AuthProtocol.OAuth2.toString());
		accessInfo.setAuthentication(auth);
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
		System.out.println(serviceManager.getUSDL(service.getId()));
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
