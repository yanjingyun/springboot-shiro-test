package com.yjy.core.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

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
		filterChainDefinitionMap.put("/**", "user");
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
		securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	/**
	 * 自定义realm
	 * 可以注入密码匹配器CredentialsMatcher、缓存管理器CacheManager
	 */
	@Bean
	public ShiroRealm shiroRealm() {
		ShiroRealm shiroRealm = new ShiroRealm();
		shiroRealm.setCredentialsMatcher(credentialsMatcher());
		shiroRealm.setCacheManager(ehCacheManager());
		return shiroRealm;
	}
	
	/**
	 * 密码匹配器CredentialsMatcher
	 */
	@Bean
	public HashedCredentialsMatcher credentialsMatcher() {
//		System.out.println(new Md5Hash("admin", "admin", 2013).toString()); //生成密码
//		System.out.println(new SimpleHash("md5", "user1", "user1", 2013).toString()); //生成密码
//		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
		MyHashedCredentialsMatcher matcher = new MyHashedCredentialsMatcher(ehCacheManager());
		matcher.setHashAlgorithmName("md5"); //hash算法
		matcher.setHashIterations(2013); //循环次数
		return matcher;
	}
	
	/**
	 * 缓存管理器CacheManager
	 */
	@Bean
	public EhCacheManager ehCacheManager() {
		EhCacheManager ehCacheManager = new EhCacheManager();
		ehCacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
		return ehCacheManager;
	}
	
	/**
	 * 开启AOP注解
	 * 开启后能使用@RequiresRoles、@RequiresPermissions等注解
	 * 配置DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor两个bean即可
	 */
	@Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }
	
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}
	
	/**
	 * ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
	 */
	@Bean(name = "shiroDialect")
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}
	
	/**
	 * cookie对象
	 */
	@Bean
	public SimpleCookie rememberMeMeCookie() {
		// 设置cookie名称，对应login.html页面<input type="checkbox" name="rememberMe"/>
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		cookie.setMaxAge(60*60*3); //设为3小时，单位为秒
		return cookie;
	}
	
	/**
	 * cookie管理器
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeMeCookie());
		cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
		return cookieRememberMeManager;
	}
}