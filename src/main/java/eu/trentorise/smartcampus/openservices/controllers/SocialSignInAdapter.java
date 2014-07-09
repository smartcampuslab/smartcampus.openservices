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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;
/**
 * Sign in class
 * This class search user data in database.
 * If user is found, then he/she is logged in.
 * @author Giulia Canobbio
 *
 */
public class SocialSignInAdapter implements SignInAdapter{

	/**
	 * Instance of {@link UserDetailsService}
	 */
	@Autowired
	private UserDetailsService manager;

	/**
	 * Signin function that authenticates user in spring security,
	 * after find him/her in db.
	 */
	@Override
	public String signIn(String arg0, Connection<?> arg1, NativeWebRequest arg2) {
		System.out.println("Sign in Adapter......");
		System.out.println("String arg0: "+arg0);
		
		UserDetails userDetails = manager.loadUserByUsername(arg0);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(), userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);
		arg2.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext(),0);
		
		System.out.println("Sign in Adapter ending......");
		return null;
	}

}
