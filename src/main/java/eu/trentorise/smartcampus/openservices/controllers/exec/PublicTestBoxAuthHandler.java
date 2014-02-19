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

import org.springframework.stereotype.Component;

/**
 * Implementation of the REST service calls without any authentication required.
 * @author raman
 *
 */
@Component("Public")
public class PublicTestBoxAuthHandler extends AbstractTestBoxAuthHandler {
	
	@Override
	public void authorize(HttpServletRequest request, HttpServletResponse response, Map<String, Object> accessAttributes) throws TestBoxException {
		throw new TestBoxException("Not supported for Public access");
	}

	@Override
	public void onAuthorized(HttpServletRequest request, HttpServletResponse response) throws TestBoxException {
		throw new TestBoxException("Not supported for Public access");
	}



	@Override
	public TestResponse performTest(HttpServletRequest request, TestBoxParams params, Map<String, Object> accessAttributes) throws TestBoxException {
		return execute(params);
	}
}
