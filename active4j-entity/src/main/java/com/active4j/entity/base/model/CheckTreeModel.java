package com.active4j.entity.base.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckTreeModel extends TreeModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8848407803934991452L;

	
	private List<CheckStatus> checkArr = new ArrayList<CheckStatus>();
	
	
	
}
