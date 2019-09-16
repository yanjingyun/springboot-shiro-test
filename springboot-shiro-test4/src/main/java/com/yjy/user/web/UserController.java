package com.yjy.user.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@RequestMapping("/")
	public String redirectIndex() {
		return "redirect:/main";
	}
	
	@RequestMapping("/login")
	public String login(String username, String password) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		try {
			subject.login(token);
			return "认证通过";
		} catch (AuthenticationException e) {
			return e.getMessage();
		}
	}
	
	@RequestMapping("/main")
	public String main() {
		return "main()...";
	}
	
	// 需要user1角色
	@RequiresRoles("user1")
	@RequestMapping("/user1")
	public String user1() {
		return "user1()...";
	}

	// 需要user2角色
	@RequiresRoles("user2")
	@RequestMapping("/user2")
	public String user2() {
		return "user2()...";
	}
	
	// 需要permission11权限
	@RequiresPermissions("permission11")
	@RequestMapping("/permission11")
	public String permission11() {
		return "permission11()...";
	}
	
	// 需要permission11或permission12权限
	@RequiresPermissions(value = {"permission11", "permission12"}, logical = Logical.OR)
	@RequestMapping("/permission11Or")
	public String permission11Or() {
		return "permission11Or()...";
	}

	// 需要permission11权限
	@RequiresPermissions("permission21")
	@RequestMapping("/permission21")
	public String permission21() {
		return "permission21()...";
	}
}
