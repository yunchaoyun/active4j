package com.active4j.monitor.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @title FileSys.java
 * @description
 * @time 2019年12月4日 上午9:37:07
 * @author 麻木神
 * @version 1.0
 */
@Getter
@Setter
public class FileSys {
	/**
	 * 盘符路径
	 */
	private String dirName;

	/**
	 * 盘符类型
	 */
	private String sysTypeName;

	/**
	 * 文件类型
	 */
	private String typeName;

	/**
	 * 总大小
	 */
	private String total;

	/**
	 * 剩余大小
	 */
	private String free;

	/**
	 * 已经使用量
	 */
	private String used;

	/**
	 * 资源的使用率
	 */
	private double usage;
}
