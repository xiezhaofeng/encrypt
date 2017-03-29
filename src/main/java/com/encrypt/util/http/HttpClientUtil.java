package com.encrypt.util.http;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
  * @Title: HttpClientUtil.java 
  * @Package com.xunxintech.ruyue.coach.io.network.httpclient 
  * @Description  
  * @author  XZF
  * @date 2017年3月7日 上午9:42:13 
  * @version   
  *
  * @Copyrigth  版权所有 (C) 2016 广州讯心信息科技有限公司.
  *
 */
public class HttpClientUtil {

	private static CloseableHttpClient httpSLLClient;

	private static RequestConfig requestConfig;
	
	private static PoolingHttpClientConnectionManager clientConnectionManager;

	static CloseableHttpClient httpClient;
	
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	public static void initHttpClient(int socketTimeout,int connectTimeout,int requestTimeout,Map<String, String> headers,int maxTotal,int maxPreRoute){
		requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(requestTimeout).build();
		clientConnectionManager = new PoolingHttpClientConnectionManager(20, TimeUnit.SECONDS);
		clientConnectionManager.setMaxTotal(maxTotal);
		clientConnectionManager.setDefaultMaxPerRoute(maxPreRoute);
		List<Header> defaultHeaders = new ArrayList<Header>();
		if(headers != null && !headers.isEmpty()){
			headers.entrySet().stream().forEach(h -> {defaultHeaders.add(new BasicHeader(h.getKey(), h.getValue()));});
		}
		httpClient = HttpClients.custom().setConnectionManager(clientConnectionManager).setDefaultHeaders(defaultHeaders).build();
		// 创建默认的httpsClient实例.
		SSLContext sslContext = SSLContexts.createSystemDefault();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
		httpSLLClient = HttpClients.custom().setConnectionManager(clientConnectionManager).setSSLSocketFactory(sslsf).build();
	}

	
	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl  地址
	 */
	public static String sendHttpPost(String httpUrl,ContentType contentType) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		return sendHttpPost(httpPost,contentType);
	}

	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl
	 *            地址
	 * @param params
	 *            参数(格式:key1=value1&key2=value2)
	 */
	public static String sendHttpPost(String httpUrl, String params,ContentType contentType) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		try {
			// 设置参数
			StringEntity stringEntity = new StringEntity(params, contentType.getCharset());
			stringEntity.setContentEncoding(contentType.getCharset().toString());
			stringEntity.setContentType(contentType.getMimeType());
			httpPost.setEntity(stringEntity);
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return sendHttpPost(httpPost,contentType);
	}

	/**
	 * 发送 post请求
	 * 
	 * @param httpUrl
	 *            地址
	 * @param maps
	 *            参数
	 */
	public static String sendHttpPost(String httpUrl, Map<String, String> maps,ContentType contentType) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		// 创建参数队列
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		for (String key : maps.keySet()) {
			nameValuePairs.add(new BasicNameValuePair(key, maps.get(key)));
		}
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return sendHttpPost(httpPost,contentType);
	}

	/**
	 * 发送 post请求（带文件）
	 * 
	 * @param httpUrl
	 *            地址
	 * @param maps
	 *            参数
	 * @param fileLists
	 *            附件
	 */
	public static String sendHttpPost(String httpUrl, Map<String, String> maps, List<File> fileLists,ContentType contentType) {
		HttpPost httpPost = new HttpPost(httpUrl);// 创建httpPost
		MultipartEntityBuilder meBuilder = MultipartEntityBuilder.create();
		for (String key : maps.keySet()) {
			meBuilder.addPart(key, new StringBody(maps.get(key), contentType));
		}
		for (File file : fileLists) {
			FileBody fileBody = new FileBody(file);
			meBuilder.addPart("files", fileBody);
		}
		HttpEntity reqEntity = meBuilder.build();
		httpPost.setEntity(reqEntity);
		return sendHttpPost(httpPost,contentType);
	}

	/**
	 * 发送Post请求
	 * 
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpPost(HttpPost httpPost,ContentType contentType) {
		CloseableHttpResponse response = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpPost.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, contentType.getCharset());
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error(e.toString());
			}
		}
		return responseContent;
	}

	/**
	 * 发送 get请求
	 * 
	 * @param httpUrl
	 */
	public static String sendHttpGet(String httpUrl) {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		return sendHttpGet(httpGet);
	}

	/**
	 * 发送 get请求Https
	 * 
	 * @param httpUrl
	 */
	public static String sendHttpsGet(String httpUrl) {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		return sendHttpsGet(httpGet);
	}

	/**
	 * 发送Get请求
	 * 
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpGet(HttpGet httpGet) {
		CloseableHttpResponse response = null;
		String responseContent = null;
		try {
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error(e.toString());
			}
		}
		return responseContent;
	}

	/**
	 * 发送Get请求Https
	 * 
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpsGet(HttpGet httpGet) {
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {

			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpSLLClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error(e.toString());
			}
		}
		return responseContent;
	}
}
