package com.abner.service;

import java.io.File;
import java.util.List;

import com.abner.annotation.Service;
import com.abner.manage.Config;
import com.abner.pojo.MyUrl;
import com.google.common.collect.Lists;
/**
 * 校验服务
 * @author wei.li
 * @time 2017年12月22日下午12:59:54
 */
@Service
public class VerifyService {
	
	//win文件名不合法字符
	private static List<Character> specialChars=Lists.newArrayList('/','\\',':','*','?','"','<','>','|');
	
	/**
	 * 是否允许执行url
	 * @param url
	 * @return      
	 * boolean
	 */
	public synchronized boolean isStart(MyUrl url) {
		if(url.getStartTime()==0){
			url.setStartTime(System.currentTimeMillis());
			return true;
		}
		return false;
	}
	
	/**
	 * 检测目录是否存在，如果不存在则创建
	 * 
	 */
	public void checkPath(String path) {
		File file = new File(path);
		if(!file.exists()){
			file.mkdirs();
		}	
	}
	
	/**
	 * 校验网页头信息
	 * @param title
	 * @return      
	 * String
	 */
	public String checkTitle(String title){
		StringBuffer titles = new StringBuffer();
		char[] charArray = title.toCharArray();
		for(char a:charArray){
			if(!specialChars.contains(a)){
				titles.append(a);
			}
		}
		return titles.toString();
	}

	/**
	 * 关键字筛选
	 * @param title
	 * @return      
	 * boolean
	 */
	public boolean checkKeyword(String title) {
		if(Config.KEYWORD.size()==0){
			return true;
		}
		for(String keyword:Config.KEYWORD){
			if(title.contains(keyword)){
				return true;
			}
		}
		return false;
	}
}
