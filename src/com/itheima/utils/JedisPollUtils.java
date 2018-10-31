package com.itheima.utils;


import java.util.ResourceBundle;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPollUtils {
	
	private static JedisPoolConfig config;
	private static JedisPool pool;
	static{
		//ResourceBundle.getBundle(properties文件名)
		//ResourceBundle :Jdk提供给我们的专门解析properties文件的类
		ResourceBundle bundle = ResourceBundle.getBundle("jedis");
		//从配置文件中获取最大连接数量
		int maxTotal = Integer.parseInt(bundle.getString("maxTotal"));
		//从配置文件中获取最大空闲数量
		int maxIdle = Integer.parseInt(bundle.getString("maxIdle"));
		//从配置文件中获取端口号
		int port = Integer.parseInt(bundle.getString("port"));
		//从配置文件中获取连接路径
		String url = bundle.getString("url");
		//创建连接池配置对象
		config = new JedisPoolConfig();
		//设置最大连接熟练
		config.setMaxTotal(maxTotal);
		//设置最大空闲数量
		config.setMaxIdle(maxIdle);
		//创建连接池对象
		pool = new JedisPool(config,url,port);
		
	}
	
	
	// 从连接池中获取jedis对象
	public static Jedis getJedis(){
		return pool.getResource();
	}
	// 关闭连接
	public static void closeJedis(Jedis jedis){
		if(jedis!=null){
			jedis.close();
			
		}
	}
}
