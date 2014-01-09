package com.openserviceproject.entities;

import java.io.Serializable;
import java.util.*;

public class ImplementationInfo implements Serializable{
	
	private String executionEnvironment;
	private List<String> serviceDependencies;
	private List<String> engine;
	private String sourceCode;
	
	public ImplementationInfo(){
		
	}

	public String getExecutionEnvironment() {
		return executionEnvironment;
	}

	public void setExecutionEnvironment(String executionEnvironment) {
		this.executionEnvironment = executionEnvironment;
	}

	public List<String> getServiceDependencies() {
		return serviceDependencies;
	}

	public void setServiceDependencies(List<String> serviceDependencies) {
		this.serviceDependencies = serviceDependencies;
	}

	public List<String> getEngine() {
		return engine;
	}

	public void setEngine(List<String> engine) {
		this.engine = engine;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}
	
	

}
