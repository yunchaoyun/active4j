package com.active4j.web.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.active4j.common.web.controller.BaseController;
import com.active4j.entity.base.model.AjaxJson;
import com.active4j.entity.base.model.CheckStatus;
import com.active4j.entity.base.model.CheckTreeModel;
import com.active4j.entity.base.model.TreeModel;
import com.active4j.entity.base.model.ValueLabelModel;
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.entity.system.entity.SysDepartEntity;
import com.active4j.entity.system.entity.SysMenuEntity;
import com.active4j.entity.system.entity.SysRoleEntity;
import com.active4j.service.system.service.SysDepartService;
import com.active4j.service.system.service.SysMenuService;
import com.active4j.service.system.service.SysRoleService;
import com.active4j.service.system.util.SystemUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;


/**
 * 公共使用的controller
 * @author teli_
 *
 */
@Controller
@RequestMapping("/common")
@Slf4j
public class CommonController extends BaseController {

	@Autowired
	private SysDepartService sysDepartService;
	
	@Autowired
	private SysMenuService sysMenuService;
	
	@Autowired
	private SysRoleService sysRoleService;
	
	/**
	 * 获取性别类型
	 * @param request
	 * @param response
	 */
	@RequestMapping("/sex/sexType")
	public void deptType(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = SystemUtils.getDictionaryMap("common_sex");
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
	 * 公共弹出页面,  部门的选择
	 * @return
	 */
	@RequestMapping("/selectdept")
	public void selectDept(HttpServletRequest request, HttpServletResponse response) {
		QueryWrapper<SysDepartEntity> queryWrapper = new QueryWrapper<SysDepartEntity>();
		queryWrapper.orderByAsc("ORDER_NO");
		//顶级父级部门
		List<SysDepartEntity> lstDeparts = sysDepartService.list(queryWrapper);
		
		List<TreeModel> lstModes = new ArrayList<TreeModel>();
		if(null != lstDeparts && lstDeparts.size() > 0) {
			lstDeparts.forEach(d -> {
				TreeModel model = new TreeModel();
				model.setId(d.getId());
				model.setParentId(StringUtils.isEmpty(d.getParentId()) ? "0" : d.getParentId());
				model.setTitle(d.getName());
				lstModes.add(model);
			});
		}
		
		ResponseUtil.write(response, new BaseWrapper<List<TreeModel>>(lstModes).wrap());
	}
	
	/**
	 * 公共弹出页面,  菜单的选择
	 * @return
	 */
	@RequestMapping("/selectmenu")
	public void selectMenu(HttpServletRequest request, HttpServletResponse response) {
		QueryWrapper<SysMenuEntity> queryWrapper = new QueryWrapper<SysMenuEntity>();
		queryWrapper.orderByAsc("ORDER_NO");
		//顶级父级部门
		List<SysMenuEntity> lstMenus = sysMenuService.list(queryWrapper);
		
		List<TreeModel> lstModes = new ArrayList<TreeModel>();
		if(null != lstMenus && lstMenus.size() > 0) {
			lstMenus.forEach(d -> {
				TreeModel model = new TreeModel();
				model.setId(d.getId());
				model.setParentId(StringUtils.isEmpty(d.getParentId()) ? "0" : d.getParentId());
				model.setTitle(d.getName());
				lstModes.add(model);
			});
		}

		ResponseUtil.write(response, new BaseWrapper<List<TreeModel>>(lstModes).wrap());
	}
	
	/**
	 * 公共弹出页面,  部门的选择
	 * @return
	 */
	@RequestMapping("/selectrole")
	public void selectRole(HttpServletRequest request, HttpServletResponse response) {
		QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<SysRoleEntity>();
		queryWrapper.orderByAsc("ORDER_NO");
		//顶级父级部门
		List<SysRoleEntity> lstRoles = sysRoleService.list(queryWrapper);
		
		List<TreeModel> lstModes = new ArrayList<TreeModel>();
		if(null != lstRoles && lstRoles.size() > 0) {
			lstRoles.forEach(d -> {
				TreeModel model = new TreeModel();
				model.setId(d.getId());
				model.setParentId(StringUtils.isEmpty(d.getParentId()) ? "0" : d.getParentId());
				model.setTitle(d.getName());
				lstModes.add(model);
			});
		}

		ResponseUtil.write(response, new BaseWrapper<List<TreeModel>>(lstModes).wrap());
	}
	
	/**
	 * 公共弹出页面,  角色的选择
	 * @return
	 */
	@RequestMapping("/selectroles")
	public void selectRoles(HttpServletRequest request, HttpServletResponse response) {
		QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<SysRoleEntity>();
		queryWrapper.orderByAsc("ORDER_NO");
		//顶级父级部门
		List<SysRoleEntity> lstRoles = sysRoleService.list(queryWrapper);
		
		List<CheckStatus> checkArr = new ArrayList<CheckStatus>();
		checkArr.add(new CheckStatus());
		
		List<CheckTreeModel> lstModes = new ArrayList<CheckTreeModel>();
		if(null != lstRoles && lstRoles.size() > 0) {
			lstRoles.forEach(d -> {
				CheckTreeModel model = new CheckTreeModel();
				model.setId(d.getId());
				model.setParentId(StringUtils.isEmpty(d.getParentId()) ? "0" : d.getParentId());
				model.setTitle(d.getName());
				model.setCheckArr(checkArr);
				lstModes.add(model);
			});
		}

		ResponseUtil.write(response, new BaseWrapper<List<CheckTreeModel>>(lstModes).wrap());
	}
	
	/**
	 * 根据角色的ID，获取权限配置
	 */
	@RequestMapping("/getmenutree")
	@ResponseBody
	public AjaxJson getMenuTree(@RequestParam String roleId, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
			//查询之前的配置
			List<String> lstMenusIds = sysRoleService.getMenuByRole(roleId);
			
			QueryWrapper<SysMenuEntity> queryWrapper = new QueryWrapper<SysMenuEntity>();
			queryWrapper.isNull("PARENT_ID");
			queryWrapper.orderByAsc("ORDER_NO");
			//顶级父级菜单
			List<SysMenuEntity> lstParentMenus = sysMenuService.list(queryWrapper);
			//拼接
			StringBuffer sb = new StringBuffer();
			sb = sb.append("[");
			roleMenuContact(lstMenusIds, lstParentMenus, sb);
			sb = sb.append("]");
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("data", sb.toString());
			j.setAttributes(map);
		}catch(Exception e) {
			log.error("配置权限取菜单出错，错误信息:" + e.getMessage());
			j.setSuccess(false);
		}
		
		return j;
	}
	
	/**
	 * 采用递归的方式，拼接父子菜单
	 * @param lstMenus
	 * @param sb
	 */
	private void roleMenuContact(List<String> lstMenusIds, List<SysMenuEntity> lstMenus, StringBuffer sb) {
		if(null != lstMenus && lstMenus.size() > 0) {
			for(int i = 0; i < lstMenus.size(); i++) {
				SysMenuEntity menu = lstMenus.get(i);
				if(lstMenusIds.size() > 0 && lstMenusIds.contains(menu.getId())) {
					//赋值树形结构
					sb = sb.append("{").append("text:").append("\"").append(menu.getName()).append("\",").append("state:{").append("checked:true").append("},").append("id:").append("\"").append(menu.getId()).append("\"");
				}else {
					//赋值树形结构
					sb = sb.append("{").append("text:").append("\"").append(menu.getName()).append("\",").append("id:").append("\"").append(menu.getId()).append("\"");
				}
				
				//查询子部门
				QueryWrapper<SysMenuEntity> queryWrapper = new QueryWrapper<SysMenuEntity>();
				queryWrapper.eq("PARENT_ID", menu.getId());
				queryWrapper.orderByAsc("ORDER_NO");
				List<SysMenuEntity> lstChildren = sysMenuService.list(queryWrapper);
				
				if(null != lstChildren && lstChildren.size() > 0) {
					sb = sb.append(", nodes: [");
					//递归
					roleMenuContact(lstMenusIds, lstChildren, sb);
					sb.append("]");
				}
				
				if(i == lstMenus.size() - 1) {
					sb = sb.append("}");
				}else {
					sb = sb.append("},");
				}
			}
		}
	}
	
}
