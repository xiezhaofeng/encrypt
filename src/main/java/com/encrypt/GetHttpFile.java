package com.encrypt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import com.encrypt.util.okhttp.OkHttpUtil;
import com.encrypt.util.string.StringUtil;

//public final static String CORRECT_CHAR = "^[\u2E80-\uFE4Fa-zA-Z0-9]+";
//
//public final static String  ILLEGAL_CHARACTER = "^$|[^`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+";

public class GetHttpFile
{
	public void main(String[] args) throws ClientProtocolException, IOException
	{
		String baseUrl = "http://download.eclipse.org";
		String uri = "http://download.eclipse.org/buildship/updates/e46/releases/1.0/1.0.21.v20161010-1640/plugins/?d";
		OkHttpUtil.initOkHttp();
		String html = OkHttpUtil.get(uri);
		
		
		Pattern pattern = Pattern.compile("(<a href=')\\S+(.jar'>|.jar.pack.gz'>)");
		Matcher matcher = pattern.matcher(html);
		String plugins = "/plugins/",features = "/features/";
		String baseFile = "C:/Users/admin/Downloads/buildship";
		while (matcher.find())
		{
			String jarUrl = baseUrl +  matcher.group().replaceAll("^<a href='|'>$", "");
			String centerFile = null;;
			if(StringUtils.contains(jarUrl, plugins)){
				centerFile = plugins;
			}else if(StringUtils.contains(jarUrl, features)){
				centerFile = features;
			}
			if(StringUtil.isNotEmpty(centerFile)){
				InputStream asStream = Request.Get(jarUrl).addHeader("Connection", "close").execute().returnContent().asStream();
//				okhttp3.Request request = new okhttp3.Request.Builder().url(jarUrl).build();
//				InputStream asStream = OkHttpUtil.execute(request).body().byteStream();
				byte[] b = new byte[1024];
				int len = 0 ;
				File mkdir = new File(baseFile+centerFile);
				if(!mkdir.exists()){
					mkdir.mkdirs();
				}
				File file = new File(mkdir.getPath()+File.separator+jarUrl.split(centerFile)[1]);
				if(!file.exists()){
					file.createNewFile();
				}
				FileOutputStream fileOut = new FileOutputStream(file);
				
				while ((len = asStream.read(b)) > -1)
				{
					fileOut.write(b, 0, len);
					
				}
				asStream.close();
				fileOut.flush();
				fileOut.close();
				
			}
			
			
			
		}
//		System.out.println(html);
	}
}
