package org.eop.spring.resttemplate.http.invoker;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eop.spring.resttemplate.http.HttpParam;
import org.eop.spring.resttemplate.http.HttpResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;

/**
 * 使用RestTemplate发送http请求
 * @author lixinjie
 * @since 2017-12-19
 */
public class RestTemplateHttpComponentsHttpInvoker implements IHttpInvoker {

	private static final Logger log = LoggerFactory.getLogger(RestTemplateHttpComponentsHttpInvoker.class);
	
	private RestTemplate restTemplate;
	
	public RestTemplateHttpComponentsHttpInvoker() {
		initRestTemplate();
	}
	
	@Override
	public HttpResult invoke(HttpParam httpParam) {
		String uuid = UUID.randomUUID().toString().substring(24);
		try {
			log.info("({})HttpParam：{}", uuid, JSON.toJSONString(httpParam));
			HttpHeaders headers = new HttpHeaders();
			addHttpHeaders(headers, httpParam);
			HttpEntity<?> requestEntity = new HttpEntity<Object>(httpParam.getBody(), headers);
			long beginTime = System.currentTimeMillis();
			ResponseEntity<String> responseEntity = restTemplate.exchange(httpParam.getUrl(), HttpMethod.resolve(httpParam.getMethod().toUpperCase()), requestEntity, String.class, httpParam.getUriVars());
			long endTime = System.currentTimeMillis();
			HttpResult httpResult = buildHttpResult(responseEntity);
			log.info("({}-{})HttpResult：{}", uuid, (endTime - beginTime), JSON.toJSONString(httpResult));
			return httpResult;
		} catch (Exception e) {
			HttpResult httpResult = buildHttpResult(e);
			log.info("({})HttpResult：{}", uuid, JSON.toJSONString(httpResult));
			log.error("(" + uuid + ")RestTemplate Http调用发生异常", e);
			return httpResult;
		}
	}

	public void initRestTemplate() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(buildHttpClient());
		configHttpComponentsClientHttpRequestFactory(clientHttpRequestFactory);
		restTemplate = new RestTemplate(clientHttpRequestFactory);
	}
	
	private HttpResult buildHttpResult(Exception exception) {
		HttpResult httpResult = new HttpResult();
		httpResult.setStatusCode(500);
		httpResult.setReasonPhrase(exception.getMessage());
		return httpResult;
	}
	
	private HttpResult buildHttpResult(ResponseEntity<String> responseEntity) {
		HttpResult httpResult = new HttpResult();
		httpResult.setStatusCode(responseEntity.getStatusCode().value());
		httpResult.setReasonPhrase(responseEntity.getStatusCode().getReasonPhrase());
		addHttpHeaders(responseEntity.getHeaders(), httpResult);
		httpResult.setBody(responseEntity.getBody());
		return httpResult;
	}
	
	private void addHttpHeaders(HttpHeaders headers, HttpResult httpResult) {
		for (Map.Entry<String, String> header : headers.toSingleValueMap().entrySet()) {
			httpResult.addHttpHeader(header.getKey(), header.getValue());
		}
	}
	
	private void addHttpHeaders(HttpHeaders headers, HttpParam httpParam) {
		for (Map.Entry<String, Object> header : httpParam.getHttpHeaders().entrySet()) {
			headers.add(header.getKey(), header.getValue().toString());
		}
	}
	
	private CloseableHttpClient buildHttpClient() {
		return HttpClients.custom()
				.setMaxConnPerRoute(256)
				.setMaxConnTotal(2560)
				.evictExpiredConnections()
				.evictIdleConnections(1200L, TimeUnit.SECONDS)
				.build();
	}
	
	private void configHttpComponentsClientHttpRequestFactory(HttpComponentsClientHttpRequestFactory clientHttpRequestFactory) {
		clientHttpRequestFactory.setBufferRequestBody(false);
		clientHttpRequestFactory.setConnectionRequestTimeout(60 * 1000);
		clientHttpRequestFactory.setConnectTimeout(60 * 1000);
		clientHttpRequestFactory.setReadTimeout(120 * 1000);
	}
	
}
