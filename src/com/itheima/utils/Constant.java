package com.itheima.utils;

public interface Constant {

	//未激活
	int USER_NOT_ACTIVE = 0;
	//激活
	int USER_IS_ACTIVE = 1;
	
	/**
	 * 分类列表json在redis中的名称
	 */
	String CATELISTJSON = "categorysjsonsssssssssss";
	String FINDRESULTJSON = "routelistjson";
	
	
	/**
	 *热门商品
	 */
	int PRO_IS_HOT = 1;
	
	/**
	 * 非热门商品
	 */
	int PRO_NOT_HOT = 0;
	
	/**路线上架**/
	public static int ROUTE_SHANGJIA=1;
	/**路线上架**/
	public static int ROUTE_WEISHANGJIA=0;
	/**
	 * 主题旅游
	 */
	int ISTHEMETOUR=1;
	
	String ACTIVE = "Y";// 用户已激活
	String NOT_ACTIVE = "N";// 用户尚未激活
	
	


}

