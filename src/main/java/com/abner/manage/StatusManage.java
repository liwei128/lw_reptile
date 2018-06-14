package com.abner.manage;
/**
 * 爬虫状态标志
 * @author wei.li
 * @time 2017年6月19日下午3:06:49
 */
public class StatusManage {
	
	//链接抓取完成标志
	public static volatile boolean  urlFinish = false;
	
	//图片下载完成标志
	public static volatile boolean imgFinish = false;
	
	//暂停标志
	public static volatile boolean isPause = false;
		
	//联网标志
	public static volatile int ping = 999;
	
	//登录状态
	public static volatile boolean isLogin = false;
	
	//是否发现购买链接
	public static volatile boolean isBuyUrl = false;
	
	//抢购结束标志
	public static volatile boolean isEnd = false;
	
	//结束消息
	public static volatile String endMsg = "";
	
	
}
