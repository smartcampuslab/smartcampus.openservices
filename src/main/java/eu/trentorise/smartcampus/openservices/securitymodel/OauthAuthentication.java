package eu.trentorise.smartcampus.openservices.securitymodel;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import eu.trentorise.smartcampus.openservices.Constants;

public class OauthAuthentication implements Authentication {

	private static final long serialVersionUID = 2811105035816583809L;

	private boolean authenticated;
	private String username;

	public OauthAuthentication(String username) {
		this.username = username;
	}

	@Override
	public String getName() {
		return username;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(new SimpleGrantedAuthority(
				Constants.ROLES.ROLE_OAUTH.toString()));
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
