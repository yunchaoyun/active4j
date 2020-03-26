package com.active4j.web.func.pay.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.active4j.common.util.DateUtils;
import com.active4j.entity.func.pay.entity.FuncWeixinPayPreOrderEntity;
import com.active4j.entity.func.pay.model.PayConstrant;
import com.active4j.service.func.pay.service.FuncWeixinPayPreOrderService;
import com.active4j.service.func.pay.util.WeChatUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @title FuncWeixinPayBackController.java
 * @description 
		  微信支付回调
 * @time  2019年12月11日 下午4:01:04
 * @author mhm
 * @version 1.0
*/
@Controller
@RequestMapping("/func/weixin")
@Slf4j
public class FuncWeixinPayBackController {

	@Autowired
	private FuncWeixinPayPreOrderService funcWeixinPayPreOrderService;
	
	public static final String FAIL     = "FAIL";
	public static final String SUCCESS  = "SUCCESS";
	
	@RequestMapping("/payback")
	public void payback(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			StringBuffer buffer = new StringBuffer();
			InputStream in = request.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
			BufferedReader reader = new BufferedReader(inputStreamReader);

			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

			reader.close();
			inputStreamReader.close();
			in.close();

			// 得到content
			String content = buffer.toString();
			log.info("进入了微信支付回调, inputstream:" + content);
			
			if(StringUtils.isNotEmpty(content)) {
				//解析字符串，得到map
				Map<String, String> result = WeChatUtil.processResponseXml(content);
				
				String returnCode = result.get("return_code");
				//成功返回
				if(StringUtils.equalsIgnoreCase(WeChatUtil.SUCCESS, returnCode)) {
					String resultCode = result.get("result_code");
					String outTradeNo = result.get("out_trade_no");
					String openId = result.get("openid");
					String subscribe = result.get("is_subscribe");
					String bankType = result.get("bank_type");
					String transactionId = result.get("transaction_id");
					
					//查询实体
					FuncWeixinPayPreOrderEntity weixinPayPreOrder = funcWeixinPayPreOrderService.findOrder(outTradeNo);
					if(null != weixinPayPreOrder && StringUtils.equals(PayConstrant.pre_order_status_submit_success, weixinPayPreOrder.getStatus())) {
						weixinPayPreOrder.setOpenId(openId);
						weixinPayPreOrder.setSubscribe(subscribe);
						weixinPayPreOrder.setBankType(bankType);
						weixinPayPreOrder.setTransactionId(transactionId);
						weixinPayPreOrder.setEndTime(DateUtils.getNow());
						if(StringUtils.equalsIgnoreCase(WeChatUtil.FAIL, resultCode)) { 
							//回调失败的处理
							weixinPayPreOrder.setStatus(PayConstrant.pre_order_status_pay_fail);
							weixinPayPreOrder.setErrCode(result.get("err_code"));
							weixinPayPreOrder.setErrMsg(result.get("err_code_des"));
							
						}else {
							//回调成功的处理
							weixinPayPreOrder.setStatus(PayConstrant.pre_order_status_pay_success);
							//设置订单状态，以便确认订单状态
							
						}
						
						//保存实体  充值成功需要完成的业务逻辑，比如保存充值记录   购买套餐成功失败需要完成的业务逻辑
						funcWeixinPayPreOrderService.saveOrUpdate(weixinPayPreOrder);
						log.info("订单号{}的支付状态已更新{}", outTradeNo, resultCode);
						
					}
				}else {
					log.info("微信支付取到returnCode为FAIL的值, 不正常");
				}
			}
			
			 //回写
	        PrintWriter writer = response.getWriter();
	        String requestXML = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
			writer.write(requestXML);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.info("微信支付回调报错，错误信息{}", e.getMessage());
		}

	}	
}
