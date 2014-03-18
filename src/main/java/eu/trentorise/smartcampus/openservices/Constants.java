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

package eu.trentorise.smartcampus.openservices;

/**
 * Enumeration class for service state.
 * A service can be:
 * a published service, user wants everyone to see his/her service;
 * a deprecated service, a new version of the same service is created;
 * an unpublished service, user creates a new service but he/she does not want to share it.
 * 
 * @author raman
 *
 */
public class Constants {

	/**
	 * Enum object with three possible service state: PUBLISH, UNPUBLISH and DEPRECATE
	 */
	public enum SERVICE_STATE {PUBLISH, UNPUBLISH, DEPRECATE};
	/**
	 * Roles enum
	 */
	public enum ROLES {ROLE_ORGOWNER, ROLE_NORMAL, ROLE_SERVICEOWNER};
	/**
	 * Possible values for ORDER BY in hsql query
	 */
	public enum ORDER {id, name};
}
