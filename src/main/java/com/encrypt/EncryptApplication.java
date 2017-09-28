package com.encrypt;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EncryptApplication {
	private static char[] commonDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
//		SpringApplication.run(EncryptApplication.class, args);
		
		HttpPost post = new HttpPost("http://wycapi.gzjt.gov.cn/api/app/common/binapi");
		//
//				String result = "{\"items\":[{\"companyId\":\"平台标识\",\"vehicleNum\":1,\"driverNum\":0.00,\"symbol\":\"123123123\","
//						+ "\"flag\":1,\"updateTime\":\"2018-12-23 17:30:00\"},"
//						+ "{\"companyId\":\"平台标识\",\"vehicleNum\":1.12,\"driverNum\":1.00,"
//						+ "\"flag\":1,\"updateTime\":\"2018-12-23 17:30:00\"},"
//						+ "{\"companyId\":\"平台标识\",\"vehicleNum\":100,\"driverNum\":100,"
//						+ "\"flag\":1,\"updateTime\":\"2018-12-23 17:30:00\"},"
//						+ "{\"companyId\":\"平台标识\",\"vehicleNum\":12.12,\"driverNum\":12.23,"
//						+ "\"flag\":\"1\",\"updateTime\":\"2018-12-23 17:30:00\"},"
//						+ "{\"companyId\":\"平台标识\",\"vehicleNum\":9.1,\"driverNum\":1.0,"
//						+ "\"flag\":\"1\",\"updateTime\":\"2018-12-23 17:30:00\"}"
//						+ "]}";
//				InputStream in = new ByteArrayInputStream(result.getBytes("UTF-8"));
//				String md5 = hash(in);
//				in.reset();
//				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//				post.addHeader("binfile-md5", md5);
//				post.addHeader("binfile-auth", "平台标识");//平台标识
//				post.addHeader("5 binfile-gzip", "false");
//				post.addHeader("binfile-reqlen", String.valueOf(in.available()));//请求长度
//				String filename = "RY_BASIC_PTYYGM_REQ_2017-01-16 16:37:22:348.json";
//				builder.addBinaryBody("binFile", in, ContentType.DEFAULT_BINARY, filename);// 文件
//				builder.addTextBody("filename", filename, ContentType.create(ContentType.DEFAULT_TEXT.getMimeType(), "UTF-8"));
//				post.setEntity(builder.build());
//				
//				int connTimeout = 10000;
//				int soTimeout = -1;
//				
//				HttpClientBuilder clientBuilder = HttpClientBuilder.create();
//				RequestConfig.Builder configBuilder = RequestConfig.custom();
//				if (connTimeout > 0) configBuilder.build();
//				if (soTimeout > 0) configBuilder.setSocketTimeout(soTimeout);
//				clientBuilder.setDefaultRequestConfig(configBuilder.build());
//				// 4.3以后会自动在interceptor中实现启用压缩和自动解压，所以不需要gzip的时候需要指定一下---binfile-gzip true/false
//				if (!false) clientBuilder.disableContentCompression();
////				clientBuilder.setProxy(proxy);----放开抓包
//				CloseableHttpClient client = clientBuilder.build();
//				HttpResponse response = client.execute(post);
//				System.out.println(response.toString());
//				System.out.println(response.getStatusLine().getStatusCode());
//				System.out.println("内容："+EntityUtils.toString(response.getEntity()));
				
				System.out.println(Request.Post(post.getURI()).bodyString("platform=RY&key=SLj16Qq7FK9nbyi4",ContentType.APPLICATION_FORM_URLENCODED).execute().returnContent().asString());
			}
			
			public static String hash(InputStream in)
			{
				try
				{
					byte[] buffer = new byte[1024];
					MessageDigest md5 = MessageDigest.getInstance("MD5");
					int numRead = 0;
					while ((numRead = in.read(buffer)) > 0)
					{
						md5.update(buffer, 0, numRead);
					}
					in.close();
					return toHexString(md5.digest());
				}
				catch (Exception e)
				{
					return "";
				}
			}
			
			private static String toHexString(byte[] b)
			{
				StringBuilder sb = new StringBuilder(b.length * 2);
				for (int i = 0; i < b.length; i++)
				{
					sb.append(commonDigit[(b[i] & 0xf0) >>> 4]);
					sb.append(commonDigit[b[i] & 0x0f]);
				}
				return sb.toString();
			}
}
