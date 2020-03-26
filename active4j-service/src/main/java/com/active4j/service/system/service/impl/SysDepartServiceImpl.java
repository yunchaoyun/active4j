package com.active4j.service.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.dao.system.dao.SysDepartDao;
import com.active4j.entity.system.entity.SysDepartEntity;
import com.active4j.entity.system.entity.SysUserEntity;
import com.active4j.service.system.service.SysDepartService;
import com.active4j.service.system.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 部门管理service类
 * @author teli_
 *
 */
@Service("sysDepartService")
@Transactional
public class SysDepartServiceImpl extends ServiceImpl<SysDepartDao, SysDepartEntity> implements SysDepartService {
	
	@Autowired
	private SysUserService sysUserService;

	/**
	 * 获取指定部门下的用户
	 * @param depart
	 * @return
	 */
	public List<SysUserEntity> getUsersByDept(SysDepartEntity depart){
		QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<SysUserEntity>();
		queryWrapper.eq("DEPT_ID", depart.getId());
		List<SysUserEntity> lstUsers = sysUserService.list(queryWrapper);
		
		return lstUsers;
	}
	
	/**
	 * 获取指定部门下的子部门
	 * @param depart
	 * @return
	 */
	public List<SysDepartEntity> getChildDepartsByDept(SysDepartEntity depart){
		QueryWrapper<SysDepartEntity> queryWrapper = new QueryWrapper<SysDepartEntity>();
		queryWrapper.eq("PARENT_ID", depart.getId());
		
		return this.list(queryWrapper);
	}

}
