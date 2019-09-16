package com.yjy.user.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yjy.user.domain.User;

@Service
public class UserService {

	private static Map<String, User> userMap = new HashMap<>();
	
	static {
		userMap.put("admin", new User("admin", "admin"));
		userMap.put("user1", new User("user1", "user1"));
		userMap.put("user2", new User("user2", "user2"));
	}

	public User findByUsername(String username) {
		return userMap.get(username);
	}
}
