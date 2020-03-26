package com.active4j.web.func.pay.controller;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.active4j.common.util.IpUtil;
import com.active4j.common.util.NumberUtil;
import com.active4j.common.util.QrCodeUtils;
import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.PageInfo;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.func.pay.entity.FuncWeixinPayPreOrderEntity;
import com.active4j.entity.func.pay.model.PayConstrant;
import com.active4j.entity.func.pay.model.PayStatusModel;
import com.active4j.entity.func.pay.model.WeixinOrderModel;
import com.active4j.service.func.pay.service.FuncWeixinPayPreOrderService;
import com.active4j.service.func.pay.util.OrderNoUtil;
import com.active4j.web.core.config.shiro.ShiroUtils;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.func.pay.wrapper.FuncWeixinPayPreOrderWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * @title FuncWeixinPayPreOrderController.java
 * @description 
		  微信支付管理
 * @time  2019年12月10日 上午10:07:22
 * @author mhm
 * @version 1.0
*/
@Controller
@RequestMapping("/func/pay/wx")
@Slf4j
@Api(value="常用功能-微信支付", tags={"微信支付操作接口"})
public class FuncWeixinPayPreOrderController extends BaseController {
	
	@Autowired
	private FuncWeixinPayPreOrderService funcWeixinPayPreOrderService;
	
	/**
	 * 
	 * @description
	 *  	生成订单号
	 * @params
	 * @return AjaxJson
	 * @author mhm
	 * @time 2020年1月3日 下午1:59:37
	 */
	@RequestMapping(value="/getOrderNo")
	@ResponseBody
	@ApiOperation(value = "微信支付生成订单号", notes = "微信支付生成订单号")
	public ResultJson getOrderNo(HttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		String userName = ShiroUtils.getSessionUserName();
		try {
			//构建预支付实体
			String outTradeNo = OrderNoUtil.getOrderNo();
			
			j.setData(outTradeNo);
		}catch(Exception e) {
			log.error("用户{}充值生成orderNo报错，错误信息{}", userName, e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	 获取表格数据 树形结构
	 * @return void
	 * @author mhm
	 * @time 2019年12月12日 下午4:50:26
	 */
	@RequestMapping("/datagrid")
	@ResponseBody
	public void datagrid(FuncWeixinPayPreOrderEntity funcWeixinPayPreOrder, PageInfo<FuncWeixinPayPreOrderEntity> page, HttpServletRequest request, HttpServletResponse response) {
		//拼接查询条件
		QueryWrapper<FuncWeixinPayPreOrderEntity> queryWrapper = QueryUtils.installQueryWrapper(funcWeixinPayPreOrder, request.getParameterMap());
		
		//执行查询
		IPage<FuncWeixinPayPreOrderEntity> lstResult = funcWeixinPayPreOrderService.page(page.getPageEntity(), queryWrapper);
		
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new FuncWeixinPayPreOrderWrapper(lstResult).wrap());
	}
	
	/**
	 * 根据微信支付返回结果，生成二维码
	 * @param key
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getQrcode")
	@ResponseBody
	public void getQrcode(String key, HttpServletRequest request, HttpServletResponse response) {
		String userName = ShiroUtils.getSessionUserName();
		try {
			
			if(StringUtils.isNotEmpty(key)) {
				
				BufferedImage image = QrCodeUtils.createImage(key);
				
				// 输出图象到页面
				ImageIO.write(image, QrCodeUtils.FORMAT_NAME, response.getOutputStream());
			}
			
		}catch(Exception e) {
			log.error("用户{}充值生成二维码报错，错误信息{}", userName, e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @description
	 *  	微信支付逻辑
	 * @params
	 * @return ResultJson
	 * @author mhm
	 * @time 2020年1月3日 下午3:33:48
	 */
	@RequestMapping(value="/getCharge")
	@ResponseBody
	@ApiOperation(value = "微信支付逻辑", notes = "微信支付逻辑")
	public ResultJson getCharge(@ApiParam(name="money", value="支付金额", required=true)String money, @ApiParam(name="orderNo", value="支付订单号", required=true)String orderNo, HttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		String userName = ShiroUtils.getSessionUserName();
		log.info("用户{}进入微信付款接口，金额{}, 订单号:{}", userName, money, orderNo);
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
			
			
			/**
			 *   微信给用户充值
			 * @param userId
			 * @param money
			 */
			j = funcWeixinPayPreOrderService.doWeixinCharge(ShiroUtils.getSessionUserId(), orderNo, m, IpUtil.getIpAddr(request), userName);
			
			//处理一下，避免前台出现null值
			if(null == j.getData()) {
				WeixinOrderModel order = new WeixinOrderModel();
				order.setCodeUrl("");
				order.setMoney(0.0);
				order.setName("");
				order.setOrderNo("");
				order.setOrderTime("");
				order.setCompany(userName);
				j.setData(order);
			}
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("sorry,系统一定哪个地方出错了");
			log.error("用户{}充值生成二维码报错，错误信息{}", userName, e.getMessage());
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
	@RequestMapping("/getStatus")
	@ResponseBody
	public ResultJson getStatus(String orderNo, HttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		
		try {
			String userId = ShiroUtils.getSessionUserId();
			PayStatusModel payStatus = funcWeixinPayPreOrderService.getWeixinPayStatus(userId, orderNo);
			//必须是有状态的支付订单 才返回
			if(StringUtils.equals(payStatus.getStatus(), PayConstrant.weixin_pay_status_success) 
					|| StringUtils.equals(payStatus.getStatus(), PayConstrant.weixin_pay_status_fail)) {
				j.setSuccess(true);
				j.setData(payStatus);
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
