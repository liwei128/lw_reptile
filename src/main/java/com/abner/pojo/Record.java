package com.abner.pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
/**
 * 单个监控实体类
 * @author wei.li
 * @time 2017年11月23日下午1:08:52
 */
public class Record {
	
	private List<String> times = new ArrayList<>();
	
	public long getNumByMinutes(int minutes){
		if(minutes==0){
			return times.size();
		}
		try {
			return getNumByMillis(minutes*60*1000);
		} catch (ParseException e) {
			return 0L;
		}
	}
	private long getNumByMillis(long timeMillis) throws ParseException{
		long num=0L;
		long nowTime = System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		for(int i=0;i<times.size();i++){
			long createTime = simpleDateFormat.parse(times.get(i)).getTime();
			if(nowTime-createTime<timeMillis){
				num++;
			}
		}
		return num;
	}
	
	public void add(int num){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		for(int i=0;i<num;i++){
			Date date = new Date();
			String formatDate = simpleDateFormat.format(date);
			times.add(formatDate);
		}
	}

	@Override
	public String toString() {
		return JSON.toJSONString(times);
	}
	
	
}
