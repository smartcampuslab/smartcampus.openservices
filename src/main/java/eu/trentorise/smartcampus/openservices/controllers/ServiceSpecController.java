package eu.trentorise.smartcampus.openservices.controllers;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
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

	@RequestMapping(value = "/{id}/spec/list", method = RequestMethod.GET)
	@ResponseBody
	public List<String> getSpecList(@PathVariable int id,
			HttpServletResponse response) {

		List<String> spec = new ArrayList<String>();
		spec.add("xwadl");
		spec.add("udsl");

		return spec;
	}

	@RequestMapping(value = "/{id}/spec/xwadl", method = RequestMethod.GET)
	@ResponseBody
	public void getXWADL(@PathVariable int id, HttpServletResponse response) {

		// check if service is published
		Service s = serviceManager.getServiceById(id);
		if (!s.getState().equals(Constants.SERVICE_STATE.PUBLISH.toString())) {
			try {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			} catch (IOException e) {
				logger.error("Error sending http error response");
			}
		}

		try {

			String wadl = serviceManager.getWADL(id);

			try {
				response.setContentType("application/xml");
				Writer w = response.getWriter();
				w.write(wadl);
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
		} catch (NullPointerException e) {
			try {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST,
						"Service not exist");
			} catch (IOException e1) {
				logger.error("Error sending http error response");
			}
		}
	}
}
