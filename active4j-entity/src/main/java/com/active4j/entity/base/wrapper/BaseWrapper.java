package com.active4j.entity.base.wrapper;

import com.alibaba.fastjson.JSON;

import lombok.Getter;
import lombok.Setter;


/**
 * 基础返回页面实体
 * @author teli_
 *
 * @param <T>
 */
@Getter
@Setter
public class BaseWrapper<T> {

	private int code = 0;

	private String msg = "";
	
	private T data;
	
	public BaseWrapper(T data) {
		this.data = data;
	}
	
	
	/**
	 * 转为字符串 输出  如果需要改写 可以覆写此方法
	 * @return
	 */
	public String wrap() {
		return JSON.toJSONString(this);
	}
	
}
