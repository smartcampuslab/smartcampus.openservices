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

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.trentorise.smartcampus.openservices.controllers.exec.TestBoxAuthHandler.TestBoxParams;
import eu.trentorise.smartcampus.openservices.entities.Authentication;
import eu.trentorise.smartcampus.openservices.entities.Method;
import eu.trentorise.smartcampus.openservices.entities.TestInfo;
import eu.trentorise.smartcampus.openservices.managers.ServiceManager;

/**
 * @author raman
 *
 */
@Controller
@RequestMapping(value="/api/testbox")
public class TestBoxController {

	@Autowired
	private ServiceManager manager;
	@Autowired
	private TestBoxAuthHandlerFactory handlerFactory;
	
	
	@RequestMapping(method=RequestMethod.GET, value="/authorize/{method}")
	public void authorize(@PathVariable int method, HttpServletRequest req, HttpServletResponse res) throws TestBoxException {
		// find method to test
		Method m = manager.getMethodById(method);
		if (m == null) throw new TestBoxException("method not found: "+ method);
		// read method auth properties or inherit the service ones
		Authentication  a = m.getTestboxProperties().getAuthenticationDescriptor();
		if (a == null) a = manager.getServiceById(m.getServiceId()).getAccessInformation().getAuthentication();
		if (a == null) throw new TestBoxException("authentication not defined: "+ method);
		// find the auth handler for the specific protocol
		TestBoxAuthHandler handler = handlerFactory.getHandler(a.getAccessProtocol());
		if (handler == null) throw new TestBoxException("no handler for authentication: "+a.getAccessProtocol());
		handler.authorize(req, res, a.getAccessAttributes());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/authorized")
	public String authorized(HttpServletRequest req, HttpServletResponse res) throws TestBoxException {
		return "success";
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/test/{method}")
	@ResponseBody
	public TestResponse doTest(@PathVariable int method, @RequestBody TestRequest req, HttpServletRequest request, HttpServletResponse res) {
		try {
			// find method to test
			Method m = manager.getMethodById(method);
			if (m == null) throw new TestBoxException("method not found: "+ method);
			// find the specific test data
			List<TestInfo> tests = m.getTestboxProperties().getTests();
			TestInfo testInfo = null;
			for (TestInfo ti : tests) {
				if (ti.getName().equals(req.getName())) {
					testInfo = ti;
					break;
				}
			}
			if (testInfo == null) throw new TestBoxException("no test found: "+req.getName());
			// if the test response is already defined, simply return it
			if (testInfo.getResponse() != null && ! testInfo.getResponse().isEmpty()) {
				TestResponse result = new TestResponse();
				result.setData(testInfo.getResponse());
				return result;
			}
			// prepare test data
			TestBoxParams params = new TestBoxParams();
			params.requestMethod = testInfo.getRequestMethod();
			params.requestUrl = testInfo.isRequestPathEditable() ? req.getRequestUrl() : testInfo.getRequestPath();
			params.requestBody = testInfo.isRequestBodyEditable() ? req.getRequestBody() : testInfo.getRequestBody();
			params.requestHeaders = testInfo.getHeaders();
			params.credentials = req.getCredentials();
			
			// read method auth properties or inherit the service ones
			Authentication  a = m.getTestboxProperties().getAuthenticationDescriptor();
			if (a == null) a = manager.getServiceById(m.getServiceId()).getAccessInformation().getAuthentication();
			if (a == null) throw new TestBoxException("authentication not defined: "+ method);
			// find the auth handler for the specific protocol
			TestBoxAuthHandler handler = handlerFactory.getHandler(a.getAccessProtocol());
			if (handler == null) throw new TestBoxException("no handler for authentication: "+a.getAccessProtocol());
			
			return handler.performTest(request, params, a.getAccessAttributes());
		} catch (TestBoxException te) {
			TestResponse tr = new TestResponse();
			if (te.status > 0) {
				res.setStatus(te.status);
			} else {
				res.setStatus(HttpStatus.BAD_REQUEST.value());
			}
			if (te.getMessage() != null) {
				tr.setData(te.getMessage());
			}
			return tr;
		}
	}
}
