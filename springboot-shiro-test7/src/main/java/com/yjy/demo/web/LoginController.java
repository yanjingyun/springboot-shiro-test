package com.yjy.demo.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yjy.core.authentication.JWTUtil;
import com.yjy.core.domain.Response;
import com.yjy.core.utils.MD5Util;
import com.yjy.demo.domain.User;
import com.yjy.demo.service.UserService;

@RestController
public class LoginController {

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public Response login(String username, String password) throws Exception {
		username = StringUtils.lowerCase(username);
		password = MD5Util.encrypt(username, password);

		User user = userService.getUser(username);

		final String errorMessage = "用户名或密码错误";
		if (user == null)
			throw new RuntimeException(errorMessage);
		if (!StringUtils.equals(user.getPassword(), password))
			throw new RuntimeException(errorMessage);

		// 生成 Token
		String token = JWTUtil.sign(username, password);
		Map<String, Object> userInfo = this.generateUserInfo(token, user);
		return new Response().message("认证成功").data(userInfo);
	}

	/**
	 * 生成前端需要的用户信息，包括：token和user信息
	 */
	private Map<String, Object> generateUserInfo(String token, User user) {
		Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("token", token);
		user.setPassword("it's a secret");
		userInfo.put("user", user);
		return userInfo;
	}
}
