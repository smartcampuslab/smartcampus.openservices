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

/**
 * Blob Object AccesInformation for Service table:
 * access policies: public or protection description;
 * testing endpoint (optional);
 * production endpoint;
 * protocols/formats: JSON, XML,...
 * 
 * @author Giulia Canobbio
 *
 */
public class AccessInformation implements Serializable{
	
	private String accessPolicies;
	private String testingEndpoint;
	private String productionEndpoint;
	private String protocols;
	private String formats;
	private Authentication authentication;

	/**
	 * Constructor method.
	 * Create a new instance of {@link AccessInformation}.
	 */
	public AccessInformation(){
		
	}

	/**
	 * Get access policies.
	 * 
	 * @return String access policies
	 */
	public String getAccessPolicies() {
		return accessPolicies;
	}

	/**
	 * Set access policies.
	 * 
	 * @param accessPolicies 
	 * 			: String
	 */
	public void setAccessPolicies(String accessPolicies) {
		this.accessPolicies = accessPolicies;
	}

	/**
	 * Get testing endpoint.
	 * 
	 * @return String testing endpoint
	 */
	public String getTestingEndpoint() {
		return testingEndpoint;
	}

	/**
	 * Set testing endpoint.
	 * 
	 * @param testingEndpoint 
	 * 			: String 
	 */
	public void setTestingEndpoint(String testingEndpoint) {
		this.testingEndpoint = testingEndpoint;
	}

	/**
	 * Get production endpoint.
	 * 
	 * @return String production endpoint
	 */
	public String getProductionEndpoint() {
		return productionEndpoint;
	}

	/**
	 * Set production endpoint.
	 * 
	 * @param productionEndpoint 
	 * 			: String 
	 */
	public void setProductionEndpoint(String productionEndpoint) {
		this.productionEndpoint = productionEndpoint;
	}

	/**
	 * Get protocols.
	 * 
	 * @return String protocols
	 */
	public String getProtocols() {
		return protocols;
	}

	/**
	 * Set protocols.
	 * 
	 * @param protocols 
	 * 			: String 
	 */
	public void setProtocols(String protocols) {
		this.protocols = protocols;
	}

	/**
	 * Get formats.
	 * 
	 * @return String formats
	 */
	public String getFormats() {
		return formats;
	}

	/**
	 * Set formats.
	 * 
	 * @param formats 
	 * 			: String
	 */
	public void setFormats(String formats) {
		this.formats = formats;
	}

	/**
	 * Get {@link Authentication} instance.
	 * 
	 * @return {@link Authentication} authentication
	 */
	public Authentication getAuthentication() {
		return authentication;
	}

	/**
	 * Set {@link Authentication} authentication.
	 * 
	 * @param authentication 
	 * 			: {@link Authentication} 
	 */
	public void setAuthentication(Authentication authentication) {
		this.authentication = authentication;
	}
	
}
