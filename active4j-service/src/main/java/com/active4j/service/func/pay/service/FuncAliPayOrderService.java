package com.active4j.service.func.pay.service;

import com.active4j.entity.func.pay.entity.FuncAliPayOrderEntity;
import com.active4j.entity.func.pay.model.PayStatusModel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @title FuncAliPayOrderService.java
 * @description 
  			支付宝预下单列表
 * @time  2020年1月9日 下午4:47:03
 * @author mhm
 * @version 1.0
*/
public interface FuncAliPayOrderService extends IService<FuncAliPayOrderEntity>{

	/**
	 * 
	 * @description
	 *  	支付宝支付核心方法
	 * @params
	 *      cusId
	 *      outTradeNo
	 *      money
	 * @return String
	 * @author mhm
	 * @time 2020年1月9日 下午4:59:00
	 */
	public String doAliPay(String cusId, String outTradeNo, double money);
	
	
	/**
	 * 
	 * @description
	 *  	根据用户ID  订单号 查询支付宝订单状态
	 * @params
	 *      userId
	 *      outTradeNo
	 * @return PayStatusModel
	 * @author mhm
	 * @time 2020年1月9日 下午5:15:45
	 */
	public PayStatusModel getAliPayStatus(String userId, String outTradeNo);
	
	/**
	 * 
	 * @description
	 *  	根据订单号查找支付宝订单
	 * @params
	 *      outTradeNo
	 * @return FuncAliPayOrderEntity
	 * @author mhm
	 * @time 2020年1月9日 下午5:35:36
	 */
	public FuncAliPayOrderEntity finAliPayOrder(String outTradeNo);
	
}
