package com.active4j.web.func.pay.wrapper;

import com.active4j.entity.base.wrapper.BaseTableWrapper;
import com.active4j.entity.func.pay.entity.FuncWeixinPayPreOrderEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * @title FuncWeixinPayPreOrderWrapper.java
 * @description 
 * @time  2020年1月8日 下午2:37:04
 * @author mhm
 * @version 1.0
*/
public class FuncWeixinPayPreOrderWrapper extends BaseTableWrapper<FuncWeixinPayPreOrderEntity> {

	public FuncWeixinPayPreOrderWrapper(IPage<FuncWeixinPayPreOrderEntity> pageResult) {
		//父类中的方法初始化数据
		super(pageResult);
		
	}
}
