package com.active4j.web.system.wrapper;

import java.util.List;

import com.active4j.entity.base.wrapper.BaseTableWrapper;
import com.active4j.entity.system.entity.SysDepartEntity;
import com.active4j.service.system.util.SystemUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
public class DeptWrapper extends BaseTableWrapper<SysDepartEntity>{
	
	public DeptWrapper(List<SysDepartEntity> pageResult) {
		//父类中的方法初始化数据
		super(pageResult);
		
		//值替换
		this.getData().stream().forEach(d -> {
			if(StringUtils.isEmpty(d.getParentId())) {
				d.setParentId("-1");
			}
			
			//部门的处理
			d.setType(SystemUtils.getDictionaryValue("dept_type", d.getType()));
		});
	}
	
}
