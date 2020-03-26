package com.active4j.monitor.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @title SYS.java
 * @description 
		  描述服务器信息
 * @time  2019年12月3日 下午5:14:00
 * @author 麻木神
 * @version 1.0
*/
@Getter
@Setter
public class SYS {

	/**
	 * 型号
	 */
	private String name;
	
	/**
	 * 生产商
	 */
	private String productor;
	
	
	/**
	 * 内核数
	 */
	private String core;
	
	
	/**
	 * 逻辑处理器
	 */
	private String cpus;
	
	/**
	 * 操作系统
	 */
	private String os;
	
	/**
	 * 系统架构
	 */
	private String structure;
	
}
