package com.encrypt.util.okhttp;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.Interceptor;
import okhttp3.Request.Builder;
import okhttp3.Response;
/**
 * 
  * @Title: OkHttpHeaderInterceptor.java 
  * @Package com.xunxintech.ruyue.coach.base.common 
  * @Description 动态配置header
  * @author  XZF
  * @date 2017年3月3日 上午11:39:59 
  * @version   
  *
  * @Copyrigth  版权所有 (C) 2016
  *
 */
public class OkHttpHeaderInterceptor implements Interceptor{
	
	private static Logger LOG = LoggerFactory.getLogger(OkHttpHeaderInterceptor.class);

	private Map<String, String>  headers;
	
	public OkHttpHeaderInterceptor(Map<String, String> headers){
		this.headers = headers;
	}
	
	@Override
	public Response intercept(Chain chain) throws IOException {
		Builder newBuilder = chain.request().newBuilder();
		Set<Entry<String, String>> entrySet = this.headers.entrySet();
		for (Entry<String, String> entry : entrySet) {
			LOG.debug("okhttp add header {}:{}",entry.getKey(),entry.getValue());
			newBuilder.addHeader(entry.getKey(), entry.getValue());
		}
		return chain.proceed(newBuilder.build());
	}

}
