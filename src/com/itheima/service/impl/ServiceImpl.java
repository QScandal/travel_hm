package com.itheima.service.impl;

import java.util.List;

import com.itheima.dao.Dao;
import com.itheima.dao.impl.Daoimpl;
import com.itheima.domain.PageBean;
import com.itheima.domain.Route;
import com.itheima.service.Service;
import com.itheima.utils.BeanFactory;

public class ServiceImpl implements Service {

	
	@Override
	public List<Route> FindByRnameAndPrice(String rname, String startPrice, String endPrice,int startIndex,int pageSize) throws Exception {
		Dao dao = (Dao) BeanFactory.getBean("Daoimpl");
		return dao.FindByRnameAndPrice(rname,startPrice,endPrice,startIndex,pageSize);
	}

	@Override
	public List<Route> FindByRaname(String rname,int startIndex,int pageSize) throws Exception {
		Dao dao = (Dao) BeanFactory.getBean("Daoimpl");
		return dao.FindByRaname(rname,startIndex,pageSize);
	}

	@Override
	public List<Route> FindByPrice(String startPrice, String endPrice,int startIndex,int pageSize) throws Exception {
		Dao dao = (Dao) BeanFactory.getBean("Daoimpl");
		return dao.FindByPrice(startPrice,endPrice,startIndex,pageSize);
	}

	@Override
	public List<Route> FindNull(int startIndex,int pageSize) throws Exception {
		Dao dao = (Dao) BeanFactory.getBean("Daoimpl");
		return dao.FindNull(startIndex,pageSize);
	}
	@Override
	public int FindByRnameAndPricecount(String rname, String startPrice, String endPrice) throws Exception {
		Dao dao = (Dao) BeanFactory.getBean("Daoimpl");
		return dao.FindByRnameAndPricecount(rname,startPrice,endPrice);
	}

	@Override
	public int FindByRanamecount(String rname) throws Exception {
		Dao dao = (Dao) BeanFactory.getBean("Daoimpl");
		return dao.FindByRanamecount(rname);
	}

	@Override
	public int FindByPricecount(String startPrice, String endPrice) throws Exception {
		Dao dao = (Dao) BeanFactory.getBean("Daoimpl");
		return dao.FindByPricecount(startPrice,endPrice);
	}

	@Override
	public int FindNullcount() throws Exception {
		Dao dao = (Dao) BeanFactory.getBean("Daoimpl");
		return dao.FindNullcount();
	}
	
	
	@Override
	public PageBean<Route> findroute(int curPage ,int pageSize,int count,List<Route> route) throws Exception {
		
		Dao dao = (Dao) BeanFactory.getBean("Daoimpl");
		//定义每页显示的条数
		
		
		
		PageBean<Route> pb = new PageBean<>();
		pb.setCount(count);
		pb.setCurPage(curPage);
		pb.setData(route);
		pb.setPageSize(pageSize);
		
		return pb;
	

	}

	
	}
