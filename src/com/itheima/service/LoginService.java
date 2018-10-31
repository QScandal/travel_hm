package com.itheima.service;

import java.sql.SQLException;

import com.itheima.domain.User;


public interface LoginService {

	User login(String username, String password) throws SQLException;

}
