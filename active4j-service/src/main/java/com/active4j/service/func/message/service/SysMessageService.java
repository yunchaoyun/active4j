package com.active4j.service.func.message.service;

import com.active4j.entity.func.message.entity.SysMessageEntity;
import com.active4j.entity.func.message.model.SysMessageModel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 
 * @title SysMessageService.java
 * @description 
		系统消息
 * @time  2019年12月18日 下午4:02:07
 * @author guyp
 * @version 1.0
 */
public interface SysMessageService extends IService<SysMessageEntity> {
	
	/**
	 * 
	 * @description
	 *  	获取系统消息明细
	 * @params
	 *      id 系统消息id
	 * @return SysMessageModel
	 * @author guyp
	 * @time 2019年12月18日 下午5:01:13
	 */
	public SysMessageModel getMsgDetailModel(String id);
	
	/**
	 * 
	 * @description
	 *  	发布系统消息
	 * @params
	 *      sysMessageEntity 系统消息实体
	 * @return void
	 * @author guyp
	 * @time 2019年12月19日 上午9:31:54
	 */
	public void publishSysMsg(SysMessageEntity sysMessageEntity);
}
