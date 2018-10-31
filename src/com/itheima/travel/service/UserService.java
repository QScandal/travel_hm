package com.itheima.travel.service;

import com.itheima.travel.domain.User;

import java.sql.SQLException;

public interface UserService {
    //用户注册
	boolean register(User user) throws Exception;
    //用户校验
	String inspectUser(String username) throws SQLException;
    //用户激活
	User active(String code) throws  Exception;
    //邮箱校验
	String inspectEmail(String email) throws SQLException;

	//登录
    User login(String username, String password) throws SQLException;

}
