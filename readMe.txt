springboot-shiro-test1: 测试认证
	shiro三个配置类：ShiroFilterFactoryBean、SecurityManager、自定义Realm
	测试(暂无前端，使用postMan测试)：
		http://localhost:8080/main会跳转到login
		http://localhost:8080/login?username=admin&password=admin1 进入登录
		http://localhost:8080/main 进入main方法

springboot-shiro-test2： 测试认证时密码加密
	所有实现了CredentialsMatcher接口的类都是密码匹配器。
	这里使用HashedCredentialsMatcher类，在shiroRealm中注入即可。
	测试：http://localhost:8080/login?username=admin&password=admin1


springboot-shiro-test3： 测试认证时密码错误次数限制，并使用EhCache缓存
	重写密码匹配器类MyHashedCredentialsMatcher，继承HashedCredentialsMatcher类。
	输入超过3次密码错误回将该账号暂时锁住10分钟（ehcache缓存实现）。
	测试：http://localhost:8080/login?username=admin&password=admin1


springboot-shiro-test4： 测试授权，并使用aop注解，并使用缓存将授权信息缓存起来
	实现授权：重写自定义ealm的doGetAuthorizationInfo()方法
	使用aop注解：配置DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor两个bean即可
	测试：
		--使用admin登录
			除了http://localhost:8080/user1和http://localhost:8080/user2这两个外，所有链接都能访问
		--使用user1登录
		http://localhost:8080/login?username=user1&password=user1 --使用user1登录
		http://localhost:8080/user1	--允许通过，拥有user1角色
		http://localhost:8080/permission11 	--允许通过，拥有permission11权限
		http://localhost:8080/permission11Or	--允许通过，拥有permission11或permission12权限
		http://localhost:8080/permission21	--报错，不允许通过，不拥有permission21权限
	缓存描述：
		可将缓存注入Realm或SecurityManager。
		未使用缓存情况：每有一个@RequiresRoles、@RequiresPermissions注解，都会调用一次自定义Realm类的doGetAuthorizationInfo()方法。
		具体缓存实现可以是：EhCache（引入shiro-ehcache包）、redis（引入shiro-redis包-未测试）


springboot-shiro-test5： 测试shiro整合thymeleaf，并在html页面使用shiro标签
	--shiro注解（参见test5项目）
		<a href="/user1">user1页面</a><br>
		<a href="/permission11">permission11页面</a><br>
		<a href="/permission21">permission21页面</a><br>
	--shiro标签
		--更多标签参考XXX
		<div shiro:hasRole="admin">admin角色</div>
		<div shiro:hasPermission="permission11">permission11权限</div>

springboot-shiro-test6： 测试整合记住我
	-1）页面添加<input name="rememberMe" type="checkbox" value="1">
	-2）LoginController修改：UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
	-3）新增配置bean
		新增cookie对象、cookie管理器的bean对象，并调整filterChainDefinitionMap配置
	使用：
		第一次登录后，关掉浏览器，再次访问该系统主页面("/main")能直接访问到


https://mrbird.cc/Spring-Boot-Shiro%20session.html

springboot-shiro-test7： 测试整合jwt


shiro集成jwt：测试token超时刷新(未开发)：	




