package com.active4j.entity.base.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class ActiveUser implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6270912302455345492L;

	private String userName;
	
	private String id;
	
	private String realName;
	
	private String avatar;
	
	private String deptName;
	
}
