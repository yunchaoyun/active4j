package com.active4j.service.func.timer.service;

import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.func.timer.entity.QuartzJobEntity;
import com.active4j.entity.func.timer.model.QuartzJobModel;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 
 * @title QuartzJobService.java
 * @description 
		定时任务
 * @time  2019年12月9日 下午4:38:13
 * @author guyp
 * @version 1.0
 */
public interface QuartzJobService extends IService<QuartzJobEntity> {
	
	/**
	 * 
	 * @description
	 *  	保存定时任务
	 * @params
	 *      job 定时任务实体
	 * @return ResultJson
	 * @author guyp
	 * @time 2019年12月10日 上午10:30:27
	 */
	public ResultJson saveJob(QuartzJobEntity job);
	
	/**
	 * 
	 * @description
	 *  	根据任务id更新状态
	 * @params
	 *      job 定时任务实体
	 * @return void
	 * @author guyp
	 * @time 2019年12月10日 下午10:47:41
	 */
	public void updateJobById(QuartzJobEntity job);
	
	/**
	 * 
	 * @description
	 *  	定时任务启用
	 * @params
	 * 		任务id
	 * @return ResultJson
	 * @author guyp
	 * @time 2019年12月11日 下午1:33:08
	 */
	public ResultJson startJob(String id);
	
	/**
	 * 
	 * @description
	 *  	定时任务暂停
	 * @params
	 * 		任务id
	 * @return ResultJson
	 * @author guyp
	 * @time 2019年12月11日 下午1:44:26
	 */
	public ResultJson pauseJob(String id);
	
	/**
	 * 
	 * @description
	 *  	删除定时任务
	 * @params
	 *      ids 任务ids
	 * @return void
	 * @author guyp
	 * @time 2019年12月11日 下午4:28:17
	 */
	public void delJobs(String ids);
	
	/**
	 * 
	 * @description
	 *  	立即执行一次任务
	 * @params
	 *      id 任务id
	 * @return boolean
	 * @author guyp
	 * @time 2019年12月12日 上午12:46:26
	 */
	public boolean runAJobNow(String id);
	
	/**
	 * 
	 * @description
	 *  	获取任务明细
	 * @params
	 *      id 任务id
	 * @return QuartzJobModel
	 * @author guyp
	 * @time 2020年1月3日 上午10:45:55
	 */
	public QuartzJobModel getJobDetailModel(String id);
}
