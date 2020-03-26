package com.active4j.entity.func.message.model;

import com.active4j.entity.func.message.entity.SysMessageEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SysMessageModel extends SysMessageEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -57398197309665210L;

	/**
	 * 发布时间
	 */
	private String time;
	
}
