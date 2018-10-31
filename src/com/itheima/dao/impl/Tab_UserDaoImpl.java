package com.itheima.dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.itheima.dao.LoginDao;
import com.itheima.domain.User;
import com.itheima.utils.C3P0Utils;

public class Tab_UserDaoImpl implements LoginDao {

	@Override
	public User login(String username, String password) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql="select * from tab_user where username=? and password=?";
		User user = runner.query(sql, new BeanHandler<User>(User.class), username,password);
		return user;
	}

}
