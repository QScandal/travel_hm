package com.itheima.travel.web.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itheima.travel.domain.Favorite;
import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.domain.User;
import com.itheima.travel.service.FavoriteService;
import com.itheima.travel.service.impl.FavoriteServiceImpl;
import com.itheima.travel.utils.PageBean;
import com.itheima.travel.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class FavoriteServlet extends BaseServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 查询我的收藏（favorite表），并分页显示
     * <p>
     * ------张耀
     */
    public void findFavoriteByPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //定义结果信息对象
        ResultInfo resultInfo = new ResultInfo();
        //后端返回结果正常为true，发生异常返回false
        boolean flag = true;
        //发生异常的错误消息
        String errorMsg = "";
        try {

            /**
             * 需要修改
             *
             //从当前session中获取user对象，向后台传送uid
             //需要查看登陆代码中的”user“
             */
            HttpSession session = request.getSession();
            String  str = (String)session.getAttribute("user");


           // System.out.println(str);
            JSONObject jsonObject = JSON.parseObject(str);

            User user = new User();
            user.setUid(jsonObject.getIntValue("uid"));
            //System.out.println(user.getUid());

            //需要修改
            //User user = new User(1, "qqq", "www", "qqq", "qqqq", "1", "1999999", "7879879", "0", "000");
            //获取当前页码，
            int curPage = 1;
            try {
                curPage = Integer.parseInt(request.getParameter("curPage"));
            } catch (Exception e) {
                curPage = 1;
            }
            //每页多少条
            int pageSize = 12;
            //判断user是否为空
			
		/*	需要修改
         */
		  if(user==null){
				flag = false;
				errorMsg = "请先登陆！";
				resultInfo.setFlag(flag);
				resultInfo.setErrorMsg(errorMsg);
			}else{
            //调用service层
            FavoriteService service = new FavoriteServiceImpl();
            PageBean<Favorite> pb = service.findFavoriteByPage(user, curPage, pageSize);
            //将PageBean对象封装到resultInfo中
            resultInfo.setFlag(flag);
            resultInfo.setData(pb);
			}
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            errorMsg = "非常抱歉,该功能正在维护中,请稍后再来...";
            resultInfo.setFlag(flag);
            resultInfo.setErrorMsg(errorMsg);
        }
        //响应数据
        response.getWriter().write(JSON.toJSONString(resultInfo));
    }



    /*========================================收藏 朱昊===========================================================*/

    /**
     * 判断该页面产品是否被收藏
     */
    public void isFavoriteByRid(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ResultInfo resultInfo = new ResultInfo();
        try {
            //System.out.println("123456");

            int rid = Integer.parseInt(request.getParameter("rid"));
            String  str = (String) request.getSession().getAttribute("user");
            JSONObject jsonObject = JSON.parseObject(str);
            //int uid = jsonObject.getIntValue("uid");
            Integer integer = null;
            FavoriteService service = new FavoriteServiceImpl();
            int count = service.findCountByRid(rid);
            //抓取未登录时获取uid异常
            try {
                integer = jsonObject.getInteger("uid");
                //用户登录后
                int uid = integer.intValue();
                //int rid = 2;//测试用
                //int uid = 1;//测试用

                int isfav = service.isFavoriteByRid(rid,uid);
                //System.out.println(isfav);
                if(isfav>0){		//该用户已经收藏
                    resultInfo.setFlag(true);//查询成功
                    resultInfo.setData(true);
                    resultInfo.setErrorMsg(count+"");		//封装不合适，考虑用errorMsg携带收藏次数传回页面
                } else {			//该用户没有收藏
                    resultInfo.setFlag(true);//查询成功
                    resultInfo.setData(false);
                    resultInfo.setErrorMsg(count+"");		//封装不合适，考虑用errorMsg携带收藏次数传回页面
                }
            } catch (Exception e) {
                //System.out.println("空指针");
                //System.out.println("用户没有登录");
                resultInfo.setFlag(true);		//查询成功
                resultInfo.setData(false);		//用户没有登录时，默认显示未收藏效果
                resultInfo.setErrorMsg(count+"");		//封装不合适，考虑用errorMsg携带收藏次数传回页面
                String jstr = JSON.toJSONString(resultInfo);
                response.getWriter().write(jstr);
                // System.out.println("空指针返回数据1111");
                return;
            }

            //返回页面
            String jstr = JSON.toJSONString(resultInfo);
            response.getWriter().write(jstr);
        } catch (Exception e) {
            //e.printStackTrace();
            resultInfo.setFlag(false);//查询失败，运行错误
            resultInfo.setData(false);		//默认显示未收藏效果
            resultInfo.setErrorMsg("服务器在维护，请稍后再试~~~~");
            String jstr = JSON.toJSONString(resultInfo);
            response.getWriter().write(jstr);
        }

    }

    /**
     * 取消收藏方法
     */
    public void delFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResultInfo resultInfo = new ResultInfo();

        try {
            //int rid = Integer.parseInt(request.getParameter("rid"));
            //User user = (User) request.getSession().getAttribute("user");
            //int uid = user.getUid();
            //测试用
            //int rid = 2;	//测试用
            //int uid = 1;	//测试用
            int rid = Integer.parseInt(request.getParameter("rid"));
            String  str = (String) request.getSession().getAttribute("user");
            JSONObject jsonObject = JSON.parseObject(str);
            int uid = jsonObject.getIntValue("uid");
            FavoriteService service = new FavoriteServiceImpl();

            //返回商品被收藏次数
            service.delFavorite(rid,uid);
            int count = service.findCountByRid(rid);

            resultInfo.setFlag(true);
            resultInfo.setData(count);

            String jstr = JSON.toJSONString(resultInfo);
            response.getWriter().write(jstr);
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("服务器正忙，请稍后再试~~~~");

            String jstr = JSON.toJSONString(resultInfo);
            response.getWriter().write(jstr);
        }
    }
    /**
     * 添加收藏方法
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResultInfo resultInfo = new ResultInfo();
        try {

            //int rid = Integer.parseInt(request.getParameter("rid"));
            //User user = (User) request.getSession().getAttribute("user");
            //int uid = user.getUid();
            //测试用
            //int rid = 2;	//测试用
            //int uid = 1;	//测试用
            int rid = Integer.parseInt(request.getParameter("rid"));
            String  str = (String) request.getSession().getAttribute("user");
            JSONObject jsonObject = JSON.parseObject(str);
            //int uid = jsonObject.getIntValue("uid");
            FavoriteService service = new FavoriteServiceImpl();
            Integer integer = null;
            //抓取未登录时获取uid异常
            try {
                integer = jsonObject.getInteger("uid");
                int uid = integer.intValue();
                //调取service层获取收藏次数
                service.addFavoriteByUid(rid,uid);	//添加收藏

                resultInfo.setFlag(true);			//执行成功标志
            } catch (Exception e) {
                resultInfo.setFlag(false);			//执行失败标志
                resultInfo.setErrorMsg("您还没有登录,请先登录...");
                e.printStackTrace();
            }

            int count = service.findCountByRid(rid);	//获取收藏数据
            resultInfo.setData(count);
            String jstr = JSON.toJSONString(resultInfo);
            response.getWriter().write(jstr);
        } catch (Exception e) {
            //e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("服务器正忙，请稍后再试~~~~");

            String jstr = JSON.toJSONString(resultInfo);
            response.getWriter().write(jstr);
        }

    }


    /*===================收藏排行榜=====================*/
    /**
     * 查询收藏排行榜
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findRoutesFavoriteRank(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        try {
            int curPage = 1;
            //1.获取请求携带的参数
            //当前页码
            String pageNumber = request.getParameter("curPage");
            if (pageNumber == null || pageNumber.length()<=0) {
                curPage = 1;
            } else {
                curPage = Integer.parseInt(pageNumber);
            }
            //获取当前rname2,startPrice,endPrice
            String rname = request.getParameter("rname");
            //System.out.println(rname);
            String startPriceStr = request.getParameter("startPrice");
            //System.out.println(startPriceStr);
            String endPriceStr = request.getParameter("endPrice");

            if (rname == null || rname.length() <= 0) {
                rname = "";
            }
            double startPrice;
            double endPrice;

            if (startPriceStr == null || startPriceStr.length() <= 0) {
                startPrice = Double.MIN_VALUE;
            } else {
                startPrice = Double.parseDouble(startPriceStr);
            }

            if (endPriceStr == null || endPriceStr.length() <= 0) {
                endPrice = Double.MAX_VALUE;
            } else {
                endPrice = Double.parseDouble(endPriceStr);

            }

            //2.设置每页的尺寸
            int pageSize = 8;
            //3.调用service层
            FavoriteService service = new FavoriteServiceImpl();
            resultInfo = service.findRoutesFavoriteRank(curPage, pageSize, rname, startPrice, endPrice);
            resultInfo.setFlag(true);
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("非常抱歉,该功能正在维护中,请稍后再来....");
        }
        response.getWriter().write(JSON.toJSONString(resultInfo));

    }
}
