package com.itheima.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.service.CategoryService;
import com.itheima.utils.BeanFactory;

public class CategoryServlet extends BaseServlet {

	public void findAllCategory(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			//System.out.println("jinfenleile");
		try {
			//1.接受请求
			//2.调用service完成查询所有分类信息的业务处理
			//CategoryService service = new CategoryServiceImpl();
			//使用工厂解耦合
			CategoryService service =(CategoryService)BeanFactory.getBean("CategoryService");
			String jlist = service.findCateAll();
			//System.out.println(jlist);
			//3.生成响应结果信息
			response.getWriter().print(jlist);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
