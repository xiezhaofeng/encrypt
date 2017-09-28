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

import com.encrypt.util.string.StringUtil;
/**
 * 
  * @Title: RSAEncrypt.java 
  * @Package com.encrypt.util 
  * @Description  RSA对称加密，SHA1WithRSA签名验证
  * @author  XZF
  * @date 2017年3月31日 上午11:35:51 
  * @version   
  *
  * @Copyrigth  版权所有 (C) 2017 
  *
 */
public class RSAEncrypt
{

	private static final int KEY_SIZE = 2048;

	private static final String TYPE = "RSA";

	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	 * 字节数据转字符串专用集合
	 */
	private static final String HEX_BYTE = "0123456789ABCDEF";

	/**
	 * 得到密钥字符串（经过base64编码）
	 * 
	 * @return
	 */
	public static String getKeyString(Key key) throws Exception
	{
		byte[] keyBytes = key.getEncoded();
		String s = Base64.encode(keyBytes);
		return s;
	}

	/**
	 * 随机生成密钥对 publicKey,privateKey
	 * 
	 * @throws Exception
	 */
	public static Keys genKeyPair() throws Exception
	{
		KeyPairGenerator keyPairGen = null;
		try
		{
			keyPairGen = KeyPairGenerator.getInstance(TYPE);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		keyPairGen.initialize(1024, new SecureRandom("1111111111111".getBytes()));
		KeyPair keyPair = keyPairGen.generateKeyPair();
		return new Keys(getKeyString(keyPair.getPublic()), getKeyString(keyPair.getPrivate()));
	}

	/**
	 * 随机生成密钥对 publicKey,privateKey
	 * 
	 * @throws Exception
	 */
	public static Keys genKeyPair(String rule) throws Exception
	{
		KeyPairGenerator keyPairGen = null;
		try
		{
			keyPairGen = KeyPairGenerator.getInstance(TYPE);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		keyPairGen.initialize(KEY_SIZE, new SecureRandom(rule.getBytes()));
		KeyPair keyPair = keyPairGen.generateKeyPair();
		return new Keys(getKeyString(keyPair.getPublic()), getKeyString(keyPair.getPrivate()));
	}

	/**
	 * 从文件中输入流中加载公钥
	 * 
	 * @param in 公钥输入流
	 * @throws Exception 加载公钥时产生的异常
	 */
	public static PublicKey getPublicKey(InputStream in) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null)
		{
			if (readLine.charAt(0) == '-')
			{
				continue;
			}
			else
			{
				sb.append(readLine);
				sb.append('\r');
			}
		}
		return getPublicKey(sb.toString());
	}

	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr 公钥数据字符串
	 * @throws Exception 加载公钥时产生的异常
	 */
	public static PublicKey getPublicKey(String publicKeyStr) throws Exception
	{
		byte[] buffer = Base64.decode(publicKeyStr);
		KeyFactory keyFactory = KeyFactory.getInstance(TYPE);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	/**
	 * 从文件中加载私钥
	 * 
	 * @param keyFileName 私钥文件名
	 * @return 是否成功
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey(InputStream in) throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String readLine = null;
		StringBuilder sb = new StringBuilder();
		while ((readLine = br.readLine()) != null)
		{
			if (readLine.charAt(0) == '-')
			{
				continue;
			}
			else
			{
				sb.append(readLine);
				sb.append('\r');
			}
		}
		return getPrivateKey(sb.toString());
	}

	public static PrivateKey getPrivateKey(String privateKeyStr) throws Exception
	{
		byte[] buffer = Base64.decode(privateKeyStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
		KeyFactory keyFactory = KeyFactory.getInstance(TYPE);
		PrivateKey key = keyFactory.generatePrivate(keySpec);
		return key;
	}

	/**
	 * 加密过程
	 * 
	 * @param publicKey　 公钥
	 * @param plainTextData　 明文数据
	 * @return
	 * @throws Exception　 加密过程中的异常信息
	 */
	public static String encrypt(PublicKey publicKey, String textData) throws Exception
	{
		if (publicKey == null) { throw new Exception("加密公钥为空, 请设置"); }
		Cipher cipher = Cipher.getInstance(TYPE, new BouncyCastleProvider());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] output = cipher.doFinal(textData.getBytes());
		return byteArrayToString(output);
	}

	/**
	 * 解密过程
	 * 
	 * @param privateKey　 私钥
	 * @param cipherData 密文数据
	 * @return 明文
	 * @throws Exception 解密过程中的异常信息
	 */
	public static String decrypt(PrivateKey privateKey, byte[] data) throws Exception
	{
		if (privateKey == null) { throw new Exception("解密私钥为空, 请设置"); }
		Cipher cipher = Cipher.getInstance(TYPE, new BouncyCastleProvider());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] output = cipher.doFinal(data);
		return new String(output);
	}

	/**
	 * 解密过程
	 * 
	 * @param privateKey 私钥
	 * @param cipherData 密文数据
	 * @return 明文
	 * @throws Exception 解密过程中的异常信息
	 */
	public static String decrypt(PrivateKey privateKey, String data) throws Exception
	{
		if (StringUtil.isEmpty(data)) { throw new Exception("解密字符串为空"); }
		return decrypt(privateKey, hexStringToByte(data));
	}

	/**
	 * 字节数据转十六进制字符串
	 * 
	 * @param data 输入数据
	 * @return 十六进制内容
	 */
	public static String byteArrayToString(byte[] data)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < data.length; i++)
		{
			// 取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移

			stringBuilder.append(HEX_BYTE.charAt((data[i] & 0xf0) >>> 4));
			// 取出字节的低四位 作为索引得到相应的十六进制标识符
			stringBuilder.append(HEX_BYTE.charAt(data[i] & 0x0f));
		}
		return stringBuilder.toString();
	}

	/**
	 * 把16进制字符串转换成字节数组
	 * 
	 * @param hexString
	 * @return byte[]
	 */
	public static byte[] hexStringToByte(String hex)
	{
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++)
		{
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static int toByte(char c)
	{
		return (byte) HEX_BYTE.indexOf(c);
	}

	/**
	 * RSA签名
	 * 
	 * @param content 待签名数据
	 * @param privateKey 商户私钥
	 * @param input_charset 编码格式
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey, String input_charset)
	{
		try
		{
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initSign(priKey);
			signature.update(content.getBytes(input_charset));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * RSA验签名检查
	 * 
	 * @param content 待签名数据
	 * @param sign 签名值
	 * @param ali_public_key 支付宝公钥
	 * @param input_charset 编码格式
	 * @return 布尔值
	 */
	public static boolean verify(String content, String sign, String ali_public_key, String input_charset)
	{
		try
		{
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			byte[] encodedKey = Base64.decode(ali_public_key);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

			java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes(input_charset));

			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return false;
	}

	public static void main(String[] args) throws Exception
	{

		Keys keys = genKeyPair();

		// 测试字符串
		String encryptStr = "Test String chaijun";
		
		System.out.println(keys.getPrivateKey());

		try
		{
			// 加密
			String byteArrayToString = RSAEncrypt.encrypt(getPublicKey(keys.getPublicKey()), encryptStr);
			System.out.println("明文长度:" + byteArrayToString.length());
			System.out.println(byteArrayToString);
			System.out.println("################################");
			// 解密

			String plainText = RSAEncrypt.decrypt(getPrivateKey(keys.getPrivateKey()), byteArrayToString);
			System.out.println("明文长度:" + plainText.length());

			System.out.println(new String(plainText));

		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}
	}
}

// compile('org.springframework.cloud:spring-cloud-netflix:1.2.6.RELEASE')
// compile('org.springframework.cloud:spring-cloud-starter-eureka:1.2.6.RELEASE')
// compile('org.springframework.cloud:spring-cloud-starter-eureka-server:1.2.6.RELEASE')
//

class Keys
{

	private String publicKey;

	private String privateKey;

	public Keys(String publicKey, String privateKey)
	{
		super();
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public String getPublicKey()
	{
		return publicKey;
	}

	public void setPublicKey(String publicKey)
	{
		this.publicKey = publicKey;
	}

	public String getPrivateKey()
	{
		return privateKey;
	}

	public void setPrivateKey(String privateKey)
	{
		this.privateKey = privateKey;
	}

}
