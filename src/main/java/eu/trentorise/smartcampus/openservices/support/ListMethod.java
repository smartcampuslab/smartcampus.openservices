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
package eu.trentorise.smartcampus.openservices.support;

import java.util.*;

import eu.trentorise.smartcampus.openservices.entities.Method;

/**
 * Simple class for JSON conversion
 * List of Method data
 * @author Giulia Canobbio
 *
 */
public class ListMethod {
	
	private List<Method> methods;
	
	/**
	 * New {@ListMethod} instance
	 */
	public ListMethod(){
		
	}

	/**
	 * 
	 * @return List<Method> methods
	 */
	public List<Method> getMethods() {
		return methods;
	}

	/**
	 * 
	 * @param methods
	 */
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

}
