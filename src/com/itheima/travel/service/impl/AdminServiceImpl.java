package com.itheima.travel.service.impl;

import com.itheima.travel.dao.AdminDao;
import com.itheima.travel.dao.impl.AdminDaoImpl;
import com.itheima.travel.domain.Category;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.Seller;
import com.itheima.travel.domain.User;
import com.itheima.travel.service.AdminService;
import com.itheima.travel.utils.C3P0Utils;
import com.itheima.travel.utils.Constant;
import com.itheima.travel.utils.JedisPoolUtils;
import com.itheima.travel.utils.PageBean;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.List;

public class AdminServiceImpl implements AdminService{

	public PageBean<Route> findAllRoute(int curPage, int pageSize) {
		AdminDao dao = new AdminDaoImpl();
		try {
			C3P0Utils.startTransaction();
			int count=dao.queryCount();
			PageBean<Route> pageBean2 = new PageBean<Route>();
			pageBean2.setCount(count);
			pageBean2.setCurPage(curPage);
			pageBean2.setPageSize(pageSize);
			PageBean<Route> pageBean=dao.findAllRoute(pageBean2);
			C3P0Utils.commitAndClose();
			return pageBean;
		} catch (SQLException e) {
			e.printStackTrace();
			C3P0Utils.rollbackAndClose();
			throw new RuntimeException();
		}
	}

	public List<Category> findAllCategory() {
		AdminDao dao = new AdminDaoImpl();
		List<Category> categorys=dao.findAllCategory();
		return categorys;
	}

	public int storeCategory(String cname) {
		AdminDao dao = new AdminDaoImpl();
		int update=dao.storeCategory(cname);
		Jedis jedis = JedisPoolUtils.getJedis();
		jedis.del(Constant.CATELISTJSON);
		jedis.close();
		return update;
	}

	public int updateCategory(String cid, String cname) {
		AdminDao dao = new AdminDaoImpl();
		int update=dao.updateCategory(cid,cname);
		Jedis jedis = JedisPoolUtils.getJedis();
		jedis.del(Constant.CATELISTJSON);
		jedis.close();
		return update;
	}

	public int deleteByCid(String cid) {
		AdminDao dao = new AdminDaoImpl();
		int update=dao.deleteByCid(cid);

		Jedis jedis = JedisPoolUtils.getJedis();
		jedis.del(Constant.CATELISTJSON);
		jedis.close();
		return update;
	}

	public Seller login(String sname, String consphone) {
		AdminDao dao = new AdminDaoImpl();
		Seller seller=dao.querySeller(sname,consphone);
		return seller;
	}

	public List<User> findUser() {
		AdminDao dao = new AdminDaoImpl();
		List<User> users=dao.findUser();
		return users;
	}

	public int forbideUser(String uid) {
		AdminDao dao = new AdminDaoImpl();
		int user=dao.forbideUser(uid);
		return user;
	}


}
