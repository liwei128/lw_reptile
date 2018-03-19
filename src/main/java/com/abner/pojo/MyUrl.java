package com.abner.pojo;


import com.abner.utils.JsonUtil;

/**
 * 链接地址实体类
 * @author wei.li
 * @time 2017年6月19日下午3:03:12
 */
public class MyUrl{

	private boolean isAlreadyLoad=false;//是否已经访问过
	
	private String title;//网页标题
	
	private String url;//地址
	
	private long startTime=0;//开始爬取的时间
	

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
	public long loadTime(){
		return System.currentTimeMillis()-startTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isAlreadyLoad() {
		return isAlreadyLoad;
	}

	public void setAlreadyLoad(boolean isAlreadyLoad) {
		this.isAlreadyLoad = isAlreadyLoad;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}

	public MyUrl(String title, String url) {
		this.title = title;
		this.url = url;
	}
	
	public MyUrl(String url) {
		this.url = url;
	}
	
	public MyUrl() {
		super();
	}

	@Override
	public int hashCode() {
		return url.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MyUrl){
			MyUrl o=(MyUrl) obj;
			return this.url.equals(o.getUrl());
		}
		return false;
	}

	
	
}
