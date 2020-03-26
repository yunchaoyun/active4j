package com.active4j.entity.base.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

/**
 * 公共返回实体
 * @author teli_
 *
 */
@Getter
@Setter
public class ResultJson {

	private int code = 0;
	
	private boolean success = true;
	
	private String msg = "操作成功";
	
	private Object data;
	
	/**
	 * 
	 * @description
	 *  	转换为json字符串
	 * @params
	 * @return String
	 * @author guyp
	 * @time 2019年12月27日 下午4:15:05
	 */
	public String toJsonStr() {
		JSONObject obj = new JSONObject();
		obj.put("code", this.code);
		obj.put("success", this.isSuccess());
		obj.put("msg", this.getMsg());
		obj.put("data", this.data);
		return obj.toJSONString();
	}
	
	/**
	 * 直接返回错误信息
	 * @param msg
	 * @return
	 */
	public static String getErrorStr(String msg) {
		ResultJson j = new ResultJson();
		j.setSuccess(false);
		j.setMsg(msg);
		
		return JSON.toJSONString(j);
	}
}
