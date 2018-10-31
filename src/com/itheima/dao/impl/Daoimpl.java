package com.itheima.dao.impl;



import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.itheima.dao.Dao;
import com.itheima.domain.Route;
import com.itheima.utils.C3P0Utils;
import com.itheima.utils.Constant;
	// 需要查找rname  价格 
public class Daoimpl implements Dao {
	//查询含rname和价格区间
	@Override
	public List<Route> FindByRnameAndPrice(String rname, String startPrice, String endPrice,int startIndex,int pageSize) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_route where rflag =? and rname like ? having price between  ? and ? order by count desc limit ?,? ";
		List<Route> routes = qr.query(sql , new BeanListHandler<Route>(Route.class),Constant.ROUTE_SHANGJIA,"%"+rname+"%",startPrice,endPrice,startIndex,pageSize);
		return routes;
	}
	//按照rname查询
	@Override
	public List<Route> FindByRaname(String rname,int startIndex,int pageSize) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_route where rflag =? and rname like ? order by count desc limit ?,?";
		List<Route> routes = qr.query(sql , new BeanListHandler<Route>(Route.class),Constant.ROUTE_SHANGJIA,"%"+rname+"%",startIndex,pageSize);
		return routes;
	}
	//根据价格查区间
	@Override
	public List<Route> FindByPrice(String startPrice, String endPrice,int startIndex,int pageSize) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_route where  rflag =? having price between ? and ? order by count desc limit ?,? ";
		List<Route> routes = qr.query(sql , new BeanListHandler<Route>(Route.class),Constant.ROUTE_SHANGJIA,startPrice,endPrice,startIndex,pageSize);
		return routes;
	}
	//查询所有
	@Override
	public List<Route> FindNull(int startIndex,int pageSize) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_route where rflag =? order by count desc limit ?,?  ";
		List<Route> routes = qr.query(sql , new BeanListHandler<Route>(Route.class),Constant.ROUTE_SHANGJIA,startIndex,pageSize);
		return routes;
	}
	/**
	 * 条件搜索查询总条数
	 */
	@Override
	public int FindByRnameAndPricecount(String rname, String startPrice, String endPrice) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from tab_route where rflag =? and rname like ? and price between ? and ? ";
		Long l = (Long) qr.query(sql , new ScalarHandler(),Constant.ROUTE_SHANGJIA,"%"+rname+"%",startPrice,endPrice);
		return l.intValue();
	}
	/**
	 * 条件搜索查询总条数
	 */
	@Override
	public int FindByRanamecount(String rname) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from tab_route  where rflag =? and rname like ? ";
		Long l = (Long) qr.query(sql , new ScalarHandler(),Constant.ROUTE_SHANGJIA,"%"+rname+"%");
		return l.intValue();
	}
	/**
	 * 条件搜索查询总条数
	 */
	@Override
	public int FindByPricecount(String startPrice, String endPrice) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from tab_route where rflag =? and  price between ? and ? ";
		Long l = (Long) qr.query(sql , new ScalarHandler(),Constant.ROUTE_SHANGJIA,startPrice,endPrice);
		return l.intValue();
	}
	/**
	 * 条件搜索查询总条数
	 */
	@Override
	public int FindNullcount() throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from tab_route where rflag =? ";
		Long l = (Long) qr.query(sql , new ScalarHandler(),Constant.ROUTE_SHANGJIA);
		return l.intValue();
	}

}
