package com.itheima.travel.dao.impl;

import com.itheima.travel.dao.RouteDao;
import com.itheima.travel.domain.Category;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.RouteImg;
import com.itheima.travel.domain.Seller;
import com.itheima.travel.utils.C3P0Utils;
import com.itheima.travel.utils.UUIDUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RouteDaoImpl implements RouteDao {
    @Override
    /**
     * 查找热卖的旅游路线
     */
    public List<Route> findPopularity() throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select * from tab_route order by count desc limit ?,? ";
        return qr.query(sql, new BeanListHandler<>(Route.class), 0, 4);
    }

    @Override
    /**
     * 查找最新的旅游路线
     */
    public List<Route> findNews() throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select * from tab_route order by rdate desc limit ?,? ";
        return qr.query(sql, new BeanListHandler<>(Route.class), 0, 4);
    }

    @Override
    /**
     * 查找主题旅游路线
     */
    public List<Route> findThemes() throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select * from tab_route where isThemeTour = ? limit ?,? ";
        return qr.query(sql, new BeanListHandler<>(Route.class), 1, 0, 4);
    }

    @Override
    /**
     * 查找本地游路线
     */
    public List<Route> findMycountries() throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select * from tab_route where rtype = ? limit ?,? ";
        return qr.query(sql, new BeanListHandler<>(Route.class), 1, 0, 6);
    }

    @Override
    /**
     * 查找出国游路线
     */
    public List<Route> findAbroad() throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select * from tab_route where rtype = ? limit ?,? ";
        return qr.query(sql, new BeanListHandler<>(Route.class), 2, 0, 6);
    }



    @Override
    /**
     *  根据旅游路线id查询详细信息
     */
    public Route findRouteByRid(int rid) throws Exception {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        //4表联合查询
        String sql = "select * from tab_route route, tab_category category, tab_seller seller, tab_route_img routeImage where route.rid=routeImage.rid and route.cid=category.cid and route.sid=seller.sid and route.rid=? ";
        List<Map<String, Object>> maps = qr.query(sql, new MapListHandler(), rid);

        Map<String, Object> map = maps.get(0);
        //封装route, category和seller
        Route route = new Route();
        Category category = new Category();
        Seller seller = new Seller();
        BeanUtils.populate(route, map);
        BeanUtils.populate(category, map);
        BeanUtils.populate(seller, map);
        route.setCategory(category);
        route.setSeller(seller);
        //遍历maps,获取list<RouteImg>集合用来封装route
        for (Map<String, Object> m : maps) {
            RouteImg routeImg = new RouteImg();
            BeanUtils.populate(routeImg, m);
            route.getRouteImgList().add(routeImg);
        }
        return route;
    }

    @Override
    /**
     * 获取旅游路线总数量
     */
    public int getTotalCount(int cid) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select count(*) from tab_route where cid = ? ";
        return ((Long)qr.query(sql, new ScalarHandler(), cid)).intValue();
    }

    @Override
    /**
     * 根据id获取旅游路线列表
     */
    public List<Route> findRouteListByCid(int cid, String rname, int startIndex, int pageSize) throws Exception {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select * from tab_route where cid = ? and rname like ? limit ?,? ";
        List<Route> routeList = qr.query(sql, new BeanListHandler<>(Route.class), cid, "%"+rname+"%", startIndex, pageSize);

        for (Route r : routeList) {
            r = findRouteByRid(r.getRid());
        }
        return routeList;
    }


    /*=====================================精确搜索===========================================*/
    @Override
    public List<Route> selectKeyWords(String keyWords) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select * from tab_route where rname like ? limit 10 ";

        //使用可变字符串在关键字中拼接%
        StringBuilder sb = new StringBuilder();
        sb.append("%");
        for (int i = 0; i < keyWords.length(); i++) {
            sb.append(keyWords.charAt(i)).append("%");
        }

        List<Route> list = qr.query(sql, new BeanListHandler<>(Route.class), sb.toString());
        return list;
    }


    /*=================================后台===========================================*/
    @Override
    /**
     * 根据商家id获取该商品发布的所有商品的数量
     * 不需要判断商品是否上架或者下架
     */
    public int getTotalCountBySid(int sid) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select count(*) from tab_route where sid = ? ";

        return ((Long)qr.query(sql, new ScalarHandler(), sid)).intValue();
    }

    @Override
    /**
     * 根据商家id获取商家的商品列表
     * 不需要判断上架或者下架
     */
    public List<Route> findRouteListBySid(int sid, int startIndex, int pageSize) throws Exception {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        //三表联合查询
        String sql = "select * from tab_route route, tab_category category, tab_seller seller where route.cid=category.cid and route.sid=seller.sid and route.sid = ? limit ?,? ";
        List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), sid, startIndex, pageSize);

        List<Route> routeList = new ArrayList<>();
        for (Map<String, Object> map : mapList) {
            Route route = new Route();
            Category category = new Category();
            Seller seller = new Seller();

            //封装
            BeanUtils.populate(route, map);
            BeanUtils.populate(category, map);
            BeanUtils.populate(seller, map);

            //获取图片集合
            sql = "select * from tab_route route, tab_route_img routeImage where route.rid=routeImage.rid and route.rid= ?";
            List<Map<String, Object>> mapList1 = qr.query(sql, new MapListHandler(), route.getRid());
            List<RouteImg> routeImgList = new ArrayList<>();
            for (Map<String, Object> map1 : mapList1) {
                RouteImg routeImg = new RouteImg();
                BeanUtils.populate(routeImg, map1);
                routeImgList.add(routeImg);
            }

            route.setRouteImgList(routeImgList);

            routeList.add(route);
        }

        return routeList;
    }

    @Override
    /**
     * 添加旅游产品
     */
    public void addRoute(String rname, double price, String routeIntroduce, String isThemeTour, int cid, String rimage, int rtype) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "insert into tab_route values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {
                null,rname,price,routeIntroduce,
                "1",new Date(),isThemeTour,0,cid,rimage,1,UUIDUtils.getId(),rtype,121.612351,31.034596
        };
        qr.update(sql, params);
    }
    /*=================================后台===========================================*/

}
