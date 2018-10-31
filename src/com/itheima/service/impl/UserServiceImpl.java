package com.itheima.service.impl;

import java.sql.SQLException;

import com.itheima.dao.UserDao;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.utils.BeanFactory;
import com.itheima.utils.C3P0Utils;
import com.itheima.utils.Constant;
import com.itheima.utils.MailUtils;
import com.itheima.utils.UUIDUtils;

public class UserServiceImpl implements UserService {
	private UserDao dao = (UserDao) BeanFactory.getBean("UserDao");

	// 查询用户名是否重复
	@Override
	public User findUserByUser(User user) throws SQLException {

		User existUser = dao.findUserByUser(user);
		System.out.println(existUser);
		return existUser;
	}

	// 注册功能
	@Override
	public void register(User user) throws Exception {
		try {
			// 开启事务 注册事件和发送激活码同时成功或失败
			C3P0Utils.startTransaction();
			user.setStatus(Constant.NOT_ACTIVE);
			String code = UUIDUtils.getCode();
			user.setCode(code);
			dao.register(user);
			// 点击注册按钮后，向邮箱发送一条激活码
			String emailMsg = "<a href='http://localhost:8080/traveltest/user?action=active&code=" + code
					+ "'>点我激活账号</a>";
			// 发送激活邮件
			System.out.println(emailMsg);
			MailUtils.sendMail(user.getEmail(), "注册账号激活", emailMsg);
			C3P0Utils.commitAndClose();
		} catch (Exception e) {
			e.printStackTrace();
			// 如果失败，事务回滚
			C3P0Utils.commitAndClose();
		}
	}

	// 用户激活
	@Override
	public User active(String code) throws SQLException {
		// 先查找数据库中的用户是否激活
		User user = dao.findUserByCode(code);
		if (user != null) {
			dao.active(user);
		}
		return user;
	}
}
