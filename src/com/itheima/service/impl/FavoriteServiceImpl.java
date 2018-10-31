package com.itheima.service.impl;

import java.sql.SQLException;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.itheima.dao.FavoriteDao;
import com.itheima.domain.Favorite;
import com.itheima.domain.PageBean;
import com.itheima.domain.ResultInfo;
import com.itheima.service.FavoriteService;
import com.itheima.utils.BeanFactory;

public class FavoriteServiceImpl implements FavoriteService {
	private FavoriteDao dao = (FavoriteDao) BeanFactory.getBean("FavoriteDao");

	@Override
	public PageBean<Favorite> myFavoriteLsit(int uid, int curPage, int pageSize) throws Exception {
		// 根据用户的uid计算用户收藏的总记录数count
		int count = dao.findTotaoCount(uid);
		// 起始索引
		int startIndex = (curPage - 1) * pageSize;
		// 分页查询
		List<Favorite> list = dao.myFavoriteLsit(uid, startIndex, pageSize);
		PageBean<Favorite> pb = new PageBean<>();
		// 将数据封装到PageBean中
		pb.setCount(count);
		pb.setCurPage(curPage);
		pb.setData(list);
		pb.setPageSize(pageSize);
		return pb;
	}
	@Override
	/**
	 * 根据rid插入数据 并将收藏次数更新
	 */
	public void addFavorite(String rid,int uid) throws Exception {
		FavoriteDao dao=(FavoriteDao) BeanFactory.getBean("FavoriteDao");
		dao.addFavorite(rid,uid);
		/*ResultInfo ri=new ResultInfo();
		ri.setFlag(true);
		ri.setData(1);
		return JSON.toJSONString(ri);*/
	}

	@Override
	/**
	 * 根据rid判断该路线是否被收藏
	 */
	public String isFavoriteByRid(String rid,int uid) throws Exception {
		FavoriteDao dao=(FavoriteDao) BeanFactory.getBean("FavoriteDao");
		Favorite fr=dao.isFavoriteByRid(rid,uid);
		ResultInfo ri=new ResultInfo();
		if(fr!=null){
			//已收藏	没有点击收藏功能
			ri.setFlag(true);
			ri.setData(new Boolean(true));
		}else{
			//未收藏 	有点击收藏功能
			ri.setFlag(true);
			ri.setData(new Boolean(false));
		}
		return JSON.toJSONString(ri);
	}


}
