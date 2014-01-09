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

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import eu.trentorise.smartcampus.openservices.dao.*;
import eu.trentorise.smartcampus.openservices.entities.*;
import eu.trentorise.smartcampus.openservices.support.EmailValidator;

/**
 * Custom User Details Service
 * retrieve data of user during login for spring security
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

	@Override
	public UserDetails loadUserByUsername(String arg0)
			throws UsernameNotFoundException {
		
		final User domainUser = userDao.getUserByUsername(arg0);
		
		return new UserDetails() {
			
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
			
			@Override
			public String getUsername() {
				return domainUser.getUsername();
			}
			
			@Override
			public String getPassword() {
				return domainUser.getPassword();
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				//Get user id from Users
				int userid = domainUser.getId();
				List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
				
				//roles: ADMIN or NORMAL user
				roles.add(new SimpleGrantedAuthority(domainUser.getRole()));
				
				//roles: ORGOWNER or SERVICEOWNER
				//TODO find a way to know for which org
				List<UserRole> user_role = usDao.getUserRoleByIdUser(userid);
				for(int i=0; i<user_role.size();i++){
					roles.add(new SimpleGrantedAuthority(user_role.get(i).getRole()));
					}
				
				return roles;
			}
		};
	}
	
}
