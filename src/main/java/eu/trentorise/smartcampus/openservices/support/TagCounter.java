/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package eu.trentorise.smartcampus.openservices.support;

/**
 * Class support for retrieving counter of tags.
 * 
 * @author Giulia Canobbio
 *
 */
public class TagCounter {
	
	private String tag;
	private int counter;
	
	/**
	 * Get counter.
	 * 
	 * @return total number for specific tag
	 */
	public int getCounter() {
		return counter;
	}
	
	/**
	 * Set counter.
	 * 
	 * @param counter 
	 * 			: int
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	/**
	 * Get tag name.
	 * 
	 * @return tag name : String
	 */
	public String getTag() {
		return tag;
	}
	
	/**
	 * Set tag name.
	 * 
	 * @param tag 
	 * 			: String
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

}
