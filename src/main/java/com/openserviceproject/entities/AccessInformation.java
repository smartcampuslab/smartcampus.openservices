package com.openserviceproject.entities;

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
