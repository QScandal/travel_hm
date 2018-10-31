package com.itheima.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.itheima.dao.FavoriteDao;
import com.itheima.domain.Favorite;
import com.itheima.domain.Route;
import com.itheima.utils.C3P0Utils;

public class FavoriteDaoImpl implements FavoriteDao {
	// 计算用户的收藏记录总数
	@Override
	public int findTotaoCount(int uid) throws SQLException {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select count(*) from tab_favorite where uid=?";
		Long l = (Long) qr.query(sql, new ScalarHandler(), uid);
		System.out.println("1111" + l.intValue());
		return l.intValue();
	}

	// 分页查询
	@Override
	public List<Favorite> myFavoriteLsit(int uid, int startIndex, int pageSize) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql = "select * from tab_favorite,tab_route where uid=? and tab_favorite.rid=tab_route.rid limit ?,?";
		// 查询出当前用户的所有收藏和路线的基本信息
		List<Map<String, Object>> list = qr.query(sql, new MapListHandler(), uid, startIndex, pageSize);
		// 遍历集合，查询出当前用户的一条收藏和路线的基本信息
		List<Favorite> flist = new ArrayList<Favorite>();
		for (Map<String, Object> map : list) {
			// 将route封装到用户的收藏信息中
			Route route = new Route();
			BeanUtils.populate(route, map);
			Favorite favorite = new Favorite();
			BeanUtils.populate(favorite, map);
			favorite.setRoute(route);
			flist.add(favorite);
			// favorite.getFlist().add(favorite);
			// System.out.println("2222" + list);
		}
		return flist;
	}

	@Override
	/**
	 * 根据rid uid插入数据 添加收藏
	 */
	public void addFavorite(String rid,int uid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String date = new Date().toLocaleString();
		String sql="insert into tab_favorite values (?,?,?)";
		qr.update(sql,rid,date,uid);
	}

	@Override
	/**
	 * 判断是否被收藏
	 */
	public Favorite isFavoriteByRid(String rid,int uid) throws Exception {
		QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
		String sql="select *from tab_favorite where rid=? and uid=? ";
		Favorite fr = qr.query(sql, new BeanHandler<>(Favorite.class), rid,uid);
		return fr;
	}
}
