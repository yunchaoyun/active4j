package com.active4j.service.system.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.dao.system.dao.SysLogDao;
import com.active4j.entity.base.model.ValueLabelModel;
import com.active4j.entity.system.entity.SysLogEntity;
import com.active4j.service.system.service.SysLogService;
import com.active4j.service.system.util.SystemUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 日志管理service类
 * @author 38943
 *
 */
@Service("sysLogService")
@Transactional
public class SysLogServiceImpl extends ServiceImpl<SysLogDao, SysLogEntity> implements SysLogService {

	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 
	 * @description
	 *  	保存操作日志
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月31日 上午9:54:45
	 */
	public void addLog(SysLogEntity log) {
		
		//基础变量赋值
		log.setCreateName("system");
		
		//有些方法参数特别长，特殊处理一下
		if(StringUtils.length(log.getParams()) > 500) {
			log.setParams(StringUtils.substring(log.getParams(), 0, 500) + "...");
		}
		
		sysLogService.save(log);
	}

	/**
	 * 
	 * @description
	 *  	获取日志类型
	 * @params
	 * @return List<ValueLabelModel>
	 * @author guyp
	 * @time 2019年12月31日 上午9:54:31
	 */
	public List<ValueLabelModel> getLstTypes() {
		List<ValueLabelModel> lstModels = new ArrayList<ValueLabelModel>();
		Map<String, String> map = SystemUtils.getDictionaryMap("log_type");
		if(null != map && map.keySet().size() > 0) {
			for(String key : map.keySet()) {
				ValueLabelModel model = new ValueLabelModel();
				model.setLabel(map.get(key));
				model.setValue(key);
				lstModels.add(model);
			}
		}
		
		return lstModels;
	}
	
}
