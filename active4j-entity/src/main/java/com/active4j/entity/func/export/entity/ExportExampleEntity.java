package com.active4j.entity.func.export.entity;

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
 * @title ExportExampleEntity.java
 * @description 
		导入导出示例实体
 * @time  2019年12月17日 上午9:43:50
 * @author guyp
 * @version 1.0
 */
@TableName("func_export_example")
@Setter
@Getter
public class ExportExampleEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8486583273348702746L;
	
	/**
	 * 姓名
	 */
	@TableField("NAME")
	@QueryField(queryColumn="NAME", condition=QueryCondition.eq)
	private String name;
	
	/**
	 * 性别
	 */
	@TableField("SEX")
	private String sex;
	
	/**
	 * 年龄
	 */
	@TableField("AGE")
	private int age;
	
	/**
	 * 手机号
	 */
	@TableField("PHONE")
	private String phone;
	
	/**
	 * 生日
	 */
	@TableField("BIRTHDAY")
	private Date birthday;
	
	/**
	 * 余额
	 */
	@TableField("BALANCE")
	private double balance;
	
}
