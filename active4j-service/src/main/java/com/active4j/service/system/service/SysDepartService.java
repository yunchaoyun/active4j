package com.active4j.service.system.service;

import java.util.List;

import com.active4j.entity.system.entity.SysDepartEntity;
import com.active4j.entity.system.entity.SysUserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysDepartService extends IService<SysDepartEntity> {
	

	/**
	 * 获取指定部门下的用户
	 * @param depart
	 * @return
	 */
	public List<SysUserEntity> getUsersByDept(SysDepartEntity depart);
	
	
	
	/**
	 * 获取指定部门下的子部门
	 * @param depart
	 * @return
	 */
	public List<SysDepartEntity> getChildDepartsByDept(SysDepartEntity depart);

}
