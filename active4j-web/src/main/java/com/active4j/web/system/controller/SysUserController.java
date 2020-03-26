package com.active4j.web.system.controller;

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
import com.active4j.entity.base.PageInfo;
import com.active4j.entity.base.annotation.Log;
import com.active4j.entity.base.model.LogType;
import com.active4j.entity.base.model.ResultJson;
import com.active4j.entity.base.model.RoleLabelModel;
import com.active4j.entity.base.wrapper.BaseWrapper;
import com.active4j.entity.system.entity.SysRoleEntity;
import com.active4j.entity.system.entity.SysUserEntity;
import com.active4j.service.system.service.SysUserService;
import com.active4j.service.system.util.SystemUtils;
import com.active4j.web.core.config.shiro.ShiroUtils;
import com.active4j.web.core.web.util.QueryUtils;
import com.active4j.web.core.web.util.ResponseUtil;
import com.active4j.web.system.wrapper.UserWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @title SysUserController.java
 * @description 
		用户管理
 * @time  2019年12月27日 下午1:57:32
 * @author guyp
 * @version 1.0
 */
@Controller
@RequestMapping("/sys/user")
@Slf4j
@Api(value="系统管理-用户管理", tags={"用户管理操作接口"})
public class SysUserController extends BaseController {

	@Autowired
	private SysUserService sysUserService;
	
	/**
	 *  获取表格数据 树形结构
	 * @param sysUserEntity
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/datagrid")
	@ResponseBody
	@ApiOperation(value="获取用户列表", notes="获取用户列表", response=UserWrapper.class)
	public void datagrid(SysUserEntity sysUserEntity, PageInfo<SysUserEntity> page, HttpServletRequest request, HttpServletResponse response) {
		//拼接查询条件
		QueryWrapper<SysUserEntity> queryWrapper = QueryUtils.installQueryWrapper(sysUserEntity, request.getParameterMap());
		
		//执行查询
		IPage<SysUserEntity> lstResult = sysUserService.page(page.getPageEntity(), queryWrapper);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new UserWrapper(lstResult).wrap());
	}
	
	/**
	 * 根据用户ID获取角色信息
	 * @param userId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/roleByUserId")
	@ApiOperation(value="获取角色信息", notes="根据用户ID获取角色信息", response=BaseWrapper.class)
	public void roleByUserId(@ApiParam(name="userId", value="用户ID") String userId, HttpServletRequest request, HttpServletResponse response) {
		SysUserEntity user = sysUserService.getById(userId);
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		if(null != user && StringUtils.isNotEmpty(user.getDeptId())) {
			//所属角色
			List<SysRoleEntity> lstRoles = sysUserService.getUserRoles(user.getId());
			StringBuffer sbRoleIds = new StringBuffer();
			StringBuffer sbRoleNames = new StringBuffer();
			for(SysRoleEntity role : lstRoles) {
				sbRoleIds = sbRoleIds.append(role.getId()).append(",");
				sbRoleNames = sbRoleNames.append(role.getName()).append(",");
			}
			RoleLabelModel labelMode = new RoleLabelModel();
			if(sbRoleIds.length() > 0) {
				labelMode.setRoleNames(sbRoleNames.substring(0, sbRoleNames.length() - 1));
				labelMode.setRoleIds(sbRoleIds.substring(0, sbRoleIds.length() - 1));
			}
			//结果处理,直接写到客户端
			ResponseUtil.write(response, new BaseWrapper<RoleLabelModel>(labelMode).wrap());
		}else {
			ResponseUtil.write(response, new BaseWrapper<RoleLabelModel>(new RoleLabelModel()).wrap());
		}
	}
	
	/**
	 * 保存方法
	 * @param sysUserEntity
	 * @return
	 */
	@RequestMapping(value="/save", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@RequiresPermissions("sys:user:save")
	@ResponseBody
	@Log(type = LogType.save, name = "保存用户信息", memo = "新增或编辑保存了用户信息")
	@ApiOperation(value = "保存用户", notes = "保存用户")
	@ApiImplicitParam(name="sysUserEntity", value="传入用户实体对象", required=true, dataType="SysUserEntity")
	public ResultJson save(@Validated SysUserEntity sysUserEntity, @ApiParam(name="roleIds", value="角色ID集合") String roleIds)  {
		ResultJson j = new ResultJson();
		
		try{
			//新增保存
			if(StringUtils.isEmpty(sysUserEntity.getId())) {
				//初始化密码
				sysUserEntity.setSalt(ShiroUtils.getRandomSalt());
				sysUserEntity.setPassword(ShiroUtils.md5("123456", sysUserEntity.getSalt()));
				
				sysUserService.saveUser(sysUserEntity, roleIds);
			}
			//编辑保存
			else {
				SysUserEntity tmp = sysUserService.getById(sysUserEntity.getId());
				MyBeanUtils.copyBeanNotNull2Bean(sysUserEntity, tmp);
				tmp.setSalt(ShiroUtils.getRandomSalt());
				tmp.setPassword(ShiroUtils.md5("123456", tmp.getSalt()));
				sysUserService.saveOrUpdateUser(tmp, roleIds);
			}
			
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("保存失败");
			log.error("保存用户信息报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 *  删除操作
	 * @param sysUserEntity
	 * @return
	 */
	@RequestMapping(value="/delete", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@RequiresPermissions("sys:user:delete")
	@ResponseBody
	@Log(type = LogType.del, name = "删除用户信息", memo = "删除了用户信息")
	@ApiOperation(value="删除部门", notes="根据部门ID删除部门")
	@ApiImplicitParam(name="sysUserEntity", value="传入用户实体对象，必须带有用户ID", required=true, dataType="SysUserEntity")
	public ResultJson delete(SysUserEntity sysUserEntity) {
		ResultJson j = new ResultJson();
		try {
			
			if(StringUtils.isNotEmpty(sysUserEntity.getId())) {
				sysUserService.delete(sysUserEntity);
			}
			
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("删除失败");
			log.error("删除用户信息出错，错误信息：{}", e.getMessage() );
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	保存密码
	 * @params
	 * @return ResultJson
	 * @author guyp
	 * @time 2020年1月6日 下午5:20:27
	 */
	@RequestMapping(value="/savepwd", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.save, name = "保存密码", memo = "用户修改保存了密码")
	@ApiOperation(value="修改密码", notes="用户修改密码")
	public ResultJson savePwd(@ApiParam(name="oldPassword", value="当前密码", required=true) String oldPassword, @ApiParam(name="password", value="新密码", required=true) String password, @ApiParam(name="repassword", value="确认密码", required=true) String repassword)  {
		ResultJson j = new ResultJson();
		
		try{
			if(StringUtils.isEmpty(oldPassword)) {
				j.setSuccess(false);
				j.setMsg("请输入当前密码");
				return j;
			}
			
			if(StringUtils.isEmpty(password)) {
				j.setSuccess(false);
				j.setMsg("请输入新密码");
				return j;
			}
			
			if(StringUtils.isEmpty(repassword)) {
				j.setSuccess(false);
				j.setMsg("请确认新密码");
				return j;
			}
			//获取登录用户id
			String userId = ShiroUtils.getSessionUser().getId();
			//根据id获取用户
			SysUserEntity user = sysUserService.getById(userId);
			//校验当前密码
			if(!StringUtils.equals(ShiroUtils.md5(oldPassword, user.getSalt()), user.getPassword())) {
				j.setSuccess(false);
				j.setMsg("当前密码不正确");
				return j;
			}
			
			String pwd = StringUtils.trim(password);
			if(pwd.length() < 6 || pwd.length() > 12) {
				j.setSuccess(false);
				j.setMsg("密码必须6到12位，且不能出现空格");
				return j;
			}
			
			if(!StringUtils.equals(pwd, repassword)) {
				j.setSuccess(false);
				j.setMsg("两次密码输入不一致");
				return j;
			}
			//密码参数赋值
			user.setSalt(ShiroUtils.getRandomSalt());
			user.setPassword(ShiroUtils.md5(pwd, user.getSalt()));
			//更新密码
			sysUserService.saveOrUpdate(user);
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("修改密码失败");
			log.error("用户修改密码报错，错误信息{}", e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	获取用户基本资料
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2020年1月6日 下午5:53:46
	 */
	@RequestMapping(value="/info")
	@ApiOperation(value="获取用户基本资料", notes="获取用户基本资料", response=BaseWrapper.class)
	public void info(HttpServletRequest request, HttpServletResponse response) {
		//获取登录用户id
		String userId = ShiroUtils.getSessionUser().getId();
		//获取任务明细
		SysUserEntity user = sysUserService.getById(userId);
		//部门id处理
		user.setDeptId(SystemUtils.getDeptNameById(user.getDeptId()));
		//用户状态处理
		user.setStatus(SystemUtils.getDictionaryValue("user_status", user.getStatus()));
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<SysUserEntity>(user).wrap());
	}
	
	/**
	 * 
	 * @description
	 *  	保存基本资料
	 * @params
	 * @return ResultJson
	 * @author guyp
	 * @time 2020年1月7日 上午9:15:35
	 */
	@RequestMapping(value="/saveinfo", method=RequestMethod.POST, consumes=MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	@ResponseBody
	@Log(type = LogType.save, name = "保存基本资料", memo = "编辑保存了基本资料")
	@ApiOperation(value = "保存基本资料", notes = "保存基本资料")
	@ApiImplicitParam(name="sysUserEntity", value="传入用户实体对象", required=true, dataType="SysUserEntity")
	public ResultJson saveInfo(SysUserEntity sysUserEntity)  {
		ResultJson j = new ResultJson();
		
		try{
			//后端校验
			if(StringUtils.isEmpty(sysUserEntity.getId())) {
				j.setSuccess(false);
				j.setMsg("用户id不能为空");
				return j;
			}
			
			if(StringUtils.isEmpty(sysUserEntity.getRealName())) {
				j.setSuccess(false);
				j.setMsg("姓名不能为空");
				return j;
			}
			
			if(sysUserEntity.getRealName().length() > 45) {
				j.setSuccess(false);
				j.setMsg("姓名输入长度超过45个字");
				return j;
			}
			
			if(StringUtils.isEmpty(sysUserEntity.getSex())) {
				j.setSuccess(false);
				j.setMsg("请选择性别");
				return j;
			}
			
			if(sysUserEntity.getTelNo().length() > 20) {
				j.setSuccess(false);
				j.setMsg("请输入正确的手机号");
				return j;
			}
			
			if(sysUserEntity.getEmail().length() > 250) {
				j.setSuccess(false);
				j.setMsg("请输入正确的邮箱");
				return j;
			}
			
			//编辑保存
			SysUserEntity tmp = sysUserService.getById(sysUserEntity.getId());
			MyBeanUtils.copyBeanNotNull2Bean(sysUserEntity, tmp);
			
			sysUserService.saveOrUpdate(tmp);
		}catch(Exception e) {
			j.setSuccess(false);
			j.setMsg("保存失败");
			log.error("保存基本资料报错，错误信息：{}", e.getMessage());
			e.printStackTrace();
		}
		
		return j;
	}
	
	/**
	 * 
	 * @description
	 *  	获取用户姓名
	 * @params
	 * @return void
	 * @author guyp
	 * @time 2020年1月7日 上午9:25:51
	 */
	@RequestMapping(value="/getname")
	@ApiOperation(value="获取用户姓名", notes="获取用户姓名", response=BaseWrapper.class)
	public void getName(HttpServletRequest request, HttpServletResponse response) {
		//获取登录用户id
		String name = ShiroUtils.getSessionUser().getRealName();
		//防止中文乱码
		response.setContentType("text/xml;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//结果处理,直接写到客户端
		ResponseUtil.write(response, new BaseWrapper<String>(name).wrap());
	}
	
}
