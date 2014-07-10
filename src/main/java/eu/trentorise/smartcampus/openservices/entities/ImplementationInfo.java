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

/**
 * Blob object for Implementation Info in table Service:
 * executive environment,
 * service dependencies,
 * hosting,
 * source code.
 * 
 * @author Giulia Canobbio
 *
 */
public class ImplementationInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private String executionEnvironment;
	private List<Integer> serviceDependencies;
	private String hosting;
	private String sourceCode;
	
	/**
	 * New instance of {@link ImplementationInfo}.
	 */
	public ImplementationInfo(){
		
	}

	/**
	 * Get executive environment.
	 * 
	 * @return String executive environment
	 */
	public String getExecutionEnvironment() {
		return executionEnvironment;
	}

	/**
	 * Set executive environment.
	 * 
	 * @param executionEnvironment
	 * 			: String
	 */
	public void setExecutionEnvironment(String executionEnvironment) {
		this.executionEnvironment = executionEnvironment;
	}

	/**
	 * Get source code.
	 * 
	 * @return String source code
	 */
	public String getSourceCode() {
		return sourceCode;
	}

	/**
	 * Set source code.
	 * 
	 * @param sourceCode
	 * 			: String
	 */
	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	/**
	 * Get list of service dependencies.
	 * 
	 * @return List<Integer> service dependencies
	 */
	public List<Integer> getServiceDependencies() {
		return serviceDependencies;
	}

	/**
	 * Set service dependencies.
	 * 
	 * @param serviceDependencies
	 * 			: list of integer
	 */
	public void setServiceDependencies(List<Integer> serviceDependencies) {
		this.serviceDependencies = serviceDependencies;
	}

	/**
	 * Get hosting.
	 * 
	 * @return String hosting
	 */
	public String getHosting() {
		return hosting;
	}

	/**
	 * Set hosting.
	 * 
	 * @param hosting
	 * 			: String
	 */
	public void setHosting(String hosting) {
		this.hosting = hosting;
	}
}
