package com.itheima.travel.service;

import com.itheima.travel.domain.Favorite;
import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.domain.User;
import com.itheima.travel.utils.PageBean;

import java.sql.SQLException;

public interface FavoriteService {


    PageBean<Favorite> findFavoriteByPage(User user, int curPage, int pageSize) throws Exception;

    /*================================收藏===========================================*/

    void addFavoriteByUid(int rid, int uid) throws SQLException;

    int isFavoriteByRid(int rid, int uid) throws SQLException;

    void delFavorite(int rid, int uid) throws SQLException;

    /*================================收藏===========================================*/

    int findCountByRid(int rid)throws SQLException;

    /*====================排行榜===============================*/
    ResultInfo findRoutesFavoriteRank(int curPage, int pageSize, String rname, double startPrice, double endPrice) throws Exception;

}
