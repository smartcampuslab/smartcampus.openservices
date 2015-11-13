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
package eu.trentorise.smartcampus.openservices.controllers;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import eu.trentorise.smartcampus.aac.AACService;
import eu.trentorise.smartcampus.openservices.UserRoles;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.UserManager;
import eu.trentorise.smartcampus.openservices.support.CookieUser;

/**
 * A controller which handles requests for the application home page.
 * 
 * @author Giulia Canobbio
 * 
 */
@Controller
@PropertySource("classpath:openservice.properties")
public class HomeController {

	private static final Logger logger = LoggerFactory
			.getLogger(HomeController.class);

	/**
	 * Instance of {@link UserManager} to retrieve data using Dao classes.
	 */
	@Autowired
	private UserManager userManager;
	/**
	 * Instance of {@link Environment}
	 */
	@Inject
	private Environment env;

	@Autowired
	private AACService oauthClient;

	/**
	 * Home service that returns user to home page. It checks if user is
	 * authenticated and changes cookie value accordingly. If cookie value is
	 * true then user is authenticated, otherwise it is false. Due to social
	 * login, it checks if cookie user already exists if user is authenticated.
	 * If this cookie does not exists and cookie value is true, then it creates
	 * it.
	 * 
	 * @param request
	 *            : {@link HttpServletRequest} which is needed to find out if a
	 *            specific cookie exists.
	 * @param response
	 *            : {@link HttpServletResponse} which returns a new cookie or if
	 *            it already exists, modified it.
	 * @return home jsp
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request, HttpServletResponse response) {
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		response.setHeader("User", username);
		String roles = SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities().toString();
		response.setHeader("Roles", roles);

		// Principal and credentials
		logger.debug("-- Principal: "
				+ SecurityContextHolder.getContext().getAuthentication()
						.getPrincipal().toString()
				+ " --"
				+ "Credentials: "
				+ SecurityContextHolder.getContext().getAuthentication()
						.getCredentials() + " --");

		// return cookie not Http Only with value true if user is authenticated
		// o.w. false
		String value = "false";
		if (!username.equalsIgnoreCase("anonymousUser")) {
			value = SecurityContextHolder.getContext().getAuthentication()
					.isAuthenticated()
					+ "";
		}
		Cookie cookie = new Cookie("value", value);
		cookie.setPath(request.getContextPath() + "/");

		boolean found = false;
		Cookie[] cookies = request.getCookies();
		String name;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				name = cookies[i].getName();
				logger.debug("Found cookies: " + i + ", name: " + name);
				if (name.equalsIgnoreCase("value")) {
					cookies[i].setValue(value);
					cookies[i].setPath(request.getContextPath() + "/");
					found = true;
					response.addCookie(cookies[i]);
				}
			}
		} else {
			logger.debug("No cookie in response");
			response.addCookie(cookie);
		}
		if (cookies != null && !found) {
			logger.debug("No cookie value in this request");
			response.addCookie(cookie);
		}

		// check if cookie user exists ow. create it if user is authenticated
		boolean fUser = false;
		if (value.equalsIgnoreCase("true")) {
			for (int i = 0; i < cookies.length; i++) {
				name = cookies[i].getName();
				logger.debug("Found cookies: " + i + ", name: " + name);
				if (name.equalsIgnoreCase("user")) {
					fUser = true;
				}
			}
			if (!fUser) {
				CookieUser cu = new CookieUser();
				cu.setUsername(username);
				// String role = UserRoles.ROLE_NORMAL.toString();
				// if (username.equals(env.getProperty("admin.username",
				// "admin"))) {
				// role = UserRoles.ROLE_ADMIN.toString();
				// }
				String role = SecurityContextHolder.getContext()
						.getAuthentication().getAuthorities().iterator().next()
						.getAuthority();
				cu.setRole(role);

				Gson gson = new Gson();
				String obj = gson.toJson(cu);

				Cookie userCookie = new Cookie("user", obj);
				userCookie.setPath(request.getContextPath() + "/");
				response.addCookie(userCookie);
			}
		}

		return "index";
	}

	/**
	 * Welcome rest service retrieves user data and it is called after login in.
	 * It returns user data and sets cookie value to true.
	 * 
	 * @param request
	 *            : {@link HttpServletRequest} which is needed to find out if a
	 *            specific cookie exists.
	 * @param response
	 *            : {@link HttpServletResponse} which returns a new cookie or if
	 *            it already exists, modified it.
	 * @return {@link ResponseObject} with user data, status (OK or NOT FOUND)
	 *         and error message (if status is NOT FOUND).
	 */
	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject printWelcome(HttpServletRequest request,
			HttpServletResponse response) {
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		User user = userManager.getUserByUsername(username);
		ResponseObject responseObject = new ResponseObject();
		if (user != null) {
			user.setPassword(null);
			responseObject.setData(user);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		} else {
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("User does not exist or database problem");
		}

		// check if cookie value exists and set it
		String value = SecurityContextHolder.getContext().getAuthentication()
				.isAuthenticated()
				+ "";
		Cookie[] cookies = request.getCookies();
		String name;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				name = cookies[i].getName();
				if (name.equalsIgnoreCase("value")) {
					cookies[i].setValue(value + "");
					cookies[i].setPath(request.getContextPath() + "/");
					response.addCookie(cookies[i]);
				}

			}
		}

		return responseObject;
	}

	/**
	 * Error rest service returns to home jsp with response status NOT FOUND. It
	 * is called when user do wrong requests.
	 * 
	 * @param value
	 *            : {@link Cookie} object, it contains value cookie set to false
	 *            or true.
	 * @param user
	 *            : {@link Cookie} object, it contains user data such as
	 *            username and roles.
	 * @param request
	 *            : {@link HttpServletRequest} DO NOTHING NOW
	 * @param response
	 *            : {@link HttpServletResponse} which returns an error response
	 *            (404, NOT FOUND).
	 * @return home jsp
	 */

	@RequestMapping()
	public String error(
			@CookieValue(value = "value", required = false) String value,
			@CookieValue(value = "user", required = false) String user,
			HttpServletRequest request, HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		return home(request, response);
	}

	/**
	 * Login rest checks if cookie value already exists and change its value to
	 * false.
	 * 
	 * @param request
	 *            : instance of {@link HttpServletRequest}
	 * @param response
	 *            : instance of {@link HttpServletResponse}
	 * @return home page with response UNAUTHORIZED
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, HttpServletResponse response) {
		// Check if cookies exist and change them
		Cookie[] cookies = request.getCookies();
		String name = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				name = cookies[i].getName();
				logger.debug("Found cookies: " + i + ", name: " + name);
				if (name.equalsIgnoreCase("value")) {
					cookies[i].setValue("false");
					cookies[i].setPath(request.getContextPath() + "/");
					response.addCookie(cookies[i]);
				}
				if (name.equalsIgnoreCase("user")) {
					cookies[i].setValue(null);
					response.addCookie(cookies[i]);
				}
			}
		}
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		return home(request, response);
	}

	/**
	 * Login failed returns an error message, because user does not exists or
	 * insert wrong credentials.
	 * 
	 * @param response
	 *            : {@link HttpServletResponse} which returns an error response
	 *            (404, NOT FOUND).
	 * @return {@link ResponseObject} with status (401, UNAUTHORIZED) and error
	 *         message.
	 */
	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject loginfailed(HttpServletResponse response) {
		ResponseObject responseObject = new ResponseObject();
		responseObject.setStatus(401);
		responseObject.setError("Invalid username or password");
		response.setStatus(401);
		return responseObject;
	}

	/**
	 * Logout rest sets authentication to false and cookie value to false. It
	 * redirects users to home page.
	 * 
	 * @param request
	 *            : {@link HttpServletRequest} which is needed to find out if a
	 *            specific cookie exists.
	 * @param response
	 *            : {@link HttpServletResponse} which returns a new cookie or if
	 *            it already exists, modified it.
	 * @return home jsp
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request,
			HttpServletResponse response) {
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();
		logger.debug("-- Logout "
				+ username
				+ " --"
				+ SecurityContextHolder.getContext().getAuthentication()
						.isAuthenticated());
		SecurityContextHolder.getContext().getAuthentication()
				.setAuthenticated(false);

		// check if cookie value exists and set it
		String value = SecurityContextHolder.getContext().getAuthentication()
				.isAuthenticated()
				+ "";
		Cookie[] cookies = request.getCookies();
		String name;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				name = cookies[i].getName();
				if (name.equalsIgnoreCase("value")) {
					cookies[i].setValue(value + "");
					cookies[i].setPath(request.getContextPath() + "/");
					response.addCookie(cookies[i]);
				}

			}
		}
		return "index";
	}

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public @ResponseBody
	String signin(HttpServletRequest request, HttpServletResponse response) {

		boolean oauthAuth = env.getProperty("oauth.active", Boolean.class,
				false);

		String loginPageURL = "";
		if (oauthAuth) {
			loginPageURL = oauthClient.generateAuthorizationURIForCodeFlow(
					env.getProperty("oauth.callback_uri"), null, null, null);
		}

		return loginPageURL;
	}

	/**
	 * Callback api manager service with username.
	 * 
	 * @param request
	 *            : instance of {@link HttpServletRequest}
	 * @param response
	 *            : instance of {@link HttpServletResponse}
	 * @return instance of {@link ResponseObject} with status (200 or 400) and
	 *         error message.
	 */
	@RequestMapping(value = "/apimanager/callback", method = RequestMethod.GET)
	@ResponseBody
	public ResponseObject apimanagercallback(HttpServletRequest request,
			HttpServletResponse response) {
		// set value cookie like welcome
		ResponseObject welcome = printWelcome(request, response);
		logger.info("After login: {}", welcome);
		// then callback api manager
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();

		// set cookie user
		CookieUser cu = new CookieUser();
		cu.setUsername(username);
		cu.setRole(UserRoles.ROLE_NORMAL.toString());

		Gson gson = new Gson();
		String obj = gson.toJson(cu);

		Cookie userCookie = new Cookie("user", obj);
		userCookie.setPath(request.getContextPath() + "/");
		response.addCookie(userCookie);

		// POST request to apimanager
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> respEnt = restTemplate.postForEntity(
				env.getProperty("apimanager.callback_uri"), username,
				String.class);

		logger.info("Headers {}", respEnt.getHeaders());
		logger.info("Body {}", respEnt.getBody());
		logger.info("Status Code {}", respEnt.getStatusCode());

		ResponseObject responseObject = new ResponseObject();
		if (respEnt.getStatusCode() == HttpStatus.OK) {
			responseObject.setData(respEnt.getBody());
			responseObject.setStatus(HttpServletResponse.SC_OK);
		} else {
			responseObject.setStatus(400);
			responseObject
					.setError("Invalid Request. Need username parameter.");
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		return responseObject;
	}
}
