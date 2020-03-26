package com.active4j.entity.system.entity;

import javax.validation.constraints.NotEmpty;

import com.active4j.entity.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典值
 * @author teli_
 *
 */
@TableName("sys_dic_value")
@Getter
@Setter
@ApiModel
public class SysDicValueEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1868320207479373423L;

	/**
	 * 显示值
	 */
	@TableField("LABEL")
	@NotEmpty(message="名称不能为空")
	@ApiModelProperty(name="字典值名称", value="字典值名称")
	private String label;
	
	/**
	 * 值
	 */
	@TableField("VALUE")
	@NotEmpty(message="值不能为空")
	@ApiModelProperty(name="字典值的值", value="字典值的值")
	private String value;
	
	/**
	 * 所属字典
	 */
	@TableField("PARENT_ID")
	@ApiModelProperty(name="字典ID", value="字典ID")
	private String parentId;
	
}
