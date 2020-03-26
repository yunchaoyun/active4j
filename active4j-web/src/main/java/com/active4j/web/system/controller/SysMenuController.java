package com.active4j.web.system.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.active4j.common.util.MyBeanUtils;
import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.annotation.Log;
import com.active4j.entity.base.model.ActiveUser;
import com.active4j.entity.base.model.LogType;
import com.active4j.entity.base.model.MenuModel;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.base.model.ValueLabelModel;
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.entity.system.entity.SysMenuEntity;
import com.active4j.service.system.service.SysMenuService;
import com.active4j.service.system.service.SysUserService;
import com.active4j.service.system.util.SystemUtils;
import com.active4j.web.core.config.shiro.ShiroUtils;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.system.wrapper.MenuWrapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title SysMenuController.java
 * @description 
		菜单管理
 * @time  2019年12月27日 下午2:34:00
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/sys/menu")
@Slf4j
@Api(value="系统管理-菜单管理", tags={"菜单管理操作接口"})
public class SysMenuController extends BaseController {
	
	@Autowired
	private SysMenuService sysMenuService;
	
	@Autowired
	private SysUserService sysUserService;
	
	/**
	 * 获取菜单类型
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/menuType")
	@ApiOperation(value = "获取菜单类型", notes = "获菜单类型", response=BaseWrapper.class)
	public void menuType(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = SystemUtils.getDictionaryMap("menu_type");
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
	 * 获取菜单名称
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/menuName")
	@ApiOperation(value = "获取菜单名称", notes = "获菜单名称", response=BaseWrapper.class)
	public void menuName(@ApiParam(name="id", value="菜单ID") String id, HttpServletRequest request, HttpServletResponse response) {
		//根据id获取目录
		SysMenuEntity menu = sysMenuService.getById(id);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		if(null != menu) {
			//结果处理,直接写到客户端
			ResponseUtil.write(response, new BaseWrapper<SysMenuEntity>(menu).wrap());
		}else {
			//结果处理,直接写到客户端
			ResponseUtil.write(response, new BaseWrapper<SysMenuEntity>(new SysMenuEntity()).wrap());
		}
		
	}
	
	/**
	 *  获取表格数据 树形结构
	 * @param sysDepartEntity
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/datagrid")
	@ResponseBody
	@ApiOperation(value="获取菜单列表", notes="获取菜单列表，树形结构显示", response=MenuWrapper.class)
	public void datagrid(SysMenuEntity sysMenuEntity, HttpServletRequest request, HttpServletResponse response) {
		//拼接查询条件
		QueryWrapper<SysMenuEntity> queryWrapper = QueryUtils.installQueryWrapper(sysMenuEntity, request.getParameterMap());
		
		//执行查询
		List<SysMenuEntity> lstResult = sysMenuService.list(queryWrapper);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new MenuWrapper(lstResult).wrap());
	}
	
	/**
	 * 保存方法
	 * @param sysMenuEntity
	 * @param parentId         上级菜单
	 * @return
	 */
	@RequestMapping(value="/save", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@RequiresPermissions("sys:menu:save")
	@ResponseBody
	@Log(type = LogType.save, name = "保存菜单信息", memo = "新增或编辑保存了菜单信息")
	@ApiOperation(value = "保存菜单", notes = "保存菜单")
	@ApiImplicitParam(name="sysMenuEntity", value="传入菜单实体对象", required=true, dataType="SysMenuEntity")
	public ResultJson save(@Validated SysMenuEntity sysMenuEntity)  {
		ResultJson j = new ResultJson();
		
		try{
			//设为空
			if(StringUtils.isEmpty(sysMenuEntity.getParentId()) || StringUtils.equals("-1", sysMenuEntity.getParentId())) {
				sysMenuEntity.setParentId(null);
			}
			
			if(StringUtils.isNotEmpty(sysMenuEntity.getParentId()) && StringUtils.equals(sysMenuEntity.getParentId(), sysMenuEntity.getId())) {
				j.setSuccess(false);
				j.setMsg("上级目录不能选择自己，请重新选择");
				return j;
			}
			
			if(StringUtils.isEmpty(sysMenuEntity.getId())) {
				//新增保存
				sysMenuService.save(sysMenuEntity);
			}else {
				//编辑保存
				SysMenuEntity tmp = sysMenuService.getById(sysMenuEntity.getId());
				MyBeanUtils.copyBeanNotNull2Bean(sysMenuEntity, tmp);
				
				sysMenuService.saveOrUpdate(tmp);
			}
		}catch(Exception e) {
			j.setMsg("保存失败");
			j.setSuccess(false);
			log.error("保存菜单信息报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 *  删除操作
	 * @param sysMenuEntity
	 * @return
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@RequiresPermissions("sys:menu:delete")
	@ResponseBody
	@Log(type = LogType.del, name = "删除菜单信息", memo = "删除了菜单信息")
	@ApiOperation(value = "删除菜单", notes = "删除菜单根据菜单ID")
	@ApiImplicitParam(name="sysMenuEntity", value="传入菜单实体对象，必须带有菜单ID", required=true, dataType="SysMenuEntity")
	public ResultJson delete(SysMenuEntity sysMenuEntity) {
		ResultJson j = new ResultJson();
		try {
			
			if(StringUtils.isNotEmpty(sysMenuEntity.getId())) {
				//删除之前校验是否存在子菜单
				List<SysMenuEntity> lstMenus = sysMenuService.getChildMenusByMenu(sysMenuEntity);
				if(null != lstMenus && lstMenus.size() > 0) {
					j.setSuccess(false);
					j.setMsg("该菜单下存在子级菜单，不能直接删除");
					return j;
				}
				
				sysMenuService.deleteMenu(sysMenuEntity);
			}
			
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("删除失败");
			log.error("删除菜单信息出错，错误信息：{}", e.getMessage() );
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	平台显示菜单
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2019年12月30日 下午11:02:59
	 */
	@RequestMapping(value="/list")
	@ApiOperation(value = "获取权限菜单", notes = "获取权限菜单")
	public void list(HttpServletRequest request, HttpServletResponse response) {
		ActiveUser user = ShiroUtils.getSessionUser();
		
		//缓存中不存在，重新查询
		ResultJson result = new ResultJson();
		List<MenuModel> lstModes = sysUserService.getActiveUserMenu(user.getId());
		result.setData(lstModes);
		String json = JSON.toJSONString(result);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		ResponseUtil.write(response, json);
	}
	
}
