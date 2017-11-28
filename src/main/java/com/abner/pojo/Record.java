package com.abner.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 * 单个监控实体类
 * @author wei.li
 * @time 2017年11月23日下午1:08:52
 */
public class Record {
	
	private List<Long> times = new ArrayList<>();
	
	public long getNumByMinutes(int minutes){
		if(minutes==0){
			return times.size();
		}
		return getNumByMillis(minutes*60*1000);
	}
	
	private long getNumByMillis(long timeMillis){
		long num=0L;
		long nowTime = System.currentTimeMillis();
		for(int i = times.size()-1;i>=0;i--){
			if(nowTime-times.get(i)<timeMillis){
				num++;
			}else{
				return num;
			}
		}
		return num;
	}
	
	public void add(int num){
		long currentTimeMillis = System.currentTimeMillis();
		for(int i=0;i<num;i++){
			times.add(currentTimeMillis);
		}
	}
	
	
}
