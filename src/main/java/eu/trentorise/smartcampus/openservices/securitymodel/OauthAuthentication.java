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
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.UserManager;
import eu.trentorise.smartcampus.profileservice.model.AccountProfile;

@Component
public class OauthAuthentication implements Authentication {

	private static final long serialVersionUID = 2811105035816583809L;

	private boolean authenticated;
	private String username;
	private AccountProfile account;

	@Autowired
	private Environment env;

	@Autowired
	private UserManager userManager;

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
		User u = userManager.getUserByUsername(username);
		String role = null;

		if (u == null) {
			if (userManager.isOauthAdmin(account)) {
				role = UserRoles.ROLE_ADMIN.toString();
			} else {
				role = env.getProperty("oauth.usermode.restricted",
						Boolean.class, true) ? UserRoles.ROLE_USER.toString()
						: UserRoles.ROLE_NORMAL.toString();
			}
		} else {
			role = u.getRole();
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

	public AccountProfile getAccount() {
		return account;
	}

	public void setAccount(AccountProfile account) {
		this.account = account;
	}

}
