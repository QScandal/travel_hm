package com.itheima.travel.service.impl;

import com.alibaba.fastjson.JSON;
import com.itheima.travel.dao.CategoryDao;
import com.itheima.travel.dao.impl.CategoryDaoImpl;
import com.itheima.travel.domain.Category;
import com.itheima.travel.service.CategoryService;
import com.itheima.travel.utils.Constant;
import com.itheima.travel.utils.JedisPoolUtils;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    @Override
    /**
     * 查询旅游项目所有的分类信息
     */
    public String findAllCategory() throws SQLException {
        // 创建jedis对象
        Jedis jedis = JedisPoolUtils.getJedis();
        // 查询redis数据库
        String jlist = jedis.get(Constant.CATELISTJSON);
        // 判断是否jedis中的数据为空
        if(jlist==null){
            // 查询mysql数据库,并将查询结果放入redis库中
            CategoryDao dao = new CategoryDaoImpl();
            //调用dao查询所有分类信息
            //将查询结果list转成json返回给web层
            List<Category> list = dao.findAllCategory();
            jlist = JSON.toJSONString(list);
            // 查询结果放入redis库中
            jedis.set(Constant.CATELISTJSON, jlist);
        }
        // 关闭jedis连接
        JedisPoolUtils.closeJedis(jedis);
        return jlist;

    }
}
