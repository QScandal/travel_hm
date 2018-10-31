package com.itheima.travel.dao.impl;

import com.itheima.travel.dao.FavoriteDao;
import com.itheima.travel.dao.RouteDao;
import com.itheima.travel.domain.Favorite;
import com.itheima.travel.domain.Route;
import com.itheima.travel.domain.User;
import com.itheima.travel.utils.C3P0Utils;
import com.itheima.travel.utils.PageBean;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FavoriteDaoImpl implements FavoriteDao {

    /**
     * 先查询收藏的总记录数
     * ----张耀
     *
     * @throws SQLException
     */
    @Override
    public int findCountByUid(User user) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select count(*) from tab_favorite where uid = ? ";
        Long count = (Long) qr.query(sql, new ScalarHandler(), user.getUid());
        return count.intValue();
    }

    /**
     * 查询该用户收藏旅游的信息
     * <p>
     * ---张耀
     *
     * @throws SQLException
     */
    @Override
    public List<Map<String, Object>> findFavoriteByPage(PageBean<Favorite> pb, User user) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select * from tab_favorite fa ,tab_route ro where fa.uid=? and fa.rid=ro.rid limit ?,?  ";
        Object[] params = {user.getUid(), pb.getStartIndex(), pb.getPageSize()};
        List<Map<String, Object>> lmap = qr.query(sql, new MapListHandler(), params);

        return lmap;
    }


    /*================收藏=====================================================*/
    @Override
    public void addFavoriteByUid(int rid, Date date, int uid) throws SQLException {

        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());

        //插入数据到tab_favorite
        String sql = "insert into tab_favorite values(?,?,?) ";
        qr.update(sql, rid,date,uid);

        //更新收藏次数到tab_route
        sql = "update tab_route set count=count+1 where rid = ? ";
        qr.update(sql, rid);
        //System.out.println("更新收藏次数成功");

    }

    @Override
    public int isFavoriteByRid(int rid, int uid) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select count(*) from tab_favorite where rid = ? and uid = ? ";
        //System.out.println("query");
        Long l = (Long)qr.query(sql, new ScalarHandler(),rid,uid);
        //System.out.println(l.intValue());
        return l.intValue();
    }

    @Override
    public void delFavorite(int rid, int uid) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "delete from tab_favorite where rid = ? and uid = ? ";
        qr.update(sql, rid,uid);

        sql = "update tab_route set count=count-1 where rid = ? ";
        qr.update(sql, rid);
    }

    //根据rid查询收藏次数
    public int findCountByRid(int rid) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select count from tab_route where rid = ? ";
        List<Object> list = qr.query(sql, new ColumnListHandler(),rid);
        return (int) list.get(0);

    }





/*=========================================收藏排行榜==================================================*/

    @Override
    /**
     * 获得指定条件的路线的数目
     */
    public int getTotalCount(String rname, double startPrice, double endPrice) throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select count(*) from tab_route where rname like ? and price >= ? and price <= ?";
        return ((Long) qr.query(sql, new ScalarHandler(), "%"+rname+"%", startPrice, endPrice)).intValue();
    }

    @Override
    /**
     * 按照字段和金额分页查询
     */
    public List<Route> findRoutesFavoriteRank(int startIndex, int pageSize, String rname, double startPrice, double endPrice) throws Exception {

        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        //select * from tab_route where price >= 1000.0 and price <=2000.0 and rname like  '%三亚%'  order by count desc, price desc limit 1,8
        String sql = "select * from tab_route where price >= ? and price <= ? and rname like ? order by count desc, price desc limit ?,? ";
        List<Route> routeList = qr.query(sql, new BeanListHandler<>(Route.class), startPrice, endPrice, "%" + rname + "%", startIndex, pageSize);

        RouteDao dao = new RouteDaoImpl();
        for (Route r : routeList) {
            r = dao.findRouteByRid(r.getRid());
        }
        return routeList;
    }


}
