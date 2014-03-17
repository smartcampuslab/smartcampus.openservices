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

import eu.trentorise.smartcampus.openservices.dao.*;
import eu.trentorise.smartcampus.openservices.entities.*;
import eu.trentorise.smartcampus.openservices.managers.OrganizationManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value= {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/spring-security.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
		"file:src/test/java/spring-embedded-db.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class OrganizationManagerTest {
	
		//Log
		private Logger log = LoggerFactory.getLogger(OrganizationManagerTest.class);
		@Autowired
		private OrganizationManager orgManager;
		@Autowired
		private OrganizationDao orgDao;
		@Autowired
		private UserRoleDao urDao;
		
		private String username;
		private Organization org;
		
		@Before
		public void setUp(){
			username = "sara";
			org = new Organization();
			org.setName("@TestOrganization");
			org.setDescription("Running JUnit Test on Organization Manager Component");
			org.setCreatorId(1);//id of user sare
			org.setCategory(4);//@Test
		}
		
		@Test
		public void testCreateOrg(){
			log.info("TEST - Create organization: "+org.getName()+", by user "+username);
			boolean result = orgManager.createOrganization(username,org);
			log.info("Result of creation: "+result);
			assertTrue("Creation not available", result==true);
		}
		
		@Test
		public void testModifyOrg(){
			log.info("TEST - Modify Organization: ");
			//get org id
			Organization newOrg = orgDao.getOrganizationByName(org.getName());
			org.setId(newOrg.getId());// db assigned it
			org.setDescription("Update description");
			boolean result = orgManager.updateOrganization(username, org);
			log.info("Result of updating: "+result);
			assertTrue("Update not available", result==true);
		}
		
		@Test
		public void testGetUserOrganizationList(){
			log.info("TEST - get user organization list");
			List<Organization> orgs = orgManager.getUserOrganizations(username);
			assertNotNull("No organization", orgs);
			for(int i=0;i<orgs.size();i++){
				log.info("Organization "+i+": "+orgs.get(i).getName());
			}
		}
		
		@Test
		public void testCreateInvitationAndAddOwner(){
			log.info("TEST - Create abd Add organization owner");
			//get org id
			Organization newOrg = orgDao.getOrganizationByName(org.getName());
			org.setId(newOrg.getId());// db assigned it
			
			//create invitation
			log.info("1 - Create invitation");
			String token = orgManager.createInvitation(username, org.getId(), "ROLE_ORGOWNER", "giulia@giulia.it");
			assertNotNull("No string token",token);
			log.info(token);
			
			//add owner
			log.info("2 - Add owner");
			boolean result = orgManager.addOwner("giulia", token);
			assertTrue("Add owner failed", result == true);
		}
		
		@Test
		public void testGetOrgById(){
			//get org id
			Organization newOrg = orgDao.getOrganizationByName(org.getName());
			org.setId(newOrg.getId());// db assigned it
			Organization myOrg = orgManager.getOrganizationById(org.getId());
			assertNotNull("Your org test does not exist",myOrg);
		}
		
		@Test
		public void testGetOrgList(){
			List<Organization> orgs = orgManager.getOrganizations(0,3,"name");
			assertNotNull("No organization", orgs);
			assertTrue("Organization list is empty", orgs.size()>0);
		}
		
		@Test
		public void testGetHistoryOrg(){
			//get org id
			Organization newOrg = orgDao.getOrganizationByName(org.getName());
			org.setId(newOrg.getId());// db assigned it
			List<ServiceHistory> history = orgManager.getHistory(org.getId());
			assertNull("You find something: history", history);
		}
		
		@Test
		public void testDeleteOrgUser(){
			//get org id
			Organization newOrg = orgDao.getOrganizationByName(org.getName());
			org.setId(newOrg.getId());// db assigned it
			boolean result = orgManager.deleteOrgUser(username, org.getId(), 3);
			if(result){
				assertTrue("Delete of an user from organization not available", result==true);
			}else{
				assertTrue("Delete of an user from organization ok", result==false);
			}
		}
		
		
		@Test
		public void testDeleteOrg(){
			//get org id
			Organization newOrg = orgDao.getOrganizationByName(org.getName());
			org.setId(newOrg.getId());// db assigned it
			boolean result = orgManager.deleteOrganization(username, org.getId());
			assertTrue("Delete organization not available", result==true);
		}
		
		

}
