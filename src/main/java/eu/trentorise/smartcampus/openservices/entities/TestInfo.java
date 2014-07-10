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
 * TestInfo object for TestBoxProperties:
 * name,
 * description,
 * request path,
 * request path editable,
 * request body,
 * request body editable,
 * request method,
 * headers,
 * response.
 * 
 * Used to test service method.
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
	 * Get name.
	 * 
	 * @return String name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set name.
	 * 
	 * @param name 
	 * 			: String
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get description.
	 * 
	 * @return String description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Set description.
	 * 
	 * @param description 
	 * 			: String
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Get request path.
	 * 
	 * @return String request path
	 */
	public String getRequestPath() {
		return requestPath;
	}
	
	/**
	 * Set request path.
	 * 
	 * @param requestPath 
	 * 			: String
	 */
	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}
	
	/**
	 * Check if request path is editable
	 * 
	 * @return boolean, true if request path is editable otherwise false.
	 */
	public boolean isRequestPathEditable() {
		return requestPathEditable;
	}
	
	/**
	 * Set request path editable.
	 * 
	 * @param requestPathEditable 
	 * 			: boolean
	 */
	public void setRequestPathEditable(boolean requestPathEditable) {
		this.requestPathEditable = requestPathEditable;
	}
	
	/**
	 * Get request body.
	 * 
	 * @return String request body
	 */
	public String getRequestBody() {
		return requestBody;
	}
	
	/**
	 * Set request body.
	 * 
	 * @param requestBody 
	 * 				: String
	 */
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	
	/**
	 * Check if request body is editable.
	 * 
	 * @return boolean true if a request body is editable, otherwise false.
	 */
	public boolean isRequestBodyEditable() {
		return requestBodyEditable;
	}
	
	/**
	 * Set request body editable.
	 * 
	 * @param requestBodyEditable 
	 * 			: boolean
	 */
	public void setRequestBodyEditable(boolean requestBodyEditable) {
		this.requestBodyEditable = requestBodyEditable;
	}
	
	/**
	 * Get request method.
	 * 
	 * @return String request method 
	 */
	public String getRequestMethod() {
		return requestMethod;
	}
	
	/**
	 * Set request method.
	 * 
	 * @param requestMethod : String
	 */
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	
	/**
	 * Get headers.
	 * 
	 * @return Map<String,String> headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}
	
	/**
	 * Set headers.
	 * 
	 * @param headers 
	 * 				: Map<String,String>
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	/**
	 * Get response.
	 * 
	 * @return String response
	 */
	public String getResponse() {
		return response;
	}
	
	/**
	 * Set response.
	 * 
	 * @param response 
	 * 				: String
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
