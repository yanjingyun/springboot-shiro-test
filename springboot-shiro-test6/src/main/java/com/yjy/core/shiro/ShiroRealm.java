package com.yjy.core.shiro;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.yjy.user.domain.User;
import com.yjy.user.service.UserService;

public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserService userService;
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User) principals.getPrimaryPrincipal();
		System.out.println(user);
		
		// 设置角色和权限
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		String roleName = userService.getRoleByUsername(user.getUsername());
		// 1.设置角色（这里暂时1个角色）
		info.addRole(roleName);
		// 2.设置权限
		if ("admin".equals(roleName)) {
			info.addStringPermission("*"); //管理员拥有所有权限
		} else {
			Set<String> permissions = userService.getPermissionsByRole(roleName);
			info.addStringPermissions(permissions);
		}
		
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String username = (String) token.getPrincipal();
		User user = userService.findByUsername(username);
		if (user == null) {
			throw new AuthenticationException("用户不存在");
		}
		return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(username), getName());
	}
}
