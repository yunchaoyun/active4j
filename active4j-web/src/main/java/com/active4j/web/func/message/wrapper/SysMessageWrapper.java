package com.active4j.web.func.message.wrapper;

import com.active4j.entity.base.wrapper.BaseTableWrapper;
import com.active4j.entity.func.message.entity.SysMessageEntity;
import com.active4j.service.system.util.SystemUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 
 * @title SysMessageWrapper.java
 * @description 
 * @time  2019年12月18日 下午4:10:28
 * @author guyp
 * @version 1.0
 */
public class SysMessageWrapper extends BaseTableWrapper<SysMessageEntity>{

	public SysMessageWrapper(IPage<SysMessageEntity> pageResult) {
		//父类中的方法初始化数据
		super(pageResult);
		
		//值替换
		this.getData().stream().forEach(d -> {
			//消息类型的处理
			d.setType(SystemUtils.getDictionaryValue("func_sys_message_type", d.getType()));
		});
	}
	
}
