package eu.trentorise.smartcampus.openservices.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;

import eu.trentorise.smartcampus.openservices.dao.UserDao;
import eu.trentorise.smartcampus.openservices.entities.Profile;
import eu.trentorise.smartcampus.openservices.entities.User;

public class AccountConnectionSignUp implements ConnectionSignUp{

	@Autowired
	private UserDao userDao;
	
/*	public AccountConnectionSignUp(UserDao userDao){
		this.userDao = userDao;
	}*/
	
	@Override
	public String execute(Connection<?> arg0) {
		UserProfile profile = arg0.fetchUserProfile();
		
		User user = new User();
		user.setUsername(profile.getUsername());
		user.setEmail(profile.getEmail());
		user.setRole("ROLE_NORMAL");
		user.setEnabled(1);
		Profile p = new Profile();
		p.setName(profile.getName());
		p.setSurname(profile.getLastName());
		user.setProfile(p);
		userDao.addUser(user);
		return profile.getUsername();
	}

}
