package com.active4j.web.func.captcha.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.active4j.common.redis.RedisApi;
import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.model.ResultJson;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title CaptchaController.java
 * @description 
		验证码管理
 * @time  2019年12月19日 下午3:30:47
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/func/captcha")
@Slf4j
@Api(value="常用功能-验证码", tags={"验证码操作接口"})
public class CaptchaController extends BaseController {
	
	@Resource(name = "captchaProducer")
    private Producer captchaProducer;

    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;
    
    @Autowired
	private RedisApi redisApi;

	/**
	 * 
	 * @description
	 *  	图形验证码
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月19日 下午4:49:57
	 */
	@RequestMapping(value="/captchaImage")
	@ApiOperation(value="获取图形验证码", notes="获取图形验证码")
	public void verCode(@ApiParam(name="type", value="验证码类型", required=true) String type, HttpServletRequest request, HttpServletResponse response) {
		ServletOutputStream out = null;
        try {
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setContentType("image/jpeg");

            String capStr = null;
            String code = null;
            BufferedImage bi = null;
            //算数验证码
            if(StringUtils.equals("math", type)) {
                String capText = captchaProducerMath.createText();
                capStr = capText.substring(0, capText.lastIndexOf("@"));
                code = capText.substring(capText.lastIndexOf("@") + 1);
                bi = captchaProducerMath.createImage(capStr);
            }
            //文字验证码
            else if(StringUtils.equals("char", type)) {
                capStr = code = captchaProducer.createText();
                bi = captchaProducer.createImage(capStr);
            }
            
            //验证码存到redis,60秒
            redisApi.set(Constants.KAPTCHA_SESSION_KEY, code, 60);
            out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();
        } catch (Exception e) {
        	log.error("获取验证码报错，错误信息：{}", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if(out != null) {
                    out.close();
                }
            } catch (IOException e) {
            	log.error("获取验证码报错，错误信息：{}", e.getMessage());
                e.printStackTrace();
            }
        }
	}
	
	/**
	 * 
	 * @description
	 *  	校验验证码
	 * @params
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月19日 下午5:21:09
	 */
	@RequestMapping(value="/verify", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@ApiOperation(value="校验验证码", notes="校验验证码")
	public ResultJson verify(@ApiParam(name="vercode", value="输入的验证码", required=true) String vercode, HttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		
		try{
			//获取缓存的验证码
			Object obj = redisApi.get(Constants.KAPTCHA_SESSION_KEY);
			String code = String.valueOf(obj != null ? obj : "");
			//校验输入的验证码
	    	if (StringUtils.isEmpty(vercode) || !StringUtils.equalsIgnoreCase(code, vercode)) {
	           j.setSuccess(false);
	        }
		} catch(Exception e) {
			j.setSuccess(false);
			log.error("校验验证码报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
}
