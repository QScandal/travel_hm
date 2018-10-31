package com.itheima.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.itheima.domain.PageBean;
import com.itheima.domain.ResultInfo;
import com.itheima.service.FindService;
import com.itheima.service.impl.FindServiceImpl;
import com.itheima.utils.BeanFactory;

public class FindServlet extends BaseServlet {

	public void findRouteListByCid(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			String rname = request.getParameter("rname");
			
			System.out.println("web层rname="+rname);
			int cid = 4;
			int curPage = 1;
			try {
				cid = Integer.parseInt(request.getParameter("cid"));
				
			} catch (Exception e) {
				e.printStackTrace();
				cid = 4;
			}
			
			try {
				curPage = Integer.parseInt(request.getParameter("curPage"));
			} catch (Exception e) {
				e.printStackTrace();
				curPage = 1;
			}
			FindService service = (FindService)BeanFactory.getBean("FindService");
			//FindService service = new FindServiceImpl();
			ResultInfo resultInfo = service.findRouteListByCid(rname,curPage,cid);
			response.getWriter().print(JSON.toJSONString(resultInfo));
			//request.getRequestDispatcher("/route_list.html").forward(request, response);
			System.out.println("成功了");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("失败了");
		}
	}

}
