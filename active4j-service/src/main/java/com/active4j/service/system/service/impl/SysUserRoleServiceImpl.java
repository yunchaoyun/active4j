package com.active4j.service.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.dao.system.dao.SysUserRoleDao;
import com.active4j.entity.system.entity.SysUserRoleEntity;
import com.active4j.service.system.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 用户角色管理service类
 * @author teli_
 *
 */
@Service("sysUserRoleService")
@Transactional
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements SysUserRoleService {

	
}
