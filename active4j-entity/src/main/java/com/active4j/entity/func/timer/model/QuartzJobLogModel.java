package com.active4j.entity.func.timer.model;

import com.active4j.entity.func.timer.entity.QuartzJobLogEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuartzJobLogModel extends QuartzJobLogEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3204386809491503419L;

	/**
	 * 开始执行时间
	 */
	private String start;
	
	/**
	 * 结束执行时间
	 */
	private String end;
}
