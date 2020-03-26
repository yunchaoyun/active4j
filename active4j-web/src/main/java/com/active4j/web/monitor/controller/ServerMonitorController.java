package com.active4j.web.monitor.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.monitor.model.ServerInfoModel;
import com.active4j.service.monitor.service.ServerMonitorService;
import com.active4j.web.core.web.util.ResponseUtil;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/monitor/server")
@Slf4j
public class ServerMonitorController extends BaseController {
	
	@Autowired
	private ServerMonitorService serverMonitorService;
	
	/**
	 * 
	 * @description
	 *  	页面获取服务器信息
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月31日 下午2:05:02
	 */
	@RequestMapping(value="/index", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiOperation(value="获取服务器信息", notes="获取服务器信息", response=BaseWrapper.class)
	public void index(HttpServletRequest request, HttpServletResponse response) {
		
		//获取服务器信息
		ServerInfoModel server = serverMonitorService.getServer();
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		if(null != server) {
			//结果处理,直接写到客户端
			ResponseUtil.write(response, new BaseWrapper<ServerInfoModel>(server).wrap());
		}else {
			ResponseUtil.write(response, new BaseWrapper<ServerInfoModel>(new ServerInfoModel()).wrap());
		}
	}
	
	/**
	 * 
	 * @description
	 *  	页面定时获取服务器信息
	 * @params
	 * @return ResultJson
	 * @author guyp
	 * @time 2019年12月31日 下午2:44:03
	 */
	@RequestMapping(value="/server", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@ApiOperation(value = "定时获取服务器信息", notes = "定时获取服务器信息")
	public ResultJson getServer(HttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		
		try {
			//获取服务器信息
			ServerInfoModel server = serverMonitorService.getServer();
        	j.setData(server);
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("获取服务器信息监控出错");
			log.error("获取服务器监控报错，错误原因：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
}
