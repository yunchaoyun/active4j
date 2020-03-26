package com.active4j.monitor.model;

/**
 * @title JVM.java
 * @description 
		  jvm 模型
 * @time  2019年12月3日 下午4:31:54
 * @author 麻木神
 * @version 1.0
*/
public class JVM {

	/**
	 * jdk名称
	 */
	private String name;
	
	/**
	 * jdk版本
	 */
	private String version;
	
	/**
	 * 虚拟机启动时间
	 */
	private String startTime;
	
	/**
	 * 虚拟机运行时间
	 */
	private String runTime;
	
	/**
	 * jdk安装路径
	 */
	private String home;
	
	/**
	 * 项目路径
	 */
	private String userDir;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getUserDir() {
		return userDir;
	}

	public void setUserDir(String userDir) {
		this.userDir = userDir;
	}
	
	
}
