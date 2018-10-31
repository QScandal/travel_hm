package com.itheima.service.impl;

import java.sql.SQLException;
import java.util.List;

import redis.clients.jedis.Jedis;

import com.alibaba.fastjson.JSON;
import com.itheima.dao.CategoryDao;
import com.itheima.dao.FindDao;
import com.itheima.dao.impl.CategoryDaoImpl;
import com.itheima.dao.impl.FindDaoImpl;
import com.itheima.domain.Category;
import com.itheima.domain.PageBean;
import com.itheima.domain.ResultInfo;
import com.itheima.domain.Route;
import com.itheima.service.FindService;
import com.itheima.utils.BeanFactory;
import com.itheima.utils.Constant;
import com.itheima.utils.JedisPollUtils;

public class FindServiceImpl implements FindService {

	@Override
	public ResultInfo findRouteListByCid(String rname,int curPage,int cid) throws SQLException {
		FindDao dao = (FindDao)BeanFactory.getBean("FindDao");
		//计算条件查找的结果总数
		int count = dao.findCountTotal(rname);
		//封装结果
		ResultInfo rf = new ResultInfo();
		//假如查找结果为0的情况下直接返回错误信息
		if(count==0){
			rf.setFlag(false);
			rf.setErrorMsg("没有找到你所查找的景点，请输入其他景点");
			return rf;
		}
		//定义每页显示的数量
		int pageSize = 10;
		//定义起始索引
		int startIndex = (curPage-1)*pageSize;
		//查找分页查找的结果
		List<Route> data = dao.findRouteListByCid(cid,rname,startIndex,pageSize);

//		System.out.println("dao层curPage"+curPage);
//		System.out.println("dao层count"+count);
		PageBean<Route> pageBean = new PageBean<Route>();
		for (Route route : data) {
			System.out.println(route);
		}
		pageBean.setCount(count);
		pageBean.setCurPage(curPage);
		pageBean.setData(data);
		pageBean.setPageSize(pageSize);
		rf.setData(pageBean);
		rf.setFlag(true);
		
		return rf;
	}

}
