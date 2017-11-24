package com.abner.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.abner.db.LogStorage;
import com.abner.db.MonitorDataStorage;
import com.abner.db.UrlStorage;
import com.abner.manage.Config;
import com.abner.enums.TaskName;
import com.abner.service.ReptileService;
/**
 * 爬虫控制服务
 * @author wei.li
 * @time 2017年6月19日下午3:02:21
 */
public class ReptileController {
	
	private static  Logger logger=Logger.getLogger(ReptileController.class);
	
	private static ReptileService reptileService = new ReptileService();
	
	
	public void init() {
		//初始化Jsoup，因为线程中断的情况下无法完成初始化
		Document doc = Jsoup.parse("");
		logger.info("Jsoup初始化完成。\n"+doc);
		//开启服务线程
		reptileService.run(TaskName.INIT);
	}
	public void start(){
		//加载用户输入的网址
		UrlStorage.addUrlStr(Config.URLS);
		//开启服务线程
		reptileService.run(TaskName.RUN);
	}
	
	public void end(){
		//停止服务
		reptileService.run(TaskName.STOP);
	}

	
	//检测上次爬取是否完成
	public boolean isFinish() {
		return UrlStorage.isFinish();
	}
	
	//获取日志
	public String loadLog() {
		return LogStorage.getLog();
	}
	
	//保存网址到文件
	public void saveAddress(){
		reptileService.run(TaskName.SAVEADDRESS);
	}
	
	//保存用户配置到文件
	public void saveUserSetting() {
		reptileService.run(TaskName.SAVEUSERSETTING);
	}
	//清除缓存
	public void cleanCache() {
		reptileService.run(TaskName.CLEANCACHE);
	}
	//获取监控信息
	public Map<String,Long> getAllMonitorData(int timeMinutes) {
		return MonitorDataStorage.getAllMonitorData(timeMinutes);
	}
	
	
}
