package com.active4j.service.func.timer.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.common.util.DateUtils;
import com.active4j.dao.func.timer.dao.QuartzJobLogDao;
import com.active4j.entity.func.timer.entity.QuartzJobLogEntity;
import com.active4j.entity.func.timer.model.QuartzJobLogModel;
import com.active4j.service.func.timer.service.QuartzJobLogService;
import com.active4j.service.system.util.SystemUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 
 * @title QuartzJobLogServiceImpl.java
 * @description 
		定时任务日志
 * @time  2019年12月10日 上午10:00:45
 * @author guyp
 * @version 1.0
 */
@Service("quartzJobLogService")
@Transactional
public class QuartzJobLogServiceImpl extends ServiceImpl<QuartzJobLogDao, QuartzJobLogEntity> implements QuartzJobLogService {

	/**
	 * 
	 * @description
	 *  	删除定时任务日志
	 * @params
	 *      ids 日志ids
	 * @return void
	 * @author guyp
	 * @time 2019年12月12日 上午11:00:53
	 */
	public void delLogs(String ids) {
		List<String> lstIds = new ArrayList<String>();
		//“,”分割成数组
		String[] strIds = ids.split(",");
		if(null != strIds && strIds.length > 0) {
			for(String id : strIds) {
				//组装list
				lstIds.add(id);
			}
		}
		//删除
		this.removeByIds(lstIds);
	}
	
	/**
	 * 
	 * @description
	 *  	清空日志表
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月12日 上午11:14:27
	 */
	public void emptyLogs() {
		this.remove(new QueryWrapper<QuartzJobLogEntity>());
	}

	/**
	 * 
	 * @description
	 *  	获取日志明细
	 * @params
	 *      id 日志id
	 * @return Model
	 * @author guyp
	 * @time 2019年12月12日 上午11:03:40
	 */
	public QuartzJobLogModel getLogDetailModel(String id) {
		QuartzJobLogModel model = new QuartzJobLogModel();
		//根据id获取任务实体
		QuartzJobLogEntity log = this.getById(id);
		if(null != log) {
			model.setJobNo(log.getJobNo());
			model.setShortName(log.getShortName());
			//任务分组的处理
			model.setJobGroup(SystemUtils.getDictionaryValue("func_timer_job_group", log.getJobGroup()));
			model.setInvokeParams(log.getInvokeParams());
			model.setJobMessage(log.getJobMessage());
			//任务执行状态的处理
			model.setStatus(SystemUtils.getDictionaryValue("func_timer_job_log_status", log.getStatus()));
			//开始执行时间的处理
			if(null != log.getStartTime()) {
				model.setStart(DateUtils.getDate2Str(log.getStartTime()));
			}
			//结束执行时间的处理
			if(null != log.getEndTime()) {
				model.setEnd(DateUtils.getDate2Str(log.getEndTime()));
			}
			model.setExceptionInfo(log.getExceptionInfo());
		}
		return model;
	}

}
