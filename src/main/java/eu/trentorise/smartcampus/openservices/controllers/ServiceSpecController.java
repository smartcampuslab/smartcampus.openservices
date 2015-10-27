package eu.trentorise.smartcampus.openservices.controllers;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.openservices.Constants;
import eu.trentorise.smartcampus.openservices.entities.Service;
import eu.trentorise.smartcampus.openservices.managers.ServiceManager;

@Controller
@RequestMapping(value = "/service/public")
public class ServiceSpecController {

	private static final Logger logger = LoggerFactory
			.getLogger(ServiceSpecController.class);
	@Autowired
	private ServiceManager serviceManager;

	private static final List<String> specs = Arrays.asList("xwadl", "usdl");

	@RequestMapping(value = "/{id}/spec/list", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getSpecList(@PathVariable int id,
			HttpServletResponse response) {

		return specs;
	}

	@RequestMapping(value = "/{id}/spec/{spec}", method = RequestMethod.GET)
	@ResponseBody
	public void getXWADL(@PathVariable int id, @PathVariable String spec,
			HttpServletResponse response) {

		// check if spec is supported

		boolean isSpecValid = spec != null && specs.contains(spec);

		if (!isSpecValid) {
			try {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Service specification requested is not supported: "
								+ spec);
				return;
			} catch (IOException e) {
				logger.error("Error sending http error response");
			}
		}
		// check if service is published
		Service s = serviceManager.getServiceById(id);
		if (s == null) {
			try {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Service not exist");
				return;
			} catch (IOException e) {
				logger.error("Error sending http error response");
			}
		}
		if (!s.getState().equals(Constants.SERVICE_STATE.PUBLISH.toString())) {
			try {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			} catch (IOException e) {
				logger.error("Error sending http error response");
			}
		}

		String contentType = null;
		String doc = null;

		if (spec.equals("xwadl")) {
			doc = serviceManager.getWADL(id);
			contentType = "application/xml";
		} else if (spec.equals("usdl")) {
			doc = serviceManager.getUSDL(id);
			contentType = "application/rdf+xml";
		}

		try {
			response.setContentType(contentType);
			Writer w = response.getWriter();
			w.write(doc);
			w.flush();
		} catch (IOException e) {
			try {
				response.sendError(
						HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						"Something has gone bad");
			} catch (IOException e1) {
				logger.error("Error sending http error response");
			}
		}
	}
}
