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
import java.util.List;
import java.util.Map;

/**
 * Service method execution properties
 * @author raman
 *
 */
public class ExecutionProperties implements Serializable{
	private static final long serialVersionUID = 1980701735154235210L;

	private String httpMethod;
	private Map<String,List<String>> headers;
	
	private String requestPathTemplate;
	private String requestBodyTemplate;
	

	/**
	 * @return the httpMethod
	 */
	public String getHttpMethod() {
		return httpMethod;
	}
	/**
	 * @param httpMethod the httpMethod to set
	 */
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	/**
	 * @return the headers
	 */
	public Map<String, List<String>> getHeaders() {
		return headers;
	}
	/**
	 * @param headers the headers to set
	 */
	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
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
