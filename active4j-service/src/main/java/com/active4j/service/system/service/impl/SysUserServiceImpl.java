package com.active4j.service.system.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.common.redis.RedisApi;
import com.active4j.dao.system.dao.SysUserDao;
import com.active4j.entity.base.model.ActiveUser;
import com.active4j.entity.base.model.MenuModel;
import com.active4j.entity.common.GlobalTimeConstant;
import com.active4j.entity.common.redis.GlobalRedisKey;
import com.active4j.entity.system.entity.SysMenuEntity;
import com.active4j.entity.system.entity.SysRoleEntity;
import com.active4j.entity.system.entity.SysUserEntity;
import com.active4j.entity.system.entity.SysUserRoleEntity;
import com.active4j.service.system.service.SysRoleService;
import com.active4j.service.system.service.SysUserRoleService;
import com.active4j.service.system.service.SysUserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 用户管理service类
 * @author teli_
 *
 */
@Service("sysUserService")
@Transactional
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private RedisApi redisApi;
	
	
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
	public List<String> findRoleNameByUserId(String userId) {
		return this.baseMapper.findRoleNameByUserId(userId);
	}
	
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
	public List<String> findMenuUrlByUserId(String userId) {
		return this.baseMapper.findMenuUrlByUserId(userId);
	}
	
	/**
	 * 保存用户， 角色保存
	 */
	public void saveUser(SysUserEntity user, String roleIds) {
		
		super.save(user);
		
		if(StringUtils.isNotEmpty(roleIds)) {
			String[] roles = roleIds.split(",");
			for(String roleId : roles) {
				SysUserRoleEntity userRole = new SysUserRoleEntity();
				userRole.setRoleId(roleId);
				userRole.setUserId(user.getId());
				sysUserRoleService.save(userRole);
			}
		}
		
	}
	
	/**
	 * 编辑用户， 角色修改
	 * @param user
	 * @param roleIds
	 */
	public void saveOrUpdateUser(SysUserEntity user, String roleIds) {
		
		//先删除之前的角色
		QueryWrapper<SysUserRoleEntity> queryWrapper = new QueryWrapper<SysUserRoleEntity>();
		queryWrapper.eq("USER_ID", user.getId());
		sysUserRoleService.remove(queryWrapper);
		
		//保存用户角色
		if(StringUtils.isNotEmpty(roleIds)) {
			String[] roles = roleIds.split(",");
			for(String roleId : roles) {
				SysUserRoleEntity userRole = new SysUserRoleEntity();
				userRole.setRoleId(roleId);
				userRole.setUserId(user.getId());
				sysUserRoleService.save(userRole);
			}
		}
		
		//修改用户信息
		super.saveOrUpdate(user);
	}
	
	/**
	 * 删除用户
	 * @param user
	 */
	public void delete(SysUserEntity user) {
		//先删除之前的角色
		QueryWrapper<SysUserRoleEntity> queryWrapper = new QueryWrapper<SysUserRoleEntity>();
		queryWrapper.eq("USER_ID", user.getId());
		sysUserRoleService.remove(queryWrapper);
		
		//删除用户
		super.removeById(user.getId());
	}
	
	
	/**
	 * 获取用户所属的角色
	 * @param user
	 * @return
	 */
	public List<SysRoleEntity> getUserRoles(String userId){
		List<SysRoleEntity> lstRoles = new ArrayList<SysRoleEntity>();
		QueryWrapper<SysUserRoleEntity> queryWrapper = new QueryWrapper<SysUserRoleEntity>();
		queryWrapper.eq("USER_ID", userId);
		List<SysUserRoleEntity> lstUserRoles = sysUserRoleService.list(queryWrapper);
		if(null != lstUserRoles && lstUserRoles.size() > 0) {
			for(SysUserRoleEntity userRole : lstUserRoles) {
				lstRoles.add(sysRoleService.getById(userRole.getRoleId()));
			}
		}
		
		return lstRoles;
		
	}
	
	
	/**
	 * 根据用户名取得用户
	 * @param userName
	 * @return
	 */
	public SysUserEntity getUserByUseName(String userName) {
		QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<SysUserEntity>();
		queryWrapper.eq("USER_NAME", userName);
		List<SysUserEntity> lstUserRoles = this.list(queryWrapper);
		
		if(null != lstUserRoles && lstUserRoles.size() > 0) {
			return lstUserRoles.get(0);
		}
		
		return null;
	}
	
	/**
	 * 根据用户信息  组装shiro session用户
	 * @param user
	 * @return
	 */
	public ActiveUser getActiveUserByUser(SysUserEntity user) {
		ActiveUser activeUser = new ActiveUser();
		activeUser.setId(user.getId());
		activeUser.setRealName(user.getRealName());
		activeUser.setUserName(user.getUserName());
		
		return activeUser;
	}
	
	
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
	public List<MenuModel> getActiveUserMenu(String userId) {
		List<MenuModel> menus = null;
		
		//先从redis缓存中获取
		String menuStr = (String)redisApi.get(GlobalRedisKey.User_Menu_Key + userId);
		if(StringUtils.isEmpty(menuStr)) {
			//缓存中没有  从数据库查询
			List<SysMenuEntity> lstMenus = this.baseMapper.findMenuByUserId(userId);
			//获取菜单
			menus = getMenus(lstMenus);
			
			menuStr = JSON.toJSONString(menus);
			
			redisApi.set(GlobalRedisKey.User_Menu_Key + userId, menuStr, GlobalTimeConstant.User_Menu_Redis_Time);
			
		}else {
			menus = JSON.parseArray(menuStr, MenuModel.class);
		}
		
//		try {
//			//角色可见菜单
//			List<SysMenuEntity> lstMenus = this.baseMapper.findMenuByUserId(userId);
//			//获取菜单
//			menus = getMenus(lstMenus);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
		
		return menus;
	}
	
	
	
	
	/**
	 * 根据角色可见菜单，组装系统菜单栏
	 * @param lstMenus
	 * @return
	 */
	private List<MenuModel> getMenus(List<SysMenuEntity> lstMenus) {
		List<MenuModel> lstModels = new ArrayList<MenuModel>();
		if(null != lstMenus && lstMenus.size() > 0) {
			//移除菜单中的按钮
			lstMenus.removeIf(m -> StringUtils.equals("1", m.getType()));
			//取最顶层菜单
			if(null != lstMenus && lstMenus.size() > 0) {
				Iterator<SysMenuEntity> itMenus = lstMenus.iterator();
				while(itMenus.hasNext()) {
					SysMenuEntity m = itMenus.next();
					if(StringUtils.isEmpty(m.getParentId())) {
						lstModels.add(getMenuModel(m, null));
						itMenus.remove();
					}
				}
			}
		}
		if(lstModels.size() > 0) {
			//排序
			Collections.sort(lstModels, (m1, m2)-> m1.getOrderNo() - m2.getOrderNo());
			//取子菜单
			for(MenuModel model : lstModels) {
				List<MenuModel> firstLst = new ArrayList<MenuModel>();
				Iterator<SysMenuEntity> itMenus = lstMenus.iterator();
				while(itMenus.hasNext()) {
					SysMenuEntity m = itMenus.next();
					if(StringUtils.equals(m.getParentId(), model.getId())) {
						firstLst.add(getMenuModel(m, null));
						itMenus.remove();
					}
				}
				if(firstLst.size() > 0) {
					//排序
					Collections.sort(firstLst, (m1, m2)-> m1.getOrderNo() - m2.getOrderNo());
					
					model.setHasChildren(true);
					model.setList(firstLst);
					
					//取子菜单
					for(MenuModel secondM : firstLst) {
						List<MenuModel> secondLst = new ArrayList<MenuModel>();
						
						Iterator<SysMenuEntity> itMenus2 = lstMenus.iterator();
						while(itMenus2.hasNext()) {
							SysMenuEntity m = itMenus2.next();
							if(StringUtils.equals(m.getParentId(), secondM.getId())) {
								secondLst.add(getMenuModel(m, null));
								itMenus2.remove();
							}
						}
						if(secondLst.size() > 0) {
							Collections.sort(secondLst, (m1, m2)-> m1.getOrderNo() - m2.getOrderNo());
							secondM.setHasChildren(true);
							secondM.setList(secondLst);
						}
					}
					
					
				}
			}
			
		}
		
		
		return lstModels;
	}
	
	private MenuModel getMenuModel(SysMenuEntity m, MenuModel parent) {
		MenuModel menu = new MenuModel();
		menu.setIcon(m.getIcon());
		menu.setJump(m.getUrl());
		menu.setName("");
		menu.setTitle(m.getName());
		menu.setSpread(false);
		menu.setOrderNo(m.getOrderNo());
		menu.setId(m.getId());
		menu.setParent(parent);
		
		return menu;
	}
}
