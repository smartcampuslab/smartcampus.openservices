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
package eu.trentorise.smartcampus.openservices.entities;

import java.io.Serializable;
import java.util.List;

/**
 * Blob Object for Method table: 
 * authentication descriptor,
 * a list of test case data, 
 * whether the method is testable.
 * 
 * @author raman
 *
 */
public class TestBoxProperties implements Serializable{
	private static final long serialVersionUID = 1L;

	private boolean testable;
	
	private String requestPathTemplate;
	private String requestBodyTemplate;
	
	private Authentication authenticationDescriptor;
	private List<TestInfo> tests;//request string, input, output
	
	/**
	 * New {@link TestBoxProperties} instance.
	 */
	public TestBoxProperties() {
	}

	/**
	 * Get authentication descriptor.
	 * 
	 * @return {@link Authentication} instance
	 */
	public Authentication getAuthenticationDescriptor() {
		return authenticationDescriptor;
	}

	/**
	 * Set authentication descriptor.
	 * 
	 * @param authenticationDescriptor 
	 * 			: {@link Authentication} instance
	 */
	public void setAuthenticationDescriptor(Authentication authenticationDescriptor) {
		this.authenticationDescriptor = authenticationDescriptor;
	}

	/**
	 * Get list of tests.
	 * 
	 * @return list of {@link TestInfo} instance
	 */
	public List<TestInfo> getTests() {
		return tests;
	}

	/**
	 * Set list of test.
	 * 
	 * @param tests 
	 * 			: {@link TestInfo} instance
	 */
	public void setTests(List<TestInfo> tests) {
		this.tests = tests;
	}

	/**
	 * @return the testable
	 */
	public boolean isTestable() {
		return testable;
	}

	/**
	 * @param testable the testable to set
	 */
	public void setTestable(boolean testable) {
		this.testable = testable;
	}

	/**
	 * @return the requestPathTemplate
	 */
	public String getRequestPathTemplate() {
		return requestPathTemplate;
	}

	/**
	 * @param requestPathTemplate the requestPathTemplate to set
	 */
	public void setRequestPathTemplate(String requestPathTemplate) {
		this.requestPathTemplate = requestPathTemplate;
	}

	/**
	 * @return the requestBodyTemplate
	 */
	public String getRequestBodyTemplate() {
		return requestBodyTemplate;
	}

	/**
	 * @param requestBodyTemplate the requestBodyTemplate to set
	 */
	public void setRequestBodyTemplate(String requestBodyTemplate) {
		this.requestBodyTemplate = requestBodyTemplate;
	}
	
}
