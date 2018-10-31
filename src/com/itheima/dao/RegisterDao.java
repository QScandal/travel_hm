package com.itheima.dao;

import java.sql.SQLException;

import com.itheima.domain.User;

public interface RegisterDao {

	void register(User user) throws SQLException;

	User findUser(User user) throws SQLException;

}
