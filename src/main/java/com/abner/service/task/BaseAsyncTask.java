package com.abner.service.task;

import java.util.List;

import com.abner.pojo.MyUrl;
import com.abner.utils.MyThreadPool;

public abstract class BaseAsyncTask {
	
	public abstract boolean load(MyUrl myUrl);
	
	public synchronized boolean isStart(MyUrl url) {
		if(url.getStartTime()==0){
			url.setStartTime(System.currentTimeMillis());
			return true;
		}
		return false;
	}
	
	public void asyncUrl(List<MyUrl> urls) {
		for(MyUrl url : urls){
			MyThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					if(isStart(url)){
						load(url);
						url.setAlreadyLoad(true);
					}
				}
			});
		}
		urls.clear();
	}
}
