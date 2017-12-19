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
import com.abner.enums.ReptileRetData;
import com.abner.pojo.FileDownloadDto;
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
@Service(TaskName.IMGDOWNLOAD)
public class ImgDownloadTask extends BaseAsyncTask implements Task{
	
	private static  Logger logger=Logger.getLogger(ImgDownloadTask.class);
	
	private ScheduledFuture<?> future;
	
	@Override
	public void execute() {
		logger.info(TaskName.IMGDOWNLOAD.getDesc()+" 任务启动。。。");
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if(StatusManage.ping==0){
					return;
				}
				List<MyUrl> imgurls = UrlStorage.getNeedImgs(Config.velocity*20);
				logger.info("图片下载队列："+imgurls.size());
				if(imgurls.size()==0){
					StatusManage.imgFinish = true;
					return;
				}
				StatusManage.imgFinish = false;
				asyncUrl(imgurls);
			}
		};	
		future = MyThreadPool.scheduleAtFixedRate(runnable, 0, Config.loadImgTimeInterval, TimeUnit.SECONDS);
	}
	


	@Override
	public void stop() {
		if(future!=null){
			future.cancel(false);
			logger.info(TaskName.IMGDOWNLOAD.getDesc()+" 任务停止");
		}
	}




	@Override
	public boolean load(MyUrl imgUrl) {
		FileDownloadDto fileDownloadDto = new FileDownloadDto(imgUrl);
		int count = 0;
		while(count<3){
			count++;
			ReptileRetData retData = FileDownloadUtil.download(fileDownloadDto);
			if(ReptileRetData.SUCCESS == retData){
				logger.info("图片下载成功,时间："+(System.currentTimeMillis()-imgUrl.getStartTime())+"ms ,保存路径："+fileDownloadDto.getFilePath());
				MonitorDataStorage.record(MonitorName.DONEIMG.name());
				return true;
			}
			if(ReptileRetData.OVER_LIMIT == retData){
				logger.info("图片链接:"+imgUrl.getUrl()+",图片大小不满足最小限制"+fileDownloadDto.getMinLimit()+"kb");
				return false;
			}
		}
		logger.error("图片下载失败,时间:"+(System.currentTimeMillis()-imgUrl.getStartTime())+"ms,网址:"+imgUrl.getUrl());
		CommonUtil.deleteFile(fileDownloadDto.getFilePath()+"/"+fileDownloadDto.getFileName());
		MonitorDataStorage.record(MonitorName.FAILIMG.name());
		return false;
	}

}
