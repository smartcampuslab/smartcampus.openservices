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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handler for the TestBox execution. 
 * Provides methods for authorization handling and actual test execution
 * @author raman
 *
 */
public interface TestBoxAuthHandler {

	/**
	 * Test box parameters with the request method, URL, body, custom headers, and credentials
	 * @author raman
	 *
	 */
	public static class TestBoxParams {
		String requestMethod;
		String requestUrl;
		String requestBody;
		Map<String,String> requestHeaders;
		Object credentials;
	}

	/**
	 * Handle authorization for the specific authentication data and request
	 * @param request
	 * @param response
	 * @param accessAttributes
	 * @throws TestBoxException
	 */
	void authorize(HttpServletRequest request, HttpServletResponse response, Map<String, Object> accessAttributes) throws TestBoxException;
	
	/**
	 * Handle the authorization result callback
	 * @param request
	 * @param response
	 * @throws TestBoxException
	 */
	void onAuthorized(HttpServletRequest request, HttpServletResponse response) throws TestBoxException;
	
	/**
	 * Perform actual test
	 * @param request 
	 * @param params
	 * @param accessAttributes
	 * @return {@link TestResponse} data
	 */
	TestResponse performTest(HttpServletRequest request, TestBoxParams params, Map<String, Object> accessAttributes) throws TestBoxException;
}
