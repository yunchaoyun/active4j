package com.active4j.service.func.pay.service;

import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.func.pay.entity.FuncWeixinPayPreOrderEntity;
import com.active4j.entity.func.pay.model.PayStatusModel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 
 * @title FuncWeixinPayPreOrderService.java
 * @description 
			    微信预下单列表
 * @time  2020年1月3日 下午3:22:03
 * @author mhm
 * @version 1.0
 */
public interface FuncWeixinPayPreOrderService extends IService<FuncWeixinPayPreOrderEntity>{

	/**
	 * 
	 * @description
	 *  	微信支付核心方法
	 * @params
	 * 		cusId
	 *      outTradeNo 订单号
	 *      money      支付金额
	 *      ip
	 * @return ResultJson
	 * @author mhm
	 * @time 2020年1月3日 下午4:33:11
	 */
	public ResultJson doWeixinCharge(String cusId, String outTradeNo, double money, String ip, String userName);
	
	/**
	 * 
	 * @description
	 *  	 查询微信订单状态
	 * @params
	 *      userId
	 *      outTradeNo
	 * @return PayStatusModel
	 * @author mhm
	 * @time 2020年1月6日 下午5:16:47
	 */
	public PayStatusModel getWeixinPayStatus(String userId, String outTradeNo);
	
	/**
	 * 
	 * @description
	 *  	获取预下单实体
	 * @params
	 *      outTradeNo
	 * @return FuncWeixinPayPreOrderEntity
	 * @author mhm
	 * @time 2020年1月7日 上午10:48:58
	 */
	public FuncWeixinPayPreOrderEntity findOrder(String outTradeNo);

}
