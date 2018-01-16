package com.abner.manage;

import com.abner.pojo.UserSetting;
import com.google.common.collect.Lists;
import java.io.File;
import java.util.List;

/**
 * 用户自定义配置
 * 时间单位统一为秒
 * @author wei.li
 * @time 2017年6月19日下午3:07:23
 */
public class Config {
	
	//爬取地址集合
	public static final List<String> URLS = Lists.newArrayList();
	
	//图片关键字
	public static final List<String> KEYWORD = Lists.newArrayList();
	
	//爬虫速度
	public static int velocity;
	
	//接收邮件地址
	public static String emailAddress;
	
	//文件大小限制（kb）
	public static int fileSize;
	
	//文件保存路径
	public static String filePath;
	
	//是否站内爬取
	public static boolean isNowDomain;
	
	//用户自定义配置
	public static UserSetting userSetting = new UserSetting();
	
	public static void setUserSetting(UserSetting userSetting) {
		Config.userSetting = userSetting;
	}

	public static void setFileSize(String property) throws Exception {
		if (property==null||property.length() == 0) {
			throw new Exception("文件大小不能为空");
		}
		int parseInt;
		try {
			parseInt = Integer.parseInt(property);
		} catch (Exception e) {
			throw new Exception("文件大小必须为整数");
		}
		if (parseInt < 0) {
			throw new Exception("文件大小不能为负数");
		}
		fileSize = parseInt;
	}

	public static void setVelocity(int velocityParam){
		velocity = velocityParam+1;
	}

	public static void setURLS(String urls) throws Exception {
		if (urls==null||urls.length() == 0) {
			throw new Exception("网址不能为空");
		}
		URLS.clear();
		if (urls.indexOf(",") != -1) {
			String[] split = urls.split(",");
			for (int i = 0; i < split.length; i++) {
				String url = split[i];
				URLS.add(checkUrl(url));
			}
		} else {
			URLS.add(checkUrl(urls));
		}
	}

	public static void setFilePath(String property) throws Exception {
		if (property==null||property.length() == 0) {
			throw new Exception("图片保存路径不能为空");
		}
		File file = new File(property);
		if (!file.exists()&&!file.mkdirs()) {
			throw new Exception("图片保存路径不合法");
		}
		if (!property.endsWith("\\")) {
			filePath = property + "\\";
		} else {
			filePath = property;
		}
	}

	private static String checkUrl(String url) throws Exception {
		if ((url.startsWith("http://")) || (url.startsWith("https://"))) {
			int num = 0;
			for (int i = 0; i < url.length(); i++) {
				if (url.charAt(i) == '/') {
					num++;
				}
			}
			if (num == 2) {
				return url + "/";
			}
			return url;
		}
		throw new Exception("链接地址不合法");
	}

	public static void setEmailAddress(String property) throws Exception {
		if(property!=null&&property.length()!=0){
			String regex = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
			if(!property.matches(regex)){
				throw new Exception("邮箱地址不合法");
			}
			emailAddress=property;
		}else{
			emailAddress=null;
		}
		
	}

	public static void setKeyword(String keywords) {
		KEYWORD.clear();
		if(keywords==null||keywords.length()==0){
			return;
		}
		if (keywords.indexOf(",") != -1) {
			String[] split = keywords.split(",");
			for (String keyword : split) {
				String trim = keyword.trim();
				if(trim.length()!=0){
					KEYWORD.add(trim);
				}
			}
		} else {
			KEYWORD.add(keywords.trim());
		}
		System.out.println(keywords);
	}

	public static void setIsNowDomain(int isNowDomain2) {
		isNowDomain=isNowDomain2==0;
	}

}
