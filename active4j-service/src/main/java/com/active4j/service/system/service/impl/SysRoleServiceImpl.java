package com.active4j.service.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.dao.system.dao.SysRoleDao;
import com.active4j.entity.system.entity.SysMenuEntity;
import com.active4j.entity.system.entity.SysRoleEntity;
import com.active4j.entity.system.entity.SysRoleMenuEntity;
import com.active4j.entity.system.entity.SysUserEntity;
import com.active4j.entity.system.entity.SysUserRoleEntity;
import com.active4j.service.system.service.SysMenuService;
import com.active4j.service.system.service.SysRoleMenuService;
import com.active4j.service.system.service.SysRoleService;
import com.active4j.service.system.service.SysUserRoleService;
import com.active4j.service.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 角色管理service类
 * @author teli_
 *
 */
@Service("sysRoleService")
@Transactional
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {
	
	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	
	@Autowired
	private SysMenuService sysMenuService;
	
	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	@Autowired
	private SysUserService sysUserService;
	
	/**
	 * 配置权限
	 * @param roleId
	 * @param roleMenuIds
	 */
	public void saveRoleMenu(String roleId, String roleMenuIds) {
		
		//删除之前的配置
		QueryWrapper<SysRoleMenuEntity> queryWrapper = new QueryWrapper<SysRoleMenuEntity>();
		queryWrapper.eq("ROLE_ID", roleId);
		sysRoleMenuService.remove(queryWrapper);
		
		if(StringUtils.isNotEmpty(roleMenuIds)) {
			//重新保存
			String[] ids = roleMenuIds.split(",");
			if(null != ids && ids.length > 0) {
				for(String menuId : ids) {
					SysRoleMenuEntity roleMenu = new SysRoleMenuEntity();
					roleMenu.setMenuId(menuId);
					roleMenu.setRoleId(roleId);
					sysRoleMenuService.save(roleMenu);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param roleId
	 * @return
	 */
	public List<String> getMenuByRole(String roleId) {
		List<String> lstMenuIds = new ArrayList<String>();
		QueryWrapper<SysRoleMenuEntity> queryWrapper = new QueryWrapper<SysRoleMenuEntity>();
		queryWrapper.eq("ROLE_ID", roleId);
		List<SysRoleMenuEntity> lstRoleMenu = sysRoleMenuService.list(queryWrapper);
		
		if(null != lstRoleMenu && lstRoleMenu.size() > 0) {
			lstRoleMenu.forEach(d -> {
				lstMenuIds.add(d.getMenuId());
			});
		}
		
		return lstMenuIds;
	}
	
	
	/**
	 * 根据用户角色，获取菜单
	 * @param roleIds
	 * @return
	 */
	public List<SysMenuEntity> getMeunByRoles(List<SysRoleEntity> lstRoles){
		List<SysMenuEntity> lstMenus = new ArrayList<SysMenuEntity>();
		
		if(null != lstRoles && lstRoles.size() > 0) {
			for(SysRoleEntity role : lstRoles) {
				QueryWrapper<SysRoleMenuEntity> queryWrapper = new QueryWrapper<SysRoleMenuEntity>();
				queryWrapper.eq("ROLE_ID", role.getId());
				List<SysRoleMenuEntity> lstRoleMenu = sysRoleMenuService.list(queryWrapper);
				if(null != lstRoleMenu && lstRoleMenu.size() > 0) {
					for(SysRoleMenuEntity roleMenu : lstRoleMenu) {
						lstMenus.add(sysMenuService.getById(roleMenu.getMenuId()));
					}
					
				}
			}
		}
		
		return lstMenus;
	}
	
	
	/**
	 * 根据角色信息，获取该角色的用户
	 * @param role
	 * @return
	 */
	public List<SysUserEntity> getUsersByRole(SysRoleEntity role) {
		
		QueryWrapper<SysUserRoleEntity> queryWrapper = new QueryWrapper<SysUserRoleEntity>();
		queryWrapper.eq("ROLE_ID", role.getId());
		List<SysUserRoleEntity> lstUserRoles = sysUserRoleService.list(queryWrapper);
		if(null != lstUserRoles && lstUserRoles.size() > 0) {
			//根据userids查询用户
			
			QueryWrapper<SysUserEntity> queryWrapperUser = new QueryWrapper<SysUserEntity>();
			queryWrapperUser.in("ID", lstUserRoles.stream().map(SysUserRoleEntity::getUserId).collect(Collectors.toList()));
			
			List<SysUserEntity> lstUsers = sysUserService.list(queryWrapperUser);
			
			return lstUsers;
		}
		
		return null;
	}
	
	
	/**
	 * 根据角色信息，获取该角色下的子角色
	 * @param role
	 * @return
	 */
	public List<SysRoleEntity> getChildRolesByRole(SysRoleEntity role){
		QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<SysRoleEntity>();
		queryWrapper.eq("PARENT_ID", role.getId());
		
		List<SysRoleEntity> lstRoles = this.list(queryWrapper);
		
		return lstRoles;
	}
	
	
	/**
	 * 删除角色，需要级联删除角色配置的菜单
	 * @param role
	 */
	public void deleteRole(SysRoleEntity role) {
		//删除角色跟菜单配置表
		QueryWrapper<SysRoleMenuEntity> queryWrapper = new QueryWrapper<SysRoleMenuEntity>();
		queryWrapper.eq("ROLE_ID", role.getId());
		sysRoleMenuService.remove(queryWrapper);
		
		this.removeById(role.getId());
		
	}
	
}
