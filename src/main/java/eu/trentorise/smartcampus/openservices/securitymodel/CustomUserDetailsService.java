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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.dao.UserRoleDao;
import eu.trentorise.smartcampus.openservices.entities.User;

/**
 * Custom User Details Service
 * retrieve data of user during login for spring security
 * and put them in security context holder
 * 
 * @author Giulia Canobbio
 *
 */
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleDao usDao;

	/**
	 * Check if user exists in database and if username and password are correct,
	 * return a {@link UserDetails} instance.
	 */
	@Override
	public UserDetails loadUserByUsername(String arg0)
			throws UsernameNotFoundException {
		
		final User domainUser = userDao.getUserByUsername(arg0);
		if (domainUser == null) throw new UsernameNotFoundException(arg0);
		if(domainUser.getPassword()!=null){
			if(domainUser.getPassword().equalsIgnoreCase("") /*|| domainUser.getPassword()==null*/) throw new SecurityException();
		}
		
		return new UserDetails() {
			
			/**
			 * Check if user is enabled
			 */
			@Override
			public boolean isEnabled() {
				int enabled = domainUser.getEnabled();
				if(enabled==0){
					return false;
				}
				else{
					return true;
				}
			}
			
			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}
			
			@Override
			public boolean isAccountNonLocked() {
				return true;
			}
			
			@Override
			public boolean isAccountNonExpired() {
				return true;
			}
			
			/**
			 * Get username
			 */
			@Override
			public String getUsername() {
				return domainUser.getUsername();
			}
			
			/**
			 * Get password
			 */
			@Override
			public String getPassword() {
				return domainUser.getPassword();
			}
			
			/**
			 * Find user role in database and put them in authority
			 */
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
				
				//roles: ADMIN or NORMAL user
				roles.add(new SimpleGrantedAuthority(domainUser.getRole()));
				
				return roles;
			}
		};
	}
	
}
