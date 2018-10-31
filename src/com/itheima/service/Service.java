package com.itheima.service;

import java.util.List;

import com.itheima.domain.PageBean;
import com.itheima.domain.Route;

public interface Service {

	PageBean<Route> findroute(int curPage, int pageSize, int count, List<Route> route) throws Exception;

	List<Route> FindByRnameAndPrice(String rname, String startPrice, String endPrice, int startIndex, int pageSize) throws Exception;

	List<Route> FindByRaname(String rname, int startIndex, int pageSize) throws Exception;

	List<Route> FindByPrice(String startPrice, String endPrice, int startIndex, int pageSize) throws Exception;

	List<Route> FindNull(int startIndex, int pageSize) throws Exception;

	int FindByRnameAndPricecount(String rname, String startPrice, String endPrice) throws Exception;

	int FindByRanamecount(String rname) throws Exception;

	int FindByPricecount(String startPrice, String endPrice) throws Exception;

	int FindNullcount() throws Exception;
	

}
