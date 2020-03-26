package com.active4j.web.func.export.wrapper;

import com.active4j.entity.base.wrapper.BaseTableWrapper;
import com.active4j.entity.func.export.entity.ExportExampleEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 
 * @title ExportExampleWrapper.java
 * @description 
 * @time  2019年12月17日 上午10:50:34
 * @author guyp
 * @version 1.0
 */
public class ExportExampleWrapper extends BaseTableWrapper<ExportExampleEntity>{

	public ExportExampleWrapper(IPage<ExportExampleEntity> pageResult) {
		//父类中的方法初始化数据
		super(pageResult);
		
	}
	
}
