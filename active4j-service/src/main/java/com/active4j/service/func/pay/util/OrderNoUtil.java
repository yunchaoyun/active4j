package com.active4j.service.func.pay.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 
 * @title OrderNoUtil.java
 * @description 
			   订单号
 * @time  2020年1月3日 下午2:03:59
 * @author mhm
 * @version 1.0
 */
public class OrderNoUtil {
	
	/**
	 * 
	 * @description
	 *  	生成商品订单号
	 * @params
	 * @return String
	 * @author mhm
	 * @time 2020年1月3日 下午2:04:17
	 */
	public static String getOrderNo() {
		//构建预支付实体  生成商品订单号
		String outTradeNo = System.currentTimeMillis() + "" + RandomStringUtils.random(6, "123456789");
		return outTradeNo;
	}
	
}
