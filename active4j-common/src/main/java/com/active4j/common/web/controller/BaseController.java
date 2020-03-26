package com.active4j.common.web.controller;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.active4j.common.web.config.DateConvertEditor;
import com.active4j.common.web.config.IntegerConvertEditor;

/**
 * 基础controller
 * @author teli_
 *
 */
public class BaseController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new DateConvertEditor());
		binder.registerCustomEditor(int.class, new IntegerConvertEditor());
	}
}
