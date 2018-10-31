package com.itheima.service;

import java.sql.SQLException;
import java.util.List;

import com.itheima.domain.Route;

public interface RouteService {

	List<Route> findfindPageProByCid(int cid, int curPage, int pageSize) throws SQLException;

	int findToatlCount(int cid) throws SQLException;

	List<Route> findfindRouteByIsThemeTour() throws SQLException;

	List<Route> findRouteByNews() throws SQLException;

	List<Route> findRouteByCount() throws SQLException;

	List<Route> findChaTra() throws SQLException;
	
	List<Route> findTwoTra() throws SQLException;
	
	String isFavoriteByRid(String rid, int i) throws Exception;

	String findRouteByRid(String rid) throws Exception;

}
