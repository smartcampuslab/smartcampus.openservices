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
import java.util.*;

/**
 * Blob Object for Method table
 * authentication descriptor
 * a list of tests
 * 
 * @author Giulia Canobbio
 *
 */
public class TestBoxProperties implements Serializable{
	private static final long serialVersionUID = 1L;

	private Authentication authenticationDescriptor;
	private List<TestInfo> tests;//request string, input, output
	
	/**
	 * New {@TestBoxProperties} instance
	 */
	public TestBoxProperties() {
	}

	/**
	 * 
	 * @return {@Authentication} instance of {@TestBoxProperties} instance
	 */
	public Authentication getAuthenticationDescriptor() {
		return authenticationDescriptor;
	}

	/**
	 * 
	 * @param authenticationDescriptor in {@TestBoxProperties} instance
	 */
	public void setAuthenticationDescriptor(Authentication authenticationDescriptor) {
		this.authenticationDescriptor = authenticationDescriptor;
	}

	/**
	 * 
	 * @return list of {@TestInfo} instance of {@TestBoxProperties} instance
	 */
	public List<TestInfo> getTests() {
		return tests;
	}

	/**
	 * 
	 * @param tests in {@TestBoxProperties} instance
	 */
	public void setTests(List<TestInfo> tests) {
		this.tests = tests;
	}
}
