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

package eu.trentorise.smartcampus.openservices.controllers.exec;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Abstract implementation of the {@link TestBoxAuthHandler} interface.
 * Provides helper methods for HTTP call executions
 * @author raman
 *
 */
public abstract class AbstractTestBoxAuthHandler implements TestBoxAuthHandler {
	/** Timeout (in ms) we specify for each http request */
    public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;
    /** Default charset */
	private static final String DEFAULT_CHARSET = "UTF-8";

	private static final ObjectMapper mapper = new ObjectMapper();
	
	protected TestResponse execute(TestBoxParams params) throws TestBoxException {
		TestResponse res = new TestResponse();
		if ("GET".equalsIgnoreCase(params.requestMethod)) {
			res.setData(getJSON(params.requestUrl, params.requestHeaders));
		}
		if ("POST".equalsIgnoreCase(params.requestMethod)) {
			res.setData(postJSON(params.requestUrl,params.requestBody, params.requestHeaders));
		}
		if ("PUT".equalsIgnoreCase(params.requestMethod)) {
			res.setData(putJSON(params.requestUrl,params.requestBody, params.requestHeaders));
		}
		if ("DELETE".equalsIgnoreCase(params.requestMethod)) {
			res.setData(deleteJSON(params.requestUrl, params.requestHeaders));
		}
		return res;
	}
	
	protected static final HttpClient getHttpClient() {
		HttpClient httpClient = new DefaultHttpClient();
		final HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		return httpClient;
	}

	protected static final String postJSON(String url, String body, Map<String, String> headers) throws TestBoxException {

		final HttpResponse resp;
		final HttpPost post = new HttpPost(url);

		for (String key : headers.keySet()) {
			post.setHeader(key, headers.get(key));
		}

		try {
			
			Map map = mapper.readValue(body, Map.class);
			StringEntity input = new StringEntity(mapper.writeValueAsString(map), DEFAULT_CHARSET);
			post.setEntity(input);

			resp = getHttpClient().execute(post);
			String response = EntityUtils.toString(resp.getEntity(),DEFAULT_CHARSET);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			} else {
				throw new TestBoxException(resp.getStatusLine().getStatusCode(), resp.getStatusLine().getReasonPhrase());
			}

		} catch (Exception e) {
			if (e instanceof TestBoxException) throw (TestBoxException)e;
			throw new TestBoxException(e.getMessage(), e);
		}
	}

	protected static final String getJSON(String url, Map<String, String> headers) throws TestBoxException {

		final HttpResponse resp;
		final HttpGet get = new HttpGet(url);

		if (headers != null) {
			for (String key : headers.keySet()) {
				get.setHeader(key, headers.get(key));
			}
		}
		try {
			resp = getHttpClient().execute(get);
			String response = EntityUtils.toString(resp.getEntity(),DEFAULT_CHARSET);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			} else {
				throw new TestBoxException(resp.getStatusLine().getStatusCode(), resp.getStatusLine().getReasonPhrase());
			}

		} catch (Exception e) {
			if (e instanceof TestBoxException) throw (TestBoxException)e;
			throw new TestBoxException(e.getMessage(), e);
		}
	}
	
	protected static final String putJSON(String url, String body, Map<String, String> headers) throws TestBoxException {

		final HttpResponse resp;
		final HttpPut put = new HttpPut(url);

		for (String key : headers.keySet()) {
			put.setHeader(key, headers.get(key));
		}

		try {
			StringEntity input = new StringEntity(body, DEFAULT_CHARSET);
			put.setEntity(input);

			resp = getHttpClient().execute(put);
			String response = EntityUtils.toString(resp.getEntity(),DEFAULT_CHARSET);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			} else {
				throw new TestBoxException(resp.getStatusLine().getStatusCode(), resp.getStatusLine().getReasonPhrase());
			}

		} catch (Exception e) {
			if (e instanceof TestBoxException) throw (TestBoxException)e;
			throw new TestBoxException(e.getMessage(), e);
		}
	}

	protected static final String deleteJSON(String url, Map<String, String> headers) throws TestBoxException {

		final HttpResponse resp;
		final HttpDelete delete = new HttpDelete(url);

		for (String key : headers.keySet()) {
			delete.setHeader(key, headers.get(key));
		}

		try {
			resp = getHttpClient().execute(delete);
			String response = EntityUtils.toString(resp.getEntity(),DEFAULT_CHARSET);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			} else {
				throw new TestBoxException(resp.getStatusLine().getStatusCode(), resp.getStatusLine().getReasonPhrase());
			}

		} catch (Exception e) {
			if (e instanceof TestBoxException) throw (TestBoxException)e;
			throw new TestBoxException(e.getMessage(), e);
		}
	}

}
