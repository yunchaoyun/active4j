package com.active4j.entity.common;


/**
 * @title GlobalTimeConstant.java
 * @description 
		  全局时间常量类
 * @time  2020年1月2日 下午1:41:40
 * @author 麻木神
 * @version 1.0
*/
public class GlobalTimeConstant {

	/**
	 * 用户角色信息存入redis  缓存时间
	 */
	public static final long Shiro_Role_Redis_Time = 1 * 60 * 60l;
	
	/**
	 * 用户菜单 存入redis 时间
	 */
	public static final long User_Menu_Redis_Time = 1 * 60 * 60l;
}
