package com.openserviceproject.entities;

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
