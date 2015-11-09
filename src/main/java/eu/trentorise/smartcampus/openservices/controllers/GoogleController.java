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

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import eu.trentorise.smartcampus.openservices.UserRoles;
import eu.trentorise.smartcampus.openservices.entities.Profile;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.UserManager;
import eu.trentorise.smartcampus.openservices.social.GoogleAuthHelper;
import eu.trentorise.smartcampus.openservices.social.GoogleUser;
import eu.trentorise.smartcampus.openservices.support.CookieUser;

/**
 * Google controller connects to google and retrieve user data.
 * 
 * @author Giulia Canobbio
 * 
 */
@Controller
@RequestMapping(value = "/api/oauth/google")
public class GoogleController {

	private static final Logger logger = LoggerFactory
			.getLogger(GoogleController.class);
	/**
	 * Instance of {@link GoogleAuthHelper} for oauth google
	 */
	@Autowired
	private GoogleAuthHelper auth;
	/**
	 * Instance of {@link UserManager} to retrieve and save user data
	 */
	@Autowired
	private UserManager userManager;
	/**
	 * Instance of {@link UserDetailsService} to authenticate user in spring
	 * security
	 */
	@Autowired
	private UserDetailsService manager;

	/**
	 * This rest web services sends an authentication request to Google. First
	 * it creates state token and then it builds login url for Google. After
	 * that state token is saved in current session.
	 * 
	 * @param response
	 *            : instance of {@link HttpServletResponse}
	 * @param request
	 *            : instance of {@link HttpServletRequest}
	 * @return {@link ResponseObject} with redirect google login url, status
	 *         (OK)
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseObject socialGooglePlus(HttpServletResponse response,
			HttpServletRequest request) {
		logger.info("****** Google auth ******");
		ResponseObject responseObject = new ResponseObject();

		String token = auth.getStateToken();

		responseObject.setData(auth.buildLoginUrl());
		responseObject.setStatus(HttpServletResponse.SC_OK);
		// save in session
		request.getSession().setAttribute("state", token);
		response.setStatus(HttpServletResponse.SC_OK);

		return responseObject;
	}

	/**
	 * This rest web service is the one that google called after login (callback
	 * url). First it retrieve code and token that google sends back. It checks
	 * if code and token are not null, then if token is the same that was saved
	 * in session. If it is not response status is UNAUTHORIZED, otherwise it
	 * retrieves user data. If user is not already saved in db, then user is
	 * added in db, iff email is not already used, otherwise it sends an
	 * UNAUTHORIZED status and redirect user to home page without authenticating
	 * him/her. If it is all ok, then it authenticates user in spring security
	 * and create cookie user. Then redirects authenticated user to home page
	 * where user can access protected resources.
	 * 
	 * @param request
	 *            : instance of {@link HttpServletRequest}
	 * @param response
	 *            : instance of {@link HttpServletResponse}
	 * @return redirect to home page
	 */
	@RequestMapping(value = "/callback", method = RequestMethod.GET, produces = "application/json")
	public String confirmStateToken(HttpServletRequest request,
			HttpServletResponse response) {

		String code = request.getParameter("code");
		String token = request.getParameter("state");
		String session_token = "";
		if (request.getSession().getAttribute("state") != null) {
			session_token = request.getSession().getAttribute("state")
					.toString();
		}

		// compare state token in session and state token in response of google
		// if equals return to home
		// if not error page
		if ((code == null || token == null) && (!token.equals(session_token))) {
			logger.error("Error: You have to sign in!");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			try {
				GoogleUser userInfo = auth.getUserInfoJson(code);
				logger.debug("User Info: " + userInfo);
				response.setStatus(HttpServletResponse.SC_OK);
				logger.debug("Check user data");

				// check if user is already in
				User userDb = userManager.getUserByUsername(userInfo.getId());
				if (userDb == null) {
					// add to db
					User user = new User();
					user.setEmail(userInfo.getEmail());
					user.setEnabled(1);
					Profile p = new Profile();
					p.setName(userInfo.getGiven_name());
					p.setSurname(userInfo.getFamily_name());
					p.setImgAvatar(userInfo.getPicture());
					user.setProfile(p);
					user.setRole(UserRoles.ROLE_NORMAL.toString());
					user.setUsername(userInfo.getId());
					try {
						userManager.createSocialUser(user);
					} catch (SecurityException s) {
						logger.error("Error: Already exists a register user with this email address. Please try to login with correct provider.");
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						return "redirect:/";
					}
				}
				// authenticate in spring security
				logger.debug("Set authentication security context holder");
				UserDetails userDetails = manager.loadUserByUsername(userInfo
						.getId());
				Authentication auth = new UsernamePasswordAuthenticationToken(
						userDetails, userDetails.getPassword(),
						userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
				request.getSession()
						.setAttribute(
								HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
								SecurityContextHolder.getContext());

				// check value and set it to true
				Cookie[] cookies = request.getCookies();
				if (cookies != null) {
					for (int i = 0; i < cookies.length; i++) {
						if (cookies[i].getName().equalsIgnoreCase("value")) {
							cookies[i].setValue("true");
							cookies[i].setPath(request.getContextPath() + "/");
							response.addCookie(cookies[i]);
						}
					}
				}
				// user cookie
				CookieUser cu = new CookieUser();
				cu.setUsername(userInfo.getId());
				cu.setRole(UserRoles.ROLE_NORMAL.toString());

				Gson gson = new Gson();
				String obj = gson.toJson(cu);

				Cookie userCookie = new Cookie("user", obj);
				userCookie.setPath(request.getContextPath() + "/");
				response.addCookie(userCookie);

			} catch (IOException e) {
				logger.error("IOException .. Problem in reading user data.", e);
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		}

		return "redirect:/";
	}
}
