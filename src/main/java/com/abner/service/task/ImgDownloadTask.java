package com.abner.service.task;

import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.abner.annotation.Service;
import com.abner.db.MonitorDataStorage;
import com.abner.db.UrlStorage;
import com.abner.manage.Config;
import com.abner.manage.StatusManage;
import com.abner.enums.MonitorName;
import com.abner.pojo.MyUrl;
import com.abner.enums.TaskName;
import com.abner.utils.CommonUtil;
import com.abner.utils.FileDownloadUtil;
import com.abner.utils.MyThreadPool;
/**
 * 图片下载服务
 * @author wei.li
 * @time 2017年11月23日下午1:23:39
 */
@Service(name = TaskName.IMGDOWNLOAD)
public class ImgDownloadTask implements Task{
	
	private static  Logger logger=Logger.getLogger(ImgDownloadTask.class);
	
	private ScheduledFuture<?> future;
	
	@Override
	public void execute() {
		logger.info(TaskName.IMGDOWNLOAD.getDesc()+" 任务启动。。。");
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if(!StatusManage.isNetwork){
					return;
				}
				List<MyUrl> imgurls = UrlStorage.getNeedImgs(Config.velocity*20);
				logger.info("图片下载队列："+imgurls.size());
				if(imgurls.size()==0){
					StatusManage.imgFinish = true;
					return;
				}
				StatusManage.imgFinish = false;
				asyncDownload(imgurls);
			}
		};	
		future = MyThreadPool.scheduleAtFixedRate(runnable, Config.loadImgTimeInterval, Config.loadImgTimeInterval, TimeUnit.SECONDS);
	}
	
	
	
	
	/**
	 * 异步下载图片
	 * 
	 */
	public void asyncDownload(List<MyUrl> imgUrls) {
		for(int i=0;i<imgUrls.size();i++){
			MyUrl imgUrl = imgUrls.get(i);
			MyThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					if(isStart(imgUrl)){
						boolean isSuccess = FileDownloadUtil.httpClientDownload(imgUrl);
						if(!isSuccess){
							CommonUtil.deleteFile(imgUrl.getFilePath());
							isSuccess=FileDownloadUtil.httpClientDownload(imgUrl);
						}
						if(!isSuccess){
							CommonUtil.deleteFile(imgUrl.getFilePath());
							MonitorDataStorage.record(MonitorName.FAILIMG.name());
							logger.error("图片下载失败,下载时长:"+imgUrl.loadTime()+"ms,标题:"+imgUrl.getTitle()+",链接:"+imgUrl.getUrl());
						}
				        imgUrl.setAlreadyLoad(true);
					}
				}
			});
			
		}
		imgUrls.clear();
	}
	
	protected synchronized boolean isStart(MyUrl imgUrl) {
		if(imgUrl.getStartTime()==0){
			imgUrl.setStartTime(System.currentTimeMillis());
			return true;
		}
		return false;
	}




	@Override
	public void stop() {
		if(future!=null){
			future.cancel(false);
			logger.info(TaskName.IMGDOWNLOAD.getDesc()+" 任务停止");
		}
	}

}
