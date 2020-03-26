package com.active4j.entity.system.entity;

import javax.validation.constraints.NotEmpty;

import com.active4j.entity.base.BaseEntity;
import com.active4j.entity.base.annotation.QueryField;
import com.active4j.entity.base.model.QueryCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;


/**
 * 系统管理	用户管理
 * @author teli_
 *
 */
@TableName("sys_user")
@Getter
@Setter
public class SysUserEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8537482314791501052L;

	/**
	 * 用户名
	 */
	@TableField("USER_NAME")
	@QueryField(queryColumn="USER_NAME", condition=QueryCondition.eq)
	@NotEmpty(message="用户名不能为空")
	private String userName;
	
	/**
	 * 真实姓名
	 */
	@TableField("REAL_NAME")
	@QueryField(queryColumn="REAL_NAME", condition=QueryCondition.like)
	@NotEmpty(message="真实姓名不能为空")
	private String realName;
	
	/**
	 *  密码
	 */
	@TableField("PASSWORD")
	private String password;
	
	/**
	 * 盐
	 */
	@TableField("SALT")
	private String salt;
	
	/**
	 * 头像
	 */
	@TableField("HEAD_IMG_URL")
	private String headImgUrl;
	
	/**
	 * 用户状态
	 */
	@TableField("STATUS")
	@QueryField(queryColumn="STATUS", condition=QueryCondition.eq)
	@NotEmpty(message="用户状态不能为空")
	private String status;
	
	/**
	 * 性别
	 */
	@TableField("SEX")
	@NotEmpty(message="性别不能为空")
	private String sex;
	
	/**
	 * 所属部门
	 */
	@TableField("DEPT_ID")
	@QueryField(queryColumn="DEPT_ID", condition=QueryCondition.eq)
	@NotEmpty(message="所属部门不能为空")
	private String deptId;
	
	/**
	 * 电子邮箱
	 */
	@TableField("EMAIL")
	@NotEmpty(message="电子邮箱不能为空")
	private String email;
	
	/**
	 * 手机号
	 */
	@TableField("TEL_NO")
	@NotEmpty(message="手机号不能为空")
	private String telNo;
	
	/**
	 * 用户编号
	 */
	@TableField("USER_NO")
	@NotEmpty(message="用户编号不能为空")
	private String userNo;
	
	/**
	 * 备注
	 */
	@TableField("MEMO")
	private String memo;
	
}
