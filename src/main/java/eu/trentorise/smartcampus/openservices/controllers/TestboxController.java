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

package eu.trentorise.smartcampus.openservices.controllers;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author raman
 *
 */
@Controller
@RequestMapping(value="/api/testbox")
public class TestboxController {

	/** Timeout (in ms) we specify for each http request */
    public static final int HTTP_REQUEST_TIMEOUT_MS = 30 * 1000;
    /** Default charset */
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final String HEADER_TARGET_URL = "targeturl";

	@SuppressWarnings("unchecked")
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody String forwardGet(HttpServletRequest req) throws TestBoxException {
		String url = req.getHeader(HEADER_TARGET_URL);
		Map<String,String> headers = new HashMap<String, String>();
		Enumeration<String> names = req.getHeaderNames();
		while(names.hasMoreElements()) {
			String name = names.nextElement();
			if (HEADER_TARGET_URL.equals(name)) continue;
			headers.put(name, req.getHeader(name));
		}
		return getJSON(url, headers);
	}
	
	protected static HttpClient getHttpClient() {
		HttpClient httpClient = new DefaultHttpClient();
		final HttpParams params = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		HttpConnectionParams.setSoTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		ConnManagerParams.setTimeout(params, HTTP_REQUEST_TIMEOUT_MS);
		return httpClient;
	}

	public static String postJSON(String url, String body, Map<String, String> headers) throws TestBoxException {

		final HttpResponse resp;
		final HttpPost post = new HttpPost(url);

		for (String key : headers.keySet()) {
			post.setHeader(key, headers.get(key));
		}

		try {
			StringEntity input = new StringEntity(body, DEFAULT_CHARSET);
			post.setEntity(input);

			resp = getHttpClient().execute(post);
			String response = EntityUtils.toString(resp.getEntity(),DEFAULT_CHARSET);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			} else {
				throw new TestBoxException(resp.getStatusLine().getStatusCode());
			}

		} catch (Exception e) {
			throw new TestBoxException(e.getMessage(), e);
		}
	}

	public static String getJSON(String url, Map<String, String> headers) throws TestBoxException {

		final HttpResponse resp;
		final HttpGet get = new HttpGet(url);

		for (String key : headers.keySet()) {
			get.setHeader(key, headers.get(key));
		}

		try {
			resp = getHttpClient().execute(get);
			String response = EntityUtils.toString(resp.getEntity(),DEFAULT_CHARSET);
			if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				return response;
			} else {
				throw new TestBoxException(resp.getStatusLine().getStatusCode());
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new TestBoxException(e.getMessage(), e);
		}
	}

	public static class TestBoxException extends Exception {
		private static final long serialVersionUID = 1L;

		int status;

		public TestBoxException(int status) {
			super();
			this.status = status;
		}

		/**
		 * @param message
		 * @param e
		 */
		public TestBoxException(String message, Exception e) {
		}
	}
}