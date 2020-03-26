package com.active4j.entity.func.pay.model;

import lombok.Data;

@Data
public class PayStatusModel {
	
	//0:成功    1：支付失败   2:支付中
	private String status;
	
	private String orderNo;
	
	private String orderName;
	
	private String orderTime;;
	
	private String money;
	
	private String errMsg;
}
