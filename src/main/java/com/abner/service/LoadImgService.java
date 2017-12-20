package com.abner.service;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.abner.annotation.Async;
import com.abner.annotation.Service;
import com.abner.annotation.Stop;
import com.abner.annotation.Timing;
import com.abner.db.MonitorDataStorage;
import com.abner.db.UrlStorage;
import com.abner.manage.Config;
import com.abner.manage.StatusManage;
import com.abner.enums.MonitorName;
import com.abner.enums.ReptileRetData;
import com.abner.pojo.FileDownloadDto;
import com.abner.pojo.MyUrl;
import com.abner.enums.TimingType;
import com.abner.utils.CommonUtil;
import com.abner.utils.FileDownloadUtil;
/**
 * 图片下载服务
 * @author wei.li
 * @time 2017年11月23日下午1:23:39
 */
@Service
public class LoadImgService extends BaseAsyncService{
	
	private static  Logger logger=Logger.getLogger(LoadImgService.class);
	
	
	@Timing(initialDelay = 0, period = 5, type = TimingType.FIXED_RATE, unit = TimeUnit.SECONDS)
	public void loadImg() {
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
		//抓取
		for(MyUrl url : imgurls){
			if(isStart(url)){
				load(url);
			}
		}
		imgurls.clear();
	}
	


	@Stop(methods = { "loadImg" })
	public void stopLoadImg() {
		
	}




	@Async
	public boolean load(MyUrl imgUrl) {
		FileDownloadDto fileDownloadDto = new FileDownloadDto(imgUrl);
		int count = 0;
		while(count<3){
			count++;
			ReptileRetData retData = FileDownloadUtil.download(fileDownloadDto);
			if(ReptileRetData.SUCCESS == retData){
				logger.info("图片下载成功,时间："+(System.currentTimeMillis()-imgUrl.getStartTime())+"ms ,保存路径："+fileDownloadDto.getFilePath());
				MonitorDataStorage.record(MonitorName.DONEIMG.name());
				imgUrl.setAlreadyLoad(true);
				return true;
			}
			if(ReptileRetData.OVER_LIMIT == retData){
				logger.info("图片链接:"+imgUrl.getUrl()+",图片大小不满足最小限制"+fileDownloadDto.getMinLimit()+"kb");
				imgUrl.setAlreadyLoad(true);
				return false;
			}
		}
		logger.error("图片下载失败,时间:"+(System.currentTimeMillis()-imgUrl.getStartTime())+"ms,网址:"+imgUrl.getUrl());
		CommonUtil.deleteFile(fileDownloadDto.getFilePath()+"/"+fileDownloadDto.getFileName());
		MonitorDataStorage.record(MonitorName.FAILIMG.name());
		imgUrl.setAlreadyLoad(true);
		return false;
	}

}
