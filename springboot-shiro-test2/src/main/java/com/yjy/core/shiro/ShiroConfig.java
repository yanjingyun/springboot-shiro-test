package com.yjy.core.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

	/**
	 * ShiroFilterFactoryBean，为了生成ShiroFilter。
	 * 它管理三个属性：securityManager，filters，setFilterChainDefinitionMap
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setLoginUrl("/login");
		shiroFilterFactoryBean.setSuccessUrl("/");
		shiroFilterFactoryBean.setUnauthorizedUrl("/403");
		shiroFilterFactoryBean.setSecurityManager(securityManager());

		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		filterChainDefinitionMap.put("/login", "anon");
		filterChainDefinitionMap.put("/logout", "logout");
		filterChainDefinitionMap.put("/assets/**", "anon"); //静态资源
		filterChainDefinitionMap.put("/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilterFactoryBean;
	}

	/**
	 * 安全管理器SecurityManager
	 * 注入realm、缓存管理器CacheManager（可在realm注入）、会话管理器SessionManager、记住我RememberMeManager
	 */
	@Bean
	public DefaultWebSecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(shiroRealm());
		return securityManager;
	}

	/**
	 * 自定义realm
	 * 可以注入密码匹配器CredentialsMatcher
	 */
	@Bean
	public ShiroRealm shiroRealm() {
		ShiroRealm shiroRealm = new ShiroRealm();
		shiroRealm.setCredentialsMatcher(credentialsMatcher());
		return shiroRealm;
	}
	
	@Bean
	public HashedCredentialsMatcher credentialsMatcher() {
//		System.out.println(new Md5Hash("admin", "admin", 2013).toString()); //生成密码
//		System.out.println(new SimpleHash("md5", "user1", "user1", 2013).toString()); //生成密码
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
		matcher.setHashAlgorithmName("md5"); //hash算法
		matcher.setHashIterations(2013); //循环次数
		return matcher;
	}
}