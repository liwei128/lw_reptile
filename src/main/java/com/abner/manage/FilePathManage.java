package com.abner.manage;

import java.io.File;
/**
 * 配置文件目录
 * @author wei.li
 * @time 2017年7月6日下午5:53:28
 */
public class FilePathManage {
	
	public static String binPath=System.getProperty("user.dir")+File.separator+"bin";
	
	public static String configPath=System.getProperty("user.dir")+File.separator+"config";
	
	public static String js = binPath + File.separator + "page.js";
	
	public static String loginJs = binPath + File.separator + "login.js";
	
	public static String buyGoodsJs = binPath + File.separator + "buyGoods.js";
	
	public static String checkLoginStatusJs = binPath + File.separator + "checkLoginStatus.js";
	
	public static String exe = binPath + File.separator  + "phantomjs.exe"; 
	
	public static String exeBackups=configPath + File.separator  + "phantomjs.exe"; 
	
	public static String reqUrls=configPath + File.separator + "reqUrls.json";
	
	public static String imgUrls=configPath + File.separator + "imgUrls.json";
	
	public static String userSetting=configPath + File.separator + "user.setting";
	
	public static String userConfig = configPath + File.separator + "user.json";
	
	public static String goodsInfoConfig = configPath + File.separator + "goodsInfo.json";
}
