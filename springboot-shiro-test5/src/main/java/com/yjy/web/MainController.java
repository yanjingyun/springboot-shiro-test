package com.yjy.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping("/")
	public String index() {
		return "redirect:/main";
	}

	@RequestMapping("/main")
	public String main() {
		return "main";
	}
	
	@RequiresRoles("user1")
	@RequestMapping("/user1")
	public String user1() {
		return "user1";
	}
	
	@RequiresPermissions("permission11")
	@RequestMapping("/permission11")
	public String permission11() {
		return "permission11";
	}

	@RequiresPermissions("permission21")
	@RequestMapping("/permission21")
	public String permission21() {
		return "permission21";
	}
}
