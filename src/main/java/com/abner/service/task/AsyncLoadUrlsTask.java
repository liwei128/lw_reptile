package com.abner.service.task;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.abner.annotation.Service;
import com.abner.db.MonitorDataStorage;
import com.abner.db.UrlStorage;
import com.abner.filter.ImgFilter;
import com.abner.filter.LinkFilter;
import com.abner.manage.Config;
import com.abner.manage.StatusManage;
import com.abner.enums.MonitorName;
import com.abner.pojo.MyUrl;
import com.abner.enums.TaskName;
import com.abner.utils.LoadUrlUtil;
import com.abner.utils.MyThreadPool;

/**
 * 异步抓取网页服务
 * @author wei.li
 * @time 2017年11月23日下午1:22:42
 */
@Service(TaskName.ASYNCLOADURL)
public class AsyncLoadUrlsTask extends BaseAsyncTask implements Task{
	
	private static  Logger logger=Logger.getLogger(AsyncLoadUrlsTask.class);
	
	private ScheduledFuture<?> future;

	@Override
	public void execute() {
		logger.info(TaskName.ASYNCLOADURL.getDesc()+" 任务启动。。。");
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
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
				asyncUrl(urls);
			}
		};
		future = MyThreadPool.scheduleAtFixedRate(runnable, 0, Config.loadUrlTimeInterval, TimeUnit.SECONDS);
		
	}

	@Override
	public void stop() {
		if(future!=null){
			future.cancel(false);
			logger.info(TaskName.ASYNCLOADURL.getDesc()+" 任务停止");
		}
	}
	
	
	@Override
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
				return true;
			}
		}
		MonitorDataStorage.record(MonitorName.FAILURL.name());
		logger.error("网页加载失败,时间:"+reqUrl.loadTime()+"ms,网址:"+reqUrl.getUrl());
		return false;
	}

	


}
