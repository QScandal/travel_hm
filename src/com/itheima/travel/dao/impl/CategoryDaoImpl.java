package com.itheima.travel.dao.impl;

import com.itheima.travel.dao.CategoryDao;
import com.itheima.travel.domain.Category;
import com.itheima.travel.utils.C3P0Utils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

public class CategoryDaoImpl implements CategoryDao {

    @Override
    /**
     * 查询所有的商品分类信息
     */
    public List<Category> findAllCategory() throws SQLException {
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select * from tab_category";
        return qr.query(sql, new BeanListHandler<>(Category.class));
    }
}
