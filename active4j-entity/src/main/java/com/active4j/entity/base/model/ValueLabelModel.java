package com.active4j.entity.base.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;


/**
 * 用户返回前端的值模型，  适用前端select
 * @author teli_
 *
 */
@Getter
@Setter
public class ValueLabelModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4736356073744665935L;

	
	private String value;
	
	private String label;
	
}
