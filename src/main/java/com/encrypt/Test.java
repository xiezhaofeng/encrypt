package com.encrypt;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.xml.sax.HandlerBase;


public class Test
{
	
	private static final String DEFAULT_ENCODING = "GBK";//编码  
	private static final int PROTECTED_LENGTH = 51200;// 输入流保护 50KB  
	
	public static void main(String[] args) throws Exception
	{
		InputStream content = Request.Get("http://szycwh.f3322.net:8008/group1/M00/00/3E/CgoKZFmuQ32AJpuRAAF-7V-GPxw618.jpg").execute().returnResponse().getEntity().getContent();

//		System.out.println(content.available());
//		byte[] bcache = new byte[content.available()];  
//	    int readSize;//每次读取的字节长度  
//	    StringBuffer buff = new StringBuffer();
//	    content.read(bcache);
//	    while ((readSize = content.read(bcache)) > -1)
//		{
//	    	if(readSize < 2048){
//	    		byte[] bcaches = new byte[readSize];
//	    		System.arraycopy(bcache, 0, bcaches, 0, readSize);
//	    		bcache = bcaches;
//	    	}
//	    	buff.append(new String(Base64.getEncoder().encode(bcache)));
//		}
//	    System.out.println(buff.toString());
	    
		
		
//	    OutputStream output = new FileOutputStream("D://t.jpg");
//	    byte[] bytes = Base64.getDecoder().decode(buff.toString().getBytes());
//	    output.write(bytes);
//	    output.flush();
//	    output.close();
	    
	    
		String buff = readInfoStream(content);
//		String buff = new String(Base64.getEncoder().encode(readInfoStream.getBytes()));
//		System.out.println(buff);
////		
//		
		
//		byte[] bcache = new byte[content.available()];
//		content.read(bcache);
//		content.close();
//		String buff = new String(Base64.getEncoder().encode(bcache));
//		System.out.println(buff.toString());
//		TTT.GenerateImage(buff.toString());
		FileOutputStream output = new FileOutputStream("D://t.jpg");
		output.write(Base64.getDecoder().decode(buff.toString()));
//		output.write(buff.getBytes());
		output.flush();
		output.close();
		
}
	
	public static String readInfoStream(InputStream input) throws Exception {  
	    if (input == null) {  
	        throw new Exception("输入流为null");  
	    }  
	    //字节数组  
	    byte[] bcache = new byte[2048];  
	    int readSize = 0;//每次读取的字节长度  
	    int totalSize = 0;//总字节长度  
	    ByteArrayOutputStream infoStream = new ByteArrayOutputStream();  
	    try {  
	        //一次性读取2048字节  
	        while ((readSize = input.read(bcache)) > 0) {  
	            totalSize += readSize;  
//	            if (totalSize > PROTECTED_LENGTH) {  
//	                throw new Exception("输入流超出50K大小限制");  
//	            }  
	            //将bcache中读取的input数据写入infoStream  
	            infoStream.write(bcache,0,readSize);  
	        }  
	    } catch (IOException e1) {  
	        throw new Exception("输入流读取异常");  
	    } finally {  
	        try {  
	            //输入流关闭  
	            input.close();  
	        } catch (IOException e) {  
	            throw new Exception("输入流关闭异常");  
	        }  
	    }  
//	    System.out.println(infoStream.toString().equals(new String(infoStream.toByteArray())));
//	    try {  
        return  new String(Base64.getEncoder().encode(infoStream.toByteArray()));
//	    } catch (UnsupportedEncodingException e) {  
//	        throw new Exception("输出异常");  
//	    }  
	}  
}
