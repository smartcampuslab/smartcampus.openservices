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
import java.util.List;

public class ImplementationInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private String executionEnvironment;
	private List<Integer> serviceDependencies;
	private String hosting;
	private String sourceCode;
	
	public ImplementationInfo(){
		
	}

	public String getExecutionEnvironment() {
		return executionEnvironment;
	}

	public void setExecutionEnvironment(String executionEnvironment) {
		this.executionEnvironment = executionEnvironment;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public List<Integer> getServiceDependencies() {
		return serviceDependencies;
	}

	public void setServiceDependencies(List<Integer> serviceDependencies) {
		this.serviceDependencies = serviceDependencies;
	}

	public String getHosting() {
		return hosting;
	}

	public void setHosting(String hosting) {
		this.hosting = hosting;
	}
}
