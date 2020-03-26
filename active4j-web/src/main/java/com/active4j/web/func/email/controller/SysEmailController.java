package com.active4j.web.func.email.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.service.func.email.service.SysEmailService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title SysEmailController.java
 * @description 
		系统默认邮件发送，邮箱参数配置
 * @time  2020年1月2日 上午9:11:27
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/sys/email")
@Slf4j
@Api(value="常用功能-邮件发送", tags={"邮件发送操作接口"})
public class SysEmailController extends BaseController {
	
	@Autowired
	private SysEmailService sysEmailService;
	
	/**
	 * 
	 * @description
	 *  	邮件发送
	 * @params
	 * @return ResultJson
	 * @author guyp
	 * @time 2020年1月2日 上午9:42:18
	 */
	@RequestMapping(value="/send", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@ApiOperation(value = "邮件发送", notes = "邮件发送")
	public ResultJson send(@ApiParam(name="mailbox", value="收件人邮箱", required=true) String mailbox, @ApiParam(name="subject", value="主题", required=true) String subject, @ApiParam(name="content", value="邮件内容", required=true) String content)  {
		ResultJson j = new ResultJson();
		
		try{
			//后端校验
			if(StringUtils.isEmpty(mailbox)) {
				j.setSuccess(false);
				j.setMsg("收件人邮箱不能为空");
				return j;
			}
			
			if(StringUtils.isEmpty(subject)) {
				j.setSuccess(false);
				j.setMsg("主题不能为空");
				return j;
			}
			
			if(StringUtils.isEmpty(content)) {
				j.setSuccess(false);
				j.setMsg("内容不能为空");
				return j;
			}
			
			//邮件发送，html类型（其余方法见sysEmailService）
			sysEmailService.sendHtmlMail(mailbox, subject, content);
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("邮件发送失败");
			log.error("邮件发送报错，错误信息:" + e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
	
}
