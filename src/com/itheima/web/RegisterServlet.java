package com.itheima.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.itheima.domain.PageBean;
import com.itheima.domain.ResultInfo;
import com.itheima.domain.User;
import com.itheima.service.RegisterService;
import com.itheima.service.impl.RegisterServiceImpl;

public class RegisterServlet extends BaseServlet {

	public void register(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("进user了");
		Map<String, String[]> userMap = request.getParameterMap();
		User user = new User();
		ResultInfo rf = null;
		try {
			BeanUtils.populate(user, userMap);
			RegisterService service = new RegisterServiceImpl();
			System.out.println("账号="+user.getUsername());
			User exisUser = service.findUser(user);
			if(exisUser != null){
				rf.setErrorMsg("该账号已被注册");
				rf.setFlag(false);
				System.out.println("该账号已被注册");
			}
			rf = service.register(user);
			System.out.println("成功了");
			response.getWriter().print(JSON.toJSONString(rf));
		} catch (Exception e) {
			System.out.println("失败了");
			
			rf.setFlag(false);
			rf.setErrorMsg("该功能正在维护");
			e.printStackTrace();
		}
	}


}
