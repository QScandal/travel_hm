package com.itheima.service.impl;

import java.sql.SQLException;

import com.itheima.dao.RegisterDao;
import com.itheima.dao.impl.RegisterDaoImpl;
import com.itheima.domain.PageBean;
import com.itheima.domain.ResultInfo;
import com.itheima.domain.User;
import com.itheima.service.RegisterService;
import com.itheima.utils.UUIDUtils;

public class RegisterServiceImpl implements RegisterService {

	@Override
	public ResultInfo register(User user) throws SQLException {
		ResultInfo rf = new ResultInfo();
		
		
		RegisterDao dao = new RegisterDaoImpl();
		user.setCode(UUIDUtils.getId());
		user.setStatus("Y");
		dao.register(user);
		rf.setData(user);
		rf.setFlag(true);
		return rf;
	}

	@Override
	public User findUser(User user) throws SQLException {
		RegisterDao dao = new RegisterDaoImpl();
		User exisUser = dao.findUser(user);
		
		return exisUser;
	}

}
