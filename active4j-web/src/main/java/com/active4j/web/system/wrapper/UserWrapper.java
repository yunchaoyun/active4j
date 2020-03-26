package com.active4j.web.system.wrapper;

import com.active4j.entity.base.wrapper.BaseTableWrapper;
import com.active4j.entity.system.entity.SysUserEntity;
import com.active4j.service.system.util.SystemUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
public class UserWrapper extends BaseTableWrapper<SysUserEntity>{
	
	public UserWrapper(IPage<SysUserEntity> pageResult) {
		//父类中的方法初始化数据
		super(pageResult);
		
		pageResult.getRecords().forEach(d -> {
			d.setDeptId(SystemUtils.getDeptNameById(d.getDeptId()));
		});
	}
	
}
