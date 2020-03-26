package com.active4j.monitor.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @title ServerInfoDomain.java
 * @description 
		  服务器监控信息返回实体
 * @time  2019年12月3日 下午4:04:43
 * @author 麻木神
 * @version 1.0
*/
@Getter
@Setter
public class ServerInfoModel {

	private JVM jvm;
	
	private SYS sys;
	
	private CPU cpu;
	
	private MEM mem;
	
	private List<FileSys> lstFileSys;
	
}
