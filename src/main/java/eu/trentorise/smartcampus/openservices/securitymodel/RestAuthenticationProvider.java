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
package eu.trentorise.smartcampus.openservices.securitymodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.entities.User;

/**
 * Authentication Provider
 * Try to login without using predefined <login-form> of spring security,
 * defining an authentication provider.
 * 
 * @author Giulia Canobbio
 * 
 * NOT IN USE
 *
 */
public class RestAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider{
	
	private PasswordEncoder passwordEncoder = new PlaintextPasswordEncoder();
	@Autowired
	private UserDao userDao;

	public RestAuthenticationProvider(){
		super();
	}
	
	/**
	 * Retrieve user data and return a {@link UserDetails} instance
	 */
	@Override
	protected final UserDetails retrieveUser(String name, UsernamePasswordAuthenticationToken auth){
		UserDetails loadedUser = null;
		final String password = auth.getCredentials().toString();
		try{
			//get user data
			User dbUser = userDao.getUserByUsername(name);
			//athenticate TODO
			
		} catch(Exception e){
			
		}
		return loadedUser;
	}

	@SuppressWarnings({ "deprecation" })
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		// TODO Auto-generated method stub
		if(authentication.getCredentials() == null){
			throw new BadCredentialsException("AbstractUserDetailAuthenticationProvider.badCredentials", userDetails);			
		}
		//Retrieve password
		final String presentedPassword = authentication.getCredentials().toString();
		if(!passwordEncoder.isPasswordValid(userDetails.getPassword(), presentedPassword, null)){
			throw new BadCredentialsException("AbstractUserDetailAuthenticationProvider.badCredentials", userDetails);
		}
		
	}
	
}
