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
 * 系统管理   部门信息
 * @author teli_
 *
 */
@TableName("sys_depart")
@Getter
@Setter
public class SysDepartEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5711712775727682901L;

	/**
	 * 部门编号
	 */
	@TableField("DEPT_NO")
	@QueryField(queryColumn="DEPT_NO")
	@NotEmpty(message="部门编号不能为空")
	private String deptNo;
	
	/**
	 * 部门名称  全称
	 */
	@TableField("NAME")
	@NotEmpty(message="部门名称不能为空")
	private String name;
	
	/**
	 * 部门名称  简称
	 */
	@TableField("SHORT_NAME")
	@QueryField(queryColumn="SHORT_NAME", condition=QueryCondition.like)
	@NotEmpty(message="部门简称不能为空")
	private String shortName;
	
	/**
	 * 上级部门ID
	 */
	@TableField("PARENT_ID")
	private String parentId;
	
	/**
	 * 部门类型  0：集团      1： 公司：     2： 部门      3： 小组
	 */
	@TableField("TYPE")
	@NotEmpty(message="部门类型不能为空")
	private String type;
	
	/**
	 * 部门排序
	 */
	@TableField("ORDER_NO")
	private int orderNo;
	
	/**
	 * 备注
	 */
	@TableField("MEMO")
	private String memo;
	
}
