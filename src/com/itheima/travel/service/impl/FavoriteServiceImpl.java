package com.itheima.travel.service.impl;


import com.itheima.travel.dao.FavoriteDao;
import com.itheima.travel.dao.impl.FavoriteDaoImpl;
import com.itheima.travel.domain.Favorite;
import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.User;
import com.itheima.travel.service.FavoriteService;
import com.itheima.travel.utils.PageBean;
import org.apache.commons.beanutils.BeanUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FavoriteServiceImpl implements FavoriteService {

    /**
     * 查询我的收藏（favorite表），并分页显示
     * <p>
     * -----张耀
     *
     * @throws Exception
     */
    @Override
    public PageBean<Favorite> findFavoriteByPage(User user, int curPage, int pageSize) throws Exception {

        //调用dao层
        FavoriteDao dao = new FavoriteDaoImpl();
        //先查询收藏的总记录数
        int count = dao.findCountByUid(user);
        //创建pageBean对象封装分页数据
        PageBean<Favorite> pb = new PageBean<>();
        pb.setCurPage(curPage);
        pb.setCount(count);
        pb.setPageSize(pageSize);

        //查询该用户收藏旅游的信息
        List<Map<String, Object>> listmap = dao.findFavoriteByPage(pb, user);
        List<Favorite> list = new ArrayList<>();
        //将list中的字段分别封装
        for (Map<String, Object> map : listmap) {
            //旅游线路商品实体类
            Route route = new Route();
            BeanUtils.populate(route, map);

            //收藏实体类
            Favorite favorite = new Favorite();
            BeanUtils.populate(favorite, map);
            favorite.setRoute(route);
            favorite.setUser(user);

            list.add(favorite);
        }
        //将list封装到pb中
        pb.setData(list);
        return pb;
    }


    /*========================================收藏 朱昊===========================================================*/

    /**
     * 判断该页面产品是否被收藏
     */
    @Override
    public void addFavoriteByUid(int rid, int uid) throws SQLException {

        //创建时间对象
        Date date = new Date();

        FavoriteDao dao = new FavoriteDaoImpl();
        dao.addFavoriteByUid(rid,date,uid);
        //返回商品被收藏次数
    }


    /**
     * 添加收藏方法
     */
    @Override
    public int isFavoriteByRid(int rid, int uid) throws SQLException {
        FavoriteDao dao = new FavoriteDaoImpl();
        int data = dao.isFavoriteByRid(rid,uid);
        return data;
    }


    /**
     * 取消收藏方法
     */
    @Override
    public void delFavorite(int rid, int uid) throws SQLException {
        FavoriteDao dao = new FavoriteDaoImpl();
        dao.delFavorite(rid,uid);
    }


    /**
     * 查询收藏次数
     */
    @Override
    public int findCountByRid(int rid) throws SQLException {
        FavoriteDao dao = new FavoriteDaoImpl();
        int data = dao.findCountByRid(rid);
        return data;
    }
    /*============收藏排行榜=======================*/

    @Override
    /**
     * 按条件搜索排行榜
     */
    public ResultInfo findRoutesFavoriteRank(int curPage, int pageSize, String rname, double startPrice, double endPrice) throws Exception {
        //创建pageBean对象
        PageBean<Route> pageBean = new PageBean<>();
        //获取路线的总条数
        FavoriteDao dao = new FavoriteDaoImpl();
        int totalCount = dao.getTotalCount(rname, startPrice, endPrice);

        //获取当前索引
        pageBean.setCurPage(curPage);
        pageBean.setPageSize(pageSize);
        pageBean.setCount(totalCount);
        int startIndex = pageBean.getStartIndex();
        //获取旅游路线集合
        List<Route> routeList = dao.findRoutesFavoriteRank(startIndex, pageSize, rname, startPrice, endPrice);

        pageBean.setData(routeList);
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.setData(pageBean);
        return resultInfo;

    }




}
