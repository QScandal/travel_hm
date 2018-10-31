package com.itheima.travel.web.servlet;

import com.itheima.travel.service.CategoryService;
import com.itheima.travel.service.impl.CategoryServiceImpl;
import com.itheima.travel.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class CategoryServlet extends BaseServlet {
    /**
     * 获取旅游项目所有的分类
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findAllCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //1.调用service层
            CategoryService service = new CategoryServiceImpl();
            String cateJList = service.findAllCategory();
            response.getWriter().write(cateJList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
