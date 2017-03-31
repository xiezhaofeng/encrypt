package com.encrypt.util.string;

import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 
  * @Title: StringUtil.java 
  * @Package com.encrypt.util.string 
  * @Description   字符串工具类
  * @author  XZF
  * @date 2017年3月31日 上午11:39:08 
  * @version   
  *
  * @Copyrigth  版权所有 (C) 2017.
  *
 */
public final class StringUtil
{

	private static final String DEFAULT_DOUBLE_VALUE = "0.00";

	private static final String IS_NUM_PATTERN = "^(-?\\d+)(\\.\\d+)?$";

	private static final String STR = "\r\n";

	private static final String VALIDE_MOBILE_PATTERN = "^[1-9]\\d{10}$";

	private static final String VALIDEP_NUMBER_PATTERN = "^[+-]?\\d+$";

	private static final String NULL_STRING = "";

	private static final String STRING = "-";

	/** Private Constructor **/
	private StringUtil()
	{
	}

	/** 数据格式 **/
	public interface DATA_PATTERN
	{

		String X_XX = "#0.00";
	}

	/**
	 * 
	 * @Title equals
	 * @Description
	 * @param str1
	 * @param str2
	 * @return boolean
	 * @throws
	 */
	public static boolean equals(String str1, String str2)
	{
		return str1 == null & str2 == null ? true : str1 == null ? false : str2 == null ? false : str1.equals(str2);
	}

	public static boolean equalsIgnoreCase(String str1, String str2)
	{
		return str1 == null & str2 == null ? true : str1 == null ? false : str2 == null ? false : str1.equalsIgnoreCase(str2);
	}

	public static boolean notEquals(String str1, String str2)
	{
		return !equals(str1, str2);
	}

	public static boolean notEqualsIgnoreCase(String str1, String str2)
	{
		return !equalsIgnoreCase(str1, str2);
	}

	public static void main(String[] args)
	{
		System.out.println(equals("f", "F", "f", "f"));
	}

	/**
	 * 
	 * @Title equals
	 * @Description 比对数组字符串相等
	 * @param strs
	 * @return boolean
	 * @throws
	 */
	public static boolean equals(String... strs)
	{
		if (strs == null) { return false; }
		if (strs.length < 2) { throw new RuntimeException("Parameters can not be less than 2"); }
		for (int i = 1; i < strs.length; i++)
		{
			if (!equals(strs[i - 1], strs[i])) { return false; }
		}
		return true;
	}

	/**
	 * 
	 * @Title equalsIgnoreCase
	 * @Description 比对数组字符串不相等
	 * @param strs
	 * @return boolean
	 * @throws
	 */
	public static boolean equalsIgnoreCase(String... strs)
	{
		if (strs == null) { return false; }
		if (strs.length < 2) { throw new RuntimeException("Parameters can not be less than 2"); }
		for (int i = 1; i < strs.length; i++)
		{
			if (!equalsIgnoreCase(strs[i - 1], strs[i])) { return false; }
		}
		return true;
	}

	/**
	 * 
	 * @Title equals
	 * @Description 比对数组字符串相等
	 * @param strs
	 * @return boolean
	 * @throws
	 */
	public static boolean notEquals(String... strs)
	{
		return !equals(strs);
	}

	/**
	 * 
	 * @Title equalsIgnoreCase
	 * @Description 比对数组字符串不相等
	 * @param strs
	 * @return boolean
	 * @throws
	 */
	public static boolean notEqualsIgnoreCase(String... strs)
	{
		return !equalsIgnoreCase(strs);
	}

	/**
	 * 判断字符串是否非null && 非空字符
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isNotEmpty(String param)
	{
		return !isEmpty(param);
	}

	/**
	 * 判断字符串是否非null && 非空字符
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isNotEmpty(String... params)
	{
		for (String param : params)
		{
			if (isEmpty(param)) { return false; }
		}
		return true;
	}

	/**
	 * 判断字符串是否非null && 非空字符
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isNotEmpty(List<String> params)
	{
		for (String param : params)
		{
			if (isEmpty(param)) { return false; }
		}
		return true;
	}

	/**
	 * 判断字符串是否为null || 空字符串
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isEmpty(String param)
	{
		return param == null || param.trim().length() == 0;
	}

	/**
	 * 判断字符串是否为null || 空字符串
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isEmpty(String... params)
	{
		for (String param : params)
		{
			if (!isEmpty(param)) { return false; }
		}
		return true;
	}

	/**
	 * 判断字符串是否为null || 空字符串
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isEmpty(List<String> params)
	{
		for (String param : params)
		{
			if (!isEmpty(param)) { return false; }
		}
		return true;
	}

	/**
	 * 转换字符串集合为String
	 * 
	 * @param list
	 * @return
	 * @throws
	 */
	public static String toString(List<String> list)
	{
		StringBuilder sbd = new StringBuilder();
		for (String lineDate : list)
		{
			sbd.append(lineDate).append(STR);
		}
		return sbd.toString();
	}

	/**
	 * 校验0.00金额格式
	 * 
	 * @param money
	 * @return
	 */
	public static boolean valideMoney(String money)
	{
		try
		{
			if (null == money || NULL_STRING.equals(money)) { return true; }
			DecimalFormat df = new DecimalFormat(DATA_PATTERN.X_XX);
			Number data = df.parse(money);
			return money.equals(df.format(data));
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * 验证数字字符串
	 * 
	 * @param numStr
	 * @return
	 */
	public static boolean valideNumberStr(String numStr)
	{
		if (null == numStr || NULL_STRING.equals(numStr)) { return true; }
		return valideStrPattern(numStr, VALIDEP_NUMBER_PATTERN);
	}

	/**
	 * 校验手机号码是否为11位数字
	 * 
	 * @param mobilePhone
	 * @return
	 */
	public static boolean valideMobilePhone(String mobilePhone)
	{
		return valideStrPattern(mobilePhone, VALIDE_MOBILE_PATTERN);
	}

	/**
	 * 根据正则表达式校验字符串
	 * 
	 * @param str
	 * @param pattern
	 * @return
	 */
	public static boolean valideStrPattern(String str, String pattern)
	{
		try
		{
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(str);
			return m.matches();
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * 获取GUID
	 * 
	 * @return
	 */
	public static String getGUID()
	{
		UUID uuid = UUID.randomUUID();
		return StringUtils.remove(uuid.toString(), STRING);
	}

	/**
	 * 转int类型
	 * 
	 * @param str
	 * @return
	 */
	public static int toInt(String str)
	{
		int res = 0;
		try
		{
			if (null != str && (!NULL_STRING.equals(str)))
			{
				res = Integer.parseInt(str);
			}
		}
		catch (Exception e)
		{
		}
		return res;
	}

	/**
	 * 转double类型
	 * 
	 * @param str
	 * @return
	 */
	public static double toDouble(String str)
	{
		double res = 0.00;
		try
		{
			if (null != str && (!NULL_STRING.equals(str)))
			{
				res = Double.parseDouble(str);
			}
		}
		catch (Exception e)
		{
		}
		return res;
	}

	/**
	 * double类型转换为0.00格式字符串
	 * 
	 * @param doub
	 * @return
	 */
	public static String toString(double doub)
	{
		String res = DEFAULT_DOUBLE_VALUE;
		try
		{
			DecimalFormat df = new DecimalFormat(DATA_PATTERN.X_XX);
			res = df.format(doub);
		}
		catch (Exception e)
		{
		}
		return res;
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param str
	 * @return True为数字
	 */
	public static boolean isNum(String str)
	{
		return matchRegex(str, IS_NUM_PATTERN);
	}

	private static boolean matchRegex(String value, String regex)
	{
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.matches();
	}
}
