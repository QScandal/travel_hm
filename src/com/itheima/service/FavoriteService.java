package com.itheima.service;

import java.sql.SQLException;

import com.itheima.domain.Favorite;
import com.itheima.domain.PageBean;

public interface FavoriteService {

	PageBean<Favorite> myFavoriteLsit(int uid,int curPage, int pageSize) throws Exception;
	String isFavoriteByRid(String rid, int i) throws Exception;

	void addFavorite(String rid, int uid) throws Exception;

}
