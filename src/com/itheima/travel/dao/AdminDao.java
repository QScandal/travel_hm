package com.itheima.travel.dao;

import com.itheima.travel.domain.Category;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.Seller;
import com.itheima.travel.domain.User;
import com.itheima.travel.utils.PageBean;

import java.util.List;

public interface AdminDao {


	int queryCount();

	PageBean<Route> findAllRoute(PageBean<Route> pageBean2);

	List<Category> findAllCategory();

	int storeCategory(String cname);

	int updateCategory(String cid, String cname);

	int deleteByCid(String cid);

	Seller querySeller(String sname, String consphone);

	List<User> findUser();

	int forbideUser(String uid);
}
