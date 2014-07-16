/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/

package eu.trentorise.smartcampus.openservices.controllers.exec;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import eu.trentorise.smartcampus.openservices.Utils;

/**
 * @author raman
 *
 */
@Component("OAuth2")
public class OAuth2TestBoxAuthHandler extends AbstractTestBoxAuthHandler {
	
	@Override
	public void authorize(HttpServletRequest request, HttpServletResponse response, Map<String, Object> accessAttributes) throws TestBoxException {
		String grantType = (String)accessAttributes.get("grant_type"); 
		String clientId = (String)accessAttributes.get("client_id");
		String scope = (String) accessAttributes.get("scope");
		String url = null;
		String authURL = (String)accessAttributes.get("authorizationUrl");
		String callbackURL = Utils.getAppURL(request);
		if (!callbackURL.endsWith("/")) {
			callbackURL += "/";
		}
		callbackURL += "api/testbox/authorized";
		
		if ("implicit".equals(grantType)) {
			url = generateAuthorizationURI(authURL, "token", clientId, callbackURL, scope, null);
		}
		else if ("authorization_code".equals(grantType)) {
			url = generateAuthorizationURI(authURL, "code", clientId, callbackURL, scope, null);
		} else {
			throw new TestBoxException("Incorrect authorize request: check configuration");
		}
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
			throw new TestBoxException("Failure performing redirect");
		}
	}

	@Override
	public void onAuthorized(HttpServletRequest request, HttpServletResponse response) throws TestBoxException {
		// TODO if code parameter is present exchange it for token and reply with token
		try {
			response.sendRedirect("/api/testbox/authorize/success");
		} catch (IOException e) {
			e.printStackTrace();
			throw new TestBoxException("Failure performing redirect");
		}
	}



	@Override
	public TestResponse performTest(HttpServletRequest request, TestBoxParams params, Map<String, Object> accessAttributes) throws TestBoxException {
		// check the grant type: in case of client credentials generate token, otherwise take token from params
		String grantType = (String)accessAttributes.get("grant_type"); 
		String token = null;
		if ("implicit".equals(grantType)) {
			token = extractAccessToken(params);
		}
		if ("authorization_code".equals(grantType)) {
			token = extractAccessToken(params);
		}
		if ("client_credentials".equals(grantType)) {
			String clientId = (String)accessAttributes.get("client_id");
			String clientSecret = (String)accessAttributes.get("client_secret");
			String authUrl = (String)accessAttributes.get("authorizationUrl");
			token = generateClientToken(authUrl, clientId, clientSecret);
		}
		if(params.requestHeaders==null){
			params.requestHeaders = new HashMap<String, String>();
		}
		params.requestHeaders.put("Authorization", "Bearer "+token);
		return execute(params);
	}

	@SuppressWarnings("unchecked")
	private String extractAccessToken(TestBoxParams params)
			throws TestBoxException {
		String token;
		Map<String, ?> map = (Map<String, ?>) params.credentials;
		if (map == null) {
			throw new TestBoxException("Missing access credentials");
		}
		token = (String) map.get("access_token");
		if (token == null) {
			throw new TestBoxException("Missing access token");
		}
		return token;
	}

	private String generateAuthorizationURI(String authURL, String responseType, String clientId, String redirectUri, String scope, String state) {
		String s = authURL;
    	s += "?response_type="+responseType+"&client_id="+clientId+"&response_type=code&redirect_uri="+redirectUri;
    	if (scope != null && !scope.trim().isEmpty()) {
    		s += "&scope="+scope;
    	}
    	if (state != null && !state.trim().isEmpty()) {
    		s += "&state="+state;
    	}
    	return s;
	}

	/**
	 * Generate client access token for the current application.
	 * @return
	 * @throws AACException
	 */
	private String generateClientToken(String url, String clientId, String clientSecret) throws TestBoxException {
        final HttpResponse resp;
        final HttpEntity entity = null;
        String path = url+"?grant_type=client_credentials&client_id="+clientId +"&client_secret="+clientSecret;
        final HttpPost post = new HttpPost(path);
        post.setEntity(entity);
        post.setHeader("Accept", "application/json");
        try {
            resp = getHttpClient().execute(post);
            final String response = EntityUtils.toString(resp.getEntity());
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            	@SuppressWarnings("unchecked")
				Map<String,Object> data = new ObjectMapper().readValue(response, Map.class);
                return (String)data.get("access_token");
            }
            throw new TestBoxException("Error validating " + resp.getStatusLine());
        } catch (final Exception e) {
            throw new TestBoxException(e);
        }
	}

}
