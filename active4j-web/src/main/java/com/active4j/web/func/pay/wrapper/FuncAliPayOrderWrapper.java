package com.active4j.web.func.pay.wrapper;

import com.active4j.entity.base.wrapper.BaseTableWrapper;
import com.active4j.entity.func.pay.entity.FuncAliPayOrderEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @title FuncAliPayOrderWrapper.java
 * @description 
 * @time  2020年1月9日 下午5:48:14
 * @author mhm
 * @version 1.0
*/
public class FuncAliPayOrderWrapper extends BaseTableWrapper<FuncAliPayOrderEntity> {

	public FuncAliPayOrderWrapper(IPage<FuncAliPayOrderEntity> pageResult) {
		//父类中的方法初始化数据
		super(pageResult);
		
	}
	
}