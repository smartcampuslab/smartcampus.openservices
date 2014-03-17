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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.UserManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value= {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/spring-security.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
		"file:src/test/resources/spring-embedded-db.xml"})
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class UserManagerTest {
	// Log
	private Logger log = LoggerFactory.getLogger(UserManagerTest.class);
	@Autowired
	private UserManager userManager;
	@Autowired
	private Environment env;
	
	private User user;
	
	@Before
	public void setUp(){
		//User test
		user = new User();
		user.setUsername("@TestUser");
		user.setPassword("test");
		user.setEmail("testJunit@test.it");
		
		/*Profile profile = new Profile();
		profile.setCountry("Italy");
		user.setProfile(profile);
		*/
		user.setRole("ROLE_NORMAL");
	}
	
	@Test
	public void testCreateAndFoundUser(){
		log.info("TEST - Create new user "+user.getUsername());
		//check if already exists
		User checkUser = userManager.getUserByUsername(user.getUsername());
		if(checkUser==null){
			// create User
			User newUser = userManager.createUser(user,"http://localhost:8080/openservice/",
					env.getProperty("email.username"),
					env.getProperty("user.message.object"),env.getProperty("user.message.body"));
			assertNotNull("No user created", newUser);
			user.setId(newUser.getId());// get user id from database
			log.info("New user " + newUser.getUsername() + ", with id "
					+ newUser.getId());

			// getById
			User foundUser = userManager.getUserById(newUser.getId());
			assertNotNull("New user found by id ", foundUser);
			log.info("New user found by id: " + foundUser.getUsername());

			// getByUsername
			User foundUser2 = userManager.getUserByUsername(newUser
					.getUsername());
			assertNotNull("New user found by username", foundUser2);
			log.info("New user found by username: " + foundUser2.getUsername());
		}
		else{
			assertNotNull("User found by username",checkUser);
		}
		
	}
	
	//modify
	@Test
	public void testModifyUserData(){
		log.info("TEST - Modify new user data "+user.getUsername());
		user.setEmail("newtestmodified@test.it");
		User modifiedUser = userManager.modifyUserData(user.getUsername(), user);
		assertNotNull("No modified user", modifiedUser);
		log.info("New user modified "+modifiedUser.getUsername()+" with new email "+modifiedUser.getEmail());
	}
	
	@Test
	public void testVerifyUserEmail(){
		log.info("TEST - Email test");
		User user = userManager.getUserByUsername("@TestUser");
		log.info("User "+user.getUsername()+" and email "+user.getEmail());
		String s = userManager.addKeyVerifyEmail(user.getUsername());
		
		//enable user after verification
		log.info("Key "+s);
		assertTrue("No key", s!=null);
		
		User enableUser = userManager.enableUserAfterVerification(/*user.getUsername(),*/ s);
		log.info("Enabled user: "+enableUser.getUsername()+", value enabled: "+enableUser.getEnabled());
		assertTrue("User exists", enableUser!=null);
		assertTrue("Enabled == 0", enableUser.getEnabled()==1);
	}
	
	//disadble user
	@Test
	public void testDisableUser(){
		log.info("TEST - Enable new user "+user.getUsername());
		User enabledUser = userManager.disabledUser(user.getUsername());
		assertNotNull("No user to enable",enabledUser);
		log.info("New user enable "+enabledUser.getUsername());
	}
	
	//enable user
	@Test
	public void testEnableUser(){
		log.info("TEST - Enable new user "+user.getUsername());
		User enabledUser = userManager.enableUser(user.getUsername());
		assertNotNull("No user to enable",enabledUser);
		log.info("New user enable "+enabledUser.getUsername());
		
		//disable
		this.testDisableUser();
	}

}
