package com.yjy.user.web;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
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
}
