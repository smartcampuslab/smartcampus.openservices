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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRequestPath() {
		return requestPath;
	}
	public void setRequestPath(String requestPath) {
		this.requestPath = requestPath;
	}
	public boolean isRequestPathEditable() {
		return requestPathEditable;
	}
	public void setRequestPathEditable(boolean requestPathEditable) {
		this.requestPathEditable = requestPathEditable;
	}
	public String getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}
	public boolean isRequestBodyEditable() {
		return requestBodyEditable;
	}
	public void setRequestBodyEditable(boolean reuqestBodyEditable) {
		this.requestBodyEditable = reuqestBodyEditable;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public String getResponse() {
		return response;
	}
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
