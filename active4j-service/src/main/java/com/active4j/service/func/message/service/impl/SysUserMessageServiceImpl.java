package com.active4j.service.func.message.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.dao.func.message.dao.SysUserMessageDao;
import com.active4j.entity.func.message.entity.SysUserMessageEntity;
import com.active4j.service.func.message.service.SysUserMessageService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 
 * @title SysUserMessageServiceImpl.java
 * @description 
		用户系统消息
 * @time  2019年12月19日 上午10:08:50
 * @author guyp
 * @version 1.0
 */
@Service("sysUserMessageService")
@Transactional
public class SysUserMessageServiceImpl extends ServiceImpl<SysUserMessageDao, SysUserMessageEntity> implements SysUserMessageService {
	
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
	public void deleteMsg(String ids) {
		String[] strs = ids.split(",");
		if(null != strs && strs.length > 0) {
			List<SysUserMessageEntity> lstMessages = new ArrayList<SysUserMessageEntity>();
			for(String id : strs) {
				//删除消息， 设为删除状态
				SysUserMessageEntity msg = this.getById(id);
				msg.setDeleteStatus(SysUserMessageEntity.Sys_User_Message_Delete_Yes);
				lstMessages.add(msg);
			}
			//批量保存
			this.saveOrUpdateBatch(lstMessages);
		}
		
	}

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
	public void readMsg(String ids) {
		String[] strs = ids.split(",");
		if(null != strs && strs.length > 0) {
			List<SysUserMessageEntity> lstMessages = new ArrayList<SysUserMessageEntity>();
			for(String id : strs) {
				//设为已读状态
				SysUserMessageEntity msg = this.getById(id);
				msg.setReadStatus(SysUserMessageEntity.Sys_User_Message_Read_Yes);
				lstMessages.add(msg);
			}
			//批量保存
			this.saveOrUpdateBatch(lstMessages);
		}
		
	}

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
	public void readAllMsg(String userId) {
		//根据条件获取未读的消息
		QueryWrapper<SysUserMessageEntity> queryWrapper = new QueryWrapper<SysUserMessageEntity>();
		queryWrapper.eq("USER_ID", userId);
		queryWrapper.eq("DELETE_STATUS", SysUserMessageEntity.Sys_User_Message_Delete_No);
		queryWrapper.eq("READ_STATUS", SysUserMessageEntity.Sys_User_Message_Read_No);
		List<SysUserMessageEntity> lstMsgs = this.list(queryWrapper);
		//将消息设为已读，保存
		if(null != lstMsgs && lstMsgs.size() > 0) {
			lstMsgs.parallelStream().forEach(d -> {
				d.setReadStatus(SysUserMessageEntity.Sys_User_Message_Read_Yes);
				this.saveOrUpdate(d);
			});
		}
	}
}
