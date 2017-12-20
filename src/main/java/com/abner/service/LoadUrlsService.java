package com.abner.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.abner.annotation.Async;
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
import com.abner.utils.LoadUrlUtil;

/**
 * 异步抓取网页服务
 * @author wei.li
 * @time 2017年11月23日下午1:22:42
 */
@Service
public class LoadUrlsService extends BaseAsyncService{
	
	private static  Logger logger=Logger.getLogger(LoadUrlsService.class);


	@Timing(initialDelay = 0, period = 5, type = TimingType.FIXED_RATE, unit = TimeUnit.SECONDS)
	public void loadUrl() {
		if(StatusManage.ping==0){
			return;
		}
		//获取需要抓取的url
		List<MyUrl> urls = UrlStorage.getNeedUrls(Config.velocity*2);
		if(urls.size()==0){
			StatusManage.urlFinish = true;
			return;
		}
		StatusManage.urlFinish = false;
		//抓取
		for(MyUrl url : urls){
			if(isStart(url)){
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
		int count = 0;
		while(count<3){
			count++;
			String html = LoadUrlUtil.get(reqUrl.getUrl());
			//若失败，则再请求一次
			if(html.length()!=0){
				MonitorDataStorage.record(MonitorName.DONEURL.name());
				logger.info("网页加载成功,时间:"+reqUrl.loadTime()+"ms,网址:"+reqUrl.getUrl());
				int reqNum = LoadUrlUtil.loadReqUrl(html,new LinkFilter(reqUrl.getUrl()));
				MonitorDataStorage.record(MonitorName.SUMIMG.name(),reqNum);
				int imageNum = LoadUrlUtil.loadImageUrl(html,new ImgFilter(reqUrl.getUrl()));
				MonitorDataStorage.record(MonitorName.SUMURL.name(),imageNum);
				reqUrl.setAlreadyLoad(true);
				return true;
			}
		}
		MonitorDataStorage.record(MonitorName.FAILURL.name());
		logger.error("网页加载失败,时间:"+reqUrl.loadTime()+"ms,网址:"+reqUrl.getUrl());
		reqUrl.setAlreadyLoad(true);
		return false;
	}
	

}
