package com.active4j.entity.base.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 树形结构
 * @author teli_
 *
 */
@Getter
@Setter
public class TreeModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3544532890164540646L;

	private String id;
	
	private String title;
	
	private String parentId;
	
}
