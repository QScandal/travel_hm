package com.itheima.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.itheima.dao.FindDao;
import com.itheima.domain.Category;
import com.itheima.domain.PageBean;
import com.itheima.domain.Route;
import com.itheima.domain.RouteImg;
import com.itheima.domain.Seller;
import com.itheima.utils.C3P0Utils;

public class FindDaoImpl implements FindDao {
	/**
	 * 查找封装分页搜索
	 */
	@Override
	public List<Route> findRouteListByCid(int cid,String rname, int startIndex, int pageSize) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_route where cid = ? and rname like ? limit ?,? ";
		List<Route> routes = qr.query(sql , new BeanListHandler<Route>(Route.class),cid,"%"+rname+"%",startIndex,pageSize);
		for (Route route : routes) {
			sql = "select * from tab_category where cid = ? ";
			Category cate = qr.query(sql, new BeanHandler<Category>(Category.class),route.getCid());
			sql = "select * from tab_route_img where rid = ? ";
			List<RouteImg> routeImgList = qr.query(sql, new BeanListHandler<RouteImg>(RouteImg.class),route.getRid());
			sql = "select * from tab_seller where sid = ? ";
			Seller seller = qr.query(sql, new BeanHandler<Seller>(Seller.class),route.getSid());
			route.setCategory(cate);
			route.setRouteImgList(routeImgList);
			route.setSeller(seller);
		}
		return routes;
		
	}
	/**
	 * 查找搜索总条数
	 */
	@Override
	public int findCountTotal(String rname) throws SQLException {
		// TODO Auto-generated method stub
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from tab_route where rname like ? ";
		System.out.println("dao层rname"+rname);
		int countTotal = ((Long)qr.query(sql ,new ScalarHandler(),"%"+rname+"%")).intValue();
		System.out.println("dao层countTotal"+countTotal);
		return countTotal;
	}

	



}
