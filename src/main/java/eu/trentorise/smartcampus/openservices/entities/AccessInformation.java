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
 * Blob Object AccesInformation for Service table
 * access policies: public or protection description
 * testing endpoint (optional)
 * production endpoint
 * protocols/formats: JSON, XML,...
 * 
 * @author Giulia Canobbio
 *
 */
public class AccessInformation implements Serializable{
	
	private String accessPolicies;
	private String testingEndpoint;
	private String productionEndpoint;
	private List<String> protocols;

	public AccessInformation(){
		
	}

	public String getAccessPolicies() {
		return accessPolicies;
	}

	public void setAccessPolicies(String accessPolicies) {
		this.accessPolicies = accessPolicies;
	}

	public String getTestingEndpoint() {
		return testingEndpoint;
	}

	public void setTestingEndpoint(String testingEndpoint) {
		this.testingEndpoint = testingEndpoint;
	}

	public String getProductionEndpoint() {
		return productionEndpoint;
	}

	public void setProductionEndpoint(String productionEndpoint) {
		this.productionEndpoint = productionEndpoint;
	}

	public List<String> getProtocols() {
		return protocols;
	}

	public void setProtocols(List<String> protocols) {
		this.protocols = protocols;
	}
}
