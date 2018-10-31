package com.itheima.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.itheima.dao.RouteDao;
import com.itheima.dao.impl.RouteDaoImpl;
import com.itheima.domain.PageBean;
import com.itheima.domain.ResultInfo;
import com.itheima.domain.Route;
import com.itheima.service.RouteService;
import com.itheima.utils.BeanFactory;

public class RouteServiceImpl implements RouteService {

	/**
	 * 查询当前页面上的商品信息
	 * @throws SQLException 
	 */
	public List<Route> findfindPageProByCid(int cid, int curPage,
			int pageSize) throws SQLException {
		//计算起始索引
		int startIndex = (curPage-1)*pageSize;
		//调用dao查询当前页的的商品信息
		RouteDao dao = new RouteDaoImpl();
		List<Route> list = dao.findPageProByCid(startIndex,pageSize,cid);
		return  list;
	}

	/**
	 * 查询总条数
	 * @throws SQLException 
	 */
	public int findToatlCount(int cid) throws SQLException {
		RouteDao dao = new RouteDaoImpl();
		return dao.findToatlCount(cid);
	}

	/**
	 * 查询主题旅游信息
	 * @throws SQLException 
	 */
	public List<Route> findfindRouteByIsThemeTour() throws SQLException {
		RouteDao dao = new RouteDaoImpl();
		
		return dao.findfindRouteByIsThemeTour();
	}

	/**
	 * 查询最新旅游信息
	 * @throws SQLException 
	 */
	public List<Route> findRouteByNews() throws SQLException {
		RouteDao dao = new RouteDaoImpl();
		return dao.findRouteByNews();
	}

	/**
	 * 查询人气旅游信息
	 * @throws SQLException 
	 */
	public List<Route> findRouteByCount() throws SQLException {
		RouteDao dao = new RouteDaoImpl();
		return dao.findRouteByCount();
	}

	
	/**
	 * 国内游
	 */
	public List<Route> findChaTra() throws SQLException {
		RouteDao dao = new RouteDaoImpl();
		return dao.findChaTra();
	}
	
	/**
	 * 境外游
	 */
	public List<Route> findTwoTra() throws SQLException {
		RouteDao dao = new RouteDaoImpl();
		return dao.findTwoTra();
	}
	
	@Override
	/**
	 * 根据rid查找路线详细信息
	 */
	public String findRouteByRid(String rid) throws Exception {
		RouteDao dao=(RouteDao)BeanFactory.getBean("RouteDao");
		Route route = dao.findRouteByRid(rid);
		ResultInfo ri=new ResultInfo();
		ri.setData(route);
		ri.setFlag(true);
		return JSON.toJSONString(ri);
	}

	@Override
	public String isFavoriteByRid(String rid, int i) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



}
