package com.itheima.travel.service.impl;

import com.itheima.travel.dao.RouteDao;
import com.itheima.travel.dao.impl.RouteDaoImpl;
import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.domain.Route;
import com.itheima.travel.service.RouteService;
import com.itheima.travel.utils.PageBean;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RouteServiceImpl implements RouteService {

    @Override
    /**
     * 获取黑马精选内容
     */
    public ResultInfo routeCareChoose() {
        //1.创建ResultInfo对象
        ResultInfo resultInfo = new ResultInfo();
        try {
            //2.调用dao层
            RouteDao dao = new RouteDaoImpl();
            //查询出最热最新和主题的路线列表集合
            List<Route> popularList = dao.findPopularity();
            List<Route> newsList = dao.findNews();
            List<Route> themesList =  dao.findThemes();

            /*========================================================*/
            List<Route> mycountiesList = dao.findMycountries();
            List<Route> abroadList = dao.findAbroad();
            /*===========================================================*/

            //3.将查询到的数据封装到map中
            Map<String, List<Route>> map = new HashMap<>();
            map.put("popularity", popularList);
            map.put("news", newsList);
            map.put("themes", themesList);

            /*=========================================================*/
            map.put("mycountries", mycountiesList);
            map.put("abroad", abroadList);
            /*=========================================================*/

            //4.将map封装到result中
            resultInfo.setData(map);
            //没有发生错误
            resultInfo.setFlag(true);
        } catch (Exception e) {
            e.printStackTrace();
            //发生错误
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("非常抱歉,该功能正在维护");
        }

        return resultInfo;
    }

    @Override
    /**
     * 根据旅游路线id查询详细信息
     */
    public ResultInfo findRouteByRid(int rid) throws Exception {
        //创建ResultInfo对象
        ResultInfo resultInfo = new ResultInfo();
        //调用dao层
        RouteDao dao = new RouteDaoImpl();
        Route route = dao.findRouteByRid(rid);

        //封装resultInfo对象
        resultInfo.setData(route);
        return resultInfo;
    }

    @Override
    /**
     * 根据旅游类别查询旅游路线列表
     */
    public ResultInfo findRouteListByCid(int cid, String rname, int curPage, int pageSize) throws Exception {
        //1.创建resultInfo对象
        ResultInfo resultInfo = new ResultInfo();
        //2.查询总数量
        RouteDao dao = new RouteDaoImpl();
        int totalCount = dao.getTotalCount(cid);

        /*=====================================================*/
        rname = rname.replace(" ", "%");
        rname = "%"+rname+"%";
        /*============================================*/

        PageBean<Route> pageBean = new PageBean<>();
        pageBean.setCount(totalCount);
        pageBean.setPageSize(pageSize);
        pageBean.setCurPage(curPage);
        int startIndex = pageBean.getStartIndex();
        List<Route> routeList = dao.findRouteListByCid(cid, rname, startIndex, pageSize);
        pageBean.setData(routeList);

        //封装resultinfo
        resultInfo.setData(pageBean);
        return resultInfo;
    }

    @Override
    /**
     * 精确搜索
     */
    public List<Route> selectKeyWords(String keyWords) throws SQLException {

            //调用dao层
            RouteDao dao = new RouteDaoImpl();
            return dao.selectKeyWords(keyWords);
    }

    /*==============================后台=============================================*/
    @Override
    /**
     * 根据商家的id获取该商家发布的所有的商品路线信息
     */
    public ResultInfo findRouteListBySid(int sid, int curPage, int pageSize) throws Exception {
        //1.创建resultinfo对象
        ResultInfo resultInfo = new ResultInfo();

        //2.调用dao层,获取总数量
        RouteDao dao = new RouteDaoImpl();
        int count = dao.getTotalCountBySid(sid);

        //3.创建pageBean对象
        PageBean<Route> pageBean = new PageBean<>();
        pageBean.setCount(count);
        pageBean.setCurPage(curPage);
        pageBean.setPageSize(pageSize);
        //计算起始索引
        int startIndex = pageBean.getStartIndex();

        //4.调用dao层,获取routelist
        List<Route> routeList = dao.findRouteListBySid(sid, startIndex, pageSize);

        //5.将routelist封装到pageBean
        pageBean.setData(routeList);

        //6.将pageBean封装到resultInfo
        resultInfo.setData(pageBean);
        return resultInfo;
    }

    @Override
    /**
     * 添加旅游产品
     */
    public void addRoute(String rname, double price, String routeIntroduce, String isThemeTour, int cid, String rimage, int rtype) throws SQLException {
        //调用dao层
        RouteDao dao = new RouteDaoImpl();
        dao.addRoute(rname, price, routeIntroduce, isThemeTour, cid, rimage, rtype);
    }
    /*==============================后台=============================================*/
}
