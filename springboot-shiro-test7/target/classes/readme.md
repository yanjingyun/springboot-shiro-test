springboot整合shiro&jwt：

模拟两个用户（参考UserService类）：
	1.用户名 admin，密码 123456，角色 admin（管理员），权限 "user:add"，"user:view"
	2.用户名 user，密码 123456，角色 regist（注册用户），权限 "user:view"

测试用例参考文件：postman测试.json