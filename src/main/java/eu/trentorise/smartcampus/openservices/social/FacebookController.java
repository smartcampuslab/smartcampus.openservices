/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.openservices.social;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Code from http://syntx.com/languages-frameworks/integrating-your-java-spring-mvc-webapp-with-facebook-doing-oauth-dance
 * @author smartcampus
 *
 */
@Controller
@RequestMapping(value="/api/social/facebook")
public class FacebookController {
	
	private static final Logger logger = LoggerFactory.getLogger(FacebookController.class);
	
	/*// --first version
	private static final String SCOPE="email,offline_access,user_about_me,user_birthday,read_friendlists";
	private static final String REDIRECT_URI="http://localhost:8080/openservice/api/social/facebook/callback";
	private static final String CLIENT_ID="";
	private static final String APP_SECRET="";
	private static final String DIALOG_OAUTH="https://www.facebook.com/dialog/oauth";
	private static final String ACCESS_TOKEN="https://graph.facebook.com/oauth/access_token";
	
	//--second version
	@Autowired
	private ConnectionRepository repository;
	@Autowired
	private FbConnectionHelper fbConnHelper;
	@Autowired
	private FbOperationsHelper fbOpHelper;
	
	//--first version: OAuth
	@RequestMapping(value="/signin", method=RequestMethod.GET)
	public void signin(HttpServletRequest request, HttpServletResponse response){
		try{
			response.sendRedirect(DIALOG_OAUTH+"?client_id="+CLIENT_ID+
					"&redirect_uri="+REDIRECT_URI+
					"&scope="+SCOPE);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/callback", params="code", method=RequestMethod.GET)
	@ResponseBody
	public void accessCode(@RequestParam("code") String code, HttpServletRequest request, 
			HttpServletResponse response){
		try{
			response.setContentType("text/html");
			String responseString = IntegrationBase.readURLGET(ACCESS_TOKEN,
					new String[]{"client_id","redirect_uri","code","client_secret"},
					new String[]{CLIENT_ID, REDIRECT_URI, code, APP_SECRET});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/callback", params="error_reason", method=RequestMethod.GET)
	@ResponseBody
	public void error(@RequestParam("error_reason") String error_reason, 
			@RequestParam("error") String error, 
			@RequestParam("error_description") String description, 
			HttpServletRequest request, 
			HttpServletResponse response){
		try{
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, description);
			logger.info("Error reason: {}", error_reason);
			logger.info("Error: {}", error);
			logger.info("Description: {}", description);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//second version - Spring Social
	@RequestMapping(value="/profile/{providerUserId}/{fbAccessToken}", method=RequestMethod.GET)
	public String getProfile(@PathVariable String fbAccessToken, @PathVariable String providerUserId, 
			Model model){
		try{
			boolean userExistsInRepo = fbConnHelper.checkForUserInRepository(providerUserId, repository);
			if(userExistsInRepo){
				fbConnHelper.updateExistingConnectionInRepository(providerUserId, fbAccessToken, repository);
			}else{
				fbConnHelper.addNewConnectionToRepository(providerUserId, fbAccessToken, repository);
			}
			Facebook fb= repository.getConnection(Facebook.class, providerUserId).getApi();
			model.addAttribute("profileLink", fb.userOperations().getUserProfile().getLink());
			model.addAttribute("profileInfo", fb.userOperations().getUserProfile());
			return "facebook/profile";
		}catch(NotConnectedException n){
			return "connect/facebookConnect";
		}
	}*/
	
	@Inject
    @Qualifier("facebook")
    Connection<Facebook> facebook;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	@ResponseBody
	public String welcome() {
		Facebook fb = facebook.getApi();

		List<Page> moviePages = fb.likeOperations().getMovies();
		
		List<String> movies = new ArrayList<String>();
		for (Page p : moviePages) {
			movies.add(p.getName());
		}
		
		// Uncomment the following line to see how easy posting to your timeline is.
		//fb.feedOperations().post(userId, "I just tried out the My Movies example site!");
		
		int numFriends = fb.friendOperations().getFriends().size();
		
		logger.info("facebook {}", true);
		logger.info("numFriends {}", numFriends);
		logger.info("name {}", facebook.getDisplayName());
		logger.info("movies {}", movies);
		
		return "welcome_user";
	}
	
}
