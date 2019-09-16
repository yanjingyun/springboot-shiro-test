package com.yjy.user.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.yjy.user.domain.User;

/**
 * 模拟用户、角色、权限数据
 */
@Service
public class UserService {

	private static Map<String, User> userMap = new HashMap<>();
	private static final Map<String, String> roleMap = new HashMap<>();
	private static final Map<String, Set<String>> permissionMap = new HashMap<>();
	
	static {
		userMap.put("admin", new User("admin", "0690bb4d9a1cbeff8ab162d9ade5fd06")); //admin&admin
		userMap.put("user1", new User("user1", "cd64f554c2d5ea0e18374e1a8107d120")); //user1&user1
		userMap.put("user2", new User("user2", "e90109dadc646f48e658275a208e24e0")); //user2&user2
	
		roleMap.put("admin", "admin");
		roleMap.put("user1", "user1");
		roleMap.put("user2", "user2");
		
		Set<String> user1Set = new HashSet<>();
		user1Set.add("permission11");
		user1Set.add("permission12");
		permissionMap.put("user1", user1Set);
		
		Set<String> user2Set = new HashSet<>();
		user2Set.add("permission21");
		user2Set.add("permission22");
		permissionMap.put("user2", user2Set);
	}

	public User findByUsername(String username) {
		return userMap.get(username);
	}
	
	public String getRoleByUsername(String username) {
		return roleMap.get(username);
	}

	public Set<String> getPermissionsByRole(String roleName) {
		return permissionMap.get(roleName);
	}
}
