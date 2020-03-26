package com.active4j.entity.func.pay.model;

/**
 * 
 * @title PayConstrant.java
 * @description 微信支付使用的常量
 * @time 2019年12月11日 上午10:39:05
 * @author mhm
 * @version 1.0
 */
public class PayConstrant {

	// 微信订单状态
	// 状态 0:创建 1：提交失败 2：提交成功 3：支付成功 4：支付失败
	public static final String pre_order_status_created = "0";
	public static final String pre_order_status_submit_fail = "1";
	public static final String pre_order_status_submit_success = "2";
	public static final String pre_order_status_pay_success = "3";
	public static final String pre_order_status_pay_fail = "4";

	// 微信支付状态 0： 成功 1：失败 2:支付中
	public static final String weixin_pay_status_success = "0";
	public static final String weixin_pay_status_fail = "1";
	public static final String weixin_pay_status_paying = "2";

}
