package com.active4j.service.func.pay.service.impl;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.active4j.common.util.DateUtils;
import com.active4j.dao.func.pay.dao.FuncAliPayOrderDao;
import com.active4j.entity.func.pay.entity.FuncAliPayOrderEntity;
import com.active4j.entity.func.pay.model.AliPayOrderModel;
import com.active4j.entity.func.pay.model.PayConstrant;
import com.active4j.entity.func.pay.model.PayStatusModel;
import com.active4j.entity.func.pay.properties.AliPayProperties;
import com.active4j.service.func.pay.service.FuncAliPayOrderService;
import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @title FuncAliPayOrderServiceImpl.java
 * @description 
  			支付宝预下单列表
 * @time  2020年1月9日 下午4:54:03
 * @author mhm
 * @version 1.0
*/
@Service("funcAliPayOrderService")
@Transactional
@Slf4j
public class FuncAliPayOrderServiceImpl extends ServiceImpl<FuncAliPayOrderDao, FuncAliPayOrderEntity> implements FuncAliPayOrderService {

	public static final String req_pay_url = "https://openapi.alipay.com/gateway.do";
	
	@Autowired
	private AliPayProperties aliPayProperties;
	
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
	public String doAliPay(String cusId, String outTradeNo, double money) {
		AlipayClient alipayClient = new DefaultAlipayClient(req_pay_url, aliPayProperties.getAppId(), 
				aliPayProperties.getPrivateKey(), "json", aliPayProperties.getCharset(), aliPayProperties.getPublicKey(), aliPayProperties.getSignType());
		AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
		request.setReturnUrl(aliPayProperties.getReturnUrl());
		request.setNotifyUrl(aliPayProperties.getNotifyUrl());
		
		String body = "演示支付宝支付付款" + money + "元";
		
		// 构建支付实体
		AliPayOrderModel order = new AliPayOrderModel();
		order.setBody(body);
		order.setOut_trade_no(outTradeNo);
		order.setPassback_params("付款");
		order.setSubject(order.getBody());
		order.setTotal_amount(String.valueOf(money));
		order.setDisable_pay_channels("credit_group");
		request.setBizContent(JSON.toJSONString(order));
		
		String form = "";
		try {
		
			AlipayTradePagePayResponse resp = alipayClient.pageExecute(request);
			form = resp.getBody(); //调用SDK生成表单
			log.info("订单号：{},支付金额:{},下单返回结果为:code:{}, msg:{}, subcode:{}, submsg:{},issuccess:{}",outTradeNo, String.valueOf(money), resp.getCode(), resp.getMsg(), resp.getSubCode(), resp.getSubMsg(),resp.isSuccess());
			//保存支付订单
			FuncAliPayOrderEntity payOrder = createAliPayEntity(cusId, order, resp);
			this.save(payOrder);
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		
		return form;
	}
	
	/**
	 * 创建阿里支付的订单实体
	 * @param order
	 * @param resp
	 * @return
	 */
	private FuncAliPayOrderEntity createAliPayEntity(String cusId, AliPayOrderModel order, AlipayTradePagePayResponse resp) {
		FuncAliPayOrderEntity payOrder = new FuncAliPayOrderEntity();
		payOrder.setBody(order.getBody());
		payOrder.setSubject(order.getSubject());
		payOrder.setCode(resp.getCode());
		payOrder.setGoodsDetail(order.getGoods_detail());
		payOrder.setMerchantOrderNo(resp.getMerchantOrderNo());
		payOrder.setMsg(resp.getMsg());
		payOrder.setOutTradeNo(order.getOut_trade_no());
		payOrder.setPassbackParams(order.getPassback_params());
		payOrder.setProductCode(order.getProduct_code());
		payOrder.setSellerId(resp.getSellerId());
		payOrder.setSubCode(resp.getSubCode());
		payOrder.setSubMsg(resp.getSubMsg());
		payOrder.setTotalAmount(Double.valueOf(order.getTotal_amount()));
		payOrder.setTradeNo(resp.getTradeNo());
		payOrder.setOrderTime(DateUtils.getNow());
		payOrder.setCusId(cusId);
		if(resp.isSuccess()) {
			payOrder.setStatus("2"); //提交成功
		}else {
			payOrder.setStatus("1"); //提交失败
		}
		
		return payOrder;
	}
	

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
	public PayStatusModel getAliPayStatus(String userId, String outTradeNo) {
		
		//查询数据库订单状态
		QueryWrapper<FuncAliPayOrderEntity> queryWrapper = new QueryWrapper<FuncAliPayOrderEntity>();
		queryWrapper.eq("CUS_ID", userId);
		queryWrapper.eq("OUT_TRADE_NO", outTradeNo);
		List<FuncAliPayOrderEntity> lstPreOrders = this.list(queryWrapper);
		if(null != lstPreOrders && lstPreOrders.size() > 0) {
			FuncAliPayOrderEntity preOrder = lstPreOrders.get(0);
			
			PayStatusModel payStatus = new PayStatusModel();
			payStatus.setMoney(String.valueOf(preOrder.getTotalAmount()));
			payStatus.setOrderName(preOrder.getSubject());
			payStatus.setOrderNo(preOrder.getOutTradeNo());
			payStatus.setOrderTime(DateUtils.getDate2Str(preOrder.getCreateDate(), DateUtils.pattern_yyyy_MM_dd));
			
			if(StringUtils.equals(preOrder.getStatus(), PayConstrant.pre_order_status_pay_fail) 
					|| StringUtils.equals(preOrder.getStatus(), PayConstrant.pre_order_status_submit_fail)) {
				//提交失败   支付失败的处理
				payStatus.setStatus(PayConstrant.weixin_pay_status_fail);
				payStatus.setErrMsg(preOrder.getMsg());
				
			}else if(StringUtils.equals(preOrder.getStatus(), PayConstrant.pre_order_status_created)) {
				//创建订单
				payStatus.setStatus(PayConstrant.weixin_pay_status_paying);
				
			}else if(StringUtils.equals(preOrder.getStatus(), PayConstrant.pre_order_status_pay_success)) {
				//订单支付成功
				payStatus.setStatus(PayConstrant.weixin_pay_status_success);
				
			}else if(StringUtils.equals(preOrder.getStatus(), PayConstrant.pre_order_status_submit_success)) {
				//订单提交成功
				payStatus.setStatus(PayConstrant.weixin_pay_status_paying);
			}
			return payStatus;
		}
		
		return null;
	}

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
	public FuncAliPayOrderEntity finAliPayOrder(String outTradeNo) {
		
		QueryWrapper<FuncAliPayOrderEntity> queryWrapper = new QueryWrapper<FuncAliPayOrderEntity>();
		queryWrapper.eq("OUT_TRADE_NO", outTradeNo);
		List<FuncAliPayOrderEntity> lstOrders = this.list(queryWrapper);
		if(null != lstOrders && lstOrders.size() > 0) {
			return lstOrders.get(0);
		}
		
		return null;
	}

}
