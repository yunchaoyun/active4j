package com.active4j.web.core.jwt;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import com.active4j.entity.base.model.ResultJson;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

	/**
	 * 执行登录认证
	 *
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
			try {
				executeLogin(request, response);
				return true;
			} catch (Exception e) {
				ResultJson j = new ResultJson();
				j.setCode(1001);
				j.setSuccess(false);
				j.setMsg("Token失效，请重新登录");
				HttpServletResponse resp = (HttpServletResponse)response;
				try {
					resp.setCharacterEncoding("UTF-8");
					response.setContentType("application/json;charset=UTF-8");
					resp.getWriter().write(JSON.toJSONString(j));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				log.error("Token失效，请重新登录");
				return false;
			}
			
			
	}
	
	 @Override
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
//		log.info("当 isAccessAllowed 返回 false 的时候，才会执行 method onAccessDenied ");

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		response.setStatus(HttpStatus.OK.value());

		// 返回 false 表示已经处理，例如页面跳转啥的，表示不在走以下的拦截器了（如果还有配置的话）
		return false;
	}
	
	

	/**
	 *
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		String token = httpServletRequest.getHeader(JwtUtil.ACCESS_TOKEN);
		if(StringUtils.isEmpty(token)) {
			token = httpServletRequest.getParameter(JwtUtil.ACCESS_TOKEN);
		}
		JwtToken jwtToken = new JwtToken(token);
		// 提交给realm进行登入，如果错误他会抛出异常并被捕获
		getSubject(request, response).login(jwtToken);
		// 如果没有抛出异常则代表登入成功，返回true
		return true;
	}
	
	/**
	 * 对跨域提供支持
	 */
	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
		httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
		httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
		// 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
		if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
			httpServletResponse.setStatus(HttpStatus.OK.value());
			return false;
		}
		return super.preHandle(request, response);
	}
	
}
