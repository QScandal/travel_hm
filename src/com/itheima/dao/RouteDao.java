package com.itheima.dao;

import java.sql.SQLException;
import java.util.List;

import com.itheima.domain.Route;

public interface RouteDao {

	List<Route> findPageProByCid(int startIndex, int pageSize, int cid) throws SQLException;

	int findToatlCount(int cid) throws SQLException;

	List<Route> findfindRouteByIsThemeTour() throws SQLException;

	List<Route> findRouteByNews() throws SQLException;

	List<Route> findRouteByCount() throws SQLException;
	
	List<Route> findChaTra() throws SQLException;
	
	List<Route> findTwoTra() throws SQLException;
	
	Route findRouteByRid(String rid) throws Exception;

}
