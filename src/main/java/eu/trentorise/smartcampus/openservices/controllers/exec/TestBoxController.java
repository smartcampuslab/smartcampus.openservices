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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
public class TestBoxController {

	@Autowired
	private ServiceManager manager;
	@Autowired
	private TestBoxAuthHandlerFactory handlerFactory;
	
	@ResponseBody
	public TestResponse doTest(@PathVariable int method, @PathVariable String test, @RequestBody TestRequest req, HttpServletRequest request) throws TestBoxException {
		// find method to test
		Method m = manager.getMethodById(method);
		if (m == null) throw new TestBoxException("method not found: "+ method);
		// find the specific test data
		List<TestInfo> tests = m.getTestboxProperties().getTests();
		TestInfo testInfo = null;
		for (TestInfo ti : tests) {
			if (ti.getName().equals(test)) {
				testInfo = ti;
				break;
			}
		}
		if (testInfo == null) throw new TestBoxException("no test found: "+test);
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
		params.requestHeaders = extractHeaders(request);
		
		// read method auth properties or the inherit the service ones
		Authentication  a = m.getTestboxProperties().getAuthenticationDescriptor();
		if (a == null) a = manager.getServiceById(m.getServiceId()).getAccessInformation().getAuthentication();
		if (a == null) throw new TestBoxException("authentication not defined: "+ method);
		// find the auth handler for the specific protocol
		TestBoxAuthHandler handler = handlerFactory.getHandler(a.getAccessProtocol());
		if (handler == null) throw new TestBoxException("no handler for authentication: "+a.getAccessProtocol());
		
		return handler.performTest(request, params, a.getAccessAttributes());
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String,String> extractHeaders(HttpServletRequest req) throws TestBoxException {
		Map<String,String> headers = new HashMap<String, String>();
		Enumeration<String> names = req.getHeaderNames();
		while(names.hasMoreElements()) {
			String name = names.nextElement();
			headers.put(name, req.getHeader(name));
		}
		return headers;
	}
	
}
