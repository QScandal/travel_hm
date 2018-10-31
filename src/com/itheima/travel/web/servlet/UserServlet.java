package com.itheima.travel.web.servlet;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.itheima.travel.domain.ResultInfo;
import com.itheima.travel.domain.User;
import com.itheima.travel.service.UserService;
import com.itheima.travel.service.impl.UserServiceImpl;
import com.itheima.travel.utils.Constant;
import com.itheima.travel.utils.JedisPoolUtils;
import com.itheima.travel.utils.MD5Util;
import com.itheima.travel.utils.UUIDUtils;
import com.itheima.travel.web.BaseServlet;
import org.apache.commons.beanutils.BeanUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends BaseServlet {

    //退出登录
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //销毁session
        request.getSession().invalidate();
        //获取cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                //如果有token则从redis删除
                if ("token".equals(cookie.getName())) {
                    Jedis jedis = JedisPoolUtils.getJedis();
                    jedis.del(cookie.getValue());
                    //删除cookie
                    Cookie newCookie = new Cookie("token", cookie.getValue());
                    newCookie.setMaxAge(0);
                    newCookie.setPath("/");
                    response.addCookie(newCookie);
                }
            }
        }
        response.sendRedirect("index.html");
    }


    // 用户注册
    public void Userregister(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //request.setCharacterEncoding("utf-8");

        ResultInfo resultInfo = new ResultInfo();
        // 获得验证码
        String checkcode = request.getParameter("check");
        // 获得session中的验证码
        String code_session = (String) request.getSession().getAttribute("code_session");
        // 获得用户提交的所有数据，将数据放入实体类中
        Map<String, String[]> map = request.getParameterMap();
        User user = new User();
        BeanUtils.populate(user, map);
        if (checkcode.length() > 0) {

            if (code_session.equalsIgnoreCase(checkcode)) {
                // 创建实体类来封装数据
                // 对密码进行处理
                user.setPassword(MD5Util.MD5Encode(user.getPassword(), "utf-8"));
                // 设置状态为未激活
                user.setStatus(Constant.nonactivated);
                // 设置用户的激活码
                String code = UUIDUtils.getId();
                user.setCode(code);
                // 创建Service层对象来注册
                UserService userservice = new UserServiceImpl();
                boolean flag = userservice.register(user);
                //
                resultInfo = new ResultInfo();
                if (flag == true) {
                    resultInfo.setFlag(flag);
                } else {
                    String err = "系统维护，注册失败";
                    resultInfo.setFlag(false);
                    resultInfo.setErrorMsg(err);
                }
                String json = JSON.toJSONString(resultInfo);
                // 返回结果
                response.getWriter().write(json);
            } else {
                resultInfo.setErrorMsg("验证码错误");
                String json = JSON.toJSONString(resultInfo);
                response.getWriter().write(json);
            }
        } else {
            resultInfo.setErrorMsg("验证码为空");
            String json = JSON.toJSONString(resultInfo);
            response.getWriter().write(json);
        }
    }

    // 检验验证码
    public void inspectCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, String> map = new HashMap<String, String>();
            // 获得用户输入的验证码
            String check = request.getParameter("check");
            //获得session 中的验证码
            String code_session = (String) request.getSession().getAttribute("code_session");
            if (code_session.equalsIgnoreCase(check)) {
                map.put("codeexist", "yes");
            } else {
                map.put("codeexist", "no");
            }
            String json = JSON.toJSONString(map);
            response.getWriter().write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 检验用户
    public void inspectUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 获得用户名
            String username = request.getParameter("username");
            // 创建service 层对象
            UserService userservice = new UserServiceImpl();
            String json = userservice.inspectUser(username);
            response.getWriter().write(json);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //用户激活
    public void active(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        //首先获得用户的激活码
        try {
            String code = request.getParameter("code");
            //创建service对象
            UserService userservice = new UserServiceImpl();
            User user = userservice.active(code);
            if (user == null) {
                //说明用户已经激活或者激活码错误
                request.getRequestDispatcher("/activedefault.html").forward(request, response);
            } else {
                //激活成功，跳转到登录界面
               // request.setAttribute("msg", "激活成功，三秒后进入登录界面");
                //定时刷新，三秒后跳转到响应的登录既然界面
                response.setHeader("refresh", "3;url=" + request.getContextPath() + "/login.html");
            }
            request.getRequestDispatcher("/activesuccess.html").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();


        }
    }

    //校验邮箱
    public void inspectEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // 获得用户邮箱
            String email = request.getParameter("email");
            // 创建service 层对象完成校验
            UserService userservice = new UserServiceImpl();
            String json = userservice.inspectEmail(email);
            response.getWriter().write(json);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //=======================================================================//
    //============================登录功能====================================//

    public void getLoginUserData(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Object object = request.getSession().getAttribute("user");
        if (object != null) {
            String user = (String) object;
            response.getWriter().print("{\"flag\" : true,\"data\":" + user + "}");
        } else {
            response.getWriter().print("{\"flag\" : false}");
        }

    }

    /**
     * 用户登录功能
     *
     * @param request
     * @param response
     */
    public void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("收到post请求");
        System.out.println(request.getParameterMap());
        System.out.println(request.getParameter("autoLogin"));
        //编码
        response.setContentType("application/json;charset=utf-8");
        // 获取参数
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String autoLogin = request.getParameter("autoLogin");
        String checkcode = request.getParameter("check");
        String code_session = (String) request.getSession().getAttribute("code_session");


        try {

            String jsonData = "";
            if (!code_session.equalsIgnoreCase(checkcode)) {
                jsonData = "{\"flag\":false,\"errorMsg\":\"验证码错误\"}";
                response.getWriter().print(jsonData);
                return;
            }
            // 校验
            UserService userService = new UserServiceImpl();
            User user = userService.login(username, password);
            //将user转化为json
            jsonData = JSON.toJSONString(user);
            System.out.println(user);
            //返回json

            if (user == null) {
                jsonData = "{\"flag\":false,\"errorMsg\":\"用户名或密码错误\"}";
                System.out.println("==========" + jsonData);
                response.getWriter().print(jsonData);
            } else if ("N".equals(user.getStatus())) {
                jsonData = "{\"flag\":false,\"errorMsg\":\"用户未激活\"}";
                response.getWriter().print(jsonData);
            } else if ("Y".equals(user.getStatus())) {
                //登录成功
                //存入session
                request.getSession().setAttribute("user", jsonData);
                if ("autoLogin".equals(autoLogin)) {
                    //存入redis
                    Jedis jedis = JedisPoolUtils.getJedis();
                    String key = MD5Util.MD5Encode(UUID.randomUUID().toString(), "utf-8");
                    jedis.set(key, jsonData);
                    JedisPoolUtils.closeJedis(jedis);
                    //增加cookie
                    Cookie newCookie = new Cookie("token", key);
                    newCookie.setMaxAge(3600 * 24 * 10);
                    newCookie.setPath("/");
                    response.addCookie(newCookie);
                }
                jsonData = "{\"flag\":true}";
                System.out.println(user);
                response.getWriter().print(jsonData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*=================================短信验证=====================================*/
    //发送手机验证码
    public void sendMsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String iphone = request.getParameter("iphone");
        Random random = new Random();
        int nextInt = random.nextInt(900000)+100000;
        System.out.println(nextInt);
        String code = nextInt+"";
        //将验证码存入session
        HttpSession session = request.getSession();
        session.setAttribute("phonecode", code);
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
        final String accessKeyId = "LTAIWmANA2HOfWNw";//你的accessKeyId,参考本文档步骤2
        final String accessKeySecret = "63rgPeEGXgXT3qyJsOgxbyjFEaAjG8";//你的accessKeySecret，参考本文档步骤2
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,
                accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request1 = new SendSmsRequest();
        //使用post提交
        request1.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
        request1.setPhoneNumbers(iphone);
        //必填:短信签名-可在短信控制台中找到
        request1.setSignName("XX旅游网");
        //必填:短信模板-可在短信控制台中找到
        request1.setTemplateCode("");/*自己的手机验证账号*/
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request1.setTemplateParam("{\"name\":\"Tom\", \"code\":\""+code+"\"}");
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        request1.setOutId("yourOutId");
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request1);
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("isv.AMOUNT_NOT_ENOUGH")) {

            System.out.println("账户余额不足");
        }
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {

            System.out.println("发送成功!");
            response.getWriter().write("true");
        }
    }

    // 检验手机验证码
    public void inspectphoneCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            Map<String, String> map = new HashMap<String, String>();
            // 获得用户输入的验证码
            String uphonecode = request.getParameter("uphonecode");
            //获得session 中的验证码
            String  uphonecode_session=(String) request.getSession().getAttribute("phonecode");
            if(uphonecode_session.equalsIgnoreCase(uphonecode))
            {
                map.put("upcodeexist","yes");
            }else
            {
                map.put("upcodeexist", "no");
            }
            String json =JSON.toJSONString(map);
            response.getWriter().write(json);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
