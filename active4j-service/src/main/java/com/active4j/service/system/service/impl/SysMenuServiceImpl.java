package com.active4j.service.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.dao.system.dao.SysMenuDao;
import com.active4j.entity.system.entity.SysMenuEntity;
import com.active4j.entity.system.entity.SysRoleMenuEntity;
import com.active4j.service.system.service.SysMenuService;
import com.active4j.service.system.service.SysRoleMenuService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 *   菜单service类
 * @author teli_
 *
 */
@Service("sysMenuService")
@Transactional
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {
	
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	/**
	 * 根据指定菜单，获取子级菜单
	 * @param menu
	 * @return
	 */
	public List<SysMenuEntity> getChildMenusByMenu(SysMenuEntity menu){
		QueryWrapper<SysMenuEntity> queryWrapper = new QueryWrapper<SysMenuEntity>();
		queryWrapper.eq("PARENT_ID", menu.getId());
		return this.list(queryWrapper);
	}

	
	/**
	 * 删除指定菜单
	 * @param menu
	 */
	public void deleteMenu(SysMenuEntity menu) {
		QueryWrapper<SysRoleMenuEntity> queryWrapper = new QueryWrapper<SysRoleMenuEntity>();
		queryWrapper.eq("MENU_ID", menu.getId());
		sysRoleMenuService.remove(queryWrapper);
		
		this.removeById(menu.getId());
	}

}
