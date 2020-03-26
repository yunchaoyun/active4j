package com.active4j.service.system.service;

import java.util.List;

import com.active4j.entity.base.model.ValueLabelModel;
import com.active4j.entity.system.entity.SysLogEntity;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SysLogService extends IService<SysLogEntity> {
	
	/**
	 * 
	 * @description
	 *  	保存操作日志
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月31日 上午9:54:45
	 */
	public void addLog(SysLogEntity log);
	
	/**
	 * 
	 * @description
	 *  	获取日志类型
	 * @params
	 * @return List<ValueLabelModel>
	 * @author guyp
	 * @time 2019年12月31日 上午9:54:31
	 */
	public List<ValueLabelModel> getLstTypes();
	
}
