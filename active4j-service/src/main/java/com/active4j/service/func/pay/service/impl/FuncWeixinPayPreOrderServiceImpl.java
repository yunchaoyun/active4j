package com.active4j.service.func.pay.service.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.common.util.ArithUtil;
import com.active4j.common.util.DateUtils;
import com.active4j.dao.func.pay.dao.FuncWeixinPayPreOrderDao;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.func.pay.entity.FuncWeixinPayPreOrderEntity;
import com.active4j.entity.func.pay.model.PayConstrant;
import com.active4j.entity.func.pay.model.PayStatusModel;
import com.active4j.entity.func.pay.model.WeixinOrderModel;
import com.active4j.entity.func.pay.properties.WeixinPayProperties;
import com.active4j.service.func.pay.service.FuncWeixinPayPreOrderService;
import com.active4j.service.func.pay.util.WeChatUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * @title WeixinPayPreOrderServiceImpl.java
 * @description 
		  微信预下单列表
 * @time  2019年12月10日 下午4:35:12
 * @author mhm
 * @version 1.0
*/
@Service("weixinPayPreOrderService")
@Transactional
@Slf4j
public class FuncWeixinPayPreOrderServiceImpl extends ServiceImpl<FuncWeixinPayPreOrderDao, FuncWeixinPayPreOrderEntity> implements FuncWeixinPayPreOrderService {
	
	@Autowired
	private WeixinPayProperties weixinPayProperties;

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
	public ResultJson doWeixinCharge(String cusId, String outTradeNo, double money, String ip, String userName) {
		ResultJson j = new ResultJson();
	
		if(StringUtils.isEmpty(weixinPayProperties.getAppId()) || StringUtils.isEmpty(weixinPayProperties.getMchId())
				|| StringUtils.isEmpty(weixinPayProperties.getNotifyUrl()) || StringUtils.isEmpty(weixinPayProperties.getKey())) {
			j.setSuccess(false);
			j.setMsg("缺少必要的微信支付参数");
			return j;
		}
		log.info("订单号{}进入微信支付", outTradeNo);
		
		//支付商品说明
		String body = "演示微信支付付款" + money + "元";
		
		//创建微信统一下单实体  详情参考微信支付文档
		FuncWeixinPayPreOrderEntity preOrder = createPrePayOder(cusId, money, outTradeNo, body, body, "付款", outTradeNo, ip, null, null, false, false);
		
		try {
			//请求微信支付 得到map结果集
			Map<String, String> result = getXmlStrFromPreOrder(preOrder);
			String returnCode = result.get("return_code");
			
			//失败返回
			if(StringUtils.equalsIgnoreCase(WeChatUtil.FAIL, returnCode)) {
				j.setSuccess(false);
				j.setMsg(result.get("return_msg"));
				
				//失败赋值
				preOrder.setStatus(PayConstrant.pre_order_status_submit_fail);
				preOrder.setErrMsg(result.get("return_msg"));
				preOrder.setErrCode(result.get("return_msg"));
				return j;
			}
			
			//成功返回
			if(StringUtils.equalsIgnoreCase(WeChatUtil.SUCCESS, returnCode)) {
				String resultCode = result.get("result_code");
				if(StringUtils.equalsIgnoreCase(WeChatUtil.FAIL, resultCode)) {
					//结果失败
					j.setSuccess(false);
					j.setMsg("微信支付系统出错，错误代码:" + result.get("err_code") + ", 错误信息:" + result.get("err_code_des"));
					
					//失败赋值
					preOrder.setStatus(PayConstrant.pre_order_status_submit_fail);
					preOrder.setErrMsg(result.get("err_code_des"));
					preOrder.setErrCode(result.get("err_code"));
					
					return j;
				}else if(StringUtils.equalsIgnoreCase(WeChatUtil.SUCCESS, resultCode)) {
					//结果成功
					String prepayId = result.get("prepay_id");
					String codeUrl = result.get("code_url");
					log.info("订单号{},产生的预下单ID:{}, 生成的code为{}.", outTradeNo, prepayId, codeUrl);
					
					//成功赋值
					preOrder.setPrepayId(prepayId);
					preOrder.setCodeUrl(codeUrl);
					preOrder.setStatus(PayConstrant.pre_order_status_submit_success);
					
					//构建返回页面model
					WeixinOrderModel order = new WeixinOrderModel();
					order.setCodeUrl(codeUrl);
					order.setMoney(money);
					order.setName(body);
					order.setOrderNo(outTradeNo);
					order.setOrderTime(DateUtils.getDateYY_MM_DD());
					order.setCompany(userName);
					j.setData(order);
					
				}
			}
			
		} catch (Exception e) {
			log.error("用户{}微信支付遇到异常，异常信息:{}", cusId, e.getMessage());
			log.error(e.getMessage(), e);
			j.setSuccess(false);
			j.setMsg("微信支付遇到异常，系统工程师正在解决中......");
			//失败赋值
			preOrder.setStatus(PayConstrant.pre_order_status_submit_fail);
			preOrder.setErrMsg(e.getMessage());
			preOrder.setErrCode(e.getMessage());
			
		}finally {
			//保存下单实体
			this.save(preOrder);
			log.info("微信下单实体成功保存,订单号{}." , preOrder.getOutTradeNo());
		}
		
		return j;
	}
	
	/**
	 * 根据订单信息 生成微信支付 预下单实体
	 * @param money     订单金额
	 * @param body      商品简要描述
	 * @param detail    商品详细描述
	 * @param attach    标识，原样返回
	 * @param productId 产品ID
	 * @param ip        用户终端IP
	 * @param openId    用户唯一微信标识
	 * @param sceneInfo 场景信息
	 * @param isCredit  是否支持信用卡
	 * @param isInvoice 是否需要开发票
	 * @return
	 */
	private FuncWeixinPayPreOrderEntity createPrePayOder(String cusId, double money, String orderNo, String body, String detail, String attach, String productId, String ip, String openId, String sceneInfo, boolean isCredit, boolean isInvoice) {
		FuncWeixinPayPreOrderEntity preOrder = new FuncWeixinPayPreOrderEntity();
		//赋值当前登录用户
		preOrder.setCusId(cusId);
		//赋值创建状态
		preOrder.setStatus(PayConstrant.pre_order_status_created);  
		//微信支付分配的公众账号ID
		preOrder.setAppId(weixinPayProperties.getAppId());
		//微信支付分配的商户号
		preOrder.setMchId(weixinPayProperties.getMchId());
		preOrder.setDeviceInfo("WEB");
		
		//随机字符串  8位随机数
		String nonceStr = RandomStringUtils.random(20, "0123456789abcdefghijklmnopqrstuvwxyz");
		preOrder.setNonceStr(nonceStr);
		preOrder.setSignType("MD5");
		
		//商品描述
		preOrder.setBody(body);
		if(StringUtils.isNotEmpty(detail)) {
			preOrder.setDetail(detail);
		}
		
		//附加数据,原样返回
		if(StringUtils.isNotEmpty(attach)) {
			preOrder.setAttach(attach);
		}
		
		//商户订单号
		preOrder.setOutTradeNo(orderNo);
		
		preOrder.setFeeType("CNY");
		
		//支付金额   微信支付是以分为单位
		double d100 = ArithUtil.mul(money, 100.0);
		preOrder.setTotalFee((int)d100);
		
		//终端IP
		preOrder.setSpBillCreateIp(ip);
		
		//交易时间
		preOrder.setTimeStart(DateUtils.getDate2StrYYYYMMDDHHMMSS());
		preOrder.setTimeExpire(DateUtils.getDate2StrYYYYMMDDHHMMSSAdd30M());
		//回调地址
		preOrder.setNotifyUrl(weixinPayProperties.getNotifyUrl());
		//交易类型
		preOrder.setTradeType("NATIVE");
		//产品ID
		preOrder.setProductId(productId);
		
		//是否支持信用卡支付
		if(!isCredit) {
			preOrder.setLimitPay("no_credit");
		}
		
		//用户表示   JSSDK必传
		preOrder.setOpenId(openId);
		
		//需要开票
		if(isInvoice) {
			preOrder.setReceipt("Y");
		}else {
			preOrder.setReceipt("N");
		}
		preOrder.setSceneInfo(sceneInfo);
		
		return preOrder;
	}
	
	private Map<String, String> getXmlStrFromPreOrder(FuncWeixinPayPreOrderEntity preOrder) throws Exception {
		Map<String, String> paramMap = new TreeMap<String, String>();
		paramMap.put("appid", preOrder.getAppId());
		paramMap.put("mch_id", preOrder.getMchId());
		paramMap.put("attach", preOrder.getAttach());
		paramMap.put("device_info", preOrder.getDeviceInfo());
		paramMap.put("nonce_str", preOrder.getNonceStr());
		paramMap.put("body", preOrder.getBody());
		paramMap.put("detail", preOrder.getDetail());
		paramMap.put("out_trade_no", preOrder.getOutTradeNo());
		paramMap.put("fee_type", preOrder.getFeeType());
		paramMap.put("total_fee", String.valueOf(preOrder.getTotalFee()));
		paramMap.put("spbill_create_ip", preOrder.getSpBillCreateIp());
		paramMap.put("time_start", preOrder.getTimeStart());
		paramMap.put("time_expire", preOrder.getTimeExpire());
		paramMap.put("notify_url", preOrder.getNotifyUrl());
		paramMap.put("trade_type", preOrder.getTradeType());
		if(StringUtils.isNotEmpty(preOrder.getOpenId())) {
			paramMap.put("openid", preOrder.getOpenId());
		}
		if(StringUtils.isNotEmpty(preOrder.getProductId())) {
			paramMap.put("product_id", preOrder.getProductId());
		}
		
		if(StringUtils.isNotEmpty(preOrder.getLimitPay())) {
			paramMap.put("limit_pay", preOrder.getLimitPay());
		}
		
		paramMap.put("receipt", preOrder.getReceipt());
		
		if(StringUtils.isNotEmpty(preOrder.getSceneInfo())) {
			paramMap.put("scene_info", preOrder.getSceneInfo());
		}
		
		//签名
		String sign = WeChatUtil.generateSignature(paramMap, weixinPayProperties.getKey());
		
		log.info("获得签名:{}", sign);
		
		//获得请求参数
		String strXml = WeChatUtil.getParamXml(paramMap, sign);
		
		log.info("获得请求参数:{}", strXml);
		
		Map<String, String> mapResult = WeChatUtil.getResultMapWithoutCert(strXml);
		
		return mapResult;
	}

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
	public PayStatusModel getWeixinPayStatus(String userId, String outTradeNo) {
		
		/**
		 *  查询数据库订单状态
		 */
		QueryWrapper<FuncWeixinPayPreOrderEntity> queryWrapper = new QueryWrapper<FuncWeixinPayPreOrderEntity>();
		queryWrapper.eq("CUS_ID", userId);
		queryWrapper.eq("OUT_TRADE_NO", outTradeNo);
		List<FuncWeixinPayPreOrderEntity> lstPreOrders = this.list(queryWrapper);
		if(null != lstPreOrders && lstPreOrders.size() > 0) {
			FuncWeixinPayPreOrderEntity preOrder = lstPreOrders.get(0);
			
			PayStatusModel payStatus = new PayStatusModel();
			payStatus.setMoney(String.valueOf(preOrder.getTotalFee()/100.0d));
			payStatus.setOrderName(preOrder.getBody());
			payStatus.setOrderNo(preOrder.getOutTradeNo());
			payStatus.setOrderTime(DateUtils.getDate2Str(preOrder.getCreateDate(), DateUtils.pattern_yyyy_MM_dd));
			
			if(StringUtils.equals(preOrder.getStatus(), PayConstrant.pre_order_status_pay_fail) 
					|| StringUtils.equals(preOrder.getStatus(), PayConstrant.pre_order_status_submit_fail)) {
				//提交失败   支付失败的处理
				payStatus.setStatus(PayConstrant.weixin_pay_status_fail);
				payStatus.setErrMsg(preOrder.getErrMsg());
				
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
	 *  	获取预下单实体
	 * @params
	 *      outTradeNo
	 * @return FuncWeixinPayPreOrderEntity
	 * @author mhm
	 * @time 2020年1月7日 上午10:48:58
	 */
	public FuncWeixinPayPreOrderEntity findOrder(String outTradeNo) {
		
		QueryWrapper<FuncWeixinPayPreOrderEntity> queryWrapper = new QueryWrapper<FuncWeixinPayPreOrderEntity>();
		queryWrapper.eq("OUT_TRADE_NO", outTradeNo);
		List<FuncWeixinPayPreOrderEntity> lstPayOrder = this.list(queryWrapper);
		
		if(null != lstPayOrder && lstPayOrder.size() > 0) {
			return lstPayOrder.get(0);
		}
		
		return null;
	}


}
