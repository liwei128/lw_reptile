package com.abner.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abner.annotation.Async;
import com.abner.annotation.Resource;
import com.abner.annotation.Service;
import com.abner.annotation.Stop;
import com.abner.annotation.Timing;
import com.abner.db.MonitorDataStorage;
import com.abner.db.PhantomjsStorage;
import com.abner.db.UrlStorage;
import com.abner.filter.ImgFilter;
import com.abner.filter.LinkFilter;
import com.abner.manage.Config;
import com.abner.manage.StatusManage;
import com.abner.enums.MonitorName;
import com.abner.pojo.MyUrl;
import com.abner.enums.TimingType;

/**
 * 异步抓取网页服务
 * @author wei.li
 * @time 2017年11月23日下午1:22:42
 */
@Service
public class LoadUrlsService{
	
	private static  Logger logger = LoggerFactory.getLogger(LoadUrlsService.class);
	
	@Resource
	private HttpService httpService;
	
	@Resource
	private ParseHtmlService parseHtmlService;
	
	@Resource
	private VerifyService verifyService;
	
	@Timing(initialDelay = 0, period = 4, type = TimingType.FIXED_RATE, unit = TimeUnit.SECONDS)
	public void loadUrl() {
		if(StatusManage.ping==0){
			return;
		}
		//获取需要抓取的url
		List<MyUrl> urls = UrlStorage.getNeedUrls(Config.velocity*2);
		logger.info("网页加载队列:{}",urls.size());
		if(urls.size()==0){
			StatusManage.urlFinish = true;
			return;
		}
		StatusManage.urlFinish = false;
		//抓取
		for(MyUrl url : urls){
			if(verifyService.isStart(url)){
				load(url);
			}
		}
		urls.clear();
	}

	@Stop(methods = { "loadUrl","cleanPhantomjs" })
	public void stopLoadUrl() {
		
	}
	
	@Timing(initialDelay = 30, period = 30, type = TimingType.FIXED_RATE, unit = TimeUnit.SECONDS)
	public void cleanPhantomjs() {
		PhantomjsStorage.clean();	
	}

	
	@Async
	public boolean load(MyUrl reqUrl) {
		try{
			String html = httpService.get(reqUrl.getUrl());
			MonitorDataStorage.record(MonitorName.DONEURL.name());
			logger.info("网页加载成功,时间:{}ms, 网址:{}",reqUrl.loadTime(),reqUrl.getUrl());
			parseHtmlService.parseReqUrl(html,new LinkFilter(reqUrl.getUrl()));
			parseHtmlService.parseImgUrl(html,new ImgFilter(reqUrl.getUrl()));
			return true;
		}catch(Exception e){
			MonitorDataStorage.record(MonitorName.FAILURL.name());
			logger.error("网页加载失败,时间:{}ms, 网址:{}",reqUrl.loadTime(),reqUrl.getUrl());
			return false;
		}finally {
			reqUrl.setAlreadyLoad(true);
		}
		
	}
	
	
	

}
