package com.active4j.web.func.timer.wrapper;

import com.active4j.entity.base.wrapper.BaseTableWrapper;
import com.active4j.entity.func.timer.entity.QuartzJobEntity;
import com.active4j.service.system.util.SystemUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 
 * @title QuartzJobWrapper.java
 * @description 
 * @time  2019年12月10日 上午10:18:01
 * @author guyp
 * @version 1.0
 */
public class QuartzJobWrapper extends BaseTableWrapper<QuartzJobEntity>{

	public QuartzJobWrapper(IPage<QuartzJobEntity> pageResult) {
		//父类中的方法初始化数据
		super(pageResult);
		
		//值替换
		this.getData().stream().forEach(d -> {
			//任务分组的处理
			d.setJobGroup(SystemUtils.getDictionaryValue("func_timer_job_group", d.getJobGroup()));
		});
	}
	
}
