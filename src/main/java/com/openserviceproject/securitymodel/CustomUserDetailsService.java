package com.openserviceproject.securitymodel;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.openserviceproject.dao.*;
import com.openserviceproject.entities.*;
import com.openserviceproject.support.EmailValidator;

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