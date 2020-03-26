package com.active4j.web.system.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.active4j.entity.base.model.CheckStatus;
import com.active4j.entity.base.model.CheckTreeModel;
import com.active4j.entity.base.model.LogType;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.entity.system.entity.SysMenuEntity;
import com.active4j.entity.system.entity.SysRoleEntity;
import com.active4j.entity.system.entity.SysUserEntity;
import com.active4j.service.system.service.SysMenuService;
import com.active4j.service.system.service.SysRoleService;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.system.wrapper.RoleWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title SysRoleController.java
 * @description 
		角色管理
 * @time  2019年12月27日 下午2:29:15
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/sys/role")
@Slf4j
@Api(value="系统管理-角色管理", tags={"角色管理操作接口"})
public class SysRoleController extends BaseController {
	
	@Autowired
	private SysRoleService sysRoleService;
	
	@Autowired
	private SysMenuService sysMenuService;

	/**
	 * 权限配置树形数据生成
	 * @return
	 */
	@RequestMapping(value="/setMenu", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ApiOperation(value = "获取权限树形数据", notes = "获取权限树形数据", response=BaseWrapper.class)
	public void setMenu(@ApiParam(name="roleId", value="角色ID", required=true) String roleId, HttpServletRequest request, HttpServletResponse response) {
		QueryWrapper<SysMenuEntity> queryWrapper = new QueryWrapper<SysMenuEntity>();
		queryWrapper.orderByAsc("ORDER_NO");
		//顶级父级部门
		List<SysMenuEntity> lstMenus = sysMenuService.list(queryWrapper);
		
		//查询之前的配置
		List<String> lstMenusIds = sysRoleService.getMenuByRole(roleId);
		
		List<CheckTreeModel> lstModes = new ArrayList<CheckTreeModel>();
		if(null != lstMenus && lstMenus.size() > 0) {
			lstMenus.forEach(d -> {
				CheckTreeModel model = new CheckTreeModel();
				model.setId(d.getId());
				model.setParentId(StringUtils.isEmpty(d.getParentId()) ? "0" : d.getParentId());
				model.setTitle(d.getName());
				List<CheckStatus> checkArr = new ArrayList<CheckStatus>();
				CheckStatus init = new CheckStatus();
				for(String menusId : lstMenusIds) {
					if(StringUtils.equals(menusId, d.getId())) {
						init.setIsChecked("1");
					}
				}
				checkArr.add(init);
				model.setCheckArr(checkArr);
				lstModes.add(model);
			});
		}
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		ResponseUtil.write(response, new BaseWrapper<List<CheckTreeModel>>(lstModes).wrap());
	}
	
	/**
	 * 根据ID 获取角色名称
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/roleName")
	@ApiOperation(value = "获取角色名称", notes = "根据ID获取角色名称", response=BaseWrapper.class)
	public void getRoleName(@ApiParam(name="id", value="角色ID") String id, HttpServletRequest request, HttpServletResponse response) {
		//根据角色id获取角色
		SysRoleEntity role = sysRoleService.getById(id);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		if(null != role) {
			//结果处理,直接写到客户端
			ResponseUtil.write(response, new BaseWrapper<SysRoleEntity>(role).wrap());
		}else {
			//结果处理,直接写到客户端
			ResponseUtil.write(response, new BaseWrapper<SysRoleEntity>(new SysRoleEntity()).wrap());
		}
		
	}
	
	/**
	 *  获取表格数据 树形结构
	 * @param sysRoleEntity
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/datagrid")
	@ResponseBody
	@ApiOperation(value="获取角色列表", notes="获取角色列表，树形结构显示", response=RoleWrapper.class)
	public void datagrid(SysRoleEntity sysRoleEntity, HttpServletRequest request, HttpServletResponse response) {
		//拼接查询条件
		QueryWrapper<SysRoleEntity> queryWrapper = QueryUtils.installQueryWrapper(sysRoleEntity, request.getParameterMap());
		
		//执行查询
		List<SysRoleEntity> lstResult = sysRoleService.list(queryWrapper);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new RoleWrapper(lstResult).wrap());
	}
	
	/**
	 * 保存方法
	 * @param sysRoleEntity
	 * @param parentId         上级
	 * @return
	 */
	@RequestMapping(value="/save", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@RequiresPermissions("sys:role:save")
	@ResponseBody
	@Log(type = LogType.save, name = "保存角色信息", memo = "新增或编辑保存了角色信息")
	@ApiOperation(value = "保存角色", notes = "保存角色")
	@ApiImplicitParam(name="sysRoleEntity", value="传入角色实体对象", required=true, dataType="SysRoleEntity")
	public ResultJson save(@Validated SysRoleEntity sysRoleEntity)  {
		ResultJson j = new ResultJson();
		
		try{
			//设为空
			if(StringUtils.isEmpty(sysRoleEntity.getParentId()) || StringUtils.equals("-1", sysRoleEntity.getParentId())) {
				sysRoleEntity.setParentId(null);
			}
			
			if(StringUtils.isNotEmpty(sysRoleEntity.getParentId()) && StringUtils.equals(sysRoleEntity.getParentId(), sysRoleEntity.getId())) {
				j.setSuccess(false);
				j.setMsg("上级角色不能选择自己，请重新选择");
				return j;
			}
			
			if(StringUtils.isEmpty(sysRoleEntity.getId())) {
				//新增保存
				sysRoleService.save(sysRoleEntity);
			}else {
				//编辑保存
				SysRoleEntity tmp = sysRoleService.getById(sysRoleEntity.getId());
				MyBeanUtils.copyBeanNotNull2Bean(sysRoleEntity, tmp);
				
				sysRoleService.saveOrUpdate(tmp);
			}
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("保存失败");
			log.error("保存角色信息报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 *  删除操作
	 * @param sysRoleEntity
	 * @return
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@RequiresPermissions("sys:role:delete")
	@ResponseBody
	@Log(type = LogType.del, name = "删除角色信息", memo = "删除了角色信息")
	@ApiImplicitParam(name="sysRoleEntity", value="传入角色实体对象，必须带有角色ID", required=true, dataType="SysRoleEntity")
	public ResultJson delete(SysRoleEntity sysRoleEntity) {
		ResultJson j = new ResultJson();
		try {
			if(StringUtils.isNotEmpty(sysRoleEntity.getId())) {
				//删除前校验该角色下有没有相关用户
				List<SysUserEntity> lstUsers = sysRoleService.getUsersByRole(sysRoleEntity);
				if(null != lstUsers && lstUsers.size() > 0) {
					j.setSuccess(false);
					j.setMsg("该角色下存在用户配置，不能直接删除");
					return j;
				}
				
				//删除前校验该角色下是否存在下属角色
				List<SysRoleEntity> lstRoles = sysRoleService.getChildRolesByRole(sysRoleEntity);
				if(null != lstRoles && lstRoles.size() > 0) {
					j.setSuccess(false);
					j.setMsg("该角色下存在下属角色，不能直接删除");
					return j;
				}
				
				sysRoleService.deleteRole(sysRoleEntity);
			}
			
		}catch(Exception e) {
			j.setMsg("删除失败");
			log.error("删除角色信息出错，错误信息：{}", e.getMessage() );
			j.setSuccess(false);
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 保存权限菜单
	 * @param roleId
	 * @param roleMenuIds
	 * @return
	 */
	@RequestMapping(value="/saverolemenu", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@RequiresPermissions("sys:role:saverolemenu")
	@ResponseBody
	@Log(type = LogType.update, name = "更新权限配置", memo = "保存更新了权限配置")
	@ApiOperation(value="保存权限菜单", notes="保存权限菜单")
	public ResultJson saveRoleMenu(@ApiParam(name="roleId", value="角色ID", required=true) String roleId, @ApiParam(name="roleMenuIds", value="角色菜单ID集合", required=true) String roleMenuIds) {
		ResultJson j = new ResultJson();
		try {
			
			//保存权限配置
			sysRoleService.saveRoleMenu(roleId, roleMenuIds);
			
			j.setMsg("权限配置成功");
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("权限配置失败");
			log.error("权限配置遇到错误，错误信息:" + e.getMessage());
		}
		
		return j;
	}
}
