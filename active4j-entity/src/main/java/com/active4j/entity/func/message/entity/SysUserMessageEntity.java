package com.active4j.entity.func.message.entity;

import java.util.Date;

import com.active4j.entity.base.BaseEntity;
import com.active4j.entity.base.annotation.QueryField;
import com.active4j.entity.base.model.QueryCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @title UserSysMessageEntity.java
 * @description 
		用户系统消息实体
 * @time  2019年12月19日 上午9:53:40
 * @author guyp
 * @version 1.0
 */
@TableName("func_sys_user_message")
@Setter
@Getter
public class SysUserMessageEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8617754940775715128L;
	
	/**
	 * 消息是否阅读（0：未读，1：已读）
	 */
	public static final String Sys_User_Message_Read_No = "0";
	public static final String Sys_User_Message_Read_Yes = "1";
	
	/**
	 * 消息是否删除（0：未删除，1：已删除）
	 */
	public static final String Sys_User_Message_Delete_No = "0";
	public static final String Sys_User_Message_Delete_Yes = "1";
	
	/**
	 * 用户id
	 */
	@TableField("USER_ID")
	@QueryField(queryColumn="USER_ID", condition=QueryCondition.eq)
	private String userId;

	/**
	 * 消息标题
	 */
	@TableField("TITLE")
	private String title;
	
	/**
	 * 消息内容
	 */
	@TableField("CONTENT")
	private String content;
	
	/**
	 * 消息类型（0：系统消息，1：其他消息）
	 */
	@TableField("TYPE")
	private String type;
	
	/**
	 * 发布时间
	 */
	@TableField("PUBLIC_TIME")
	private Date publicTime;
	
	/**
	 * 消息是否阅读（0：未读，1：已读）
	 */
	@TableField("READ_STATUS")
	private String readStatus;
	
	/**
	 * 消息是否删除（0：未删除，1：已删除）
	 */
	@TableField("DELETE_STATUS")
	@QueryField(queryColumn="DELETE_STATUS", condition=QueryCondition.eq)
	private String deleteStatus;
	
	/**
	 * 系统消息id
	 */
	@TableField("SYS_MESSAGE_ID")
	private String sysMessageId;
	
}
