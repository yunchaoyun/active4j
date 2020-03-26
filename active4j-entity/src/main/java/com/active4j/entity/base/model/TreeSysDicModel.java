package com.active4j.entity.base.model;

import lombok.Getter;
import lombok.Setter;


/**
 * 数据字典 显示树形结构
 * @author teli_
 *
 */
@Getter
@Setter
public class TreeSysDicModel {
	
	private String id;
	
	private String parentId;
	
	private String name;
	
	private String value;
	
	private String memo;
	

}
