package com.itheima.dao.impl;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;
import com.itheima.utils.C3P0Utils;
import com.itheima.utils.Constant;


public class UserDaoImpl implements UserDao {
	// 查询数据库用户是否已注册
	@Override
	public User findUserByUser(User user) throws SQLException {
		QueryRunner qr = new QueryRunner();
		String sql = "select * from tab_user where username=?";
		User existUser = qr.query(C3P0Utils.getConnection(), sql, new BeanHandler<>(User.class), user.getUsername());
		System.out.println(existUser);
		return existUser;
	}

	// 用户注册
	@Override
	public void register(User user) throws SQLException {
		QueryRunner qr = new QueryRunner();
		String sql = "insert into tab_user values(?,?,?,?,?,?,?,?,?,?) ";
		qr.update(C3P0Utils.getConnection(), sql, user.getUid(), user.getUsername(), user.getPassword(), user.getName(),
				user.getBirthday(), user.getSex(), user.getTelephone(), user.getEmail(),Constant.ROUTE_WEISHANGJIA ,
				user.getCode());
	}

	// 根据code查询用户是否激活
	@Override
	public User findUserByCode(String code) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_user where code=? ";
		User user = qr.query(sql, new BeanHandler<>(User.class), code);
		return user;
	}

	// 激活用户
	@Override
	public void active(User user) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "update tab_user set status=?,code=null where uid=? ";
		qr.update(sql, Constant.ACTIVE, user.getUid());

	}
}
