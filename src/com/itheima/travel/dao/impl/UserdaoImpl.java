package com.itheima.travel.dao.impl;

import com.itheima.travel.dao.Userdao;
import com.itheima.travel.domain.User;
import com.itheima.travel.utils.C3P0Utils;
import com.itheima.travel.utils.Constant;
import com.itheima.travel.utils.MD5Util;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;

public class UserdaoImpl implements Userdao {

    //用户注册
    @Override
    public void register(User user) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "insert into tab_user values(?,?,?,?,?,?,?,?,?,?) ";
        runner.update(C3P0Utils.getConnection(),sql, null, user.getUsername(), user.getPassword(), user.getName(), user.getBirthday(), user.getSex(), user.getTelephone(), user.getEmail()
                , user.getStatus(), user.getCode());
    }

    //用户名校验
    @Override
    public User inspectUser(String username) throws SQLException {
        QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select *from tab_user where username=? ";
        return runner.query(sql, new BeanHandler<User>(User.class), username);
    }

    //通过激活码来查找用户
    @Override
    public User findUserBycode(String code) throws SQLException {
        QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select *from tab_user where code =?";
        return runner.query(sql, new BeanHandler<User>(User.class), code);
    }

    //激活用户
    @Override
    public void active(User user) throws SQLException {
        QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "update tab_user set status =? where uid=?";
        runner.update(sql, Constant.nactivated, user.getUid());
    }

    //检验邮箱
    @Override
    public User inspectEmail(String email) throws SQLException {
        QueryRunner runner = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select *from tab_user where email=? ";
        return runner.query(sql, new BeanHandler<User>(User.class), email);
    }


    @Override
    /**
     * 用户登录
     */
    public User findUserByUsernameAndPassword(String username, String password) throws SQLException {
        password = MD5Util.MD5Encode(password, "utf-8");
        QueryRunner qr = new QueryRunner(C3P0Utils.getDataSource());
        String sql = "select * from tab_user where username=? and password=? ";
        return qr.query(sql, new BeanHandler<User>(User.class), username, password);
    }

}
