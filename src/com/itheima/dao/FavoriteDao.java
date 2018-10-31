package com.itheima.dao;

import java.sql.SQLException;
import java.util.List;

import com.itheima.domain.Favorite;

public interface FavoriteDao {

	int findTotaoCount(int uid) throws SQLException;

	List<Favorite> myFavoriteLsit(int uid, int startIndex, int pageSize) throws Exception;
	
	void addFavorite(String rid,int uid) throws Exception;

	Favorite isFavoriteByRid(String rid, int uid) throws Exception;
}
