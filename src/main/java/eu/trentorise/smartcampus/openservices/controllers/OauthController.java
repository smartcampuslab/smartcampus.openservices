package eu.trentorise.smartcampus.openservices.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import eu.trentorise.smartcampus.aac.AACException;
import eu.trentorise.smartcampus.aac.AACService;
import eu.trentorise.smartcampus.openservices.entities.User;
import eu.trentorise.smartcampus.openservices.managers.UserManager;
import eu.trentorise.smartcampus.openservices.securitymodel.OauthAuthentication;
import eu.trentorise.smartcampus.profileservice.BasicProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;

@Controller
public class OauthController {

	@Autowired
	private AACService oauthClient;

	@Autowired
	private BasicProfileService profileService;

	@Autowired
	private UserManager userManager;

	@Autowired
	private Environment env;

	@Autowired
	private OauthAuthentication oauth;

	private static final Logger logger = org.slf4j.LoggerFactory
			.getLogger(OauthController.class);

	@RequestMapping(method = RequestMethod.GET, value = "/oauthcheck")
	public ModelAndView oauthChecker(
			@RequestParam(required = false) String code,
			@RequestParam(required = false) String error,
			@RequestParam(value = "error_description", required = false) String errorDesc,
			HttpServletResponse resp) throws IOException {

		if ("access_denied".equals(error)) {
			resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorDesc);
			return null;
		}
		if (code != null) {
			try {
				String userToken = oauthClient.exchngeCodeForToken(code,
						env.getProperty("oauth.callback_uri"))
						.getAccess_token();
				BasicProfile profile = profileService
						.getBasicProfile(userToken);
				oauth.setName(profile.getUserId());
				oauth.setAuthenticated(true);
				SecurityContextHolder.getContext().setAuthentication(oauth);
				// if authentication true
				// search in local db for roles
				// otherwise create new user with role based on restricted
				// save user in local db
				if (userManager.getUserByUsername(profile.getUserId()) == null) {
					User u = new User();
					u.setUsername(profile.getUserId());
					// email cannot be null
					u.setEmail(profile.getUserId());
					userManager.createOauthUser(u);
				}
			} catch (SecurityException e) {
				logger.error("Error in oauth controller", e);
			} catch (AACException e) {
				logger.error("Error in oauth controller", e);
			} catch (ProfileServiceException e) {
				logger.error("Error in oauth controller", e);
			}

			return new ModelAndView("redirect: /");
		} else {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
					"request without either code and error");
			return null;
		}
	}
}
