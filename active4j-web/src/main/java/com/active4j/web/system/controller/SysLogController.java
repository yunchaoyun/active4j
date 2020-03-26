package com.active4j.web.system.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.PageInfo;
import com.active4j.entity.base.model.ValueLabelModel;
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.entity.system.entity.SysLogEntity;
import com.active4j.service.system.service.SysLogService;
import com.active4j.service.system.util.SystemUtils;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.system.wrapper.LogWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * @title SysLogController.java
 * @description 
		日志管理
 * @time  2019年12月27日 下午2:42:00
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/sys/log")
@Api(value="系统管理-系统日志管理", tags={"系统日志管理操作接口"})
public class SysLogController extends BaseController {

	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 
	 * @description
	 *  	获取表格数据
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月31日 上午9:50:58
	 */
	@RequestMapping(value="/datagrid")
	@ResponseBody
	@ApiOperation(value="获取系统日志列表", notes="获取系统日志列表", response=LogWrapper.class)
	public void datagrid(SysLogEntity sysLogEntity, PageInfo<SysLogEntity> page, HttpServletRequest request, HttpServletResponse response) {
		//拼接查询条件
		QueryWrapper<SysLogEntity> queryWrapper = QueryUtils.installQueryWrapper(sysLogEntity, request.getParameterMap());
		
		//执行查询
		IPage<SysLogEntity> lstResult = sysLogService.page(page.getPageEntity(), queryWrapper);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new LogWrapper(lstResult).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	页面获取日志类型
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月31日 上午10:06:01
	 */
	@RequestMapping(value="/logtype")
	@ApiOperation(value="获取日志类型", notes="页面获取日志类型", response=BaseWrapper.class)
	public void logType(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = SystemUtils.getDictionaryMap("log_type");
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
	
}
