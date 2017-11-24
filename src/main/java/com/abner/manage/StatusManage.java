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
	public static volatile boolean isNetwork = true;
	
}
