package com.itheima.dao;

import java.sql.SQLException;

import com.itheima.domain.User;

public interface UserDao {

	User findUserByUser(User user) throws SQLException;

	void register(User user) throws SQLException;

	User findUserByCode(String code) throws SQLException;

	void active(User user) throws SQLException;

}
