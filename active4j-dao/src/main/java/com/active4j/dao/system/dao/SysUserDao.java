package com.active4j.dao.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.active4j.entity.system.entity.SysMenuEntity;
import com.active4j.entity.system.entity.SysUserEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SysUserDao extends BaseMapper<SysUserEntity>{

	
	/**
	 * 
	 * @description
	 *  	根据用户ID 获取用户所属角色名称
	 * @params
	 *      用户ID
	 * @return List<String>
	 * @author 麻木神
	 * @time 2020年1月2日 下午8:37:23
	 */
	public List<String> findRoleNameByUserId(@Param("userId") String userId);
	
	
	/**
	 * 
	 * @description
	 *  	根据用户ID 获取用户所有菜单URL
	 * @params
	 *      用户ID
	 * @return List<String>
	 * @author 麻木神
	 * @time 2020年1月2日 下午8:55:58
	 */
	public List<String> findMenuUrlByUserId(@Param("userId") String userId);
	
	/**
	 * 
	 * @description
	 *  	根据用户ID 获取用户所有菜单
	 * @params
	 *      userId 用户ID
	 * @return List<SysMenuEntity>
	 * @author guyp
	 * @time 2020年1月3日 下午1:18:08
	 */
	public List<SysMenuEntity> findMenuByUserId(@Param("userId") String userId);
}
