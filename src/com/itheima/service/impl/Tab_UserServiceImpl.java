package com.itheima.service.impl;

import java.sql.SQLException;

import com.itheima.dao.LoginDao;
import com.itheima.domain.User;
import com.itheima.service.LoginService;
import com.itheima.utils.BeanFactory;


public class Tab_UserServiceImpl implements LoginService {

	@Override
	public User login(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		LoginDao dao = (LoginDao) BeanFactory.getBean("Tab_UserDao");
		User user=dao.login(username,password);
		return user;
	}

}
