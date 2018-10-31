package com.itheima.service;

import java.sql.SQLException;

import com.itheima.domain.PageBean;
import com.itheima.domain.ResultInfo;

public interface FindService {

	ResultInfo findRouteListByCid(String rname,int curPage, int cid) throws SQLException;

}
