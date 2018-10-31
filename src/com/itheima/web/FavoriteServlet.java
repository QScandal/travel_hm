package com.itheima.web;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.itheima.domain.Favorite;
import com.itheima.domain.PageBean;
import com.itheima.domain.ResultInfo;
import com.itheima.domain.User;
import com.itheima.service.FavoriteService;
import com.itheima.utils.BeanFactory;

/**
 * Servlet implementation class FavoriteServlet
 */
public class FavoriteServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;

	// 查看我的收藏	
	public void findFavoriteByPage(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ResultInfo resultInfo = new ResultInfo();
		Boolean flag = true;
		// System.out.println("进来了");
		try {
			/*
			 * User user = new User(); user.setUid(1);
			 * request.getSession().setAttribute("user", user); //
			 * 从数据库中查找出我的收藏，并通过Ajax展示在前端页面上 user = (User)
			 * request.getSession().getAttribute("user"); // 用户尚未登录，提示用户登录 if (user == null)
			 * {
			 * 
			 * return; } // 用户登陆，如果用户登录，则进行我的收藏页面的展示 int uid = user.getUid();
			 */
			// 初始化当前页面为1
			int uid = 3;
			int curPage = 1;
			// try..catch增强代码的健壮性，当输入当前页码有误时，将其自动跳转到收藏列表首页
			try {
				curPage = Integer.parseInt(request.getParameter("curPage"));
			} catch (Exception e) {
				curPage = 1;
			}
			int pageSize = 6;
			// 调用service层，将数据封装到PageBean中
			FavoriteService service = (FavoriteService) BeanFactory.getBean("FavoriteService");
			PageBean<Favorite> pb = service.myFavoriteLsit(uid, curPage, pageSize);
			resultInfo.setData(pb);
			// 将返回的数据转换为json类型传回前台
			//flag = true;
			resultInfo.setFlag(flag);
			resultInfo.setErrorMsg("查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			String errorMsg = "查询失败";
			resultInfo.setErrorMsg(errorMsg);
			flag = false;
		}
		resultInfo.setFlag(flag);
		String jsonString = JSON.toJSONString(resultInfo);
		response.getWriter().print(jsonString);
	}
	public void isFavoriteByRid(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String rid = request.getParameter("rid");
			//String uid = request.getParameter("uid");
			User user=new User();
			user.setUid(5);
			FavoriteService service = (FavoriteService) BeanFactory.getBean("FavoriteService");
			String resultinfo=service.isFavoriteByRid(rid,user.getUid());
			response.getWriter().print(resultinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 添加收藏
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addFavorite(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			//1.获取请求携带的参数信息
			String rid = request.getParameter("rid");
			//String uid=request.getParameter("uid");
			User user=new User();
			user.setUid(5);
			//2.调用service层处理业务逻辑
			FavoriteService service = (FavoriteService) BeanFactory.getBean("FavoriteService");
			//根据rid和uid查询收藏次数
			service.addFavorite(rid,user.getUid());
			
			//3.生成响应信息 
			//response.getWriter().print(resultinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
