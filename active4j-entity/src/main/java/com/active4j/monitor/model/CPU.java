package com.active4j.monitor.model;

import com.active4j.common.util.ArithUtil;

/**
 * 
 * @title CPU.java
 * @description 
		  服务器信息  CPU
 * @time  2019年12月4日 上午10:38:34
 * @author 麻木神
 * @version 1.0
 */
public class CPU {
	/**
	 * 核心数
	 */
	private int cpuNum;

	/**
	 * CPU总的使用率
	 */
	private double total;

	/**
	 * CPU系统使用率
	 */
	private double sys;

	/**
	 * CPU用户使用率
	 */
	private double used;

	/**
	 * CPU当前等待率
	 */
	private double wait;

	/**
	 * CPU当前空闲率
	 */
	private double free;

	public int getCpuNum() {
		return cpuNum;
	}

	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}

	public double getTotal() {
		return ArithUtil.round(ArithUtil.mul(total, 100), 2);
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getSys() {
		return ArithUtil.round(ArithUtil.mul(sys / total, 100), 2);
	}

	public void setSys(double sys) {
		this.sys = sys;
	}

	public double getUsed() {
		return ArithUtil.round(ArithUtil.mul(used / total, 100), 2);
	}

	public void setUsed(double used) {
		this.used = used;
	}

	public double getWait() {
		return ArithUtil.round(ArithUtil.mul(wait / total, 100), 2);
	}

	public void setWait(double wait) {
		this.wait = wait;
	}

	public double getFree() {
		return ArithUtil.round(ArithUtil.mul(free / total, 100), 2);
	}

	public void setFree(double free) {
		this.free = free;
	}
}
