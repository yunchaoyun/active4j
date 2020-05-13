package com.active4j.web.func.timer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.active4j.common.util.CronUtils;
import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.PageInfo;
import com.active4j.entity.base.annotation.Log;
import com.active4j.entity.base.model.LogType;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.base.model.ValueLabelModel;
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.entity.func.timer.entity.QuartzJobEntity;
import com.active4j.entity.func.timer.model.QuartzJobModel;
import com.active4j.service.func.timer.service.QuartzJobService;
import com.active4j.service.system.util.SystemUtils;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.func.timer.wrapper.QuartzJobWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title QuartzJobController.java
 * @description 
		定时任务管理
 * @time  2019年12月10日 上午10:10:26
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/func/timer/job")
@Slf4j
@Api(value="常用功能-定时任务", tags={"定时任务操作接口"})
public class QuartzJobController extends BaseController {
	
	@Autowired
	private QuartzJobService quartzJobService;

	/**
	 * 
	 * @description
	 *  	获取任务分组
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2020年1月2日 下午4:38:39
	 */
	@RequestMapping(value="/jobgroup")
	@ApiOperation(value="获取任务分组", notes="页面获取任务分组", response=BaseWrapper.class)
	public void jobGroup(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = SystemUtils.getDictionaryMap("func_timer_job_group");
		List<ValueLabelModel> lstModels = new ArrayList<ValueLabelModel>();
		if(null != map && map.keySet().size() > 0) {
			for(String key : map.keySet()) {
				ValueLabelModel model = new ValueLabelModel();
				model.setLabel(map.get(key));
				model.setValue(key);
				lstModels.add(model);
			}
		}

		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<List<ValueLabelModel>>(lstModels).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	获取任务
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2020年1月2日 下午4:39:20
	 */
	@RequestMapping(value="/jobstatus")
	@ApiOperation(value="获取任务状态", notes="页面获取任务状态", response=BaseWrapper.class)
	public void jobStatus(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = SystemUtils.getDictionaryMap("func_timer_job_status");
		List<ValueLabelModel> lstModels = new ArrayList<ValueLabelModel>();
		if(null != map && map.keySet().size() > 0) {
			for(String key : map.keySet()) {
				ValueLabelModel model = new ValueLabelModel();
				model.setLabel(map.get(key));
				model.setValue(key);
				lstModels.add(model);
			}
		}

		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<List<ValueLabelModel>>(lstModels).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	获取执行策略
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2020年1月3日 上午10:07:36
	 */
	@RequestMapping(value="/misfirepolicy")
	@ApiOperation(value="获取执行策略", notes="页面获取执行策略", response=BaseWrapper.class)
	public void misfirePolicy(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = SystemUtils.getDictionaryMap("func_timer_job_misfire_policy");
		List<ValueLabelModel> lstModels = new ArrayList<ValueLabelModel>();
		if(null != map && map.keySet().size() > 0) {
			for(String key : map.keySet()) {
				ValueLabelModel model = new ValueLabelModel();
				model.setLabel(map.get(key));
				model.setValue(key);
				lstModels.add(model);
			}
		}

		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<List<ValueLabelModel>>(lstModels).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	获取并发执行状态
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2020年1月3日 上午10:09:30
	 */
	@RequestMapping(value="/concurrentstatus")
	@ApiOperation(value="获取并发执行状态", notes="页面获取并发执行状态", response=BaseWrapper.class)
	public void concurrentStatus(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = SystemUtils.getDictionaryMap("func_timer_job_concurrent_status");
		List<ValueLabelModel> lstModels = new ArrayList<ValueLabelModel>();
		if(null != map && map.keySet().size() > 0) {
			for(String key : map.keySet()) {
				ValueLabelModel model = new ValueLabelModel();
				model.setLabel(map.get(key));
				model.setValue(key);
				lstModels.add(model);
			}
		}

		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<List<ValueLabelModel>>(lstModels).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	获取任务明细
	 * @params
	 *      id 任务id
	 * @return void
	 * @author guyp
	 * @time 2020年1月3日 上午10:46:57
	 */
	@RequestMapping(value="/detail")
	@ApiOperation(value="获取任务明细", notes="获取任务明细", response=BaseWrapper.class)
	public void detail(String id, HttpServletRequest request, HttpServletResponse response) {
		//获取任务明细
		QuartzJobModel model = quartzJobService.getJobDetailModel(id);
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<QuartzJobModel>(model).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	获取表格数据
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月10日 上午10:20:42
	 */
	@RequestMapping(value="/datagrid")
	@ResponseBody
	@ApiOperation(value="获取定时任务列表", notes="获取定时任务列表", response=QuartzJobWrapper.class)
	public void datagrid(QuartzJobEntity quartzJobEntity, PageInfo<QuartzJobEntity> page, HttpServletRequest request, HttpServletResponse response) {
		//拼接查询条件
		QueryWrapper<QuartzJobEntity> queryWrapper = QueryUtils.installQueryWrapper(quartzJobEntity, request.getParameterMap());
		//执行查询
		IPage<QuartzJobEntity> lstResult = quartzJobService.page(page.getPageEntity(), queryWrapper);
		//结果处理，直接写到客户端
		ResponseUtil.write(response, new QuartzJobWrapper(lstResult).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	保存定时任务
	 * @params
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月11日 下午3:27:40
	 */
	@RequestMapping(value="/save", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.save, name = "保存定时任务", memo = "新增或编辑保存了定时任务")
	@ApiOperation(value="保存定时任务", notes="保存定时任务")
	@ApiImplicitParam(name="quartzJobEntity", value="传入定时任务实体对象", required=true, dataType="QuartzJobEntity")
	public ResultJson save(@Validated QuartzJobEntity quartzJobEntity)  {
		ResultJson j = new ResultJson();
		
		try{
			//这里使用了@validated来校验数据，如果数据异常则会统一抛出异常，方便异常中心统一处理，所以不再做字段为空和长度校验，具体校验规则请见是实体字段
			//校验cron表达式
			if(!CronUtils.isValid(quartzJobEntity.getCronExpression())) {
				j.setSuccess(false);
				j.setMsg("请输入正确的cron表达式");
				return j;
			}
			
			//保存定时任务
			j = quartzJobService.saveJob(quartzJobEntity);
		}catch(Exception e) {
			log.error("保存定时任务报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			j.setMsg("保存定时任务失败");
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	删除定时任务
	 * @params
	 *      ids 任务ids
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月11日 下午4:20:08
	 */
	@RequestMapping(value="/del", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.del, name = "删除定时任务", memo = "删除了定时任务")
	@ApiOperation(value="删除定时任务", notes="根据任务ID删除定时任务")
	public ResultJson del(@ApiParam(name="ids", value="定时任务id集合") String ids) {
		ResultJson j = new ResultJson();
		try {
			//ids校验
			if(StringUtils.isEmpty(ids)) {
				j.setSuccess(false);
				j.setMsg("请选择一条记录");
			}
			//删除定时任务
			quartzJobService.delJobs(ids);
		}catch(Exception e) {
			log.error("删除定时任务报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			j.setMsg("删除定时任务失败");
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	启用定时任务
	 * @params
	 *      id 任务id
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月11日 下午1:49:44
	 */
	@RequestMapping(value="/start", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.update, name = "启用定时任务", memo = "启用了定时任务")
	@ApiOperation(value="启用定时任务", notes="根据任务ID启用定时任务")
	public ResultJson start(@ApiParam(name="id", value="定时任务id", required=true) String id) {
		ResultJson j = new ResultJson();
		try {
			//id校验
			if(StringUtils.isEmpty(id)) {
				j.setSuccess(false);
				j.setMsg("该任务不存在，请重新选择");
				return j;
			}
			//启用任务
			j = quartzJobService.startJob(id);
		}catch(Exception e) {
			log.error("启用定时任务报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			j.setMsg("启用定时任务失败");
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	立即执行一次定时任务
	 * @params
	 *      id 任务id
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月12日 上午12:51:33
	 */
	@RequestMapping(value="/one", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.save, name = "执行定时任务", memo = "立即执行了一次定时任务")
	@ApiOperation(value="立即执行一次定时任务", notes="根据任务ID立即执行一次定时任务")
	public ResultJson one(@ApiParam(name="id", value="定时任务id", required=true) String id) {
		ResultJson j = new ResultJson();
		try {
			//id校验
			if(StringUtils.isEmpty(id)) {
				j.setSuccess(false);
				j.setMsg("该任务不存在，请重新选择");
				return j;
			}
			//启用任务
			if(!quartzJobService.runAJobNow(id)) {
				j.setSuccess(false);
				j.setMsg("立即执行一次任务失败");
				return j;
			}
		}catch(Exception e) {
			log.error("立即执行一次定时任务报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			j.setMsg("立即执行一次定时任务失败");
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	暂停定时任务
	 * @params
	 *      id 任务id
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月11日 下午1:50:38
	 */
	@RequestMapping(value="/pause", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.update, name = "暂停定时任务", memo = "暂停了定时任务")
	@ApiOperation(value="暂停定时任务", notes="根据任务ID暂停定时任务")
	public ResultJson pause(@ApiParam(name="id", value="定时任务id", required=true) String id) {
		ResultJson j = new ResultJson();
		try {
			//id校验
			if(StringUtils.isEmpty(id)) {
				j.setSuccess(false);
				j.setMsg("该任务不存在，请重新选择");
				return j;
			}
			//暂停任务
			j = quartzJobService.pauseJob(id);
		}catch(Exception e) {
			log.error("暂停定时任务报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			j.setMsg("暂停定时任务失败");
			e.printStackTrace();
		}
		
		return j;
	}
	
}
