package com.itheima.travel.web.servlet;

import com.alibaba.fastjson.JSON;
import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.domain.Route;
import com.itheima.travel.service.RouteService;
import com.itheima.travel.service.impl.RouteServiceImpl;
import com.itheima.travel.web.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class RouteServlet extends BaseServlet {


    /**
     * 查找地图功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findMapByRid(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        try {
            //1.获取请求携带的路线的id
            int rid = Integer.parseInt(request.getParameter("rid"));
            //2.调用service层
            RouteService service = new RouteServiceImpl();
            resultInfo = service.findRouteByRid(rid);
            //没有出现异常
            resultInfo.setFlag(true);
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("非常抱歉,该功能正在维护中,请稍后再来...");
        }
        //返回响应
        response.getWriter().write(JSON.toJSONString(resultInfo));
    }


    /**
     * 查询历史记录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findHistoryRoutList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ResultInfo resultInfo = new ResultInfo();
        try {
            //历史记录
            List<Route> historyRouteList = new ArrayList<>();
            //a.获取cookies
            String rids = "";
            Cookie[] cookies = request.getCookies();
            //b.判断cookies是否存在
            if (cookies != null) {
                //存在,判断cookies中是否有rids的cookies
                for (Cookie cookie : cookies) {
                    if ("rids".equals(cookie.getName())) {
                        //获取rids的值
                        rids = cookie.getValue();
                        //格式化字符串rids
                        String[] split = rids.split("-");
                        //去除每个rid,查找该商品
                        RouteService service = new RouteServiceImpl();
                        for (String ridstr : split) {
                            Route route = (Route) service.findRouteByRid(Integer.parseInt(ridstr)).getData();
                            //将cookies中rid所属的商品查询出来
                            historyRouteList.add(route);
                        }
                    }
                }

            }
            resultInfo.setData(historyRouteList);
            resultInfo.setFlag(true);
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("非常抱歉,该功能正在维护中,请稍后再来.....");
        }
        response.getWriter().write(JSON.toJSONString(resultInfo));

    }

    /**
     * 根据旅游类别查询旅游路线列表
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findRouteListByCid(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        try {
            int curPage = 1;
            //1.获取请求携带的参数
            int cid = Integer.parseInt(request.getParameter("cid"));
            //当前页码
            String pageNumber = request.getParameter("curPage");
            if (pageNumber == null || pageNumber.length()<=0) {
                curPage = 1;
            } else {
                curPage = Integer.parseInt(pageNumber);
            }
            /*======================================================*/
            String rname = request.getParameter("rname");
            if (rname == "" || rname == null || rname.length() <= 0) {
                rname = "";
            }

            //2.设置每页的尺寸
            int pageSize = 8;
            //3.调用service层
            RouteService service = new RouteServiceImpl();
            resultInfo = service.findRouteListByCid(cid, rname, curPage, pageSize);
            resultInfo.setFlag(true);
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("非常抱歉,该功能正在维护中,请稍后再来...");
        }
        response.getWriter().write(JSON.toJSONString(resultInfo));
    }
    /**
     * 根据旅游路线的id查询详细信息
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findRouteByRid(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ResultInfo resultInfo = new ResultInfo();
        try {
            //1.获取请求携带的路线的id
            int rid = Integer.parseInt(request.getParameter("rid"));
            //2.调用service层
            RouteService service = new RouteServiceImpl();
             resultInfo = service.findRouteByRid(rid);
            //没有出现异常
            resultInfo.setFlag(true);

            /**
             * 将历史查询结构存储在cookies中===============================================
             */
            String rids = rid+"";
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                //判断是否存在rids的cookies
                for (Cookie cookie : cookies) {
                    if ("rids".equals(cookie.getName())) {
                        //存在
                        rids = cookie.getValue();
                        //将rid转为集合类型
                        String[] split = rids.split("-");
                        List<String> list = Arrays.asList(split);
                        //将其转化为linkedlist,便于操作
                        LinkedList<String> ridsList = new LinkedList<>(list);
                        //判断当前的rid是否已经浏览过
                        if (ridsList.contains(rid+"")) {
                            //浏览过,将之前浏览过的pid从集合中删除
                            ridsList.remove(rid+"");
                        }
                        //将新浏览的rid加到集合头部
                        ridsList.addFirst(rid+"");
                        //将ridsList转为2-3-4格式的字符串响应回去
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < ridsList.size(); i++) {
                            sb.append(ridsList.get(i));
                            sb.append("-");
                        }
                        //去掉最后一个"-"
                        rids = sb.substring(0, sb.length() - 1);
                    }
                }
            }
            Cookie newCookies = new Cookie("rids", rids);
            response.addCookie(newCookies);
        /*   ===========================================================    */


        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("非常抱歉,该功能正在维护中,请稍后再来...");
        }
        //返回响应
        response.getWriter().write(JSON.toJSONString(resultInfo));
    }

    /**
     * 查看黑马精选内容
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void routeCareChoose(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //1.调用service层
        RouteService service = new RouteServiceImpl();
        ResultInfo resulteInfo = service.routeCareChoose();
        //2.生成响应信息
        response.getWriter().write(JSON.toJSONString(resulteInfo));
    }




    /*==================================精确搜索=================================================*/
    /**
     * 查询关键字数据
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void selectKeyWords(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String keyWords = request.getParameter("keyWords");

            // 调用service层获取旅游对象集合数据
            RouteService service = new RouteServiceImpl();
            List<Route> list = service.selectKeyWords(keyWords);

            // 将数据转成json格式响应
            String jlist = JSON.toJSONString(list);
            response.getWriter().write(jlist);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 搜索框中获取历史搜索关键字记录 1.将数据放入到cookie中
     *
     * @throws Exception
     */

    public void historyKeyWords(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 获取关键词，解码
        String keyWords = request.getParameter("keyWords");
        //keyWords = new String(keyWords.getBytes("iso-8859-1"), "utf-8");
        System.out.println(keyWords);

        // 判断cookie中是否由数据
        Cookie[] cookies = request.getCookies();
        // 判断cookies中是否有数据，是否有words
        Cookie cookie = findCookieByName("words", cookies);

        if (cookie == null) {
            cookie = new Cookie("words", keyWords);
            cookie.setMaxAge(3600);
            cookie.setPath("/");
        } else {
            // 是，则拼接
            // 先获取其中的值
            String value = cookie.getValue();

            //将数据存放到cookie中时，判断其中是否已经存在，存在则删除原有的在插入，没有则直接插入
            //分割字符串时需要使用转义字符
            String[] split = value.split("\\+");
            List<String> list=Arrays.asList(split);//将数组转换为list集合
            List<String> list2 = new ArrayList<String>(list);
            if(list.contains(keyWords)){

                list2.remove(keyWords);
                //重新插入
            }
            Collections.reverse(list2);
            list2.add(keyWords);
            Collections.reverse(list2);
            //判断cookie中存储的数据条数，大于8条则删除最后一个
            if(list2.size()>=8){
                list2.remove(list2.size()-1);
            }
//			Collections.reverse(list2);
            //拼接字符串
            String str = org.apache.commons.lang3.StringUtils.join(list2.toArray(), "+");

            // 重置cookie中的值
            System.out.println(str);//*************************
            cookie.setValue(str);
            cookie.setMaxAge(3600);
            cookie.setPath("/");
        }
        response.addCookie(cookie);


        // 获取route的rid，转发至详情页面。
        String rid = request.getParameter("rid");
        response.sendRedirect(request.getContextPath() + ("/route_detail.html?rid=" + rid));

        /*//************************************

        int index = Integer.parseInt(request.getParameter("index"));
        if(index==0){
            // 获取route的rid，转发至详情页面。
            String rid = request.getParameter("rid");
            response.sendRedirect(request.getContextPath() + ("/route_detail.html?rid=" + rid));

        }
        if(index==1){
            //route_list.html?cid="+cid+"&rname="+rname+"&index=1
            String cid = request.getParameter("cid");
            String rname = request.getParameter("rname");

            response.sendRedirect(request.getContextPath() + ("/route_list.html?cid="+cid+"&rname="+rname));


        }*/

    }

    /**
     * 判断cookie中是否有记录
     */
    private Cookie findCookieByName(String name, Cookie[] cookies) {
        if (cookies != null && cookies.length != 0) {
            for (Cookie c : cookies) {

                if (name.equals(c.getName())) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * 搜索框中获取历史搜索关键字记录 2.将cookie的数据返回给页面展示
     *
     * @throws Exception
     */
    public void historyKeyWordsShow(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // 判断cookie中是否由数据
        Cookie[] cookies = request.getCookies();
        // 判断cookies中是否有数据，是否有words
        Cookie cookie = findCookieByName("words", cookies);

        //如果为空
        if(cookie==null){
            /*response.getWriter().print(null);;*/
            return;
        }
        String value = cookie.getValue();

        // 截取字符串，去除重复值，
        //分割字符串时需要使用转义字符
        String[] split = value.split("\\+");
        System.out.println(value);
        response.getWriter().print(value);

    }


    /*================================后台================================================*/
    /**
     * 添加产品
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addRoute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();

        try {
            //1.获取添加的商品信息
            int cid = 5;
            String cname = request.getParameter("cname");
            if ("全球自由行".equals(cname)) {
                cid = 8;
            }
            if ("国内游".equals(cname)) {
                cid = 5;
            }
            if ("出境游".equals(cname)) {
                cid = 4;
            }
            if ("抱团定制".equals(cname)) {
                cid = 7;
            }
            if ("港澳游".equals(cname)) {
                cid = 6;
            }
            if ("酒店".equals(cname)) {
                cid = 2;
            }
            if ("门票".equals(cname)) {
                cid = 1;
            }
            if ("香港车票".equals(cname)) {
                cid = 3;
            }

            String rname = request.getParameter("rname");
            String routeIntroduce = request.getParameter("routeIntroduce");
            double price = Double.parseDouble(request.getParameter("price"));
            String isThemeTour = request.getParameter("isThemeTour");
            String rimage = request.getParameter("rimage");
            //判断isThemeTour


            /*==================================================================*/
            /*String[] split = rimage.split("fakepath");
            rimage = "img\\product\\upload"+split[split.length-1];*/
            /*==================================================================*/


            //System.out.println(isThemeTour);
            int rtype = 1;
            if (cid == 5) {
                rtype = 1;
            } else if (cid == 4) {
                rtype = 2;
            }
            //2.调用service层
            RouteService service = new RouteServiceImpl();
            service.addRoute(rname, price, routeIntroduce, isThemeTour, cid, rimage, rtype);
            //返回true,添加成功
            resultInfo.setFlag(true);
        } catch (Exception e) {
            e.printStackTrace();
            //添加失败
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("非常抱歉,该功能正在维护中,请稍后再来...");
        }
        response.getWriter().print(JSON.toJSONString(resultInfo));

    }

    /**
     * 根据商家的id获取该商家发布的所有的商品路线信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findRouteListBySid(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //创建前台需要的数据传输对象data为PageBean
        ResultInfo resultInfo = new ResultInfo();
        try {
            //1.获取请求携带的sid参数和curPage
            //int sid = Integer.parseInt(request.getParameter("sid"));

            /*测试数据   最后要删除的*/
            int sid = 1;
            int curPage = 1;
            /*测试数据   最后要删除的*/

            try {
                curPage = Integer.parseInt(request.getParameter("curPage"));
            } catch (Exception e) {
                curPage = 1;
            }
            //设置每页的展示商品的条数
            int pageSize = 8;


            //2.调用service层处理业务逻辑
            RouteService service = new RouteServiceImpl();
            resultInfo = service.findRouteListBySid(sid, curPage, pageSize);
            resultInfo.setFlag(true);
        } catch (Exception e) {
            e.printStackTrace();
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("非常抱歉,该功能正在维护中,请稍后再来....");
        }
        //3.将resultInfo返回给前台
        response.getWriter().print(JSON.toJSONString(resultInfo));
    }
    /*================================后台================================================*/

}
