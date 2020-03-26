package com.active4j.entity.base.model;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Data;

@Data
public class MenuModel {
	
	@JSONField(serialize = false)
	private String id;

	private String title;
	
	private String name = "";
	
	private String icon;
	
	private boolean spread = false;
	
	private String jump;
	
	@JSONField(serialize = false)
	private int orderNo;
	
	@JSONField(serialize = false)
	private boolean hasChildren;
	
	@JSONField(serialize = false)
	private MenuModel parent;
	
	private List<MenuModel> list;

}
