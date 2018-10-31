package com.itheima.travel.dao;

import com.itheima.travel.domain.User;

import java.sql.SQLException;

public interface Userdao {
    //用户注册
	void register(User user) throws SQLException;
    //用户名校验
	User inspectUser(String username) throws SQLException;
	//通过用户激活码来查找用户
	User findUserBycode(String code) throws SQLException;
	//激活用户
	void active(User user) throws SQLException;
	//检验邮箱
	User inspectEmail(String email) throws SQLException;

	//登录
	User findUserByUsernameAndPassword(String username, String password) throws SQLException;
}
