package com.active4j.service.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.dao.system.dao.SysRoleMenuDao;
import com.active4j.entity.system.entity.SysRoleMenuEntity;
import com.active4j.service.system.service.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 *  角色 菜单service类
 * @author teli_
 *
 */
@Service("sysRoleMenuService")
@Transactional
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenuEntity> implements SysRoleMenuService {

}
