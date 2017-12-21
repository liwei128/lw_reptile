package com.abner.service;

import com.abner.pojo.MyUrl;

public abstract class BaseAsyncService {
	
	public synchronized boolean isStart(MyUrl url) {
		if(url.getStartTime()==0){
			url.setStartTime(System.currentTimeMillis());
			return true;
		}
		return false;
	}
}
