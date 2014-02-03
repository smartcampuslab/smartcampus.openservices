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

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import eu.trentorise.smartcampus.openservices.dao.FileDao;
import eu.trentorise.smartcampus.openservices.entities.File;
import eu.trentorise.smartcampus.openservices.entities.ResponseObject;

/**
 * Sample:
 * upload a file and save it to database
 * 
 * @author Giulia Canobbio
 *
 */
@Controller
@RequestMapping(value="/api/file")
public class FileController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	private ResponseObject responseObject;
	@Autowired
	private FileDao fileDao;
	
	@RequestMapping(method = RequestMethod.GET)
	public String showRegistration() {
		return "uploadFile";
	}
	
	/**
	 * User can upload a file to an organization
	 * @param file : a {@link ResponseObject} instance
	 * @return a {@link ResponseObject} instance with uploaded file data, 
	 * status (OK, BAD REQUEST or NOT FOUND) and
	 * error message (if status is NOT FOUND or BAD REQUEST).
	 */
	@RequestMapping(value="/upload/{org_id}", method = RequestMethod.POST, produces="application/json")
	public @ResponseBody ResponseObject upload(@PathVariable int org_id, @RequestBody MultipartFile file){
		logger.info("-- UPLOAD File in Database --");
		responseObject = new ResponseObject();
		
		//add Data in File from Multipart File
		if (!file.isEmpty()) {
			//File savedFile = new File();
			java.io.File saveFile = new java.io.File("resources/file/"+org_id+"/"+file.getOriginalFilename());
			
			try {
			
				file.transferTo(saveFile);
			
			
				//savedFile.setFilename(file.getOriginalFilename());
				//savedFile.setOrg_id(org_id);
			
			
				//savedFile.setFile(file.getBytes());
				
				responseObject.setData(file.getOriginalFilename());
				responseObject.setStatus(HttpServletResponse.SC_OK);
			
			} catch (IOException e) {
				e.printStackTrace();
				responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				responseObject.setError("Problem in reading file");
			}

			/*
			fileDao.saveFile(savedFile);

			File newFile = fileDao.getFileByFilename(savedFile.getFilename());
			if (newFile != null) {
				responseObject.setData(newFile);
				responseObject.setStatus(HttpServletResponse.SC_OK);
			} else {
				responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
				responseObject.setError("Error in saving new file");
			}
			*/
		} else {
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			responseObject.setError("Uploaded file is missing");
		}
		
		return responseObject;
	}
	
	/**
	 * Download a wanted file.
	 * Search by file id.
	 * @param file_id : int
	 * @return a {@link ResponseObject} instance with wanted file data, status (OK or NOT FOUND) and
	 * error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/download/{file_id}", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseObject download(@PathVariable int file_id){
		logger.info("-- Download File by id --");
		responseObject = new ResponseObject();
		File file = fileDao.getFileById(file_id);
		if(file!=null){
			responseObject.setData(file);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("File does not exist with this id.");
		}
		return responseObject;
	}
	
	/**
	 * Download a list of organization files.
	 * Search by organization id.
	 * @param org_id : int organization id
	 * @return a list of {@link ResponseObject} instances with file data of organization, 
	 * status (OK or NOT FOUND) and error message (if status is NOT FOUND).
	 */
	@RequestMapping(value="/download/{org_id}", method = RequestMethod.GET, produces="application/json")
	public @ResponseBody ResponseObject downloadList(@PathVariable int org_id){
		logger.info("-- Download list of file for organization --");
		responseObject = new ResponseObject();
		List<File> list = fileDao.getFileByOrgId(org_id);
		if(list!=null){
			responseObject.setData(list);
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}else{
			responseObject.setStatus(HttpServletResponse.SC_NOT_FOUND);
			responseObject.setError("No files exist for this organization.");
		}
		return responseObject;
	}
	
	/**
	 * Delete a file, searching by id.
	 * @param file_id : int
	 * @return a {@link ResponseObject} instance with status (OK or BAD REQUEST) and
	 * error message (if status is BAD REQUEST).
	 */
	@RequestMapping(value="/delete/{file_id}", method = RequestMethod.POST, produces="application/json")
	public @ResponseBody ResponseObject delete(@PathVariable int file_id){
		logger.info("-- Delete a file --");
		responseObject = new ResponseObject();
		
		fileDao.deleteFile(file_id);
		
		File deletedFile = fileDao.getFileById(file_id);
		if(deletedFile!=null){
			responseObject.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			responseObject.setError("Error in deleting file");
		}else{
			responseObject.setStatus(HttpServletResponse.SC_OK);
		}
		return responseObject;
	}
	
	
}
