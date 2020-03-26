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
import com.active4j.entity.base.model.LogType;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.base.model.ValueLabelModel;
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.entity.system.entity.SysDepartEntity;
import com.active4j.entity.system.entity.SysUserEntity;
import com.active4j.service.system.service.SysDepartService;
import com.active4j.service.system.service.SysUserService;
import com.active4j.service.system.service.SystemService;
import com.active4j.service.system.util.SystemUtils;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.system.wrapper.DeptWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;


/**
 * 部门管理controller
 * @author teli_
 *
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
@Api(value="系统管理-部门管理", tags={"部门管理操作接口"})
public class SysDeptController extends BaseController {
	
	@Autowired
	private SysDepartService sysDepartService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private SysUserService sysUserService;
	
	/**
	 * 获取部门类型
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/deptType")
	@ApiOperation(value = "获取部门类型", notes = "获取部门类型", response=BaseWrapper.class)
	public void deptType(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = SystemUtils.getDictionaryMap("dept_type");
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
	 * 根据ID 获取部门名称
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/deptName")
	@ApiOperation(value = "获取部门名称", notes = "根据部门主键ID, 获取部门名称", response=BaseWrapper.class)
	public void getDeptName(@ApiParam(name="id", value="部门ID") String id, HttpServletRequest request, HttpServletResponse response) {
		//根据部门id获取部门
		SysDepartEntity depart = sysDepartService.getById(id);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		if(null != depart) {
			//结果处理,直接写到客户端
			ResponseUtil.write(response, new BaseWrapper<SysDepartEntity>(depart).wrap());
		}else {
			//结果处理,直接写到客户端
			ResponseUtil.write(response, new BaseWrapper<SysDepartEntity>(new SysDepartEntity()).wrap());
		}
		
	}
	
	/**
	 * 根据用户名称获取部门信息
	 * @param userId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/deptNameByUserId")
	@ApiOperation(value="获取用户部门", notes="根据用户ID,获取用户所在部门名称", response=BaseWrapper.class)
	public void deptNameByUserId(@ApiParam(name="userId", value="用户ID") String userId, HttpServletRequest request, HttpServletResponse response) {
		SysUserEntity user = sysUserService.getById(userId);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		if(null != user && StringUtils.isNotEmpty(user.getDeptId())) {
			SysDepartEntity depart = sysDepartService.getById(user.getDeptId());
			
			//结果处理,直接写到客户端
			ResponseUtil.write(response, new BaseWrapper<SysDepartEntity>(depart).wrap());
		}else {
			ResponseUtil.write(response, new BaseWrapper<SysDepartEntity>(new SysDepartEntity()).wrap());
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
	@ApiOperation(value="获取部门列表", notes="获取部门列表，以树形显示", response=DeptWrapper.class)
	public void datagrid(SysDepartEntity sysDepartEntity, HttpServletRequest request, HttpServletResponse response) {

		//拼接查询条件
		QueryWrapper<SysDepartEntity> queryWrapper = QueryUtils.installQueryWrapper(sysDepartEntity, request.getParameterMap());
		
		//执行查询
		List<SysDepartEntity> lstResult = sysDepartService.list(queryWrapper);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new DeptWrapper(lstResult).wrap());
	}
	
	/**
	 * 保存方法
	 * @param sysDepartEntity
	 * @param parentId         上级部门
	 * @return
	 */
	@RequestMapping(value="/save", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@RequiresPermissions("sys:dept:save")
	@ResponseBody
	@Log(type = LogType.save, name = "保存部门信息", memo = "新增或编辑保存了部门信息")
	@ApiOperation(value="保存部门", notes="保存部门")
	@ApiImplicitParam(name="sysDepartEntity", value="传入部门实体对象", required=true, dataType="SysDepartEntity")
	public ResultJson save(@Validated SysDepartEntity sysDepartEntity)  {
		ResultJson j = new ResultJson();
		
		try{
			//设为空
			if(StringUtils.isEmpty(sysDepartEntity.getParentId()) || StringUtils.equals("-1", sysDepartEntity.getParentId())) {
				sysDepartEntity.setParentId(null);
			}
			
			if(StringUtils.isNotEmpty(sysDepartEntity.getParentId()) && StringUtils.equals(sysDepartEntity.getParentId(), sysDepartEntity.getId())) {
				j.setSuccess(false);
				j.setMsg("上级部门不能选择自己，请重新选择");
				return j;
			}
			
			if(StringUtils.isEmpty(sysDepartEntity.getId())) {
				//新增保存
				sysDepartService.save(sysDepartEntity);
			}else {
				//编辑保存
				SysDepartEntity tmp = sysDepartService.getById(sysDepartEntity.getId());
				MyBeanUtils.copyBeanNotNull2Bean(sysDepartEntity, tmp);
				
				sysDepartService.saveOrUpdate(tmp);
				
			}
			//刷新部门数据
			systemService.initDeparts();
		}catch(Exception e) {
			log.error("保存部门信息报错，错误信息：{}", e.getMessage());
			j.setSuccess(false);
			j.setMsg("保存失败");
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 *  删除操作
	 * @param sysDepartEntity
	 * @return
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@RequiresPermissions("sys:dept:delete")
	@ResponseBody
	@Log(type = LogType.del, name = "删除部门信息", memo = "删除了部门信息")
	@ApiOperation(value="删除部门", notes="根据部门ID删除部门")
	@ApiImplicitParam(name="sysDepartEntity", value="传入部门实体对象，必须带有部门ID", required=true, dataType="SysDepartEntity")
	public ResultJson delete(SysDepartEntity sysDepartEntity) {
		ResultJson j = new ResultJson();
		try {
			if(StringUtils.isNotEmpty(sysDepartEntity.getId())) {
				
				//查询部门下面的用户，存在用户则不能删除该部门
				List<SysUserEntity> lstUsers = sysDepartService.getUsersByDept(sysDepartEntity);
				if(null != lstUsers && lstUsers.size() > 0) {
					j.setSuccess(false);
					j.setMsg("该部门下存在用户，不能直接删除");
					return j;
				}
				
				List<SysDepartEntity> lstDepts = sysDepartService.getChildDepartsByDept(sysDepartEntity);
				if(null != lstDepts && lstDepts.size() > 0) {
					j.setSuccess(false);
					j.setMsg("该部门下存在子部门，不能直接删除");
					return j;
				}
				
				sysDepartService.removeById(sysDepartEntity.getId());
				//刷新部门数据
				systemService.initDeparts();
			}
			
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("部门删除错误!");
			log.error("删除部门信息出错，错误信息：{}", e.getMessage() );
			e.printStackTrace();
		}
		
		return j;
	}
	
}
