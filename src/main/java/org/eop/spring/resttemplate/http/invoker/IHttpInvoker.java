package org.eop.spring.resttemplate.http.invoker;

import org.eop.spring.resttemplate.http.HttpParam;
import org.eop.spring.resttemplate.http.HttpResult;

/**
 * @author lixinjie
 * @since 2017-12-19
 */
public interface IHttpInvoker {

	HttpResult invoke(HttpParam httpParam);
}
