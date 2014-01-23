package eu.trentorise.smartcampus.openservice.test.resttemplate;

import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.web.client.RestTemplate;

public class StatefulRestTemplate extends RestTemplate{
	
	private final HttpClient httpClient;
	private final org.apache.http.client.CookieStore cookieStore;
	private final HttpContext httpContext;
	private final StatefulHttpComponentsClientHttpRequestFactory factory;
	
	public StatefulRestTemplate(){
		super();
		httpClient = new DefaultHttpClient();
		cookieStore = (org.apache.http.client.CookieStore) new BasicCookieStore();
		httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, getCookieStore());
		factory = new StatefulHttpComponentsClientHttpRequestFactory(httpClient, httpContext);
		super.setRequestFactory(factory);
	}
	
	public HttpClient getHttpClient(){
		return httpClient;
	}
	
	public org.apache.http.client.CookieStore getCookieStore(){
		return cookieStore;
	}
	
	public HttpContext getHttpContext(){
		return httpContext;
	}
	
	public StatefulHttpComponentsClientHttpRequestFactory getFactory(){
		return factory;
	}

}
