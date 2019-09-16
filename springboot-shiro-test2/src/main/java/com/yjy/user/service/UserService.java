package com.yjy.user.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yjy.user.domain.User;

@Service
public class UserService {

	private static Map<String, User> userMap = new HashMap<>();
	
	static {
		userMap.put("admin", new User("admin", "0690bb4d9a1cbeff8ab162d9ade5fd06")); //admin&admin
		userMap.put("user1", new User("user1", "cd64f554c2d5ea0e18374e1a8107d120")); //user1&user1
		userMap.put("user2", new User("user2", "e90109dadc646f48e658275a208e24e0")); //user2&user2
	}

	public User findByUsername(String username) {
		return userMap.get(username);
	}
}
