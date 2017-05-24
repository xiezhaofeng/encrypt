package com.encrypt.util.okhttp;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import okio.BufferedSink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.encrypt.util.JSONUtils;

/**
 * 
  * @Title: OkHttpUtil.java 
  * @Package com.encrypt.util.okhttp 
  * @Description   字符串工具类
  * @author  XZF
  * @date 2017年3月31日 上午11:39:24 
  * @version   
  *
  * @Copyrigth  版权所有 (C) 2017 .
  *
 */
public class OkHttpUtil {
	
	private static Logger LOG = LoggerFactory.getLogger(OkHttpUtil.class);

    private static OkHttpClient mOkHttpClient;

	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
	
	public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("text/json; charset=utf-8");
	
	public static final int DEFAULT_MAX_REQUESTS = 500;
	
	public static final int DEFAULT_MAX_REQUESTS_PER_HOST = 50;

	private static Map<String, String> okhttpHeaders = null;
	
	/**
	 * 
	 * @Title initOkHttp
	 * @Description inIt okHttp
	 * @param connectionTimeout
	 * @param readTimeout void   
	 * @throws
	 */
	public static void initOkHttp(){
		mOkHttpClient=loadOkHttp(30, 30,30,null, DEFAULT_MAX_REQUESTS, DEFAULT_MAX_REQUESTS_PER_HOST);
	}
	
	/**
	 * 
	 * @Title initOkHttp
	 * @Description inIt okHttp
	 * @param connectionTimeout.30
	 * @param readTimeout void   
	 * @throws
	 */
	public static void initOkHttp(long connectionTimeout, long readTimeout){
		mOkHttpClient=loadOkHttp(connectionTimeout, readTimeout,readTimeout,null, DEFAULT_MAX_REQUESTS, DEFAULT_MAX_REQUESTS_PER_HOST);
	}
	
	
	/**
	 * @param connectionTimeout
	 * @param readTimeout
	 * @param writeTimeout
	 * @param headers
	 */
	public static void initOkHttp(long connectionTimeout, long readTimeout,long writeTimeout, Map<String, String> headers){
		mOkHttpClient=loadOkHttp(connectionTimeout, readTimeout,writeTimeout,headers, DEFAULT_MAX_REQUESTS, DEFAULT_MAX_REQUESTS_PER_HOST);
	}
	
	
	/**
	 * @param connectionTimeout
	 * @param readTimeout
	 * @param writeTimeout
	 * @param headers
	 * @param maxRequests
	 * @param maxRequestsPerHost
	 */
	public static void initOkHttp(long connectionTimeout, long readTimeout,long writeTimeout,Map<String, String> headers, int maxRequests, int maxRequestsPerHost){
		mOkHttpClient=loadOkHttp(connectionTimeout, readTimeout,writeTimeout,headers, maxRequests, maxRequestsPerHost);
	}
	
	/**
	 * @param maxRequestsPerHost TODO
	 * @param maxRequests TODO
	 * 
	 * @Title loadOkHttp
	 * @Description load okHttp
	 * @param connectionTimeout
	 * @param readTimeout
	 * @param writeTimeout
	 * @param connectionClose
	 * @return OkHttpClient   
	 * @throws
	 */
	public static OkHttpClient loadOkHttp(long connectionTimeout, long readTimeout,long writeTimeout,Map<String, String> headers, int maxRequests, int maxRequestsPerHost){
		
		Builder builder = new OkHttpClient().newBuilder()
				.connectTimeout(connectionTimeout, TimeUnit.SECONDS)
				.writeTimeout(writeTimeout, TimeUnit.SECONDS)
		        .readTimeout(readTimeout, TimeUnit.SECONDS)
				.retryOnConnectionFailure(true);
		//debug add logger interceptor
		if(LOG.isDebugEnabled()){
			HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
			interceptor.setLevel(Level.BODY);
			builder.addInterceptor(interceptor);
		}
		// add http headers
//		if(headers != null && !headers.isEmpty()){
//			LOG.debug("init header interceptor...");
//			builder.addInterceptor(new OkHttpHeaderInterceptor(headers));
//		}
		okhttpHeaders = headers;
		OkHttpClient okHttpClient = builder.build();
		Dispatcher dispatcher = okHttpClient.dispatcher();
		dispatcher.setMaxRequests(maxRequests);
		dispatcher.setMaxRequestsPerHost(maxRequestsPerHost);
		return okHttpClient;
	}
	
	public static void getDispatcherInfo() {
		Dispatcher dispatcher = null;
		
		if(mOkHttpClient != null) {
			dispatcher = mOkHttpClient.dispatcher();
			LOG.info("MaxRequests {}, MaxRequestsPerHost {}", dispatcher.getMaxRequests(), dispatcher.getMaxRequestsPerHost());
			
			List<Call> queuedCalls = dispatcher.queuedCalls();
			int queuedCallsCount = dispatcher.queuedCallsCount();
			LOG.info("Queued Calls {}", queuedCallsCount);
			for(Call c : queuedCalls) {
				if(LOG.isDebugEnabled()){
					LOG.debug(c.toString());
				}
			}
			
			List<Call> runningCalls = dispatcher.runningCalls();
			int runningCallsCount = dispatcher.runningCallsCount();
			LOG.info("Running Calls {}", runningCallsCount);
			for(Call c: runningCalls){
				if(LOG.isDebugEnabled()){
					LOG.debug(c.toString());
				}
			}
		}
	}
	
	private OkHttpUtil(long connectionTimeout, long readTimeout){
		
	}
	
    /**
     * 同步调用。
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException{
        return mOkHttpClient.newCall(request).execute();
    }
    
    /**
     * 同步调用。
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request,long connectionTimeout, long readTimeout) throws IOException{
		OkHttpClient copy = mOkHttpClient.newBuilder().readTimeout(readTimeout, TimeUnit.MILLISECONDS)
				.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS).build();
		return copy.newCall(request).execute();
    }
    
    /**
     * 同步调用。
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request,long connectionTimeout, long readTimeout,long writeTimeout) throws IOException{
		OkHttpClient copy = mOkHttpClient.newBuilder().readTimeout(readTimeout, TimeUnit.MILLISECONDS)
				.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
				.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS).build();
		return copy.newCall(request).execute();
    }
    
    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback,long connectionTimeout, long readTimeout){
    	OkHttpClient copy = mOkHttpClient.newBuilder().readTimeout(readTimeout, TimeUnit.MILLISECONDS)
				.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS).build();
    	copy.newCall(request).enqueue(responseCallback);
    }
    
    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback,long connectionTimeout, long readTimeout,long writeTimeout){
    	OkHttpClient copy = mOkHttpClient.newBuilder().readTimeout(readTimeout, TimeUnit.MILLISECONDS)
				.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
				.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS).build();
    	copy.newCall(request).enqueue(responseCallback);
    }

    
    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback){
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }
    /**
     * 开启异步线程访问网络, 且不在意返回结果（实现空callback）
     * @param request
     */
    public static void enqueue(Request request){
        mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
			public void onFailure(Call call, IOException e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				// TODO Auto-generated method stub

			}
        });
    }
    
    /**
     * 根据url发出请求获取数据
     * @param url
     */
    public static String get(String url,long connectionTimeout, long readTimeout) throws IOException{
        Request request = getRequestBuilder().url(url).build();
        printHeaders(request);
        Response response = execute(request,connectionTimeout,readTimeout);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    
    /**
     * 根据url发出请求获取数据
     * @param url
     */
    public static String get(String url) throws IOException{
        Request request = getRequestBuilder().url(url).build();
        printHeaders(request);
        Response response = execute(request);
        if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    
    /**
     * 根据url和json格式参数发出请求获取数据
     * @param url
     * @param json
     */
	public static String post(String url, String json,long connectionTimeout, long readTimeout) throws IOException {
	    RequestBody body = new RequestBody() {

			@Override
			public void writeTo(BufferedSink arg0) throws IOException {
				arg0.writeUtf8(json);
			}

			@Override
			public MediaType contentType() {
				return MEDIA_TYPE_MARKDOWN;
			}
		};
	    Request request = getRequestBuilder()
	      .url(url)
	      .post(body)
	      .build();
	    printHeaders(request);
	    Response response = execute(request,connectionTimeout,readTimeout);
	    if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
	}
    
    /**
     * 根据url和json格式参数发出请求获取数据
     * @param url
     * @param json
     */
	public static String post(String url, String json) throws IOException {
	    RequestBody body = new RequestBody() {

			@Override
			public void writeTo(BufferedSink arg0) throws IOException {
				arg0.writeUtf8(json);
			}

			@Override
			public MediaType contentType() {
				return MEDIA_TYPE_MARKDOWN;
			}
		};
	    Request request = getRequestBuilder()
	      .url(url)
	      .post(body)
	      .build();
	    printHeaders(request);
	    Response response = execute(request);
	    if (response.isSuccessful()) {
            String responseUrl = response.body().string();
            return responseUrl;
        } else {
            throw new IOException("Unexpected code " + response);
        }
	}
	
	/**
	 * 
	 * @Title getRequestBuild
	 * @Description 获取自定义请求头的request
	 * @return okhttp3.Request.Builder   
	 * @throws
	 */
	public static okhttp3.Request.Builder getRequestBuilder() {
		Request.Builder builder = new Request.Builder();
		if(okhttpHeaders != null && !okhttpHeaders.isEmpty()){
			Set<Entry<String, String>> entrySet = okhttpHeaders.entrySet();
			for (Entry<String, String> entry : entrySet) {
				builder.addHeader(entry.getKey(), entry.getValue());
			}
		}
		return builder;
	}
	
	public static void postWithAsync(String url,String json,Callback responseCallback) throws Exception {
		 RequestBody body = new RequestBody() {

				@Override
				public void writeTo(BufferedSink arg0) throws IOException {
					arg0.writeUtf8(json);
				}

				@Override
				public MediaType contentType() {
					return MEDIA_TYPE_MARKDOWN;
				}
			};
		
		   Request request = getRequestBuilder()
				      .url(url)
				      .post(body)
				      .build();
		   printHeaders(request);
		OkHttpUtil.enqueue(request, responseCallback);
		
	}

    /**
     * 根据url和json对象发出请求获取数据
     * @param url
     * @param object
     */
	public static String post(String url, Object object) throws IOException {
		return post(url,JSONUtils.toJackson(object));
	}
	
	private static void printHeaders(Request request) {
		LOG.debug("headers: {}", request.headers());
		Headers headers = request.headers();
		   for (int i = 0; i < headers.size(); i++) {
			   LOG.debug(headers.name(i) + ": " + headers.value(i));
		   }
	}

}
