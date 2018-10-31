package com.itheima.dao.impl;

import java.sql.SQLException;
import java.util.UUID;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.itheima.dao.RegisterDao;
import com.itheima.domain.User;
import com.itheima.utils.C3P0Utils;
import com.itheima.utils.UUIDUtils;

public class RegisterDaoImpl implements RegisterDao {

	@Override
	public void register(User user) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql ="insert into tab_user values(?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {null,user.getUsername(),user.getPassword(),user.getName(),
							user.getBirthday(),user.getSex(),user.getTelephone(),
							user.getEmail(),user.getStatus(),UUIDUtils.getId()
				
		};
		qr.update(sql,params);
	}

	@Override
	public User findUser(User user) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_user where username = ? " ;
		User exisUser = qr.query(sql, new BeanHandler<User>(User.class),user.getUsername());
		
		return null;
	}

}
