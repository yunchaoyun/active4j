package com.active4j.web.system.wrapper;

import java.util.List;

import com.active4j.entity.base.model.TreeSysDicModel;
import com.active4j.entity.base.wrapper.BaseTableWrapper;

public class DicWrapper extends BaseTableWrapper<TreeSysDicModel> {

	public DicWrapper(List<TreeSysDicModel> lstResult) {
		//父类中的方法初始化数据
		super(lstResult);
	}

}
