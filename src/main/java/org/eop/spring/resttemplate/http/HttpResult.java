package org.eop.spring.resttemplate.http;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lixinjie
 * @since 2017-12-19
 */
@SuppressWarnings("serial")
public class HttpResult implements Serializable {

	private static transient final Logger log = LoggerFactory.getLogger(HttpResult.class);
	
	private int statusCode;
	private String reasonPhrase;
	private Map<String, Object> httpHeaders = new HashMap<>();
	private String body;
	private transient Object typedBody;
	
	public HttpResult() {
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public Map<String, Object> getHttpHeaders() {
		return httpHeaders;
	}

	public void setHttpHeaders(Map<String, Object> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public void addHttpHeader(String name, Object value) {
		httpHeaders.put(name, value);
	}
	
	public boolean isOK() {
		return statusCode == 200;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getTypedBody(BodyType reponseBodyType) {
		if (typedBody == null) {
			typedBody = getTypedBodyInternal(getReponseBodyType(reponseBodyType), body);
		}
		return (T)typedBody;
	}
	
	private BodyType getReponseBodyType(BodyType reponseBodyType) {
		if (reponseBodyType != null) {
			return reponseBodyType;
		}
		BodyType bodyType = BodyType.parse((String)httpHeaders.get("Content-Type"));
		if (bodyType != null) {
			return bodyType;
		}
		return BodyType.PLAIN;
	}
	
	private Object getTypedBodyInternal(BodyType reponseBodyType, String body) {
		switch (reponseBodyType) {
			case JSON:
				return getJsonBody(body);
			case XML:
				return getXmlBody(body);
			default:
				return getPlainBody(body);
		}
	}
	
	private String getPlainBody(String body) {
		return body;
	}
	
	private JSONObject getJsonBody(String body) {
		try {
			return JSON.parseObject(body);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("", e);
		}
		return null;
	}
	
	private Element getXmlBody(String body) {
		try {
			return DocumentHelper.parseText(body).getRootElement();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("", e);
		}
		return null;
	}
}
