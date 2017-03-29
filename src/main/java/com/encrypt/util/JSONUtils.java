package com.encrypt.util;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.xebia.jacksonlombok.JacksonLombokAnnotationIntrospector;

/**
 * 
 * @author RiesenQiao
 * @version 2013-3-6
 */
public  class JSONUtils {
	public static ObjectMapper objectMapper;
	
	public static String toJackson(Object object) throws JsonProcessingException{
		return objectMapper.writeValueAsString(object);
	}
	
	/**
	 * remove blank
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
        if (str!=null) {
            Pattern p = Pattern.compile("\r|\n");
            Matcher m = p.matcher(str);
          return  m.replaceAll("");
        }
        return str;
	}
	
	
	static{
		System.out.println("init objectMapper...");
		objectMapper = new ObjectMapper();
		objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>(){
			@Override
			public void serialize(Object arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException, JsonProcessingException {
			arg1.writeString("");
			}
		});
		JSONUtils.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		JSONUtils.objectMapper.setAnnotationIntrospector(new MyJacksonLombokAnnotationIntrospector());
//		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}
	
}
