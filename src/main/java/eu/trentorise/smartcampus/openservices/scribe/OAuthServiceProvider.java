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

import org.scribe.builder.ServiceBuilder;
import org.scribe.oauth.OAuthService;

/**
 * Source code from http://java.dzone.com/articles/spring-mvc-and-scribe-simple
 * Class that create a connection with twitter or linkedin
 * @author smartcampus
 *
 */
public class OAuthServiceProvider {
	
	private OAuthServiceConfig config;
	
	/**
	 * Constructor
	 */
	public OAuthServiceProvider(){
		
	}
	/**
	 * Constructor with a parameter
	 * @param config : instance of {@link OAuthServiceConfig}
	 */
	public OAuthServiceProvider(OAuthServiceConfig config){
		this.config=config;
	}
	
	/**
	 * Method that builds a connection with provider
	 * @return instace of {@link OAuthService}
	 */
	public OAuthService getService(){
		System.out.println(".. OAuthServiceProvider: "+config+
		".. Api key: "+config.getApiKey()+
		".. Api secret: "+config.getApiSecret()+
		".. Callback uri: "+config.getCallback()
				);
		return new ServiceBuilder().provider(config.getApiClass())
				.apiKey(config.getApiKey())
				.apiSecret(config.getApiSecret())
				.callback(config.getCallback())
				.build();
	}
}
