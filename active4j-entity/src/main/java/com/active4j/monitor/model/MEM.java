package com.active4j.monitor.model;

import com.active4j.common.util.ArithUtil;

/**
 * 
 * @title MEM.java
 * @description 服务器监控信息 内存信息
 * @time 2019年12月4日 上午11:20:08
 * @author 麻木神
 * @version 1.0
 */
public class MEM {
	/**
	 * 内存总量
	 */
	private double total;

	/**
	 * 已用内存
	 */
	private double used;

	/**
	 * 剩余内存
	 */
	private double free;
	
	/**
	 * jvm 内存总量
	 */
	private double jvmTotal;
	
	/**
	 * jvm 已使用
	 */
	private double jvmUsed;
	
	/**
	 * jvm 空余
	 */
	private double jvmFree;

	public double getTotal() {
		return ArithUtil.div(total, (1024 * 1024 * 1024), 2);
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public double getUsed() {
		return ArithUtil.div(used, (1024 * 1024 * 1024), 2);
	}

	public void setUsed(long used) {
		this.used = used;
	}

	public double getFree() {
		return ArithUtil.div(free, (1024 * 1024 * 1024), 2);
	}

	public void setFree(long free) {
		this.free = free;
	}

	public double getUsage() {
		return ArithUtil.mul(ArithUtil.div(used, total, 4), 100);
	}
	
	public double getJvmUsage(){
        return ArithUtil.mul(ArithUtil.div(jvmTotal - jvmFree, jvmTotal, 4), 100);
    }
	public double getJvmTotal() {
		return  ArithUtil.div(jvmTotal, (1024 * 1024), 2);
	}

	public void setJvmTotal(double jvmTotal) {
		this.jvmTotal = jvmTotal;
	}

	public double getJvmUsed() {
		return ArithUtil.div(jvmTotal - jvmFree, (1024 * 1024), 2);
	}

	public void setJvmUsed(double jvmUsed) {
		this.jvmUsed = jvmUsed;
	}

	public double getJvmFree() {
		return ArithUtil.div(jvmFree, (1024 * 1024), 2);
	}

	public void setJvmFree(double jvmFree) {
		this.jvmFree = jvmFree;
	}
	
}