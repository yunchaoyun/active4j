package com.active4j.entity.common;


/**
 * 全局常量类
 * @author teli_
 *
 */
public class GlobalConstant {
	
	/**
	 * 验证码存放Key
	 */
	public static final String SESSION_KEY_OF_RAND_CODE = "randCode"; // todo 要统一常量

	/**
	 * layui表格默认排序赋值的字段
	 */
	public static final String Order_Field = "field";//排序字段
	public static final String Order_Type = "order";//排序类型
	
	/**
	 * 没有权限进行此操作
	 */
	public static final String Err_Msg_No_Auth = "没有权限进行此操作";
	public static final String Err_Msg_All = "系统错误，请联系管理员";
	
	/**
	 * 默认创建人
	 */
	public static final String Default_Create_Name = "system";
	
	/**
	 * 定时任务执行目标key
	 */
	public static final String QUARTZ_JOB_PARAM_KEY = "QUARTZ_JOB_PARAM_KEY";
}
