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

	private String sourceCode;
	
	/**
	 * New instance of {@link ImplementationInfo}.
	 */
	public ImplementationInfo(){
		
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

}
