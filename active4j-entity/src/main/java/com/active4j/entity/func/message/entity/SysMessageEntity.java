package com.active4j.entity.func.message.entity;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.active4j.entity.base.BaseEntity;
import com.active4j.entity.base.annotation.QueryField;
import com.active4j.entity.base.model.QueryCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @title SysMessageEntity.java
 * @description 
		系统消息实体
 * @time  2019年12月18日 下午3:51:40
 * @author guyp
 * @version 1.0
 */
@TableName("func_sys_message")
@Setter
@Getter
public class SysMessageEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8910614354694592687L;

	/**
	 * 消息类型（0：系统消息，1：其他消息）
	 */
	public static final String Sys_Message_Type_System = "0";
	public static final String Sys_Message_Type_Other = "1";
	
	/**
	 * 消息状态（0：草稿，1：已发布）
	 */
	public static final String Sys_Message_Status_Draft = "0";
	public static final String Sys_Message_Status_Publish = "1";
	
	/**
	 * 消息标题
	 */
	@TableField("TITLE")
	@QueryField(queryColumn="TITLE", condition=QueryCondition.eq)
	@NotEmpty(message="消息标题不能为空")
	@Size(min = 0, max=100, message="消息标题最多只能输入100个字符")
	private String title;
	
	/**
	 * 消息内容
	 */
	@TableField("CONTENT")
	@NotEmpty(message="消息内容不能为空")
	@Size(min = 0, max=500, message="消息内容最多只能输入500个字符")
	private String content;
	
	/**
	 * 消息类型（0：系统消息，1：其他消息）
	 */
	@TableField("TYPE")
	@QueryField(queryColumn="TYPE", condition=QueryCondition.eq)
	@NotEmpty(message="消息类型不能为空")
	@Size(min = 0, max=10, message="消息类型最多只能输入10个字符")
	private String type;
	
	/**
	 * 消息状态（0：草稿，1：已发布）
	 */
	@TableField("STATUS")
	@QueryField(queryColumn="STATUS", condition=QueryCondition.eq)
	private String status;
	
	/**
	 * 发布时间
	 */
	@TableField("PUBLIC_TIME")
	private Date publicTime;
	
}
