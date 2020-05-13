package com.active4j.web.core.web.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 响应类 处理往客户端返回值
 * 
 * @author teli_
 *
 */
@Slf4j
public class ResponseUtil {

	
	/**
	 * 将字符串写出到客户端  
	 * @param response
	 * @param result
	 */
	public static void write(HttpServletResponse response, String result) {
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Cache-Control", "no-store");
		try {
			PrintWriter pw = response.getWriter();
			pw.write(result);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			log.error("往客户端写出数据报错，错误信息:" + e.getMessage());
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 返回json格式数据
	 * @param response
	 * @param result
	 */
	public static void writeJson(HttpServletResponse response, String result) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store");
		try {
			PrintWriter pw = response.getWriter();
			pw.write(result);
			pw.flush();
			pw.close();
		} catch (IOException e) {
			log.error("往客户端写出数据报错，错误信息:" + e.getMessage());
			log.error(e.getMessage(), e);
		}
	}

}
