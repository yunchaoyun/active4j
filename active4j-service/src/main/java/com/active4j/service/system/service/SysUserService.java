package com.active4j.service.system.service;

import java.util.List;

import com.active4j.entity.base.model.ActiveUser;
import com.active4j.entity.base.model.MenuModel;
import com.active4j.entity.system.entity.SysRoleEntity;
import com.active4j.entity.system.entity.SysUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysUserService extends IService<SysUserEntity> {
	
	
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
	public List<String> findRoleNameByUserId(String userId);
	
	
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
	public List<String> findMenuUrlByUserId(String userId);

	
	public void saveUser(SysUserEntity user, String roleIds);
	
	
	/**
	 * 编辑用户， 角色修改
	 * @param user
	 * @param roleIds
	 */
	public void saveOrUpdateUser(SysUserEntity user, String roleIds);
	
	
	/**
	 * 删除用户
	 * @param user
	 */
	public void delete(SysUserEntity user);
	
	
	/**
	 * 获取用户所属的角色
	 * @param user
	 * @return
	 */
	public List<SysRoleEntity> getUserRoles(String userId);
	
	
	/**
	 * 根据用户名取得用户
	 * @param userName
	 * @return
	 */
	public SysUserEntity getUserByUseName(String userName);
	
	/**
	 * 根据用户信息  组装shiro session用户
	 * @param user
	 * @return
	 */
	public ActiveUser getActiveUserByUser(SysUserEntity user);
	
	/**
	 * 
	 * @description
	 *  	获取用户可见菜单
	 * @params
	 *      用户ID
	 * @return String
	 * @author 麻木神
	 * @time 2020年1月2日 下午2:45:45
	 */
	public List<MenuModel> getActiveUserMenu(String userId);
}
