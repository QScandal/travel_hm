package com.itheima.service;

import java.sql.SQLException;

import com.itheima.domain.PageBean;
import com.itheima.domain.ResultInfo;
import com.itheima.domain.User;

public interface RegisterService {

	ResultInfo register(User user) throws SQLException;

	User findUser(User user) throws SQLException;

}
