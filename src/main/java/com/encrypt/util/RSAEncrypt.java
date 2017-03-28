package com.encrypt.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RSAEncrypt {  
    
    private static final String TYPE = "RSA";

	private static final String HEX_BYTE = "0123456789ABCDEF";

    /** 
     * 字节数据转字符串专用集合 
     */  
    private static final char[] HEX_CHAR=  {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};  
//    private static final char[] HEX_CHAR=  {0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30};  
      
    /**
     * 得到密钥字符串（经过base64编码）
     * @return
     */
    public static String getKeyString(Key key) throws Exception {
          byte[] keyBytes = key.getEncoded();
          String s = Base64.encode(keyBytes);
          return s;
    }
    
    /** 
     * 随机生成密钥对 publicKey,privateKey
     * @throws Exception 
     */  
    public static Keys genKeyPair() throws Exception{  
        KeyPairGenerator keyPairGen= null;  
        try {  
            keyPairGen= KeyPairGenerator.getInstance(TYPE);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        keyPairGen.initialize(1024, new SecureRandom());  
        KeyPair keyPair= keyPairGen.generateKeyPair();  
        return new Keys(getKeyString(keyPair.getPublic()), getKeyString(keyPair.getPrivate()));
    }  
  
    /** 
     * 从文件中输入流中加载公钥 
     * @param in 公钥输入流 
     * @throws Exception 加载公钥时产生的异常 
     */  
	public static PublicKey getPublicKey(InputStream in) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null) {
			if (readLine.charAt(0) == '-') {
				continue;
			} else {
				sb.append(readLine);
				sb.append('\r');
			}
		}
		return getPublicKey(sb.toString());
	}
  
  
    /** 
     * 从字符串中加载公钥 
     * @param publicKeyStr 公钥数据字符串 
     * @throws Exception 加载公钥时产生的异常 
     */  
	public static PublicKey getPublicKey(String publicKeyStr) throws Exception {
		byte[] buffer = Base64.decode(publicKeyStr);
		KeyFactory keyFactory = KeyFactory.getInstance(TYPE);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	} 
  
    /** 
     * 从文件中加载私钥 
     * @param keyFileName 私钥文件名 
     * @return 是否成功 
     * @throws Exception  
     */  
	public static PrivateKey getPrivateKey(InputStream in) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null) {
			if (readLine.charAt(0) == '-') {
				continue;
			} else {
				sb.append(readLine);
				sb.append('\r');
			}
		}
		return getPrivateKey(sb.toString());
	}
  
	public static PrivateKey getPrivateKey(String privateKeyStr) throws Exception {
		byte[] buffer = Base64.decode(privateKeyStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
		KeyFactory keyFactory = KeyFactory.getInstance(TYPE);
		PrivateKey key =  keyFactory.generatePrivate(keySpec);
		return key;
	}
  
    /** 
     * 加密过程 
     * @param publicKey 公钥 
     * @param plainTextData 明文数据 
     * @return 
     * @throws Exception 加密过程中的异常信息 
     */  
	public static byte[] encrypt(PublicKey publicKey, String textData) throws Exception {
		if (publicKey == null) {
			throw new Exception("加密公钥为空, 请设置");
		}
		Cipher cipher = Cipher.getInstance(TYPE, new BouncyCastleProvider());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] output = cipher.doFinal(textData.getBytes());
		return output;
	}

    /** 
     * 解密过程 
     * @param privateKey 私钥 
     * @param cipherData 密文数据 
     * @return 明文 
     * @throws Exception 解密过程中的异常信息 
     */  
	public static byte[] decrypt(PrivateKey privateKey, byte[] cipherData) throws Exception {
		if (privateKey == null) {
			throw new Exception("解密私钥为空, 请设置");
		}
		Cipher cipher = Cipher.getInstance(TYPE, new BouncyCastleProvider());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] output = cipher.doFinal(cipherData);
		return output;
	}
  
      
    /** 
     * 字节数据转十六进制字符串 
     * @param data 输入数据 
     * @return 十六进制内容 
     */  
    public static String byteArrayToString(byte[] data){  
        StringBuilder stringBuilder= new StringBuilder();  
        for (int i=0; i<data.length; i++){  
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移  
            stringBuilder.append(HEX_CHAR[(data[i] & 0xf0)>>> 4]);  
            //取出字节的低四位 作为索引得到相应的十六进制标识符  
            stringBuilder.append(HEX_CHAR[(data[i] & 0x0f)]);  
        }  
        return stringBuilder.toString();  
    }  
  
    /**
     * 把16进制字符串转换成字节数组
     * @param hexString
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
     int len = (hex.length() / 2);
     byte[] result = new byte[len];
     char[] achar = hex.toCharArray();
     for (int i = 0; i < len; i++) {
      int pos = i * 2;
      result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
     }
     return result;
    }
    private static int toByte(char c) {
        byte b = (byte) HEX_BYTE.indexOf(c);
        return b;
     }
    public static void main(String[] args) throws Exception{  
        //rsaEncrypt.genKeyPair();  

//		String priveteKeyStr = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKTFVbo4VhAtBwR3UHhxgRb/xcWcDAzeF0lEDQX4bYL7O8dk4sKd2NFhZ7yViA2zhfMd6jKcUOIoUOBLBXNjXRAXh+n2qCaTNchPW1X3bh1+hYadOuHIDemfWwDiHfFqIYnqYYUAPMO/QxOy/IZIS3v8RSoNqplwPpExHS1vQLHDAgMBAAECgYB+f2mDYADixk7e5OMItMtQZujk/xpZPqBOBUqiTMl2h36rZA6bLyfoB4nmxD4BltO9hDfqxwtptV3x9Gao4Z3tnS+MB3C+PY6G53rWiUnac3g2lgcDYKY1M8Hwwg6azR31SjUHvRSlgz1Hmil5UQizCf9PVlH/YeN/oVcihO+lQQJBAPpukzsbtkbe8X8h5DRP9fcijUKwPp9B/L/A1aDvhblc0xaNUB/S4FtMkymZbMu5XVkukNSr2sWrVkYX/pRoqxMCQQCobzA2CMgwBoG17kDN6aMMuxQQkDofmXkUcNyQGaWapEfoPZSNO3Y+Bbs+0kaqdwKbyk0y7GDiThs8UCvfdoSRAkAKWBTmxR9DUCQjfy/g5Uigm6HAFm2PwHcDKKDqU9pvLKFfdhjQJ7KysrgFaNvwkkiDzYOX9T1FGu2mWYlfce9XAkAIahWUfXfSvmB4gECIiOV2W1ozQaFFttsPpFvAbyeQxCg7d+gV7iSIEK08BDE3jt54ffDBXIGyjIhbyP2IGlmhAkEApih41JuHlkDNFXDy67F7ZnsWsiNredpSadqGVNVsjHxIv+VCg3wquq8MxxgUHHdO41ZpLyoojzKSE5eyN+Hh7g==";
//		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkxVW6OFYQLQcEd1B4cYEW/8XFnAwM3hdJRA0F+G2C+zvHZOLCndjRYWe8lYgNs4XzHeoynFDiKFDgSwVzY10QF4fp9qgmkzXIT1tV924dfoWGnTrhyA3pn1sA4h3xaiGJ6mGFADzDv0MTsvyGSEt7/EUqDaqZcD6RMR0tb0CxwwIDAQAB";
			
		Keys keys = genKeyPair();
  
        //测试字符串  
        String encryptStr= "Test String chaijunDfjf老师蹑中一地五笔土杜建毛笔阴阳两隔的老师的kun";  
  
        try {  
            //加密  
            byte[] cipher = RSAEncrypt.encrypt(getPublicKey(keys.getPublicKey()), encryptStr); 
            System.out.println("密文长度:"+ cipher.length);
            String byteArrayToString = RSAEncrypt.byteArrayToString(cipher);
			System.out.println(byteArrayToString);  
			System.out.println("################################");
            //解密  
           
			byte[] bytes = hexStringToByte(byteArrayToString);
			byte[] plainText = RSAEncrypt.decrypt(getPrivateKey(keys.getPrivateKey()), bytes);
            System.out.println("明文长度:"+ plainText.length);  
//            System.out.println(RSAEncrypt.byteArrayToString(plainText));  
            System.out.println(new String(plainText));  
        } catch (Exception e) {  
            System.err.println(e.getMessage());  
        }  
    }  
}  



//compile('org.springframework.cloud:spring-cloud-netflix:1.2.6.RELEASE')
//compile('org.springframework.cloud:spring-cloud-starter-eureka:1.2.6.RELEASE')
//compile('org.springframework.cloud:spring-cloud-starter-eureka-server:1.2.6.RELEASE')
//

class Keys{
	private String publicKey;
	private String privateKey;
	
	public Keys(String publicKey, String privateKey) {
		super();
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	
}

