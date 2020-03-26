package com.active4j.web.system.wrapper;

import com.active4j.entity.base.wrapper.BaseTableWrapper;
import com.active4j.entity.system.entity.SysLogEntity;
import com.active4j.service.system.util.SystemUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
public class LogWrapper extends BaseTableWrapper<SysLogEntity>{

	public LogWrapper(IPage<SysLogEntity> pageResult) {
		//父类中的方法初始化数据
		super(pageResult);
		
		//值替换
		this.getData().stream().forEach(d -> {
			//日志类型的处理
			d.setType(SystemUtils.getDictionaryValue("log_type", d.getType()));
		});
	}
	
}
