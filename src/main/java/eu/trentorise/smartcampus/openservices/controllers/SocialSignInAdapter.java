package eu.trentorise.smartcampus.openservices.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.web.context.request.NativeWebRequest;

import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.entities.User;

public class SocialSignInAdapter implements SignInAdapter{
	
	@Autowired
	private UserDao userDao;
	
/*	public SocialSignInAdapter(UserDao userDao){
		this.userDao=userDao;
	}*/

	@Override
	public String signIn(String arg0, Connection<?> arg1, NativeWebRequest arg2) {
		User user = userDao.getUserByUsername(arg0);
		
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		//roles: ADMIN or NORMAL user
		roles.add(new SimpleGrantedAuthority(user.getRole()));
		
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(user.getUsername(),null, roles));
		return null;
	}

}
