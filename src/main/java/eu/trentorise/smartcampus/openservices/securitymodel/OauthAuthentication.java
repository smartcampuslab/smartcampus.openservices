package eu.trentorise.smartcampus.openservices.securitymodel;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.openservices.UserRoles;

@Component
public class OauthAuthentication implements Authentication {

	private static final long serialVersionUID = 2811105035816583809L;

	private boolean authenticated;
	private String username;

	@Autowired
	Environment env;

	public OauthAuthentication() {
	}

	public void setName(String username) {
		this.username = username;
	}

	@Override
	public String getName() {
		return username;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String role = UserRoles.ROLE_USER.toString();
		if (username.equals(env.getProperty("admin.username"))) {
			role = UserRoles.ROLE_ADMIN.toString();
		}
		return Arrays.asList(new SimpleGrantedAuthority(role));
	}

	@Override
	public Object getCredentials() {
		return username;
	}

	@Override
	public Object getDetails() {
		return username;
	}

	@Override
	public Object getPrincipal() {
		return username;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {
		authenticated = isAuthenticated;
	}

}
