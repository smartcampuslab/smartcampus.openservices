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
package eu.trentorise.smartcampus.openservices.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.entities.Profile;
import eu.trentorise.smartcampus.openservices.entities.User;
/**
 * Sign up
 * If user does not exist in UserConnection table, then this class is called.
 * First user data is saved in UserConnection then it is saved in User table.
 * @author Giulia Canobbio
 *
 */
public class AccountConnectionSignUp implements ConnectionSignUp{
	/**
	 * Instance of {@link UserDao}
	 */
	@Autowired
	private UserDao userDao;
	
	/**
	 * Retrieve user data and save it in db.
	 * This method returns username string.
	 */
	@Override
	public String execute(Connection<?> arg0) {
		System.out.println("Connection Sign Up......");
		UserProfile profile = arg0.fetchUserProfile();

		User user = new User();
		
		String username;

		if (profile.getUsername() == null) {
			username = profile.getName()+"@facebook";
			//user.setUsername(profile.getName());
		} else {
			username = profile.getUsername()+"@facebook";
			//user.setUsername(profile.getUsername());
		}
		user.setUsername(username);
		user.setEmail(profile.getEmail());
		user.setRole("ROLE_NORMAL");
		user.setEnabled(1);
		Profile p = new Profile();
		p.setName(profile.getFirstName());
		p.setSurname(profile.getLastName());
		user.setProfile(p);
		userDao.addUser(user);
		
		System.out.println("Connection Sign Up ending...");
		
		return username;
	}

}
