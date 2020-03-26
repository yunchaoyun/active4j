package com.active4j.web.func.pay.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.active4j.common.util.DateUtils;
import com.active4j.entity.func.pay.entity.FuncAliPayOrderEntity;
import com.active4j.entity.func.pay.model.PayConstrant;
import com.active4j.entity.func.pay.properties.AliPayProperties;
import com.active4j.service.func.pay.service.FuncAliPayOrderService;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title FuncAliPayBackController.java
 * @description 
  			支付宝支付回调
 * @time  2020年1月9日 下午5:28:22
 * @author mhm
 * @version 1.0
 */
@Controller
@RequestMapping("/func/ali")
@Slf4j
public class FuncAliPayBackController {
	
	@Autowired
	private AliPayProperties aliPayProperties;
	
	@Autowired
	private FuncAliPayOrderService funcAliPayOrderService;
	
	/**
	 * 
	 * @description
	 *  	支付宝returnUrl参数签名校验
	 * @params
	 * @return void
	 * @author mhm
	 * @time 2019年12月17日 上午11:09:43
	 */
	@RequestMapping("/returnUrl")
	public void returnUrl(HttpServletRequest request, HttpServletResponse response) {
		log.info("进入支付宝支付returnUrl");
		try {
			Map<String, String> params = new HashMap<String, String>();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}

			// 调用SDK验证签名
			boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPayProperties.getAliPublicKey(), aliPayProperties.getCharset(), aliPayProperties.getSignType());

			if (signVerified) {
				// 商户订单号
				String out_trade_no = request.getParameter("out_trade_no");

				// 支付宝交易号
				String trade_no = request.getParameter("trade_no");

				// 付款金额
				String total_amount = request.getParameter("total_amount");
				
				log.info("接受到支付宝支付返回的同步结果:out_trade_no:{}, trade_no:{}, total_amount:{}, passback_params:{}", out_trade_no, trade_no, total_amount);
				
				FuncAliPayOrderEntity payOrder = funcAliPayOrderService.finAliPayOrder(out_trade_no);
				
				if(null != payOrder) {
					//关闭页面
					String content = "<script type=\"text/javascript\">window.close();</script>";
					response.getWriter().print(content);
				}
			} else {
				log.info("接受到支付宝returnUrl参数签名校验失败");
				response.getWriter().print("failure");
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().print("failure");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.error("接受到支付宝returnUrl验证签名出现异常，异常信息:{}", e.getMessage());
		}

	}

	/**
	 * 
	 * @description
	 *  	接收支付宝支付回调
	 * @params
	 * @return void
	 * @author mhm
	 * @time 2019年12月17日 上午11:19:04
	 */
	@RequestMapping("/notifyUrl")
	public void notifyUrl(HttpServletRequest request, HttpServletResponse response) {
		log.info("进入支付宝支付notifyUrl");
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
				params.put(name, valueStr);
			}

			// 调用SDK验证签名
			boolean signVerified = AlipaySignature.rsaCheckV1(params, aliPayProperties.getAliPublicKey(), aliPayProperties.getCharset(), aliPayProperties.getSignType());
			if (signVerified) {

				// 商户订单号
				String out_trade_no = request.getParameter("out_trade_no");

				// 支付宝交易号
				String trade_no = request.getParameter("trade_no");

				// 交易状态
				String trade_status = request.getParameter("trade_status");
				
				String appId = request.getParameter("app_id");

				log.info("收到支付宝回调结果:out_trade_no:{}, trade_no:{}, trade_status:{}", out_trade_no, trade_no, trade_status);

				if (trade_status.equals("TRADE_FINISHED")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 如果有做过处理，不执行商户的业务程序

					// 注意：
					// 退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
					
					log.info("订单号:{}, 收到FINISHED,说明交易已关闭", out_trade_no);
				} else if (trade_status.equals("TRADE_SUCCESS")) {
					// 判断该笔订单是否在商户网站中已经做过处理
					// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					// 如果有做过处理，不执行商户的业务程序

					// 注意：
					// 付款完成后，支付宝系统发送该交易状态通知
					
					FuncAliPayOrderEntity aliPayOrder = funcAliPayOrderService.finAliPayOrder(out_trade_no);
					log.info("订单aliPayOrder:{}, 收到TRADE_SUCCESS", aliPayOrder);
					
					if(null != aliPayOrder && StringUtils.equals(PayConstrant.pre_order_status_submit_success, aliPayOrder.getStatus())
							&& StringUtils.equals(aliPayProperties.getAppId(), appId)) {
						// 支付宝交易号
						String buyer_id = request.getParameter("buyer_id");
						String seller_id = request.getParameter("seller_id");
					
						aliPayOrder.setSellerId(seller_id);
						aliPayOrder.setBuyerId(buyer_id);
						aliPayOrder.setEndTime(DateUtils.getNow());
						aliPayOrder.setTradeNo(trade_no);
						aliPayOrder.setStatus(PayConstrant.pre_order_status_pay_success);
						
						//保存回调成功
						funcAliPayOrderService.saveOrUpdate(aliPayOrder);
						log.info("订单号:{}, 收到TRADE_SUCCESS", out_trade_no);
					}
					
				} else {
					log.info("订单aliPayOrder为空, 收到TRADE_SUCCESS");
				}

				response.getWriter().write("success");
			} else {
				log.info("接受到支付宝notifyUrl参数签名校验失败");
				response.getWriter().write("fail");
			}

		} catch (AlipayApiException e) {
			e.printStackTrace();
			log.error("接受到支付宝notifyUrl验证签名出现异常，异常信息:{}", e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
