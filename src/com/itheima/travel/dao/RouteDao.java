package com.itheima.travel.dao;

import com.itheima.travel.domain.Route;

import java.sql.SQLException;
import java.util.List;

public interface RouteDao {
    List<Route> findPopularity() throws SQLException;

    List<Route> findNews() throws SQLException;

    List<Route> findThemes() throws SQLException;

    Route findRouteByRid(int rid) throws Exception;

    int getTotalCount(int cid) throws SQLException;

    List<Route> findRouteListByCid(int cid, String rname, int startIndex, int pageSize) throws Exception;

    List<Route> findMycountries() throws SQLException;

    List<Route> findAbroad() throws SQLException;

    List<Route> selectKeyWords(String keyWords) throws SQLException;

    /*==============================后台============================================*/
    int getTotalCountBySid(int sid) throws SQLException;

    List<Route> findRouteListBySid(int sid, int startIndex, int pageSize) throws SQLException, Exception;

    void addRoute(String rname, double price, String routeIntroduce, String isThemeTour, int cid, String rimage, int rtype) throws SQLException;
    /*==============================后台============================================*/
}
