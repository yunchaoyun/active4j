package com.active4j.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {
	
	
	/**
	 * 判断输入的字符串是两位小数金额
	 * @param s
	 * @return
	 */
	public static boolean isDouble(String s) {
		Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式
        Matcher match = pattern.matcher(s);
		
		
		return match.matches();
	}
	
}
