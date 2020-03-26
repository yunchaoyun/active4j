package com.active4j.web.system.wrapper;

import java.util.List;

import com.active4j.entity.base.wrapper.BaseTableWrapper;
import com.active4j.entity.system.entity.SysMenuEntity;
import com.active4j.service.system.util.SystemUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

public class MenuWrapper extends BaseTableWrapper<SysMenuEntity>{
	
	public MenuWrapper(List<SysMenuEntity> pageResult) {
		//父类中的方法初始化数据
		super(pageResult);
		
		//值替换
		this.getData().stream().forEach(d -> {
			if(StringUtils.isEmpty(d.getParentId())) {
				d.setParentId("-1");
			}
			
			//菜单类型的处理
			d.setType(SystemUtils.getDictionaryValue("menu_type", d.getType()));
		});
	}
	
}
