package com.abner.db;

import java.util.HashMap;
import java.util.Map;
import com.abner.enums.MonitorName;
import com.abner.pojo.Record;
import com.google.common.collect.Maps;
/**
 * 监控数据仓库
 * @author wei.li
 * @time 2017年11月23日下午12:49:00
 */
public class MonitorDataStorage {
	
	private static Map<String,Record> map = new HashMap<>();
	
	/**
	 * 记录监控
	 * @param monitorName      
	 * void
	 */
	public static void record(String monitorName){
		Record record=getRecord(monitorName);
		record.add(1);
		
	}
	/**
	 * 记录多次监控
	 * @param monitorName
	 * @param num      
	 * void
	 */
	public static void record(String monitorName,int num){
		Record record=getRecord(monitorName);
		record.add(num);
	}
	
	private static Record getRecord(String monitorName) {
		synchronized (monitorName) {
			Record record = map.get(monitorName);
			if(record==null){
				record = new Record();
				map.put(monitorName, record);
			}
			return record;
		}
		
	}
	/**
	 * 获取在一定时间内的单个监控数据
	 * @param monitorName
	 * @param timeMinutes
	 * @return      
	 * long
	 */
	public static long getRecordNum(String monitorName,int timeMinutes){
		Record record = getRecord(monitorName);
		return record.getNumByMinutes(timeMinutes);
	}
	/**
	 * 获取一定时间内所有监控数据
	 * @param timeMinutes
	 * @return      
	 * MonitorData
	 */
	public static Map<String,Long> getAllMonitorData(int timeMinutes){
		Map<String, Long> allMonitorData = Maps.newHashMap();
		for(MonitorName monitorName:MonitorName.values()){
			allMonitorData.put(monitorName.name(),getRecordNum(monitorName.name(),timeMinutes));
		}
		return allMonitorData;
	}
	/**
	 * 获取所有监控数据
	 * @param timeMinutes
	 * @return      
	 * MonitorData
	 */
	public static Map<String,Long> getAllMonitorData(){
		return getAllMonitorData(0);
	}
	/**
	 * 清空监控数据
	 *       
	 * void
	 */
	public static void clear(){
		map.clear();
	}
	

}
