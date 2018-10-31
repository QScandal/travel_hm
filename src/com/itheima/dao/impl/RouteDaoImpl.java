package com.itheima.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.itheima.dao.RouteDao;
import com.itheima.domain.Category;
import com.itheima.domain.Route;
import com.itheima.domain.RouteImg;
import com.itheima.domain.Seller;
import com.itheima.utils.C3P0Utils;
import com.itheima.utils.Constant;

public class RouteDaoImpl implements RouteDao {

	/**
	 * 查询当前页的商品信息
	 * @throws SQLException 
	 */
	public List<Route> findPageProByCid(int startIndex, int pageSize,
			int cid) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_route where cid = ? and rflag=? limit ?,? ";
		//System.out.println(cid);
		//System.out.println(Constant.PRO_WEIXIAJAI);
		//System.out.println(pageSize);
		Object params[] = {
				cid,Constant.ROUTE_SHANGJIA,startIndex,pageSize
		};
		
		//System.out.println(query);
		List<Route> list = runner.query(sql, new BeanListHandler<Route>(Route.class),params);
		return list;
	}

	/**
	 * 查询总条数
	 * @throws SQLException 
	 */
	public int findToatlCount(int cid) throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from tab_route where cid =? and rflag = ? ";
		Long l = (Long)runner.query(sql, new ScalarHandler(),cid,Constant.ROUTE_SHANGJIA);
		return l.intValue();
	}

	/**
	 * 查询主题旅游信息
	 * @throws SQLException 
	 */
	public List<Route> findfindRouteByIsThemeTour() throws SQLException {
			QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
			String sql = "select * from tab_route where rflag = ? and isThemeTour= ? limit 4 ";
			Object[] pamas= {
					Constant.ROUTE_SHANGJIA,Constant.ISTHEMETOUR
			};
			List<Route> list = runner.query(sql , new BeanListHandler<Route>(Route.class),pamas);
		return list;
	}

	/**
	 * 查询最新旅游信息
	 * @throws SQLException 
	 */
	public List<Route> findRouteByNews() throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_route where rflag = ? order by rdate desc limit 4 ";
		List<Route> list = runner.query(sql , new BeanListHandler<Route>(Route.class),Constant.ROUTE_SHANGJIA);
		return list;
	}

	/**
	 * 查询人气旅游信息
	 * @throws SQLException 
	 */
	public List<Route> findRouteByCount() throws SQLException {
		QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_route where rflag = ? and count >0 limit 4 ";
		List<Route> list = runner.query(sql , new BeanListHandler<Route>(Route.class),Constant.ROUTE_SHANGJIA);
		return list;
	}
	
	/**
	 * 国内游
	 */
	public List<Route> findChaTra() throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_route where rflag = 1 and cid = 5 limit 6 ";
				//"select * from product where pflag=? and is_hot=? limit 9 ";
		return qr.query(sql, new BeanListHandler<Route>(Route.class));
	}
	
	/**
	 * 境外游
	 */
	public List<Route> findTwoTra() throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_route where rflag = 1 and cid = 4 limit 6 ";
		//"select * from product where pflag=? and is_hot=? limit 9 ";
		return qr.query(sql, new BeanListHandler<Route>(Route.class));
	}
	
	@Override
	/**
	 * 根据rid查找路线详细信息
	 */
	public Route findRouteByRid(String rid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql="select *from tab_route where rid =? ";
		Route route = qr.query(sql, new BeanHandler<Route>(Route.class), rid);
		sql="select *from tab_route route,tab_category category,tab_seller seller,tab_route_img routeimg where route.rid=? and route.cid=category.cid and route.sid=seller.sid and routeimg.rid=route.rid";
		List<Map<String, Object>> maplist = qr.query(sql, new MapListHandler(), rid);
		for (Map<String, Object> m : maplist) {
			//封装category seller routeimg三个实体类
			//每一个(routeimg category seller route)
			Category category=new Category();
			BeanUtils.populate(category, m);
			Seller seller=new Seller();
			BeanUtils.populate(seller, m);
			RouteImg routeimg=new RouteImg();
			BeanUtils.populate(routeimg, m);
			route.setCategory(category);
			route.setSeller(seller);
			route.getRouteImgList().add(routeimg);
		}
		return route;
	}
	
}
