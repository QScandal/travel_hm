package com.itheima.dao;

import java.sql.SQLException;
import java.util.List;

import com.itheima.domain.Route;

public interface FindDao {

	List<Route> findRouteListByCid(int cid, String rname, int startIndex, int pageSize) throws SQLException;

	int findCountTotal(String rname) throws SQLException;

}
