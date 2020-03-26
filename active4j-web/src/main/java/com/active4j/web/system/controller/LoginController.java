package com.active4j.web.system.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.active4j.common.redis.RedisApi;
import com.active4j.entity.base.annotation.Log;
import com.active4j.entity.base.model.AccessTokenModel;
import com.active4j.entity.base.model.LogType;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.common.GlobalConstant;
import com.active4j.entity.system.entity.SysUserEntity;
import com.active4j.service.system.service.SysUserService;
import com.active4j.web.core.config.shiro.ShiroUtils;
import com.active4j.web.core.jwt.JwtUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * @title LoginController.java
 * @description 
		用户登录功能
 * @time  2019年12月27日 上午9:13:57
 * @author guyp
 * @version 1.0
 */
@Controller
@Api(value="用户登录登出", tags={"用户登录登出操作接口"})
public class LoginController {
	
	@Autowired
	private SysUserService userService;
	
	@Autowired
	private RedisApi redisApi;
	
	/**
	 * 
	 * @description
	 * @params
	 *      登出
	 * @return ResultJson
	 * @author guyp
	 * @time 2019年12月27日 上午9:15:00
	 */
	@RequestMapping(value="/logout", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiOperation(value = "用户登出", notes = "用户登出")
	@ResponseBody
	@Log(type = LogType.logout, name = "用户登出", memo = "用户已登出")
	public ResultJson logout(HttpServletRequest request) {
		ShiroUtils.logout();
		String token = request.getHeader(JwtUtil.ACCESS_TOKEN);
		if(StringUtils.isEmpty(token)) {
			token = request.getParameter(JwtUtil.ACCESS_TOKEN);
		}
	    //清空用户Token缓存
	    redisApi.del(JwtUtil.PREFIX_USER_TOKEN + token);
		return new ResultJson();
	}

	/**
	 * 
	 * @description
	 * @params
	 *      登录
	 * @return ResultJson
	 * @author guyp
	 * @time 2019年12月27日 上午9:14:48
	 */
	@RequestMapping(value="login", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiOperation(value = "用户登录", notes = "用户登录")
	@ResponseBody
	@Log(type = LogType.login, name = "用户登录", memo = "用户成功登录")
	public ResultJson loginAction(@ApiParam(name="username", value="用户名", required=true) String username, @ApiParam(name="password", value="密码", required=true) String password, @ApiParam(name="vercode", value="验证码", required=true) String vercode) {
		ResultJson j = new ResultJson();
		
		try {
			// 后端校验
			if (StringUtils.isEmpty(username)) {
				j.setSuccess(false);
				j.setMsg("用户名不能为空");
				return j;
			}

			if (StringUtils.isEmpty(password)) {
				j.setSuccess(false);
				j.setMsg("密码不能为空");
				return j;
			}

			if (StringUtils.isEmpty(vercode)) {
				j.setSuccess(false);
				j.setMsg("验证码不能为空");
				return j;
			}

			//从redis中获取验证码
			String serverVercode = (String) redisApi.get(GlobalConstant.SESSION_KEY_OF_RAND_CODE);
			
			// 验证码的校验
			if (!StringUtils.equalsIgnoreCase(vercode, serverVercode)) {
				j.setSuccess(false);
				j.setMsg("验证码填写错误");
				return j;
			}
			
			// 用户存在校验
			SysUserEntity sysUser = userService.getUserByUseName(username);
			if (null == sysUser) {
				j.setSuccess(false);
				j.setMsg("账号或密码不正确");
				return j;
			}
			
			// 密码校验
			String userpassword = ShiroUtils.md5(password, sysUser.getSalt());
			if(!StringUtils.equals(sysUser.getPassword(), userpassword)) {
				j.setSuccess(false);
				j.setMsg("账号或密码不正确");
				return j;
			}

			//生成access_token
			String accessToken = JwtUtil.sign(username, sysUser.getPassword());
			j.setData(new AccessTokenModel(accessToken));
			
			//将access_token存入redis缓存
			redisApi.set(JwtUtil.PREFIX_USER_TOKEN + accessToken, accessToken, (int)JwtUtil.EXPIRE_TIME / 1000);
			
		} catch (IncorrectCredentialsException e) {
			j.setSuccess(false);
			j.setMsg("用户名或密码填写错误");
		} catch (UnknownAccountException e) {
			j.setSuccess(false);
			j.setMsg("用户名或密码填写错误");
		} catch (LockedAccountException e) {
			j.setSuccess(false);
			j.setMsg("当前账户已锁定，请联系管理员");
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("系统错误，请联系管理员");
		}

		return j;
	}

}
