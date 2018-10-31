package com.itheima.travel.dao.impl;

import com.itheima.travel.dao.AdminDao;
import com.itheima.travel.domain.Category;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.Seller;
import com.itheima.travel.domain.User;
import com.itheima.travel.utils.C3P0Utils;
import com.itheima.travel.utils.PageBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminDaoImpl implements AdminDao{

	public PageBean<Route> findAllRoute(PageBean<Route> pageBean) {
		QueryRunner queryRunner = new QueryRunner();
		String sql = "select * from tab_route a,tab_category b,tab_seller c where a.cid=b.cid and a.sid=c.sid limit ?,?  ";
		ArrayList<Route> arrayList = new ArrayList<Route>();
		try {Object[] params={(pageBean.getCurPage()-1)*pageBean.getPageSize(),pageBean.getPageSize()};
			List<Map<String, Object>> query = queryRunner.query(C3P0Utils.getConnection(),sql,new MapListHandler(),params);
			for (Map<String, Object> map : query) {
				Category category = new Category(); 
				Seller seller = new Seller();
				Route route = new Route();
				BeanUtils.populate(category, map);
				BeanUtils.populate(seller, map);
				BeanUtils.populate(route, map);
				route.setSeller(seller);
				route.setCategory(category);
				arrayList.add(route);
			}
			pageBean.setData(arrayList);
			return pageBean;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public int queryCount() {
		QueryRunner queryRunner = new QueryRunner();
		String sql = "select count(*) from tab_route ";

		try {
			Long count = (Long)queryRunner.query(C3P0Utils.getConnection(),sql, new ScalarHandler());
			return count.intValue();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<Category> findAllCategory() {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_category order by cid asc ";

		try {
			List<Category> categorys = queryRunner.query(sql, new BeanListHandler<Category>(Category.class));
			return categorys;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public int storeCategory(String cname) {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "insert into tab_category values(null,?) ";
		try {
			int update = queryRunner.update(sql,cname);
			return update;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public int updateCategory(String cid, String cname) {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "update tab_category set cname=? where cid=? ";

		try {
			int update = queryRunner.update(sql,cname,cid);
			return update;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public int deleteByCid(String cid) {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "delete from tab_category where cid=? ";
		try {
			int update = queryRunner.update(sql,cid);
			return update;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public Seller querySeller(String sname, String consphone) {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_seller where sname=? and consphone=? ";
		try {
			Seller seller = queryRunner.query(sql, new BeanHandler<Seller>(Seller.class),sname,consphone);
			return seller;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	public List<User> findUser() {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_user ";

		try {
			List<User> users = queryRunner.query(sql, new BeanListHandler<User>(User.class));
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public int forbideUser(String uid) {
		QueryRunner queryRunner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "update tab_user set username=null,password=null,email=null where uid=? ";
		try {
			int update = queryRunner.update(sql,uid);
			return update;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}




}
