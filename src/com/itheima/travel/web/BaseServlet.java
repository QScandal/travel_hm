package com.itheima.travel.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 处理所有的判断
 * 直接反射让指定方法执行
 */
public class BaseServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        try {
            //1.获取请求标识
            String methodName = request.getParameter("method");
            //2.获取当前执行类的字节码对象
            Class clazz = this.getClass();
            //3.获取指定方法的字节码对象    获取public修饰的方法字节码对象
            Method method = clazz.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //4.让方法执行
            method.invoke(this, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

}
