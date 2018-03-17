package com.abner.controller;

import java.util.Map;

import com.abner.annotation.Controller;
import com.abner.annotation.Resource;
import com.abner.annotation.Singleton;
import com.abner.db.LogStorage;
import com.abner.db.MonitorDataStorage;
import com.abner.db.UrlStorage;
import com.abner.manage.Config;
import com.abner.service.CommonService;
import com.abner.service.LoadImgService;
import com.abner.service.LoadUrlsService;
import com.abner.service.LogService;
import com.abner.service.MailSendService;
import com.abner.service.MonitorNetworkService;
/**
 * 爬虫控制服务
 * @author wei.li
 * @time 2017年6月19日下午3:02:21
 */
@Controller
public class ReptileController {
	
	@Resource
	private CommonService commonService;
	
	@Resource
	private LoadUrlsService loadUrlsService;
	
	@Resource
	private LoadImgService loadImgService;
	
	@Resource
	private LogService logService;
	
	@Resource
	private MailSendService mailSendService;
	
	@Resource
	private MonitorNetworkService monitorNetworkService;
	
	@Singleton

	public void init() {
		logService.readLogs();
		monitorNetworkService.monitorNetwork();
		commonService.readUrlFile();
		commonService.readUserSetting();
		commonService.restoreLink();
	}
	public void start(){
		//加载用户输入的网址
		UrlStorage.addUrlStr(Config.URLS);
		//开启服务线程
		loadUrlsService.loadUrl();
		loadUrlsService.cleanPhantomjs();
		loadImgService.loadImg();
		mailSendService.sendMail();
	}
	
	public void end(){
		//停止服务
		loadUrlsService.stopLoadUrl();
		loadImgService.stopLoadImg();
		mailSendService.stopSendMail();
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
		commonService.saveUrlFile();
	}
	
	//保存用户配置到文件
	public void saveUserSetting() {
		commonService.saveUserSetting();
	}
	//清除缓存
	public void cleanCache() {
		commonService.cleanCache();
	}
	//获取监控信息
	public Map<String,Long> getAllMonitorData(int timeMinutes) {
		return MonitorDataStorage.getAllMonitorData(timeMinutes);
	}
	//计算失败率
	public double calculateRate(Long doneUrl, Long failUrl) {
		return commonService.calculateRate(doneUrl, failUrl);
	}




}
