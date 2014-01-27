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

/**
 * Response Entity Object for Controllers
 * This is object response of restful web services
 * 
 * @author Giulia Canobbio
 *
 */
public class ResponseObject {
	
	private Object data;
	private int status;
	private String error;
	
	/**
	 * New {@Response Object} instance
	 */
	public ResponseObject(){
		
	}

	/**
	 * 
	 * @return Object data of {@Response Object} instance
	 */
	public Object getData() {
		return data;
	}

	/**
	 * 
	 * @param data in {@Response Object} instance
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 
	 * @return in status of {@Response Object} instance
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status in {@Response Object} instance
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 
	 * @return String error of {@Response Object} instance
	 */
	public String getError() {
		return error;
	}

	/**
	 * 
	 * @param error in {@Response Object} instance
	 */
	public void setError(String error) {
		this.error = error;
	}

}
