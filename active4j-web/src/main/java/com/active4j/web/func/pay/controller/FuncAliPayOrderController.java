package com.active4j.web.func.pay.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.active4j.common.util.NumberUtil;
import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.PageInfo;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.func.pay.entity.FuncAliPayOrderEntity;
import com.active4j.entity.func.pay.model.PayConstrant;
import com.active4j.entity.func.pay.model.PayStatusModel;
import com.active4j.entity.func.pay.properties.AliPayProperties;
import com.active4j.service.func.pay.service.FuncAliPayOrderService;
import com.active4j.web.core.config.shiro.ShiroUtils;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.func.pay.wrapper.FuncAliPayOrderWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @title FuncAliPayOrderController.java
 * @description 
  			支付宝支付管理
 * @time  2020年1月9日 下午4:23:26
 * @author mhm
 * @version 1.0
*/
@Controller
@RequestMapping("/func/pay/ali")
@Slf4j
@Api(value="常用功能-支付宝支付", tags={"支付宝支付操作接口"})
public class FuncAliPayOrderController extends BaseController {
	
	@Autowired
	private AliPayProperties aliPayProperties;
	
	@Autowired
	private FuncAliPayOrderService funcAliPayOrderService;
	
	/**
	 * 
	 * @description
	 *  	 获取表格数据 树形结构
	 * @params
	 *      funcAliPayOrderEntity
	 *      page
	 *      request
	 *      response
	 * @return void
	 * @author mhm
	 * @time 2020年1月9日 下午5:47:06
	 */
	@RequestMapping("/datagrid")
	@ResponseBody
	public void datagrid(FuncAliPayOrderEntity funcAliPayOrderEntity, PageInfo<FuncAliPayOrderEntity> page, HttpServletRequest request, HttpServletResponse response) {
		try {
			//拼接查询条件
			QueryWrapper<FuncAliPayOrderEntity> queryWrapper = QueryUtils.installQueryWrapper(funcAliPayOrderEntity, request.getParameterMap());
			
			//执行查询
			IPage<FuncAliPayOrderEntity> lstResult = funcAliPayOrderService.page(page.getPageEntity(), queryWrapper);
			
			//结果处理,直接写到客户端
			ResponseUtil.write(response, new FuncAliPayOrderWrapper(lstResult).wrap());
		}catch(Exception e) {
			e.printStackTrace();
			log.error("查询支付宝下单数据报错，错误信息:" + e.getMessage());
		}
	}
	
	/**
	 * 
	 * @description
	 *  	支付宝支付逻辑
	 * @params
	 *      money
	 *      orderNo
	 *      attach
	 *      request
	 *      response
	 *      ResultJson
	 * @author mhm
	 * @time 2020年1月9日 下午5:12:28
	 */
	@RequestMapping(value="/getCharge")
	@ResponseBody
	@ApiOperation(value = "支付宝支付逻辑", notes = "支付宝支付逻辑")
	public ResultJson getCharge(@ApiParam(name="money", value="支付金额", required=true)String money, @ApiParam(name="orderNo", value="支付订单号", required=true)String orderNo, HttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		String userName = ShiroUtils.getSessionUserName();
		log.info("用户{}进入支付宝付款接口，金额{}, 订单号:{}", userName, money, orderNo);
		try {
			
			//进行校验
			if(StringUtils.isEmpty(money)) {
				j.setSuccess(false);
				j.setMsg("请输入充值金额");
			}
			
			Double m = null;
			try {
				m = Double.parseDouble(money);
			}catch(Exception e) {
				log.error("金额填写错误，无法转为double");
				j.setSuccess(false);
				j.setMsg("金额填写错误");
			}
			
			if(!NumberUtil.isDouble(money)) {
				j.setSuccess(false);
				j.setMsg("金额填写错误");
			}
			
			if(StringUtils.isEmpty(orderNo)) {
				j.setSuccess(false);
				j.setMsg("订单号生成错误,请稍后再试");
			}
			
			if(!j.isSuccess()) {
				return j;
			}
			
			if(StringUtils.isEmpty(aliPayProperties.getAppId())) {
				j.setSuccess(false);
				j.setMsg("不支持支付宝支付");
				return j;
			}
			
			/**
			 *   微信给用户充值
			 * @param userId
			 * @param money
			 */
			String form = funcAliPayOrderService.doAliPay(ShiroUtils.getSessionUserId(), orderNo, m);
			
			j.setData(form);
			
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("sorry,系统一定哪个地方出错了");
			log.error("用户{}支付宝付款生成二维码报错，错误信息{}", userName, e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 微信支付获取微信订单支付状态
	 * @param orderNo
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getOrderStatus")
	@ResponseBody
	public ResultJson getOrderStatus(String orderNo, HttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		try {
			String userId = ShiroUtils.getSessionUserId();
			PayStatusModel payStatus = funcAliPayOrderService.getAliPayStatus(userId, orderNo);
			//必须是有状态的支付订单 才返回
			if(StringUtils.equals(payStatus.getStatus(), PayConstrant.weixin_pay_status_success) 
					|| StringUtils.equals(payStatus.getStatus(), PayConstrant.weixin_pay_status_fail)) {
				j.setSuccess(true);
			}else {
				j.setSuccess(false);
			}
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("sorry, 系统遇到问题, 请稍后再试");
			log.error("微信支付订单:{}, 获取状态错误，错误信息:{}", orderNo, e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	

}
