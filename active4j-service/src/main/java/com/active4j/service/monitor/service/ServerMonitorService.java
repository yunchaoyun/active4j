package com.active4j.service.monitor.service;

import com.active4j.monitor.model.ServerInfoModel;

/**
 * 
 * @title ServerMonitorService.java
 * @description 
		服务器监控
 * @time  2019年12月31日 下午1:51:44
 * @author guyp
 * @version 1.0
 */
public interface ServerMonitorService {
	
	/**
	 * 
	 * @description
	 *  	获取服务器信息
	 * @params
	 * @return ServerInfoModel
	 * @author guyp
	 * @time 2019年12月31日 下午1:52:12
	 */
	public ServerInfoModel getServer();
}
