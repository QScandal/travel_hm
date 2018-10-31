package com.itheima.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.itheima.domain.User;

public class LoginOK extends BaseServlet {

	public void getLoginUserData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				User user = (User) request.getSession().getAttribute("user");
				if(user != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("user", user);
				map.put("flag", true);
				String jsonString = JSON.toJSONString(map);
				response.getWriter().write(jsonString);
				}
	}

	public void loginOut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("进来了");
			request.getSession().invalidate();
			System.out.println(request.getSession());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("flag", false);
			System.out.println(map.get("put"));
			response.sendRedirect("index.html");
/*			String jsonString = JSON.toJSONString(map);
			response.getWriter().write(jsonString);*/
}

}
