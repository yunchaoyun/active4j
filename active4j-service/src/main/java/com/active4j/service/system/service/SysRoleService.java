package com.active4j.service.system.service;

import java.util.List;

import com.active4j.entity.system.entity.SysMenuEntity;
import com.active4j.entity.system.entity.SysRoleEntity;
import com.active4j.entity.system.entity.SysUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysRoleService extends IService<SysRoleEntity> {
	
	
	/**
	 * 配置权限
	 * @param roleId
	 * @param roleMenuIds
	 */
	public void saveRoleMenu(String roleId, String roleMenuIds);
	
	
	/**
	 * 
	 * @param roleId
	 * @return
	 */
	public List<String> getMenuByRole(String roleId);
	
	/**
	 * 根据用户角色，获取菜单
	 * @param roleIds
	 * @return
	 */
	public List<SysMenuEntity> getMeunByRoles(List<SysRoleEntity> lstRoles);
	
	
	/**
	 * 根据角色信息，获取该角色的用户
	 * @param role
	 * @return
	 */
	public List<SysUserEntity> getUsersByRole(SysRoleEntity role);
	
	
	/**
	 * 根据角色信息，获取该角色下的子角色
	 * @param role
	 * @return
	 */
	public List<SysRoleEntity> getChildRolesByRole(SysRoleEntity role);
	
	
	/**
	 * 删除角色，需要级联删除角色配置的菜单
	 * @param role
	 */
	public void deleteRole(SysRoleEntity role);
}
