package com.active4j.entity.func.timer.model;

import com.active4j.entity.func.timer.entity.QuartzJobEntity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class QuartzJobModel extends QuartzJobEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7170895391154630675L;

	/**
	 * 上次执行时间
	 */
	private String previous;
	
	/**
	 * 下次执行时间
	 */
	private String next;
}
