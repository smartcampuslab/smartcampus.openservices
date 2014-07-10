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
 * Configuration class for OAuth.
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
	 * New instance of {@link OAuthServiceConfig}.
	 */
	public OAuthServiceConfig(){
		
	}
	/**
	 * New instance of {@link OAuthServiceConfig} with parameters for configuration file.
	 * 
	 * @param apiKey 
	 * 				: String
	 * @param apiSecret 
	 * 				: String
	 * @param callback 
	 * 				: String, callback url
	 * @param apiClass 
	 * 				: Class<? extends Api>
	 */
	public OAuthServiceConfig(String apiKey, String apiSecret, String callback, Class<? extends Api> apiClass){
		super();
		this.apiKey=apiKey;
		this.apiSecret=apiSecret;
		this.callback=callback;
		this.apiClass=apiClass;
	}
	/**
	 * Get api key.
	 * 
	 * @return String apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}
	/**
	 * Set api key.
	 * 
	 * @param apiKey 
	 * 			: String
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	/**
	 * Get api secret.
	 * 
	 * @return String apiSecret
	 */
	public String getApiSecret() {
		return apiSecret;
	}
	/**
	 * Set api secret.
	 * 
	 * @param apiSecret 
	 * 			: String
	 */
	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}
	/**
	 * Get callback url.
	 * 
	 * @return String callback uri
	 */
	public String getCallback() {
		return callback;
	}
	/**
	 * Set callback url.
	 * 
	 * @param callback 
	 * 			: String
	 */
	public void setCallback(String callback) {
		this.callback = callback;
	}
	/**
	 * Get api class.
	 * 
	 * @return Class<? extends Api> apiClass
	 */
	public Class<? extends Api> getApiClass() {
		return apiClass;
	}
	/**
	 * Set api class.
	 * 
	 * @param apiClass 
	 * 			: Class<? extends Api>
	 */
	public void setApiClass(Class<? extends Api> apiClass) {
		this.apiClass = apiClass;
	}

	
}
