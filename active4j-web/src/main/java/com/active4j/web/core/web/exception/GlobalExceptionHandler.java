package com.active4j.web.core.web.exception;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.common.GlobalConstant;
import com.active4j.web.core.web.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @title GlobalExceptionHandler.java
 * @description 
		全局异常管理
 * @time 2019年12月11日 下午5:09:21
 * @author 麻木神
 * @version 1.0
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	/**
	 * 
	 * @description
	 *  	实体字段校验  校验不通过抛出异常
	 * @return void
	 * @author 麻木神
	 * @time 2019年12月11日 下午5:25:57
	 */
	@ExceptionHandler(BindException.class)
	public void methodArgumentNotValidException(BindException ex, HttpServletRequest request, HttpServletResponse response) {
		//打印异常信息
		log.error(ex.getMessage());
		//返回前端
		List<ObjectError> errors = ex.getBindingResult().getAllErrors();
		if(null != errors && errors.size() > 0) {
			//构造返回前端json字符串
			ResultJson j = new ResultJson();
			j.setSuccess(false);
			j.setMsg(errors.get(0).getDefaultMessage());
			ResponseUtil.writeJson(response, j.toJsonStr());
		}
	}

	
	@ExceptionHandler(UnauthorizedException.class)
	public void unauthorizedException(UnauthorizedException ex, HttpServletRequest request, HttpServletResponse response) {
		//对于ajax异步请求，需要返回json结果
		ResponseUtil.writeJson(response, ResultJson.getErrorStr(GlobalConstant.Err_Msg_No_Auth));
	}
		
	
	@ExceptionHandler(Exception.class)
	public void exception(Exception ex, HttpServletRequest request, HttpServletResponse response) {
		//对于ajax异步请求，需要返回json结果
		ResponseUtil.writeJson(response, ResultJson.getErrorStr(GlobalConstant.Err_Msg_All));
	}
	
	
}
