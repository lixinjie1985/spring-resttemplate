package org.eop.spring.resttemplate.http;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lixinjie
 * @since 2017-12-19
 */
@SuppressWarnings("serial")
public class HttpParam implements Serializable {

	private String url;
	private String method;
	private Map<String, Object> uriVars = new HashMap<>();
	private Map<String, Object> httpHeaders = new HashMap<>();
	private Object body;
	private transient BodyType requestBodyType;
	private transient BodyType responseBodyType;
	
	public HttpParam() {
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Map<String, Object> getUriVars() {
		return uriVars;
	}

	public void setUriVars(Map<String, Object> uriVars) {
		this.uriVars = uriVars;
	}

	public Map<String, Object> getHttpHeaders() {
		return httpHeaders;
	}

	public void setHttpHeaders(Map<String, Object> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public BodyType getRequestBodyType() {
		return requestBodyType;
	}

	public void setRequestBodyType(BodyType requestBodyType) {
		this.requestBodyType = requestBodyType;
		addHttpHeader("Content-Type", requestBodyType.contentType());
	}
	
	public BodyType getResponseBodyType() {
		return responseBodyType;
	}

	public void setResponseBodyType(BodyType responseBodyType) {
		this.responseBodyType = responseBodyType;
		addHttpHeader("Accept", responseBodyType.contentType());
	}

	public void addUriVar(String name, Object value) {
		uriVars.put(name, value);
	}
	
	public void addHttpHeader(String name, Object value) {
		httpHeaders.put(name, value);
	}
}
