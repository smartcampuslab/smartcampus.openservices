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
package eu.trentorise.smartcampus.openservice.test.controller;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.mchange.io.FileUtils;

import eu.trentorise.smartcampus.openservices.entities.ResponseObject;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value= {"file:src/main/webapp/WEB-INF/spring/root-context.xml",
		"file:src/main/webapp/WEB-INF/spring/spring-security.xml",
		"file:src/main/webapp/WEB-INF/spring/appServlet/servlet-context.xml",
		"file:src/main/webapp/WEB-INF/spring/spring-embedded-db.xml"})
		@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public class FileControllerTest {

	private static final String BASE_URL = "http://localhost:8080/openservice/api/file";
	private RestTemplate restTemplate;
	//Log
	private Logger log = LoggerFactory.getLogger(FileControllerTest.class);
	
	@Before
	public void setUp(){
		restTemplate = new RestTemplate();		
	}
	
	@Test
	public void testUploadFile() throws Exception{
		log.info("* Test Catalog REST: /upload - STARTING");
		
		//try create a File
		File file = new File("/Users/Giulia/Pictures/Black-Wallpapers-14.jpg");

		//Resource logo = new ClassPathResource("/Users/Giulia/Pictures/Black-Wallpapers-14.jpg");
		MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<String, Object>();
		//FileSystemResource logo = new FileSystemResource("/Users/Giulia/Pictures/Black-Wallpapers-14.jpg");
		//log.info("****** "+logo.getFilename());
		//log.info("****** "+logo.getFile());
		log.info("file absolute path: "+file.getAbsolutePath());
		mvm.add("file", new FileSystemResource(file));//logo
		
		/*//headers
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "multipart/form-data");
		//headers.set("Accept", "text/plain");

		ResponseEntity<ResponseObject> respEnt = restTemplate.exchange(BASE_URL+"/upload/1", HttpMethod.POST, 
				new HttpEntity<Object>(mvm, headers), ResponseObject.class);
		*/
		log.info("***** Before post");
		ResponseEntity<ResponseObject> respEnt = restTemplate.postForEntity(BASE_URL+"/upload/1", mvm,ResponseObject.class);
		
		log.info("Headers: "+respEnt.getHeaders());
		log.info("Body data: "+respEnt.getBody().getData()+", status: "+respEnt.getBody().getStatus()
				+", error: "+respEnt.getBody().getError());
		log.info("Status: "+respEnt.getStatusCode());
		
		//- OR --
		/*
		// Set the headers...
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "multipart/form-data"); 
		//headers.set("Accept", "text/plain");

		ResponseObject result = restTemplate.exchange(
			BASE_URL+"/upload/{organizationId}",
		    HttpMethod.POST,
		    new HttpEntity<MultiValueMap<String, Object>>(mvm, headers),
		    ResponseObject.class,
		    1
		).getBody();
		
		log.info("ResponseObject: "+result);
		*/
	}
}
