package com.active4j.entity.system.entity;

import java.util.Date;

import com.active4j.entity.base.BaseEntity;
import com.active4j.entity.base.annotation.QueryField;
import com.active4j.entity.base.model.QueryCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统日志
 * 
 * @author teli_
 *
 */
@TableName("sys_log")
@Getter
@Setter
public class SysLogEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -184403479101795727L;

	/**
	 * 日志类型 1:登录  2:退出  3:保存  4:新增  5:删除 6:更新  7:正常  8:异常
	 */
	@TableField("TYPE")
	@QueryField(queryColumn="TYPE", condition=QueryCondition.eq)
	private String type;

	/**
	 * 名称
	 */
	@TableField("NAME")
	private String name;

	/**
	 * 用户名
	 */
	@TableField("USER_NAME")
	@QueryField(queryColumn="USER_NAME", condition=QueryCondition.eq)
	private String userName;

	/**
	 * 操作类名
	 */
	@TableField("CLASS_NAME")
	private String className;

	/**
	 * 方法名
	 */
	@TableField("METHOD_NAME")
	private String methodName;

	/**
	 * 参数
	 */
	@TableField("PARAMS")
	private String params;

	/**
	 * IP
	 */
	@TableField("IP")
	private String ip;

	/**
	 * 浏览器类型
	 */
	@TableField("BROSWER")
	private String broswer;

	/**
	 * 操作时间
	 */
	@TableField("OPERATOR_TIME")
	@QueryField(queryColumn="OPERATOR_TIME", condition=QueryCondition.range)
	private Date operatorTime;

	/**
	 * 备注 补充信息
	 */
	@TableField("MEMO")
	private String memo;

}
