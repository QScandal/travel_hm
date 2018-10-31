package com.itheima.dao;

import java.sql.SQLException;

import com.itheima.domain.User;


public interface LoginDao {

	User login(String username, String password) throws SQLException;

}
