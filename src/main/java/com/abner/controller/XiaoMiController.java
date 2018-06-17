package com.abner.controller;


import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abner.annotation.Controller;
import com.abner.annotation.Resource;
import com.abner.annotation.Singleton;
import com.abner.db.LogStorage;
import com.abner.manage.StatusManage;
import com.abner.manage.mi.Config;
import com.abner.service.LoadUrlsService;
import com.abner.service.LogService;
import com.abner.service.XiaoMiService;


/**
 * 抢购小米
 * @author liwei
 * @date: 2018年6月11日 上午11:54:32
 *
 */
@Controller
public class XiaoMiController {
	
	private static  Logger logger = LoggerFactory.getLogger(XiaoMiController.class);
	
	@Resource
	private XiaoMiService xiaomiService;
	
	@Resource
	private LoadUrlsService loadUrlsService;
	
	@Resource
	private LogService logService;
	
	
	public void start(){
		logger.info("user:{},buyUrl:{}",Config.user,Config.goodsInfo.getBuyUrl());
		StatusManage.isLogin = false;
		Config.submitCount = new AtomicInteger(0);
		xiaomiService.keeplogin();
		xiaomiService.start();
		
		
	}
	
	@Singleton
	public void init() {
		logService.readLogs();
		loadUrlsService.cleanPhantomjs();
	}

	public String loadLog() {
		return LogStorage.getLog();
	}

	public void parseUrl(String url) {
		xiaomiService.parseUrl(url);
	}
}
