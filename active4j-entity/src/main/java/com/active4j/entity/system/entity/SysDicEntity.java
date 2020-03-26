package com.active4j.entity.system.entity;

import javax.validation.constraints.NotEmpty;

import com.active4j.entity.base.BaseEntity;
import com.active4j.entity.base.annotation.QueryField;
import com.active4j.entity.base.model.QueryCondition;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据字典表
 * @author teli_
 *
 */
@TableName("sys_dic")
@Getter
@Setter
@ApiModel
public class SysDicEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7956568515229889144L;

	/**
	 * 字典代码
	 */
	@TableField("CODE")
	@NotEmpty(message="字典编码不能为空")
	@ApiModelProperty(name="code", value="字典代码")
	private String code;
	
	/**
	 * 字段名称
	 */
	@TableField("NAME")
	@QueryField(condition=QueryCondition.like, queryColumn="NAME")
	@NotEmpty(message="字典名称不能为空")
	@ApiModelProperty(name="name", value="字典名称")
	private String name;
	
	/**
	 * 备注信息
	 */
	@TableField("MEMO")
	@ApiModelProperty(name="name", value="字典备注")
	private String memo;
	
}
