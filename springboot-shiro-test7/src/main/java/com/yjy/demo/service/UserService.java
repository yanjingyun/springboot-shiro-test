package com.yjy.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.yjy.demo.domain.User;

@Service
public class UserService {

	/**
	 * 模拟两个用户
	 * 1.用户名 admin，密码 123456，角色 admin（管理员），权限 "user:add"，"user:view"
	 * 2.用户名 user，密码 123456，角色 regist（注册用户），权限 "user:view"
	 */
	private static List<User> users() {
		List<User> users = new ArrayList<>(); 
		users.add(new User("admin", "bfc62b3f67a4c3e57df84dad8cc48a3b",
				new HashSet<>(Collections.singletonList("admin")),
				new HashSet<>(Arrays.asList("user:add", "user:view"))));
		users.add(new User("user", "60405d5708f95f4b32bad80ef56b4975",
				new HashSet<>(Collections.singletonList("regist")),
				new HashSet<>(Collections.singletonList("user:view"))));
		return users;
	}

	/**
	 * 获取用户
	 */
	public User getUser(String username) {
		List<User> users = users();
		return users.stream().filter(user -> StringUtils.equalsIgnoreCase(username, user.getUsername())).findFirst()
				.orElse(null);
	}
}
