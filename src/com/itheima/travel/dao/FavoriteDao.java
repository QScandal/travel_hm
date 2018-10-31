package com.itheima.travel.dao;

import com.itheima.travel.domain.Favorite;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.User;
import com.itheima.travel.utils.PageBean;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface FavoriteDao {

    int findCountByUid(User user) throws SQLException;


    List<Map<String, Object>> findFavoriteByPage(PageBean<Favorite> pb, User user) throws SQLException;


    /*=============收藏===================================*/
    void addFavoriteByUid(int rid, Date date, int uid) throws SQLException;

    int isFavoriteByRid(int rid, int uid) throws SQLException;

    void delFavorite(int rid, int uid) throws SQLException;
    int findCountByRid(int rid)throws SQLException;



    /*====================收藏排行榜=============================*/
    int getTotalCount(String rname, double startPrice, double endPrice) throws SQLException;

    List<Route> findRoutesFavoriteRank(int startIndex, int pageSize, String rname, double startPrice, double endPrice) throws SQLException, Exception;
}
