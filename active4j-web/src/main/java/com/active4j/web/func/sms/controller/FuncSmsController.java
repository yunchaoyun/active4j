package com.active4j.web.func.sms.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.active4j.common.func.sms.util.SmsUtil;
import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.entity.func.sms.model.AliModel;
import com.active4j.entity.func.sms.model.QcloudModel;
import com.active4j.entity.func.sms.model.YunchaoyunModel;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.func.sms.properties.AliSmsProperties;
import com.active4j.web.func.sms.properties.QcloudSmsProperties;
import com.active4j.web.func.sms.properties.YunChaoYunProperties;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title FuncSmsController.java
 * @description 
		 集成短信功能  腾讯云  阿里云  云潮云
 * @time  2020年1月2日 上午10:05:22
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/func/sms")
@Slf4j
@Api(value="常用功能-短信服务", tags={"短信服务操作接口"})
public class FuncSmsController extends BaseController {
	
	@Autowired
	private YunChaoYunProperties yunchaoyunProperties;
	
	@Autowired
	private QcloudSmsProperties qcloudSmsProperties;
	
	@Autowired
	private AliSmsProperties aliSmsProperties;
	
	/**
	 * @description
	 *  	使用云潮云
	 * @return String
	 * @author 麻木神
	 * @time 2019年12月10日 下午2:22:30
	 */
	@RequestMapping(value="/yunchaoyun")
	@ApiOperation(value = "使用云潮云短信", notes = "云潮云短信获取配置参数", response=BaseWrapper.class)
	public void yunchaoyun(HttpServletRequest request, HttpServletResponse response) {
		
		/**
		 * 只是为了演示方便这么写
		 */
		String apiKey = yunchaoyunProperties.getApikey();
		
		String apiToken = yunchaoyunProperties.getApiToken();
		
		String smsSign = "active4j开发平台";
		
		String templateNo = "1912100047";
		
		YunchaoyunModel model = new YunchaoyunModel();
		model.setApiKey(apiKey);
		model.setApiToken(apiToken);
		model.setSmsSign(smsSign);
		model.setTemplateNo(templateNo);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<YunchaoyunModel>(model).wrap());
	} 
	
	
	/**
	 * @description
	 *  	演示利用云潮云短信SDK发送短信
	 * @return AjaxJson
	 * @author 麻木神
	 * @time 2019年12月10日 下午3:57:28
	 */
	@RequestMapping(value="/sendYunchaoyunSms", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@ApiOperation(value = "云潮云短信发送", notes = "云潮云短信发送")
	public ResultJson sendYunchaoyunSms(@ApiParam(name="apiKey", value="apiKey", required=true) String apiKey, @ApiParam(name="apiToken", value="apiToken", required=true) String apiToken, @ApiParam(name="smsSign", value="短信签名", required=true) String smsSign, @ApiParam(name="templateNo", value="模版编号", required=true) String templateNo, @ApiParam(name="phoneNo", value="手机号", required=true) String phoneNo) {
		ResultJson j = new ResultJson();
		try {
			
			SmsUtil.sendYunChaoYunSms(apiKey, apiToken, smsSign, templateNo, phoneNo, new String[] {"8888"});
			j.setMsg("短信提交成功, 是否能接受到短信，还需要运营商返回状态");
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("短信发送失败");
			log.error(e.getMessage(), e);
		}
		
		return j;
	}
	
	
	
	
	/**
	 * @description
	 *  	使用腾讯云
	 * @return String
	 * @author 麻木神
	 * @time 2019年12月10日 下午2:22:30
	 */
	@RequestMapping(value="/qcloudsms")
	@ApiOperation(value = "使用腾讯云短信", notes = "腾讯云短信获取配置参数", response=BaseWrapper.class)
	public void qcloudsms(HttpServletRequest request, HttpServletResponse response) {
		
		/**
		 * 只是为了演示方便这么写
		 */
		String appid = qcloudSmsProperties.getAppid();
		
		String appkey = qcloudSmsProperties.getAppkey();
		
		String smsSign = "江苏众禾";
		
		String templateId = "494564";
		
		QcloudModel model = new QcloudModel();
		model.setAppid(appid);
		model.setAppkey(appkey);
		model.setSmsSign(smsSign);
		model.setTemplateId(templateId);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<QcloudModel>(model).wrap());
	} 
	
	
	/**
	 * @description
	 *  	演示利用腾讯云短信SDK发送短信
	 * @return AjaxJson
	 * @author 麻木神
	 * @time 2019年12月10日 下午3:57:28
	 */
	@RequestMapping(value="/sendQcloudSms", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@ApiOperation(value = "腾讯云短信发送", notes = "腾讯云短信发送")
	public ResultJson sendQcloudSms(@ApiParam(name="appid", value="appid", required=true) String appid, @ApiParam(name="appkey", value="appkey", required=true) String appkey, @ApiParam(name="smsSign", value="短信签名", required=true) String smsSign, @ApiParam(name="templateId", value="模版id", required=true) String templateId, @ApiParam(name="phoneNo", value="手机号", required=true) String phoneNo) {
		ResultJson j = new ResultJson();
		try {
			
			SmsUtil.sendQcloudSms(appid, appkey, smsSign, templateId, phoneNo, new String[] {"8888"});
			j.setMsg("短信提交成功, 是否能接受到短信，还需要运营商返回状态");
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("短信发送失败");
			log.error(e.getMessage(), e);
		}
		
		return j;
	}
	
	
	/**
	 * 
	 * @description
	 *  	跳转到阿里云短信页面
	 * @return String
	 * @author 麻木神
	 * @time 2019年12月16日 上午8:58:56
	 */
	@RequestMapping(value="/alisms")
	@ApiOperation(value = "使用阿里云短信", notes = "阿里云短信获取配置参数", response=BaseWrapper.class)
	public void alisms(HttpServletRequest request, HttpServletResponse response) {
		
		AliModel model = new AliModel();
		model.setAccessKeyId(aliSmsProperties.getAccessKeyId());
		model.setAccessSecret(aliSmsProperties.getAccessSecret());
		model.setRegionId(aliSmsProperties.getRegionId());
		model.setSmsSign("江苏众禾");
		model.setTemplateCode("SMS_180046878");
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<AliModel>(model).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	演示利用阿里云云短信SDK发送短信
	 * @return AjaxJson
	 * @author 麻木神
	 * @time 2019年12月16日 上午11:12:59
	 */
	@RequestMapping(value="/sendAliSms", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@ApiOperation(value = "阿里云短信发送", notes = "阿里云短信发送")
	public ResultJson sendAliSms(@ApiParam(name="appid", value="appid", required=true) String appid, @ApiParam(name="regionId", value="regionId", required=true) String regionId, @ApiParam(name="accessKeyId", value="accessKeyId", required=true) String accessKeyId, @ApiParam(name="accessSecret", value="accessSecret", required=true) String accessSecret, @ApiParam(name="smsSign", value="短信签名", required=true) String smsSign, @ApiParam(name="templateCode", value="模版号", required=true)  String templateCode, @ApiParam(name="phoneNo", value="手机号", required=true)  String phoneNo) {
		ResultJson j = new ResultJson();
		try {
			
			SmsUtil.sendAliSms(regionId, accessKeyId, accessSecret, phoneNo, smsSign, templateCode, "{'code':'8888'}");
			j.setMsg("短信提交成功, 是否能接受到短信，还需要运营商返回状态");
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("短信发送失败");
			log.error(e.getMessage(), e);
		}
		
		return j;
	}
	
}
