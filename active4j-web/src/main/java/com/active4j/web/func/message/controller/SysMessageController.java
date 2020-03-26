package com.active4j.web.func.message.controller;

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

import com.active4j.common.util.MyBeanUtils;
import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.PageInfo;
import com.active4j.entity.base.annotation.Log;
import com.active4j.entity.base.model.LogType;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.base.model.ValueLabelModel;
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.entity.func.message.entity.SysMessageEntity;
import com.active4j.entity.func.message.model.SysMessageModel;
import com.active4j.service.func.message.service.SysMessageService;
import com.active4j.service.system.util.SystemUtils;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.func.message.wrapper.SysMessageWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title SysMessageController.java
 * @description 
		系统消息
 * @time  2019年12月18日 下午3:44:36
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/func/message/sys")
@Slf4j
@Api(value="常用功能-系统消息", tags={"系统消息操作接口"})
public class SysMessageController extends BaseController {
	
	@Autowired
	private SysMessageService sysMessageService;
	
	/**
	 * 
	 * @description
	 *  	获取系统消息类型
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2020年1月4日 上午11:11:37
	 */
	@RequestMapping(value="/type")
	@ApiOperation(value="获取系统消息类型", notes="页面获取系统消息类型", response=BaseWrapper.class)
	public void type(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = SystemUtils.getDictionaryMap("func_sys_message_type");
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
	 *  	获取系统消息状态
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2020年1月4日 下午12:03:09
	 */
	@RequestMapping(value="/status")
	@ApiOperation(value="获取系统消息状态", notes="页面获取系统消息状态", response=BaseWrapper.class)
	public void status(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = SystemUtils.getDictionaryMap("func_sys_message_status");
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
	 *  	获取系统消息明细
	 * @params
	 *      id 消息id
	 * @return void
	 * @author guyp
	 * @time 2020年1月4日 下午12:06:18
	 */
	@RequestMapping(value="/detail")
	@ApiOperation(value="获取系统消息明细", notes="获取系统消息明细", response=BaseWrapper.class)
	public void detail(@ApiParam(name="id", value="系统消息id", required=true) String id, HttpServletRequest request, HttpServletResponse response) {
		//获取任务明细
		SysMessageModel model = sysMessageService.getMsgDetailModel(id);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<SysMessageModel>(model).wrap());
	}
	
	
	/**
	 * 
	 * @description
	 *  	获取表格数据
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月18日 下午4:05:52
	 */
	@RequestMapping(value="/datagrid")
	@ResponseBody
	@ApiOperation(value="获取系统消息列表", notes="获取系统消息列表", response=SysMessageWrapper.class)
	public void datagrid(SysMessageEntity sysMessageEntity, PageInfo<SysMessageEntity> page, HttpServletRequest request, HttpServletResponse response) {
		//拼接查询条件
		QueryWrapper<SysMessageEntity> queryWrapper = QueryUtils.installQueryWrapper(sysMessageEntity, request.getParameterMap());
		//执行查询
		IPage<SysMessageEntity> lstResult = sysMessageService.page(page.getPageEntity(), queryWrapper);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理，直接写到客户端
		ResponseUtil.write(response, new SysMessageWrapper(lstResult).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	保存系统消息
	 * @params
	 *      sysMessageEntity 系统消息实体
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月18日 下午5:28:02
	 */
	@RequestMapping(value="/save", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.save, name = "保存系统消息", memo = "新增或编辑保存了系统消息")
	@ApiOperation(value="保存系统消息", notes="保存系统消息")
	@ApiImplicitParam(name="sysMessageEntity", value="传入系统消息实体对象", required=true, dataType="SysMessageEntity")
	public ResultJson save(@Validated SysMessageEntity sysMessageEntity) {
		ResultJson j = new ResultJson();
		
		try{
			//新增保存
			if(StringUtils.isEmpty(sysMessageEntity.getId())) {
				//系统消息状态设为草稿
				sysMessageEntity.setStatus(SysMessageEntity.Sys_Message_Status_Draft);
				//保存
				sysMessageService.save(sysMessageEntity);
			}
			//编辑保存
			else {
				//根据id获取实体
				SysMessageEntity temp = sysMessageService.getById(sysMessageEntity.getId());
				//拷贝实体
				MyBeanUtils.copyBeanNotNull2Bean(sysMessageEntity, temp);
				//保存
				sysMessageService.saveOrUpdate(temp);
			}
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("保存失败");
			log.error("保存系统消息报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	删除系统消息
	 * @params
	 *      id 系统消息id
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月18日 下午5:29:40
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.del, name = "删除系统消息", memo = "删除了系统消息")
	@ApiOperation(value="删除系统消息", notes="删除系统消息")
	public ResultJson delete(@ApiParam(name="id", value="系统消息id", required=true) String id) {
		ResultJson j = new ResultJson();
		
		try {
			//id不为空就删除消息实体
			if(StringUtils.isNotEmpty(id)) {
				sysMessageService.removeById(id);
			}
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("删除失败");
			log.error("删除系统消息报错，错误信息：{}", e.getMessage() );
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	发布系统消息
	 * @params
	 *      id 消息实体id
	 * @return AjaxJson
	 * @author guyp
	 * @time 2019年12月19日 上午9:30:18
	 */
	@RequestMapping(value="/publish", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.normal, name = "发布系统消息", memo = "发布了系统消息")
	@ApiOperation(value="发布系统消息", notes="发布系统消息")
	public ResultJson publish(@ApiParam(name="id", value="系统消息id", required=true) String id) {
		ResultJson j = new ResultJson();
		
		try {
			if(StringUtils.isNotEmpty(id)) {
				//根据id获取消息实体
				SysMessageEntity msg = sysMessageService.getById(id);
				//判断是否已发布
				if(StringUtils.equals(SysMessageEntity.Sys_Message_Status_Publish, msg.getStatus())) {
					j.setSuccess(false);
					j.setMsg("已经发布的消息，不能再次发布");
					return j;
				}
				//线程池发送系统消息
				sysMessageService.publishSysMsg(msg);
			}
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("发布系统消息失败");
			log.error("发布系统消息报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
}
