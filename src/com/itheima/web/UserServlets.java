package com.itheima.web;

import com.alibaba.fastjson.JSON;
import com.itheima.domain.ResultInfo;
import com.itheima.domain.User;
import com.itheima.service.UserService;
import com.itheima.utils.BeanFactory;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * Servlet implementation class UserServlet
 */
public class UserServlets extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private UserService service = (UserService) BeanFactory.getBean("UserService");

	// 用户注册功能
	public void getLoginUserData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	// 用户激活
	public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ResultInfo resultInfo = new ResultInfo();
		Boolean flag = true;
		try {
			String code = request.getParameter("code");

			User user = service.active(code);
			if (user == null) {
				// 用户已激活或者激活码错误
				resultInfo.setFlag(flag);
				resultInfo.setErrorMsg("用户已激活或者激活码错误");
			} else {
				// 激活成功，跳转到登录页面
				response.sendRedirect(request.getContextPath() + "/login.html");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.getWriter().print(JSON.toJSONString(resultInfo));

	}

	// 用户注册功能
	public void register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ResultInfo resultInfo = new ResultInfo();
		Boolean flag = true;
		try {
			// 获取页面传来的数据
			Map<String, String[]> map = request.getParameterMap();
			// 将获取的数据封装到user实体中
			User user = new User();
			BeanUtils.populate(user, map);
			// 从session中获取自动生成的验证码
			String session_code = (String) request.getSession().getAttribute("code");
			// 如果生成的验证码与输入的验证码一致，则执行注册操作
			if (user.getCode().equals(session_code)) {
				// 判断用户是否已经注册，如果用户名重复，则无法注册，显示用户名重复
				// System.out.println(1111);

				User existUser = service.findUserByUser(user);
				System.out.println("+++" + user);
				// 用户已存在，无法注册
				if (existUser != null) {
					flag = false;
					resultInfo.setErrorMsg("该用户名已被占用");
					// return;
				} else {
					// 用户不存在，可以注册,开始注册功能的开发
					service.register(user);
					// System.out.println(1);
					// response.sendRedirect("register_ok.html");
					resultInfo.setFlag(flag);
				}
			
			}else {
				resultInfo.setFlag(false);
				resultInfo.setErrorMsg("验证码有误");
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			resultInfo.setFlag(flag);
			resultInfo.setErrorMsg("信息有误");
		}
		response.getWriter().print(JSON.toJSONString(resultInfo));
	}
}
