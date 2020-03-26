package com.active4j.entity.common.redis;


/**
 * @title GlobalRedisKey.java
 * @description 
		  公共reids key前缀
 * @time  2020年1月2日 下午1:24:00
 * @author 麻木神
 * @version 1.0
*/
public class GlobalRedisKey {

	/**
	 * 用户授权 存放用户角色编号set集合
	 */
	public static final String Redis_User_Role_Key = "Redis_User_Role_Key:";
	
	/**
	 * 用户授权 存放用户菜单set集合
	 */
	public static final String Redis_User_Menu_Key = "Redis_User_Menu_Key:";
	
	/**
	 * 存放用户菜单集合
	 */
	public static final String User_Menu_Key = "User_Menu_Key:";
}
