package com.openserviceproject.securitymodel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.openserviceproject.dao.UserDao;
import com.openserviceproject.entities.User;

public class RestAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider{
	
	private PasswordEncoder passwordEncoder = new PlaintextPasswordEncoder();
	@Autowired
	private UserDao userDao;

	public RestAuthenticationProvider(){
		super();
	}
	
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
