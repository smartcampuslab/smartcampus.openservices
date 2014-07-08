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
package eu.trentorise.smartcampus.openservices.scribe;

import org.scribe.builder.api.Api;

/**
 * Source code from http://java.dzone.com/articles/spring-mvc-and-scribe-simple
 * Configuration class for OAuth
 * Details of provider and other properties in spring configuration file.
 * 
 * @author Giulia Canobbio
 *
 */
public class OAuthServiceConfig {
	
	private String apiKey;
	private String apiSecret;
	private String callback;
	private Class<? extends Api> apiClass;
	
	/**
	 * Constructor
	 */
	public OAuthServiceConfig(){
		
	}
	/**
	 * Constructor with parameters for configuration file
	 * @param apiKey : String
	 * @param apiSecret : String
	 * @param callback : String
	 * @param apiClass : Class<? extends Api>
	 */
	public OAuthServiceConfig(String apiKey, String apiSecret, String callback, Class<? extends Api> apiClass){
		super();
		this.apiKey=apiKey;
		this.apiSecret=apiSecret;
		this.callback=callback;
		this.apiClass=apiClass;
	}
	/**
	 * 
	 * @return String apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}
	/**
	 * 
	 * @param apiKey : String
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	/**
	 * 
	 * @return String apiSecret
	 */
	public String getApiSecret() {
		return apiSecret;
	}
	/**
	 * 
	 * @param apiSecret : String
	 */
	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}
	/**
	 * 
	 * @return String callback uri
	 */
	public String getCallback() {
		return callback;
	}
	/**
	 * 
	 * @param callback : String
	 */
	public void setCallback(String callback) {
		this.callback = callback;
	}
	/**
	 * 
	 * @return Class<? extends Api> apiClass
	 */
	public Class<? extends Api> getApiClass() {
		return apiClass;
	}
	/**
	 * 
	 * @param apiClass : Class<? extends Api>
	 */
	public void setApiClass(Class<? extends Api> apiClass) {
		this.apiClass = apiClass;
	}

	
}
