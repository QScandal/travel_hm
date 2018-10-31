package com.itheima.travel.web.filter;

import com.itheima.travel.utils.JedisPoolUtils;
import com.itheima.travel.utils.MD5Util;
import redis.clients.jedis.Jedis;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Servlet Filter implementation class VerifyFilter
 */
public class VerifyFilter implements Filter {

    /**
     * Default constructor. 
     */
    public VerifyFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		//向下转型
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		//获取user
		Object user = req.getSession().getAttribute("user");
		if (user == null) {
			//在cookie中查找token
			Cookie[] cookies = req.getCookies();
			if(cookies != null){
				for(Cookie cookie : cookies) {
					//如果有token则自动登录
					if("token".equals(cookie.getName())){
						Jedis jedis = JedisPoolUtils.getJedis();
						String userJson = jedis.get(cookie.getValue());
						jedis.del(cookie.getValue());
						req.getSession().setAttribute("user", userJson);
						
						//更新redis
						String key = MD5Util.MD5Encode(UUID.randomUUID().toString(), "utf-8");
						jedis.set(key, userJson);
						JedisPoolUtils.closeJedis(jedis);
						
						//更新cookie
						Cookie newCookie = new Cookie("token", key);
						newCookie.setMaxAge(3600*24*10);
						newCookie.setPath("/");
						res.addCookie(newCookie);
						System.out.println("自动登录完成");
						break;
					}
				}
			}
		}
		
		chain.doFilter(req, res);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
