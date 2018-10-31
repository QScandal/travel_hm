package com.itheima.travel.dao;

import com.itheima.travel.domain.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao {
    List<Category> findAllCategory() throws SQLException;
}
