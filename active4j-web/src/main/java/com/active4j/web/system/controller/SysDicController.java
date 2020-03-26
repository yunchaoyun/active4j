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
import com.active4j.entity.base.model.LogType;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.base.model.TreeSysDicModel;
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.entity.system.entity.SysDicEntity;
import com.active4j.entity.system.entity.SysDicValueEntity;
import com.active4j.service.system.service.SysDicService;
import com.active4j.service.system.service.SysDicValueService;
import com.active4j.service.system.service.SystemService;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.system.wrapper.DicWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title SysDicController.java
 * @description 
		数据字典管理
 * @time  2019年12月27日 下午1:48:29
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/sys/dic")
@Slf4j
@Api(value="系统管理-数据字典管理", tags={"数据字典管理操作接口"})
public class SysDicController extends BaseController {

	@Autowired
	private SysDicService sysDicService;
	
	@Autowired
	private SysDicValueService sysDicValueService;
	
	@Autowired
	private SystemService systemService;
	
	/**
	 * 根据ID 获取字典
	 * @param parentId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/getDic")
	@ApiOperation(value="获取字典", notes="获取数据字典配置值", response=BaseWrapper.class)
	public void getDic(@ApiParam(name="parentId", value="数据字典ID", required=true) String parentId, HttpServletRequest request, HttpServletResponse response) {
		SysDicEntity dic = sysDicService.getById(parentId);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		ResponseUtil.write(response, new BaseWrapper<SysDicEntity>(dic).wrap());
	}
	
	/**
	 *    获取数据，树形结构显示
	 * @param sysDicEntity
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/datagrid")
	@ResponseBody
	@ApiOperation(value="获取数据字典列表", notes="获取数据字典列表，树形结构显示", response=DicWrapper.class)
	public void datagrid(SysDicEntity sysDicEntity, HttpServletRequest request, HttpServletResponse response) {
		
		//拼接查询条件
		QueryWrapper<SysDicEntity> queryWrapper = QueryUtils.installQueryWrapper(sysDicEntity, request.getParameterMap());
		
		//执行查询
		List<SysDicEntity> lstResult = sysDicService.list(queryWrapper);
		
		List<TreeSysDicModel> lstModes = getDicTree(lstResult);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		ResponseUtil.write(response, new DicWrapper(lstModes).wrap());
		
	}
	
	/**
	 * 保存方法
	 * @param sysDicEntity
	 * @return
	 */
	@RequestMapping(value="/save", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@RequiresPermissions("sys:dic:save")
	@ResponseBody
	@Log(type = LogType.save, name = "保存数据字典信息", memo = "新增或编辑保存了数据字典信息")
	@ApiOperation(value = "保存数据字典", notes = "保存数据字典")
	@ApiImplicitParam(name="sysDicEntity", value="传入数据字典实体对象", required=true, dataType="SysDicEntity")
	public ResultJson save(@Validated SysDicEntity sysDicEntity)  {
		ResultJson j = new ResultJson();
		
		try{
			if(StringUtils.isEmpty(sysDicEntity.getId())) {
				//新增保存
				sysDicService.save(sysDicEntity);
				//刷新一下数据字典
				systemService.initDictionary();
			}else {
				//编辑保存
				SysDicEntity tmp = sysDicService.getById(sysDicEntity.getId());
				MyBeanUtils.copyBeanNotNull2Bean(sysDicEntity, tmp);
				sysDicService.saveOrUpdate(tmp);
				//刷新一下数据字典
				systemService.initDictionary();
			}
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("保存数据字典失败");
			log.error("保存数据字典信息报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 保存字典值
	 * @param sysDicValueEntity
	 * @param parentId
	 * @return
	 */
	@RequestMapping(value="/saveval", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@RequiresPermissions("sys:dic:saveval")
	@ResponseBody
	@Log(type = LogType.save, name = "保存字典值", memo = "新增或编辑保存了字典值")
	@ApiOperation(value = "保存数据字典值", notes = "保存数据字典值，键值对")
	@ApiImplicitParam(name="sysDicValueEntity", value="传入数据字典值实体对象", required=true, dataType="SysDicValueEntity")
	public ResultJson saveVal(@Validated SysDicValueEntity sysDicValueEntity, @ApiParam(name="parentId", value="父数据字典ID", required=true) String parentId)  {
		ResultJson j = new ResultJson();
		
		try{
			//后端校验
			if(StringUtils.isEmpty(parentId)) {
				j.setSuccess(false);
				j.setMsg("请选择字典");
				return j;
			}
			
			sysDicValueEntity.setParentId(parentId);
			
			if(StringUtils.isEmpty(sysDicValueEntity.getId())) {
				//新增保存
				sysDicValueService.save(sysDicValueEntity);
				//刷新一下数据字典
				systemService.initDictionary();
			}else {
				//编辑保存
				SysDicValueEntity tmp = sysDicValueService.getById(sysDicValueEntity.getId());
				MyBeanUtils.copyBeanNotNull2Bean(sysDicValueEntity, tmp);
				sysDicValueService.saveOrUpdate(tmp);
				//刷新一下数据字典
				systemService.initDictionary();
			}
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("保存字典值失败");
			log.error("保存字典值报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 *  删除操作
	 * @return
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@RequiresPermissions("sys:dic:delete")
	@ResponseBody
	@Log(type = LogType.del, name = "删除数据字典信息", memo = "删除了数据字典信息")
	@ApiOperation(value = "删除字典", notes = "删除字典,根据传入的值删除字典还是字典值")
	public ResultJson delete(@ApiParam(name="id", value="数据字典ID", required=true) String id, @ApiParam(name="parentId", value="父数据字典ID", required=true) String parentId) {
		ResultJson j = new ResultJson();
		try {
			
			if(StringUtils.equals("-1", parentId)) {
				//判断是否存在子值
				QueryWrapper<SysDicValueEntity> queryWrapper = new QueryWrapper<SysDicValueEntity>();
				queryWrapper.eq("PARENT_ID", id);
				List<SysDicValueEntity> lstDicValues = sysDicValueService.list(queryWrapper);
				if(lstDicValues.size() > 0) {
					j.setSuccess(false);
					j.setMsg("该字典下存在子值，请先删除字典值");
					return j;
				}
				
				//字典的删除
				sysDicService.removeById(id);
			}else {
				//值的删除
				sysDicValueService.removeById(id);
			}
			
			//刷新一下数据字典
			systemService.initDictionary();
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("删除失败");
			log.error("删除数据字典信息出错，错误信息：{}", e.getMessage() );
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 获取数据字典的树形结构显示
	 * @param lstDics
	 * @return
	 */
	public List<TreeSysDicModel> getDicTree(List<SysDicEntity> lstDics) {
		
		List<TreeSysDicModel> lstTree = new ArrayList<TreeSysDicModel>();
		
		if(null != lstDics && lstDics.size() > 0) {
			for(SysDicEntity dic : lstDics) {
				
				//父表的数据
				TreeSysDicModel treeModel = new TreeSysDicModel();
				treeModel.setId(dic.getId());
				treeModel.setParentId("-1");
				treeModel.setMemo(dic.getMemo());
				treeModel.setName(dic.getName());
				treeModel.setValue(dic.getCode());
				lstTree.add(treeModel);
				
				//子表的数据
				QueryWrapper<SysDicValueEntity> queryWrapper = new QueryWrapper<SysDicValueEntity>();
				queryWrapper.eq("PARENT_ID", dic.getId());
				List<SysDicValueEntity> lstValues = sysDicValueService.list(queryWrapper);
				if(null != lstValues && lstValues.size() > 0) {
					for(SysDicValueEntity val : lstValues) {
						treeModel = new TreeSysDicModel();
						treeModel.setId(val.getId());
						treeModel.setParentId(dic.getId());
						treeModel.setMemo("");
						treeModel.setName(val.getLabel());
						treeModel.setValue(val.getValue());
						lstTree.add(treeModel);
					}
				}
			}
		}
		
		return lstTree;
	}
}
