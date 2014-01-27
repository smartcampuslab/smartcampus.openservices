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

package eu.trentorise.smartcampus.openservices.entities;

import java.io.Serializable;
import java.util.Map;

/**
 * TestInfo object for TestBoxProperties
 * name
 * description
 * request path
 * request path editable
 * request body
 * request body editable
 * request method
 * headers
 * response
 * 
 * Used to test service method
 * 
 * @author raman
 *
 */
public class TestInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private String requestPath;
	private boolean requestPathEditable;
	private String requestBody;
	private boolean requestBodyEditable;
	private String requestMethod;
	private Map<String,String> headers;
	private String response;
	
	/**
	 * 
	 * @return String name of {@TestInfo} instance
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 
	 * @param name in {@TestInfo} instance
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return String description of {@TestInfo} instance
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * 
	 * @param description in {@TestInfo} instance
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * @return String request path of {@TestInfo} instance
	 */
	public String getRequestPath() {
		return requestPath;
	}
	
	/**
	 * 
	 * @param requestPath in {@TestInfo} instance
	 */
	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}
	
	/**
	 * 
	 * @return boolean request path editable of {@TestInfo} instance
	 */
	public boolean isRequestPathEditable() {
		return requestPathEditable;
	}
	
	/**
	 * 
	 * @param requestPathEditable in {@TestInfo} instance
	 */
	public void setRequestPathEditable(boolean requestPathEditable) {
		this.requestPathEditable = requestPathEditable;
	}
	
	/**
	 * 
	 * @return String request body of {@TestInfo} instance
	 */
	public String getRequestBody() {
		return requestBody;
	}
	
	/**
	 * 
	 * @param requestBody in {@TestInfo} instance
	 */
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	
	/**
	 * 
	 * @return boolean request body editable of {@TestInfo} instance
	 */
	public boolean isRequestBodyEditable() {
		return requestBodyEditable;
	}
	
	/**
	 * 
	 * @param requestBodyEditable in {@TestInfo} instance
	 */
	public void setRequestBodyEditable(boolean requestBodyEditable) {
		this.requestBodyEditable = requestBodyEditable;
	}
	
	/**
	 * 
	 * @return String request method of {@TestInfo} instance
	 */
	public String getRequestMethod() {
		return requestMethod;
	}
	
	/**
	 * 
	 * @param requestMethod in {@TestInfo} instance
	 */
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	/**
	 * 
	 * @return Map<String,String> headers of {@TestInfo} instance
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}
	
	/**
	 * 
	 * @param headers in {@TestInfo} instance
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	/**
	 * 
	 * @return String response of {@TestInfo} instance
	 */
	public String getResponse() {
		return response;
	}
	
	/**
	 * 
	 * @param response in {@TestInfo} instance
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TestInfo [name=" + name + ", description=" + description
				+ ", requestPath=" + requestPath + ", requestPathEditable="
				+ requestPathEditable + ", requestBody=" + requestBody
				+ ", requestBodyEditable=" + requestBodyEditable
				+ ", requestMethod=" + requestMethod + ", headers=" + headers
				+ ", response=" + response + "]";
	}
	
}
