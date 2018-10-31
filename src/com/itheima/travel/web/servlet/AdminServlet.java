package com.itheima.travel.web.servlet;

import com.alibaba.fastjson.JSON;
import com.itheima.travel.domain.*;
import com.itheima.travel.service.AdminService;
import com.itheima.travel.service.impl.AdminServiceImpl;
import com.itheima.travel.utils.PageBean;
import com.itheima.travel.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends BaseServlet {
    public void findAllRoute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String data = request.getParameter("curPage");
            int curPage;
            int pageSize = 5;
            try {
                curPage = Integer.parseInt(data);
            } catch (Exception e) {
                curPage = 1;
            }
            AdminService service = new AdminServiceImpl();
            PageBean<Route> pageBean = service.findAllRoute(curPage, pageSize);
            response.getWriter().print(JSON.toJSONString(pageBean));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void findAllCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            AdminService service = new AdminServiceImpl();
            List<Category> categorys = service.findAllCategory();
            response.getWriter().print(JSON.toJSONString(categorys));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void storeCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cname = request.getParameter("cname");
            AdminService service = new AdminServiceImpl();
            int update = service.storeCategory(cname);
            if (update == 1) {
                System.out.println(2);
                response.getWriter().print(1);
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            System.out.println(3);
            response.getWriter().print(2);
            e.printStackTrace();
        }
    }

    public void updateCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cid = request.getParameter("cid");
            String cname = request.getParameter("cname");
            AdminService service = new AdminServiceImpl();
            int update = service.updateCategory(cid, cname);
            if (update == 1) {
                response.getWriter().print(1);
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            response.getWriter().print(2);
            e.printStackTrace();
        }
    }

    public void deleteByCid(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String cid = request.getParameter("cid");
            AdminService service = new AdminServiceImpl();
            int update = service.deleteByCid(cid);
            if (update == 1) {
                response.getWriter().print(1);
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            response.getWriter().print(2);
            e.printStackTrace();
        }
    }

    public void login(HttpServletRequest request, HttpServletResponse response) //登录页面验证
            throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        try {
            String sname = request.getParameter("sname");
            String consphone = request.getParameter("consphone");
            System.out.println(sname + consphone);
            AdminService service = new AdminServiceImpl();
            Seller seller = service.login(sname, consphone);
            if (seller != null) {
                request.getSession().setAttribute("seller", seller);
                Cookie cookie = new Cookie("seller", "" + seller.getSid());
                cookie.setMaxAge(3600 * 24 * 30);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            resultInfo.setFlag(true);
            resultInfo.setData(seller);
            response.getWriter().print(JSON.toJSONString(resultInfo));
        } catch (Exception e) {
            resultInfo.setFlag(false);
            response.getWriter().print(JSON.toJSONString(resultInfo));
            e.printStackTrace();
        }
    }

    public void loginStatus(HttpServletRequest request, HttpServletResponse response) //检测是否登录
            throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        try {
            Object attribute = request.getSession().getAttribute("seller");
            Seller seller = null;
            if (attribute != null) {
                seller = (Seller) attribute;
            }
            resultInfo.setFlag(true);
            resultInfo.setData(seller);
            response.getWriter().print(JSON.toJSONString(resultInfo));
        } catch (Exception e) {
            resultInfo.setFlag(false);
            response.getWriter().print(JSON.toJSONString(resultInfo));
            e.printStackTrace();
        }
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) //登出
            throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        try {
            request.getSession().setAttribute("seller", null);
            resultInfo.setFlag(true);
            response.getWriter().print(JSON.toJSONString(resultInfo));
        } catch (Exception e) {
            resultInfo.setFlag(false);
            response.getWriter().print(JSON.toJSONString(resultInfo));
            e.printStackTrace();
        }
    }


    public void findUser(HttpServletRequest request, HttpServletResponse response) //查询前台用户
            throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        try {
            AdminService service = new AdminServiceImpl();    //修改
            List<User> users = service.findUser();
            resultInfo.setFlag(true);
            resultInfo.setData(users);
            System.out.println(JSON.toJSONString(resultInfo));
            response.getWriter().print(JSON.toJSONString(resultInfo));
        } catch (Exception e) {
            resultInfo.setFlag(false);
            e.printStackTrace();
            response.getWriter().print(JSON.toJSONString(resultInfo));
        }
    }

    public void forbideUser(HttpServletRequest request, HttpServletResponse response) //查询前台用户
            throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        try {
            String uid = request.getParameter("uid");
            AdminService service = new AdminServiceImpl();
            int update = service.forbideUser(uid);
            if (update == 1) {
                resultInfo.setFlag(true);
                response.getWriter().print(JSON.toJSONString(resultInfo));
            } else {
                resultInfo.setFlag(false);
                response.getWriter().print(JSON.toJSONString(resultInfo));
            }
        } catch (Exception e) {
            resultInfo.setFlag(false);
            response.getWriter().print(JSON.toJSONString(resultInfo));
            e.printStackTrace();
        }
    }


}
