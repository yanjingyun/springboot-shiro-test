package com.yjy.core.authentication;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.yjy.demo.domain.User;
import com.yjy.demo.service.UserService;

/**
 * 自定义 Realm，包含认证和授权两大模块
 */
public class ShiroRealm extends AuthorizingRealm {
	
	@Autowired
	private UserService userService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JWTToken;
	}

	/**
	 * 授权，获取用户角色和权限
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {
		String username = JWTUtil.getUsername(token.toString());
		User user = userService.getUser(username);

		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		// 获取用户角色集（模拟值，实际从数据库获取）
		simpleAuthorizationInfo.setRoles(user.getRole());
		// 获取用户权限集（模拟值，实际从数据库获取）
		simpleAuthorizationInfo.setStringPermissions(user.getPermission());

		return simpleAuthorizationInfo;
	}

	/**
	 * 认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		// 这里的 token是从 JWTFilter 的 executeLogin 方法传递过来的，已经经过了解密
		String token = (String) authenticationToken.getCredentials();

		String username = JWTUtil.getUsername(token);

		if (StringUtils.isBlank(username))
			throw new AuthenticationException("token校验不通过");

		// 通过用户名查询用户信息
		User user = userService.getUser(username);
		if (user == null)
			throw new AuthenticationException("用户名或密码错误");
		if (!JWTUtil.verify(token, username, user.getPassword()))
			throw new AuthenticationException("token校验不通过");

		return new SimpleAuthenticationInfo(token, token, getName());
	}
}
