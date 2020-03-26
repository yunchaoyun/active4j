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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.PageInfo;
import com.active4j.entity.base.annotation.Log;
import com.active4j.entity.base.model.LogType;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.base.model.ValueLabelModel;
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.entity.func.timer.entity.QuartzJobLogEntity;
import com.active4j.entity.func.timer.model.QuartzJobLogModel;
import com.active4j.service.func.timer.service.QuartzJobLogService;
import com.active4j.service.system.util.SystemUtils;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.func.timer.wrapper.QuartzJobLogWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title QuartzJobLogController.java
 * @description 
		定时任务日志管理
 * @time  2019年12月12日 上午10:52:50
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/func/timer/joblog")
@Slf4j
@Api(value="常用功能-定时任务日志", tags={"定时任务日志操作接口"})
public class QuartzJobLogController extends BaseController {
	
	@Autowired
	private QuartzJobLogService quartzJobLogService;
	
	/**
	 * 
	 * @description
	 *  	获取执行状态
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2020年1月3日 下午12:24:30
	 */
	@RequestMapping(value="/status")
	@ApiOperation(value="获取执行状态", notes="页面获取执行状态", response=BaseWrapper.class)
	public void status(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = SystemUtils.getDictionaryMap("func_timer_job_log_status");
		List<ValueLabelModel> lstModels = new ArrayList<ValueLabelModel>();
		if(null != map && map.keySet().size() > 0) {
			for(String key : map.keySet()) {
				ValueLabelModel model = new ValueLabelModel();
				model.setLabel(map.get(key));
				model.setValue(key);
				lstModels.add(model);
			}
		}
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<List<ValueLabelModel>>(lstModels).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	查看任务日志明细
	 * @params
	 *      id 日志id
	 * @return void
	 * @author guyp
	 * @time 2020年1月3日 下午12:31:10
	 */
	@RequestMapping(value="/detail")
	@ApiOperation(value="获取任务日志明细", notes="获取任务日志明细", response=BaseWrapper.class)
	public void detail(String id, HttpServletRequest request, HttpServletResponse response) {
		//获取任务明细
		QuartzJobLogModel model = quartzJobLogService.getLogDetailModel(id);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<QuartzJobLogModel>(model).wrap());
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
	@ApiOperation(value="获取定时任务日志列表", notes="获取定时任务日志列表", response=QuartzJobLogWrapper.class)
	public void datagrid(QuartzJobLogEntity quartzJobLogEntity, PageInfo<QuartzJobLogEntity> page, HttpServletRequest request, HttpServletResponse response) {
		//拼接查询条件
		QueryWrapper<QuartzJobLogEntity> queryWrapper = QueryUtils.installQueryWrapper(quartzJobLogEntity, request.getParameterMap());
		//执行查询
		IPage<QuartzJobLogEntity> lstResult = quartzJobLogService.page(page.getPageEntity(), queryWrapper);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理，直接写到客户端
		ResponseUtil.write(response, new QuartzJobLogWrapper(lstResult).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	删除定时任务日志
	 * @params
	 *      ids 任务日志ids
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月11日 下午4:20:08
	 */
	@RequestMapping(value="/del", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.del, name = "删除定时任务日志", memo = "删除了定时任务日志")
	@ApiOperation(value="删除定时任务日志", notes="根据任务ID删除定时任务日志")
	public ResultJson del(@ApiParam(name="ids", value="定时任务日志id集合") String ids) {
		ResultJson j = new ResultJson();
		try {
			//ids校验
			if(StringUtils.isEmpty(ids)) {
				j.setSuccess(false);
				j.setMsg("请选择一条记录");
			}
			//删除定时任务日志
			quartzJobLogService.delLogs(ids);
		}catch(Exception e) {
			log.error("删除定时任务日志报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			j.setMsg("删除定时任务日志失败");
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	清空定时任务日志
	 * @params
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月12日 上午10:57:33
	 */
	@RequestMapping(value="/empty", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.del, name = "清空定时任务日志", memo = "清空了所有定时任务日志")
	@ApiOperation(value="清空定时任务", notes="清空定时任务")
	public ResultJson empty() {
		ResultJson j = new ResultJson();
		try {
			//清空定时任务日志
			quartzJobLogService.remove(new QueryWrapper<QuartzJobLogEntity>());
		}catch(Exception e) {
			log.error("清空定时任务日志报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			j.setMsg("清空定时任务日志失败");
			e.printStackTrace();
		}
		
		return j;
	}
	
}
