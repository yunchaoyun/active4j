package com.active4j.service.func.message.service;

import com.active4j.entity.func.message.entity.SysUserMessageEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 
 * @title SysUserMessageService.java
 * @description 
		用户系统消息
 * @time  2019年12月19日 上午10:07:14
 * @author guyp
 * @version 1.0
 */
public interface SysUserMessageService extends IService<SysUserMessageEntity> {
	
	/**
	 * 
	 * @description
	 *  	删除用户消息
	 * @params
	 *      ids 用户消息ids
	 * @return void
	 * @author guyp
	 * @time 2020年1月4日 上午10:48:50
	 */
	public void deleteMsg(String ids);
	
	/**
	 * 
	 * @description
	 *  	已读用户消息
	 * @params
	 *      id 用户消息ids
	 * @return void
	 * @author guyp
	 * @time 2020年1月4日 上午10:51:17
	 */
	public void readMsg(String ids);
	
	/**
	 * 
	 * @description
	 *  	已读全部用户消息
	 * @params
	 * 		userId 用户id
	 * @return void
	 * @author guyp
	 * @time 2020年1月4日 上午10:53:44
	 */
	public void readAllMsg(String userId);
}
