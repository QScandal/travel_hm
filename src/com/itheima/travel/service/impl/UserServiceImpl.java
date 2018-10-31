package com.itheima.travel.service.impl;

import com.alibaba.fastjson.JSON;
import com.itheima.travel.dao.Userdao;
import com.itheima.travel.dao.impl.UserdaoImpl;
import com.itheima.travel.domain.User;
import com.itheima.travel.service.UserService;
import com.itheima.travel.utils.C3P0Utils;
import com.itheima.travel.utils.MailUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserServiceImpl implements UserService {
    //用户注册
    @Override
    public boolean register(User user) throws Exception {
        //创建dao层对象来添加数据
        boolean flag = false;
        //开启事务
        try {
            C3P0Utils.startTransaction();
            Userdao userdao = new UserdaoImpl();
            userdao.register(user);
            String message = "<a href='http://192.168.37.31:8888/travelTest/userServlet?method=active&&code=" + user.getCode() + "'>请点击激活</a>";
            MailUtils.sendMail(user.getEmail(), "java8组", message);

            flag = true;
            //提交事务
            C3P0Utils.commitAndClose();
        } catch (Exception e) {
            //事物回滚
            C3P0Utils.rollbackAndClose();
            e.printStackTrace();
        }
        return flag;
    }

    //检验用户名
    @Override
    public String inspectUser(String username) throws SQLException {
        //创建map集合来存放信息
        Map<String, String> map = new HashMap<String, String>();
        Userdao userdao = new UserdaoImpl();
        //创建dao层对象来查询数据库
        User user = userdao.inspectUser(username);
        if (user == null) {
            map.put("exist", "yes");
        } else {
            map.put("exist", "no");
        }
        //将集合转成json对象
        String json = JSON.toJSONString(map);
        return json;
    }

    //用户激活
    @Override
    public User active(String code) throws Exception {
        //创建dao层对象完成查找用户
        Userdao userdao = new UserdaoImpl();
        User user = userdao.findUserBycode(code);
        if (user != null) {
            userdao.active(user);
        }
        return user;
    }

    //检验邮箱
    @Override
    public String inspectEmail(String email) throws SQLException {
        //创建map集合来存放信息
        Map<String, String> map = new HashMap<String, String>();
        Userdao userdao = new UserdaoImpl();
        //创建dao层对象来查询数据库信息
        User user = userdao.inspectEmail(email);
        if (user == null) {
            map.put("emailexist", "yes");
        } else {
            map.put("emailexist", "no");
        }
        //将集合转成json
        String json = JSON.toJSONString(map);
        return json;
    }


    @Override
    /**
     * 登录
     */
    public User login(String username, String password) throws SQLException {
        Userdao userDao = new UserdaoImpl();
        return userDao.findUserByUsernameAndPassword(username, password);
    }

}
