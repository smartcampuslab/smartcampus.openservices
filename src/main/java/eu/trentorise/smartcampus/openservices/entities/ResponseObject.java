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
	private Long totalNumber;
	
	/**
	 * New {@link ResponseObject} instance.
	 */
	public ResponseObject(){
		
	}

	/**
	 * Get data.
	 * 
	 * @return Object data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * Set data.
	 * 
	 * @param data 
	 * 			: Object
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * Get status: 202 OK or status error.
	 * 
	 * @return int status 
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Set status: 200 OK otherwise status error.
	 * 
	 * @param status 
	 * 			: int
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * Get error: not null when status is different from 200.
	 * 
	 * @return String error
	 */
	public String getError() {
		return error;
	}

	/**
	 * Set error: only when status is different from 200.
	 * 
	 * @param error 
	 * 			: String
	 */
	public void setError(String error) {
		this.error = error;
	}
	
	/**
	 * Get total number.
	 * Total number is used for pagination when data is a list 
	 * and it contains the total number of element saved in db.
	 * 
	 * @return tota number : Long
	 */
	public Long getTotalNumber() {
		return totalNumber;
	}

	/**
	 * Set total number.
	 * Total number is used for pagination when data is a list 
	 * and it contains the total number of element saved in db.
	 * 
	 * @param totalNumber 
	 * 			: Long
	 */
	public void setTotalNumber(Long totalNumber) {
		this.totalNumber = totalNumber;
	}

}
