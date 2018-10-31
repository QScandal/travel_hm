package com.itheima.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.itheima.domain.User;
import com.itheima.service.LoginService;
import com.itheima.utils.BeanFactory;

public class LoginServlet extends BaseServlet {

	public void login(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
				try {
					String username = request.getParameter("username");
					String password = request.getParameter("password");
					String autologin = request.getParameter("autologin");// 勾选自动登录,该值为on
					String input_code = request.getParameter("check");
					String session_code = (String) request.getSession().getAttribute("code");
					LoginService service = (LoginService) BeanFactory.getBean("Tab_UserService");
					User user=service.login(username,password);
					Map<String, Object> map = new 	HashMap<String,Object>();
					if(user==null){
						map.put("errorMsg","用户名或密码错误");
						String jString = JSON.toJSONString(map);
						response.getWriter().print(jString);
					}
					if(!"y".equalsIgnoreCase(user.getStatus())){
						map.put("errorMsg", "您的账户尚未激活,请去注册的邮箱激活...");
						String jString = JSON.toJSONString(map);
						response.getWriter().print(jString);
					}else if (!(session_code.equalsIgnoreCase(input_code))){
						map.put("errorMsg", "验证码错误");
						String jString = JSON.toJSONString(map);
						response.getWriter().print(jString);
					}else { //登录成功,直接跳转到...页面
						if(autologin!=null){
			                   Cookie cookie = new Cookie("user", username+"=="+password);  
			                   cookie.setMaxAge(1000*3600*24*10);                     
			                   response.addCookie(cookie);  
						}
						request.getSession().setAttribute("user", user);
						map.put("flag", true);
						map.put("username", username);
						String jString = JSON.toJSONString(map);
						response.getWriter().print(jString);
					}
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
	}

}
