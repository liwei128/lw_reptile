package com.abner.db;
/**
 * 日志仓库
 * @author wei.li
 * @time 2017年11月23日下午12:48:49
 */
public class LogStorage {
	
	public static StringBuilder log = new StringBuilder();
	
	public static String lock = "lock";

	
	public static void addLog(String text){
		synchronized(lock){
			log.append(text).append("\n").toString();
		}
	}
	
	public static String getLog(){
		synchronized(lock){
			String logText = log.toString();
			log.delete(0, log.length());
			return logText;	
		}
	}
}
