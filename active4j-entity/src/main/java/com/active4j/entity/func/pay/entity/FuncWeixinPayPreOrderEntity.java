package com.active4j.entity.func.pay.entity;

import java.util.Date;

import com.active4j.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信支付预下单实体
 * 
 * @author teli_
 *
 */
@TableName("func_wx_pay_pre_order")
@Getter
@Setter
public class FuncWeixinPayPreOrderEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5544202081581886125L;

	//微信支付分配的公众账号ID
	@TableField("APP_ID")
	private String appId;

	//微信支付分配的商户号
	@TableField("MCH_ID")
	private String mchId;

	//自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
	@TableField("DEVICE_INFO")
	private String deviceInfo;
	
	//随机字符串，长度要求在32位以内
	@TableField("NONCE_STR")
	private String nonceStr;
	
	//通过签名算法计算得出的签名值
	@TableField("SIGN")
	private String sign;
	
	//签名类型，默认为MD5
	@TableField("SIGN_TYPE")
	private String signType;
	
	//商品简单描述
	@TableField("BODY")
	private String body;
	
	//商品详细描述
	@TableField("DETAIL")
	private String detail;
	
	//附加数据
	@TableField("ATTACH")
	private String attach;
	
	//商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
	@TableField("OUT_TRADE_NO")
	private String outTradeNo;
	
	//符合ISO 4217标准的三位字母代码，默认人民币：CNY
	@TableField("FEE_TYPE")
	private String feeType;
	
	//订单总金额，单位为分
	@TableField("TOTAL_FEE")
	private int totalFee;
	
	//支持IPV4和IPV6两种格式的IP地址。用户的客户端IP
	@TableField("SP_BILL_CREATE_IP")
	private String spBillCreateIp;
	
	//JSAPI -JSAPI支付 NATIVE -Native支付 APP -APP支付
	@TableField("TRADE_TYPE")
	private String tradeType;
	
	//此参数必传。此参数为二维码中包含的商品ID，商户自行定义
	@TableField("PRODUCT_ID")
	private String productId;
	
	//此参数为微信用户在商户对应appid下的唯一标识
	@TableField("OPEN_ID")
	private String openId;
	
	//交易起始时间
	@TableField("TIME_START")
	private String timeStart;
	
	//交易失效时间
	@TableField("TIME_EXPIRE")
	private String timeExpire;
	
	//异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数
	@TableField("NOTIFY_URL")
	private String notifyUrl;
	
	//电子发票入口开放标识
	@TableField("RECEIPT")
	private String receipt;
	
	//上传此参数no_credit--可限制用户不能使用信用卡支付
	@TableField("LIMIT_PAY")
	private String limitPay;
	
	//场景信息
	@TableField("SCENE_INFO")
	private String sceneInfo;
	
	//预下单ID
	@TableField("PREPAY_ID")
	private String prepayId;
	
	//交易ID
	@TableField("TRANSACTION_ID")
	private String transactionId;
	
	//二维码链接
	@TableField("CODE_URL")
	private String codeUrl;
	
	//状态   0:创建   1：提交失败   2：提交成功   3：支付成功   4：支付失败
	@TableField("STATUS")
	private String status;
	
	//错误信息
	@TableField("ERR_CODE")
	private String errCode;
	
	//错误信息
	@TableField("ERR_MSG")
	private String errMsg;
	
	//是否关注
	@TableField("SUBSCRIBE")
	private String subscribe;
	
	//银行
	@TableField("BANK_TYPE")
	private String bankType;
	
	//支付结束时间
	@TableField("END_TIME")
	private Date endTime;
	
	//关联客户
	@TableField("CUS_ID")
	private String cusId;
}
