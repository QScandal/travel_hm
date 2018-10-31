package com.itheima.travel.service;

import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.domain.Route;

import java.sql.SQLException;
import java.util.List;

public interface RouteService {
    ResultInfo routeCareChoose();

    ResultInfo findRouteByRid(int rid) throws Exception;

    ResultInfo findRouteListByCid(int cid, String rname, int curPage, int pageSize) throws SQLException, Exception;

    /*===============================精确搜索==========================================*/
    List<Route> selectKeyWords(String keyWords) throws SQLException;
    /*===============================精确搜索==========================================*/


    /*=================================后台==========================================*/
    ResultInfo findRouteListBySid(int sid, int curPage, int pageSize) throws SQLException, Exception;

    void addRoute(String rname, double price, String routeIntroduce, String isThemeTour, int cid, String rimage, int rtype) throws SQLException;
    /*=================================后台==========================================*/
}
