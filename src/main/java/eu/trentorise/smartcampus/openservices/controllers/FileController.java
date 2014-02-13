package eu.trentorise.smartcampus.openservices.controllers;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import eu.trentorise.smartcampus.openservices.entities.ResponseObject;

@Controller
@RequestMapping(value="/api/file/")
public class FileController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	/**
	 * {@link ResponseObject} Response object contains requested data, 
	 * status of response and if necessary a custom error message.
	 */
	private ResponseObject responseObject;
	
	@RequestMapping(value="home")
	public String fileShow(){
		return "file";
	}
	
	@RequestMapping(value = "upload/{organizationId}", method = RequestMethod.POST)
	public @ResponseBody ResponseObject uploadFile(@PathVariable int organizationId, @RequestParam("file") MultipartFile file,
			HttpServletRequest request) {
		logger.info("-- FILE -- Uploading file ...");
		responseObject = new ResponseObject();
		logger.info("Request Real Path: "+request.getSession().getServletContext().getRealPath("/"));
		if(!file.isEmpty() && file!=null){
			logger.info("File "+file);
			try {
				//src/main/webapp/uploadedFile/
				File f = new File("/uploadedFile/"+organizationId+"/"
						+ file.getOriginalFilename());
				logger.info("Absolute path: "+f.getAbsolutePath());
				//check if this exists
				if(!f.exists()){
					logger.info("Directory does not exist, then creating...");
					if(f.mkdirs()){
						logger.info("Directory created successfully");
					}
				}
				file.transferTo(f);
				responseObject.setData(file.getOriginalFilename());
				responseObject.setStatus(HttpServletResponse.SC_OK);
				logger.info("-- File uploaded correctly --");
			} catch (IllegalStateException e) {
				logger.info("-- Error in uploading file, server error --");
				e.printStackTrace();
				responseObject.setError("Problem in uploading the file.");
				responseObject.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
			} catch (IOException e) {
				logger.info("-- Error in uploading file, problem in reading --");
				e.printStackTrace();
				responseObject.setError("Error: Cannot read file.");
				responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		else{
			logger.info("-- Error in uploading file, file not found --");
			responseObject.setError("File not found");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
		return responseObject;
	}
	
	@RequestMapping(value = "download/{organizationId}", method = RequestMethod.GET)
	public @ResponseBody ResponseObject downloadFile(@PathVariable int organizationId, HttpServletResponse response) {
		logger.info("-- FILE -- Download file ...");
		responseObject = new ResponseObject();
		/*
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream("/Users/Giulia/Desktop/uploadedFile/"+organizationId+"/*");

			response.setHeader("Content-Disposition",
					"attachment; filename=test.txt");

			try {
				int c;
				while ((c = inputStream.read()) != -1) {
					response.getWriter().write(c);
				}
			} finally {
				if (inputStream != null)
					inputStream.close();
				response.getWriter().close();
			}
			responseObject.setStatus(HttpServletResponse.SC_OK);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseObject.setError("File not found");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
		} // read the file
		catch (IOException e) {
			responseObject.setError("Problem in writing file");
			responseObject.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
		}
		*/
		
		//-- OR --
		try{
			File f = new File("/uploadedFile/"+organizationId+"/*.*");
			FileSystemResource fsr = new FileSystemResource(f);
			responseObject.setData(fsr);
			responseObject.setStatus(HttpServletResponse.SC_OK);
			logger.info("Download successfully");
		} catch (Throwable e) {
			logger.info("Download error");
			responseObject.setError("File not found");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			e.printStackTrace();
		}/*
		catch (NullPointerException n){
			logger.info("Download error - NullPointerException");
			responseObject.setError("File not found");
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			n.printStackTrace();
		}*/
		
		return responseObject;
	}

}
