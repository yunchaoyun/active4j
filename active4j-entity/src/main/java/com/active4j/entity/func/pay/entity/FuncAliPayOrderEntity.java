package com.active4j.entity.func.pay.entity;


import java.util.Date;

import com.active4j.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;


/**
 * 支付宝支付 订单实体
 * @author teli_
 *
 */
@TableName("func_ali_pay_order")
@Getter
@Setter
public class FuncAliPayOrderEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3741270779407863521L;
	
	//网关返回码
	@TableField("CODE")
	private String code;
	
	//网关返回码描述
	@TableField("MSG")
	private String msg;
	
	//业务返回码
	@TableField("SUB_CODE")
	private String subCode;
	
	//业务返回码描述
	@TableField("SUB_MSG")
	private String subMsg;
	
	//支付宝订单号
	@TableField("TRADE_NO")
	private String tradeNo;
	
	//商户订单号
	@TableField("OUT_TRADE_NO")
	private String outTradeNo;
	
	//收款支付宝账号对应的支付宝唯一用户号。
	@TableField("SELLER_ID")
	private String sellerId;
	
	@TableField("BUYER_ID")
	private String buyerId;
	
	//商户原始订单号
	@TableField("MERCHANT_ORDER_NO")
	private String merchantOrderNo;
	
	//销售产品码，与支付宝签约的产品码名称。
	@TableField("PRODUCT_CODE")
	private String productCode = "FAST_INSTANT_TRADE_PAY";
	
	//订单总金额
	@TableField("TOTAL_AMOUNT")
	private double totalAmount;
	
	//订单标题
	@TableField("SUBJECT")
	private String subject;
	
	//订单描述
	@TableField("BODY")
	private String body;
	
	//订单包含的商品列表信息
	@TableField("GOODS_DETAIL")
	private String goodsDetail;
	
	//公用回传参数
	@TableField("PASSBACK_PARAMS")
	private String passbackParams;
	
	//状态   0:创建   1：提交失败   2：提交成功   3：支付成功   4：支付失败
	@TableField("STATUS")
	private String status;
	
	//关联客户
	@TableField("CUS_ID")
	private String cusId;
	
	//订单时间
	@TableField("ORDER_TIME")
	private Date orderTime;
	
	//支付完成时间
	@TableField("END_TIME")
	private Date endTime;
	
}
