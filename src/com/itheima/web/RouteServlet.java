package com.itheima.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.itheima.domain.PageBean;
import com.itheima.domain.ResultInfo;
import com.itheima.domain.Route;
import com.itheima.service.RouteService;
import com.itheima.service.impl.RouteServiceImpl;
import com.itheima.utils.BeanFactory;


public class RouteServlet extends BaseServlet {
	
	/**
	 * 境外游
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findTwoTra(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ResultInfo ri = new ResultInfo();
		try {
			RouteService service = new RouteServiceImpl();
			List<Route> list = service.findTwoTra();
			
			//System.out.println(list);
			
			ri.setData(list);
			ri.setFlag(true);
			
			
		} catch (SQLException e) {
			ri.setFlag(false);
			e.printStackTrace();
		}
		String resulteInfo = JSON.toJSONString(ri);
		response.getWriter().print(resulteInfo);
		//System.out.println(resulteInfo);
	}
	
	/**
	 * 国内游
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findIndexTra(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//创建ResultInfo
		ResultInfo ri = new ResultInfo();
		try {
			RouteService service = new RouteServiceImpl();
			//国内
			List<Route> clist = service.findChaTra();
			ri.setData(clist);
			
			ri.setFlag(true);
			/*//境外
			List<Route> wlist = service.findWaiTra();
			ri.setData(wlist);
			ri.setFlag(true);*/
			
		} catch (SQLException e) {
			ri.setFlag(false);
			ri.setErrorMsg("补不起 不成功");
			e.printStackTrace();
		}
		String resulteInfo = JSON.toJSONString(ri);
		response.getWriter().print(resulteInfo);
		//System.out.println(resulteInfo);
	}

	
	/**
	 * 查询人气旅游信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findRouteByCount(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			RouteService service = new RouteServiceImpl();
			List<Route> popularity = service.findRouteByCount();
			
			//System.out.println(news);
			
			ResultInfo info = new ResultInfo();
			info.setData(popularity);
			info.setErrorMsg("程序有误");
			info.setFlag(true);
			response.getWriter().print(JSON.toJSONString(info)); 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 查询最新旅游 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findRouteByNews(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			RouteService service = new RouteServiceImpl();
			List<Route> news = service.findRouteByNews();
			
			//System.out.println(news);
			
			ResultInfo info = new ResultInfo();
			info.setData(news);
			info.setErrorMsg("程序有误");
			info.setFlag(true);
			response.getWriter().print(JSON.toJSONString(info));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 主题旅游查询
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findRouteByIsThemeTour(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			RouteService service = new RouteServiceImpl();
			List<Route> isThemeTour = service.findfindRouteByIsThemeTour();
			ResultInfo info = new ResultInfo();
			info.setData(isThemeTour);
			info.setErrorMsg("程序有误");
			info.setFlag(true);
			response.getWriter().print(JSON.toJSONString(info));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 分类的分页查询
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void findPageProByCid(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			//1.获取分类的cid
			int cid = Integer.parseInt(request.getParameter("cid"));
			//2.获取当前页的页码
			int curPage = 1;
			try {
				curPage = Integer.parseInt(request.getParameter("curPage"));
			} catch (Exception e) {
				curPage = 1;
			}
			//3.定义每页显示条数
			int pageSize = 6;
			//4.调用service完成分类的分页查询
			RouteService service = new RouteServiceImpl();
			ResultInfo resultInfo = new ResultInfo();
			
			List<Route> jlist = service.findfindPageProByCid(cid,curPage,pageSize);
			//System.out.println(jlist);
		
			//5.查询总条数
			int totalCount = service.findToatlCount(cid);
			//计算总页数
			int totalPage = (int) Math.ceil(totalCount*1.0/pageSize);
			
			PageBean<Route> bean = new PageBean<Route>();
			bean.setCurPage(curPage);
			bean.setPageSize(pageSize);
			bean.setCount(totalCount);
			bean.setTotalPage(totalPage);
			bean.setData(jlist);
			
			ResultInfo info = new ResultInfo();
			info.setData(bean);
			info.setErrorMsg("无");
			info.setFlag(true);
			//System.out.println(JSON.toJSONString(info));
			response.getWriter().print(JSON.toJSONString(info));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		public void findRouteByRid(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			try {
				//1.获取请求携带的参数信息
				String rid = request.getParameter("rid");
				//2.调用service层处理业务逻辑
				RouteService service=(RouteService)BeanFactory.getBean("RouteService");
				String resultinfo=service.findRouteByRid(rid);
				//3.生成响应信息	前端页面ajax接收数据 必须使用response传递
				response.getWriter().print(resultinfo);
				//request.setAttribute("ri", resultinfo);
				//request.getRequestDispatcher("/route_detail.html").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	

}

