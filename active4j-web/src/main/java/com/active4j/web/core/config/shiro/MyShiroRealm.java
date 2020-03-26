package com.active4j.web.core.config.shiro;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.active4j.common.redis.RedisApi;
import com.active4j.entity.base.model.ActiveUser;
import com.active4j.entity.common.GlobalTimeConstant;
import com.active4j.entity.common.redis.GlobalRedisKey;
import com.active4j.entity.system.entity.SysUserEntity;
import com.active4j.service.system.service.SysUserService;
import com.active4j.web.core.jwt.JwtToken;
import com.active4j.web.core.jwt.JwtUtil;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MyShiroRealm extends AuthorizingRealm {
	
	@Autowired
	private RedisApi redisApi;
	
	@Autowired
	private SysUserService userService;
	
	/**
	 * 必须重写此方法，不然Shiro会报错
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ActiveUser user = (ActiveUser) principals.getPrimaryPrincipal();
		/**
		 * 基于角色的权限
		 * 	先从redis中获取角色set集合，获取不到再从DB中取
		 */
		List<String> lstPersRoles = new ArrayList<String>();
		Set<Object> stRoles = redisApi.sGet(GlobalRedisKey.Redis_User_Role_Key + user.getUserName());
		if(null != stRoles && stRoles.size() > 0) {
			//类型转换一下
			for(Object obj : stRoles) {
				lstPersRoles.add((String)obj);
			}
		}else {
			//redis缓存中没有, 需要从DB中获取
			//查询用户角色
			try {
				lstPersRoles = userService.findRoleNameByUserId(user.getId());
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		
			if(lstPersRoles.size() > 0) {
				//存入redis以便下次查询
				redisApi.sSetAndTime(GlobalRedisKey.Redis_User_Role_Key + user.getUserName(), GlobalTimeConstant.Shiro_Role_Redis_Time, lstPersRoles.toArray());
			}
		}
		
		/**
		 * 基于资源的权限
		 * 	先从redis中获取角色set集合，获取不到再从DB中取
		 */
		List<String> lstPersMenus = new ArrayList<String>();
		Set<Object> stMenus = redisApi.sGet(GlobalRedisKey.Redis_User_Menu_Key + user.getUserName());
		if(null != stMenus && stMenus.size() > 0) {
			//类型转换一下
			for(Object obj : stMenus) {
				lstPersMenus.add((String)obj);
			}
		}else {
			//redis缓存中没有，需要从DB中获取
			lstPersMenus = userService.findMenuUrlByUserId(user.getId());
			
			if(lstPersMenus.size() > 0) {
				//存入redis以便下次查询
				redisApi.sSetAndTime(GlobalRedisKey.Redis_User_Menu_Key + user.getUserName(), GlobalTimeConstant.Shiro_Role_Redis_Time, lstPersMenus.toArray());
			}
			
		}

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(lstPersMenus);
		info.addRoles(lstPersRoles);
		return info;
	}

	/**
	 * 认证信息 登录
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		// 获取token
		String token = (String) authcToken.getCredentials();
		
		// 从jwt中获取username
		String userName = JwtUtil.getUsername(token);
		
		// 查询用户
		SysUserEntity sysUser = userService.getUserByUseName(userName);
		if (null == sysUser) {
			throw new UnknownAccountException("账号或密码不正确");
		}

		if(!jwtTokenRefresh(token, userName, sysUser.getPassword())) {
			throw new UnknownAccountException("账号或密码不正确");
		}
		
		if (StringUtils.equals(sysUser.getStatus(), "0")) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}

		ActiveUser user = userService.getActiveUserByUser(sysUser);

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, token, getName());

		return info;

	}
	
	/**
	 * JWTToken刷新生命周期 （解决用户一直在线操作，提供Token失效问题）
	 * 1、登录成功后将用户的JWT生成的Token作为k、v存储到cache缓存里面(这时候k、v值一样)
	 * 2、当该用户再次请求时，通过JWTFilter层层校验之后会进入到doGetAuthenticationInfo进行身份验证
	 * 3、当该用户这次请求JWTToken值还在生命周期内，则会通过重新PUT的方式k、v都为Token值，缓存中的token值生命周期时间重新计算(这时候k、v值一样)
	 * 4、当该用户这次请求jwt生成的token值已经超时，但该token对应cache中的k还是存在，则表示该用户一直在操作只是JWT的token失效了，程序会给token对应的k映射的v值重新生成JWTToken并覆盖v值，该缓存生命周期重新计算
	 * 5、当该用户这次请求jwt在生成的token值已经超时，并在cache中不存在对应的k，则表示该用户账户空闲超时，返回用户信息已失效，请重新登录。
	 * 6、每次当返回为true情况下，都会给Response的Header中设置Authorization，该Authorization映射的v为cache对应的v值。
	 * 7、注：当前端接收到Response的Header中的Authorization值会存储起来，作为以后请求token使用
	  *    参考方案：https://blog.csdn.net/qq394829044/article/details/82763936
	 * 
	 * @param userName
	 * @param passWord
	 * @return
	 */
	public boolean jwtTokenRefresh(String token, String userName, String passWord) {
		try {
			String cacheToken = (String) redisApi.get(JwtUtil.PREFIX_USER_TOKEN + token);
			if (StringUtils.isNotEmpty(cacheToken)) {
				//校验token有效性
				if (!JwtUtil.verify(token, userName, passWord)) {
					String newAuthorization = JwtUtil.sign(userName, passWord);
					redisApi.set(JwtUtil.PREFIX_USER_TOKEN + token, newAuthorization, (int)JwtUtil.EXPIRE_TIME/1000);
				} else {
					redisApi.set(JwtUtil.PREFIX_USER_TOKEN + token, cacheToken, (int)JwtUtil.EXPIRE_TIME/1000);
				}
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			log.error("更新token遇到异常，异常信息:" + e.getMessage());
			e.printStackTrace();
			return false;
		}
		
	}


}
