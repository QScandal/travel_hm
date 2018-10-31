package com.itheima.service;

import java.sql.SQLException;

import com.itheima.domain.User;

public interface UserService {

	User findUserByUser(User user) throws SQLException;

	void register(User user) throws Exception;

	User active(String code) throws SQLException;

}
