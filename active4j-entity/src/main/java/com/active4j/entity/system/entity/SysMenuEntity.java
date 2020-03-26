package com.active4j.entity.system.entity;

import javax.validation.constraints.NotEmpty;

import com.active4j.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;


/**
 * 
 * @author teli_
 *
 */
@TableName("sys_menu")
@Getter
@Setter
public class SysMenuEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5853913751550999700L;

	/**
	 * 菜单名称
	 */
	@TableField("NAME")
	@NotEmpty(message="菜单名称不能为空")
	private String name;
	
	/**
	 * 父级菜单ID
	 */
	@TableField("PARENT_ID")
	private String parentId;
	
	/**
	 * 菜单类型        0：菜单        1： 按钮
	 */
	@TableField("TYPE")
	@NotEmpty(message="菜单类型不能为空")
	private String type;
	
	/**
	 * 菜单URL
	 */
	@TableField("URL")
	private String url;
	
	/**
	 * 菜单图标
	 */
	@TableField("ICON")
	private String icon;
	
	/**
	 * 菜单排序
	 */
	@TableField("ORDER_NO")
	private int orderNo;
	
	/**
	 * 备注
	 */
	@TableField("MEMO")
	private String memo;
	
}
