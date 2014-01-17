package eu.trentorise.smartcampus.openservices.support;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;

public class CookieFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		
		String name;
		//String value = SecurityContextHolder.getContext().getAuthentication().isAuthenticated()+"";
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for (int i = 0; i < cookies.length; i++) {
				name = cookies[i].getName();
				if(name.equalsIgnoreCase("value")){
					cookies[i].setValue("false");
				}
				response.addCookie(cookies[i]);
			}
		}
		else{
			Cookie cookie = new Cookie("value", "false");
			cookie.setPath("/openservice/");
			response.addCookie(cookie);
		}
		arg2.doFilter(arg0, arg1);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	
	

}
