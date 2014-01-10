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
 * InOutPairs list: 0- request string, 1- input, 2- output
 * Fixed Requests: 0- request string, 1- input
 * Parametric Request: 0- example request string, 1- example input
 * @author Giulia Canobbio
 *
 */
public class TestBoxProperties implements Serializable{
	
	private String authenticationDescriptor;
	private List<String> InOutPairs;//request string, input, output
	private List<String> fixedRequests;//request string, input
	private List<String> parametricRequest;//example request string, example input
	
	public TestBoxProperties() {
	}

	public String getAuthenticationDescriptor() {
		return authenticationDescriptor;
	}

	public void setAuthenticationDescriptor(String authenticationDescriptor) {
		this.authenticationDescriptor = authenticationDescriptor;
	}

	public List<String> getInOutPairs() {
		return InOutPairs;
	}

	public void setInOutPairs(List<String> inOutPairs) {
		InOutPairs = inOutPairs;
	}

	public List<String> getFixedRequests() {
		return fixedRequests;
	}

	public void setFixedRequests(List<String> fixedRequests) {
		this.fixedRequests = fixedRequests;
	}

	public List<String> getParametricRequest() {
		return parametricRequest;
	}

	public void setParametricRequest(List<String> parametricRequest) {
		this.parametricRequest = parametricRequest;
	}

}