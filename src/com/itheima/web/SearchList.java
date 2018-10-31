package com.itheima.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.itheima.domain.PageBean;
import com.itheima.domain.ResultInfo;
import com.itheima.domain.Route;
import com.itheima.service.Service;
import com.itheima.service.impl.ServiceImpl;
import com.itheima.utils.BeanFactory;

/**
 * Servlet implementation class SearchList
 */
public class SearchList extends BaseServlet {

	public void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ResultInfo rf = new ResultInfo();
		try {
			String Rname = request.getParameter("rname");
			String startPrice = request.getParameter("startPrice");
			String endPrice = request.getParameter("endPrice");

				int curPage =1;
			
			
				//默认第一页,每页数量8个
				
				int pageSize=8;
				//获取前台需求的页数pageNumber
				try {
					curPage = Integer.parseInt(request.getParameter("curPage"));
				} catch (Exception e) {
					curPage=1;
				}
				
			

			// 开始索引
			int startIndex = (curPage - 1) * pageSize;
			int count = 0;
			List<Route> route = null;
			Service service = (Service)BeanFactory.getBean("serviceimpl");
			// 判断Rname,startPrice,endPrice 是否存在
			if (Rname.length() != 0 && startPrice.length() != 0 && endPrice.length() != 0) {
				// 都存在
				route = service.FindByRnameAndPrice(Rname, startPrice, endPrice, startIndex, pageSize);
				count = service.FindByRnameAndPricecount(Rname, startPrice, endPrice);

				if (Rname.length() != 0 && startPrice.length() == 0 && endPrice.length() == 0) {
					// 文本存在,价格不存在
					route = service.FindByRaname(Rname, startIndex, pageSize);
					count = service.FindByRanamecount(Rname);
				}
				if (Rname.length() == 0 && startPrice.length() != 0 && endPrice.length() != 0) {
					// 文本不存在,价格存在
					route = service.FindByPrice(startPrice, endPrice, startIndex, pageSize);
					count = service.FindByPricecount(startPrice, endPrice);
				}
			} else {
				// 不存在
				route = service.FindNull(startIndex, pageSize);
				count = service.FindNullcount();
			}

			PageBean<Route> pb = service.findroute(curPage, pageSize, count, route);

			rf.setFlag(true);
			rf.setData(pb);
			rf.setErrorMsg("现在正在维护");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		response.getWriter().print(JSON.toJSONString(rf));

	}
	
		
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
