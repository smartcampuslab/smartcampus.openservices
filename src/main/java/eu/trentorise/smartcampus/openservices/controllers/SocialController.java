package eu.trentorise.smartcampus.openservices.controllers;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import eu.trentorise.smartcampus.openservices.entities.ResponseObject;

@Controller
@RequestMapping(value = "/api/social")
public class SocialController {

	private static final Logger logger = LoggerFactory.getLogger(SocialController.class);
	private RestTemplate temp;
	
	@RequestMapping(value = "/google", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject socialGooglePlus() {
		ResponseObject responseObject = new ResponseObject();
		temp = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
		body.add("redirect_uri","http://localhost:8080/openservice/"/*URLEncoder.encode("http://localhost:8080/openservice")*/);
		body.add("scope", "https://www.googleapis.com/auth/plus.me");
		
		HttpEntity<?> httpEntity = new HttpEntity<Object>(body,headers);
		
		ResponseEntity<Object> r =temp.exchange("http://localhost:8080/openservice/signin/google", HttpMethod.POST, httpEntity, Object.class);
		
		logger.info("## ResponseEntity Headers: " + r.getHeaders().getLocation() + " ##");
		logger.info("## ResponseEntity Body: " + r.getBody() + " ##");
		logger.info("## Status code: " + r.getStatusCode());
		
		if(r.getHeaders().getLocation()!=null){
			responseObject.setData(r.getHeaders().getLocation());
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		else{
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("Problem with Google Plus. Retry Later.");
		}
		
		return responseObject;
	}
}
