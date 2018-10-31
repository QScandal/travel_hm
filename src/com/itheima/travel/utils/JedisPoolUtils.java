package com.itheima.travel.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

public class JedisPoolUtils {

	private static JedisPoolConfig config;
	private static JedisPool pool;
	static{
		// 解析jedis的配置文件(jedis.properties)
		// ResourceBundle jdk给我们提供的专门解析properties文件的类
		ResourceBundle bundle = ResourceBundle.getBundle("jedis");
		// 创建连接池配置信息对象
		config = new JedisPoolConfig();
		//设置最大连接数量
		int maxTotal = Integer.parseInt(bundle.getString("maxTotal"));
		config.setMaxTotal(maxTotal);
		//设置最大空闲数量
		int maxIdle = Integer.parseInt(bundle.getString("maxIdle"));
		config.setMaxIdle(maxIdle);
		// 创建连接池对象
		String url = bundle.getString("url");
		int port = Integer.parseInt(bundle.getString("port"));
		pool = new JedisPool(config,url,port);
		
	}
	
	
	// 从连接池中获取jedis对象
	public static Jedis getJedis(){
		return pool.getResource();
	}
	// 关闭jedis连接
	public static void closeJedis(Jedis jedis){
		if(jedis!=null){
			jedis.close();
		}
	}
}
