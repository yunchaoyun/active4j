package com.active4j.web.func.message.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.entity.func.message.entity.SysUserMessageEntity;
import com.active4j.service.func.message.service.SysUserMessageService;
import com.active4j.web.core.config.shiro.ShiroUtils;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.func.message.wrapper.SysUserMessageWrapper;
import com.active4j.web.func.timer.wrapper.QuartzJobWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title SysUserMessageController.java
 * @description 
		用户系统消息
 * @time  2019年12月19日 上午11:36:55
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/func/message/user")
@Slf4j
@Api(value="常用功能-用户消息", tags={"用户消息操作接口"})
public class SysUserMessageController extends BaseController {
	
	@Autowired
	private SysUserMessageService sysUserMessageService;
	
	/**
	 * 
	 * @description
	 *  	主页获取系统未读消息，显示原点
	 * @params
	 * @return ResultJson
	 * @author guyp
	 * @time 2019年12月19日 上午11:37:30
	 */
	@RequestMapping(value="/newmsg", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@ApiOperation(value="获取系统未读消息", notes="获取系统未读消息")
	public ResultJson newMsg(HttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		
		try{
			//获取登录用户id
			String userId = ShiroUtils.getSessionUser().getId();
			//拼装查询条件
			QueryWrapper<SysUserMessageEntity> queryWrapper = new QueryWrapper<SysUserMessageEntity>();
			queryWrapper.eq("USER_ID", userId);
			queryWrapper.eq("DELETE_STATUS", SysUserMessageEntity.Sys_User_Message_Delete_No);
			queryWrapper.eq("READ_STATUS", SysUserMessageEntity.Sys_User_Message_Read_No);
			List<SysUserMessageEntity> lstMsgs = sysUserMessageService.list(queryWrapper);
			if(null == lstMsgs || lstMsgs.size() == 0) {
				j.setSuccess(false);
			}
		}catch(Exception e) {
			j.setSuccess(false);
			log.error("获取系统消息显示原点报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	获取用户消息明细
	 * @params
	 *      id 消息id
	 * @return void
	 * @author guyp
	 * @time 2020年1月4日 下午1:57:43
	 */
	@RequestMapping(value="/detail")
	@ApiOperation(value="获取用户消息明细", notes="获取用户消息明细", response=BaseWrapper.class)
	public void detail(@ApiParam(name="id", value="用户消息id", required=true) String id, HttpServletRequest request, HttpServletResponse response) {
		//获取消息明细
		SysUserMessageEntity msg = sysUserMessageService.getById(id);
		if(null != msg) {
			//消息设为已读
			msg.setReadStatus(SysUserMessageEntity.Sys_User_Message_Read_Yes);
			//更新保存
			sysUserMessageService.saveOrUpdate(msg);
		}
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<SysUserMessageEntity>(msg).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	显示表格数据
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月19日 下午1:24:09
	 */
	@RequestMapping(value="/datagrid")
	@ResponseBody
	@ApiOperation(value="获取用户消息列表", notes="获取用户消息列表", response=QuartzJobWrapper.class)
	public void datagrid(SysUserMessageEntity sysUserMessageEntity, PageInfo<SysUserMessageEntity> page, HttpServletRequest request, HttpServletResponse response) {
		//获取登录用户id
		String userId = ShiroUtils.getSessionUser().getId();
		sysUserMessageEntity.setUserId(userId);
		//获取未删除的信息
		sysUserMessageEntity.setDeleteStatus(SysUserMessageEntity.Sys_User_Message_Delete_No);
		//拼接查询条件
		QueryWrapper<SysUserMessageEntity> queryWrapper = QueryUtils.installQueryWrapper(sysUserMessageEntity, request.getParameterMap());
		//执行查询
		IPage<SysUserMessageEntity> lstResult = sysUserMessageService.page(page.getPageEntity(), queryWrapper);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理，直接写到客户端
		ResponseUtil.write(response, new SysUserMessageWrapper(lstResult).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	删除用户系统消息
	 * @params
	 * @return ResultJson
	 * @author guyp
	 * @time 2019年12月19日 下午1:48:08
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.del, name = "删除用户系统消息", memo = "删除了用户系统消息")
	@ApiOperation(value="删除用户消息", notes="根据消息ID删除系统消息")
	public ResultJson delete(@ApiParam(name="ids", value="用户消息ids集合", required=true) String ids, HttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		
		try {
			if(StringUtils.isNotEmpty(ids)) {
				//批量删除用户消息
				sysUserMessageService.deleteMsg(ids);
			}
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("删除失败");
			log.error("删除用户系统消息报错，错误信息：{}", e.getMessage() );
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	已读用户系统消息
	 * @params
	 * @return ResultJson
	 * @author guyp
	 * @time 2019年12月19日 下午1:53:32
	 */
	@RequestMapping(value="/read", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@ApiOperation(value="已读用户消息", notes="根据消息ID已读系统消息")
	public ResultJson read(@ApiParam(name="ids", value="用户消息ids集合", required=true) String ids, HttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		
		try {
			if(StringUtils.isNotEmpty(ids)) {
				//已读用户消息
				sysUserMessageService.readMsg(ids);
			}
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("已读用户系统消息失败");
			log.error("已读用户系统消息报错，错误信息：{}", e.getMessage() );
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	全部已读用户系统消息
	 * @params
	 * @return ResultJson
	 * @author guyp
	 * @time 2019年12月19日 下午1:59:05
	 */
	@RequestMapping(value="/readall", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@ApiOperation(value="全部已读用户消息", notes="全部已读系统消息")
	public ResultJson readAll(HttpServletRequest request, HttpServletResponse response) {
		ResultJson j = new ResultJson();
		
		try {
			//获取登录的userId
			String userId = ShiroUtils.getSessionUser().getId();
			//已读全部用户消息
			sysUserMessageService.readAllMsg(userId);
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("全部已读用户系统消息失败");
			log.error("全部已读用户系统消息报错，错误信息：{}", e.getMessage() );
			e.printStackTrace();
		}
		
		return j;
	}
}
