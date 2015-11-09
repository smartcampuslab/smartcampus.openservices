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
package eu.trentorise.smartcampus.openservices.controllers;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import eu.trentorise.smartcampus.openservices.entities.ResponseObject;

/**
 * Retrieve a Multipart file from a form and then save it in an organization
 * 
 * @author Giulia Canobbio
 * 
 */
@Controller
@RequestMapping(value = "/api/file/")
@PropertySource("classpath:openservice.properties")
public class FileController {

	private static final Logger logger = LoggerFactory
			.getLogger(FileController.class);

	/**
	 * Instance of {@link Environment} to get all variables in properties file
	 */
	@Autowired
	private Environment env;

	/**
	 * Retrieve an image and save it.
	 * 
	 * @param organizationId
	 *            : int
	 * @param file
	 *            : {@link MultipartFile}
	 * @param request
	 *            : instance of {@link HttpServletRequest}
	 * @param response
	 *            : instance of {@link HttpServletResponse}
	 * @return {@link ResponseObject} with filename, status (OK, NOT ACCEPTABLE,
	 *         BAD REQUEST or NOT FOUND) and error message (if status is NOT
	 *         ACCEPTABLE, BAD REQUEST or NOT FOUND).
	 */
	@RequestMapping(value = "upload/{organizationId}", method = RequestMethod.POST)
	public @ResponseBody
	ResponseObject uploadFile(@PathVariable int organizationId,
			@RequestParam("file") MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) {

		String dirFile = env.getProperty("filedir");

		ResponseObject responseObject = new ResponseObject();
		String realPath = request.getSession().getServletContext()
				.getRealPath("/images");
		logger.debug("Request Real Path: " + realPath);
		if (!file.isEmpty() && file != null) {
			try {
				File f = new File(dirFile + "/" + organizationId + "/"
						+ file.getOriginalFilename());
				logger.debug("Absolute path: " + f.getAbsolutePath());
				// check if this exists
				if (!f.exists()) {
					logger.debug("Directory does not exist, then creating...");
					if (f.mkdirs()) {
						logger.debug("Directory created successfully");
					}
				}
				file.transferTo(f);
				responseObject.setData(file.getOriginalFilename());
				responseObject.setStatus(HttpServletResponse.SC_OK);
				logger.debug("File uploaded correctly");

			} catch (IllegalStateException e) {
				logger.error("Error in uploading file, server error", e);
				responseObject.setError("Problem in uploading the file.");
				responseObject.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
				response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);

			} catch (IOException e) {
				logger.error("Error in uploading file, problem in reading", e);
				responseObject.setError("Error: Cannot read file.");
				responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} else {
			logger.info("Error in uploading file, file not found");
			responseObject.setError("File not found");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}

}
