package eu.trentorise.smartcampus.openservices.securitymodel;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Authentication Filter
 * Check attributes username and password and then put them in a {@UserDetails} 
 * instance.
 * After that, build an Authentication object with user data.
 * 
 * @author Giulia Canobbio
 *
 * NOT IN USE
 */
public class AuthenticationTokenProcessingFilter extends GenericFilterBean{
	
	@Autowired
	private CustomUserDetailsService udservice;
	private AuthenticationManager authManager;
	
	public AuthenticationTokenProcessingFilter(AuthenticationManager authManager){
		this.authManager=authManager;
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		//Check username and password
		String username = arg0.getAttribute("j_username").toString();
		String password = arg0.getAttribute("j_password").toString();
		//retrieve UserDetails
		UserDetails userDetails = udservice.loadUserByUsername(username);
		//build an Authentication object with user data
		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) arg0));
        //set authentication info into SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authManager.authenticate(authentication));
        //filter chain
        arg2.doFilter(arg0, arg1);
        
	}

}
