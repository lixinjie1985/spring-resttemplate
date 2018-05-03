package org.eop.spring.resttemplate.http;

/**
 * @author lixinjie
 * @since 2017-12-20
 */
public enum BodyType {

	HTML("text/html"),
	XHTML("application/xhtml+xml"),
	PLAIN("text/plain"),
	JSON("application/json"),
	XML("application/xml"),
	FORM("application/x-www-form-urlencoded"),
	FILE("multipart/form-data");
	
	private String contentType;
	private String charset = "utf-8";
	
	private BodyType(String contentType) {
		this.contentType = contentType;
	}
	
	public BodyType charset(String charset) {
		this.charset = charset;
		return this;
	}
	
	public String contentType() {
		return contentType + ";charset=" + charset;
	}
	
	public static BodyType parse(String contentType) {
		if (contentType == null || contentType.isEmpty()) {
			return null;
		}
		int beginIndex = contentType.lastIndexOf("/") + 1;
		if (beginIndex < 1) {
			return null;
		}
		int endIndex = contentType.indexOf(";", beginIndex);
		if (endIndex < 0) {
			return null;
		}
		for (BodyType bodyType : values()) {
			if (bodyType.contentType.contains(contentType.substring(beginIndex, endIndex).trim())) {
				return bodyType;
			}
		}
		return null;
	}
}
