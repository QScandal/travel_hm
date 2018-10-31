package com.itheima.service.impl;

import java.util.List;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.itheima.dao.CategoryDao;
import com.itheima.dao.impl.CategoryDaoImpl;
import com.itheima.domain.Category;
import com.itheima.service.CategoryService;
import com.itheima.utils.Constant;
import com.itheima.utils.JedisPollUtils;


public class CategoryServiceImpl implements CategoryService{

	/**
	 * 查询分类信息
	 */
	public String findCateAll() throws Exception {
		//采用redis做缓存提高影响效率,降低mysql服务器压力
		//创建jedis对象
		Jedis jedis = JedisPollUtils.getJedis();
		//查询redis数据库
		String jlist = jedis.get(Constant.CATELISTJSON);
		//jlist = null;
		//判断查询结果
		if(jlist == null){
			//查询mysql数据库 并将查询结果放入redis中
			CategoryDao dao = new CategoryDaoImpl();
			List<Category> list = dao.findCateAll();
			jlist = JSON.toJSONString(list);
			//System.out.println("Mysql:=="+jlist);
			//查询结果放入redis库中
			jedis.set(Constant.CATELISTJSON, jlist);
		}
		JedisPollUtils.closeJedis(jedis);
		return jlist;
	}

}
