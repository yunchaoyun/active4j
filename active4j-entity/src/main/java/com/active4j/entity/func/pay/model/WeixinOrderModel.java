package com.active4j.entity.func.pay.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @title WeixinOrderModel.java
 * @description 
			   微信订单 返回页面model
 * @time  2020年1月3日 下午5:42:20
 * @author mhm
 * @version 1.0
 */
@Setter
@Getter
public class WeixinOrderModel {
	
	private String orderNo;
	
	private String name;
	
	private String orderTime;
	
	private double money;
	
	private String codeUrl;

	private String company;
}
