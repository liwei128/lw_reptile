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
import com.abner.db.UrlStorage;
import com.abner.manage.Config;
import com.abner.manage.StatusManage;
import com.abner.enums.MonitorName;
import com.abner.enums.ReptileRetData;
import com.abner.pojo.FileDownloadDto;
import com.abner.pojo.MyUrl;
import com.abner.enums.TimingType;
import com.abner.utils.FileUtil;
/**
 * 图片下载服务
 * @author wei.li
 * @time 2017年11月23日下午1:23:39
 */
@Service
public class LoadImgService{
	
	private static  Logger logger = LoggerFactory.getLogger(LoadImgService.class);
	
	@Resource
	private HttpService httpService;
	
	@Resource
	private VerifyService verifyService;
	
	@Timing(initialDelay = 0, period = 4, type = TimingType.FIXED_RATE, unit = TimeUnit.SECONDS)
	public void loadImg() {
		if(StatusManage.ping==0){
			return;
		}
		List<MyUrl> imgurls = UrlStorage.getNeedImgs(Config.velocity*20);
		logger.info("图片下载队列:{}",imgurls.size());
		if(imgurls.size()==0){
			StatusManage.imgFinish = true;
			return;
		}
		StatusManage.imgFinish = false;
		//抓取
		for(MyUrl url : imgurls){
			if(verifyService.isStart(url)){
				load(url);
			}
		}
		imgurls.clear();
	}
	


	@Stop(methods = { "loadImg" })
	public void stopLoadImg() {
		
	}




	@Async
	public void load(MyUrl imgUrl) {
		FileDownloadDto fileDownloadDto = new FileDownloadDto(imgUrl);
		try {
			ReptileRetData retData = httpService.download(fileDownloadDto);
			if(ReptileRetData.SUCCESS == retData){
				logger.info("图片下载成功,时间:{}ms, 保存路径:{}",imgUrl.loadTime(),fileDownloadDto.getFilePath());
				MonitorDataStorage.record(MonitorName.DONEIMG.name());
			}
			if(ReptileRetData.OVER_LIMIT == retData){
				logger.info("图片:{},小于{}kb",imgUrl.getUrl(),fileDownloadDto.getMinLimit());
			}
		} catch (Exception e) {
			logger.error("图片下载失败,时间:{}ms, 网址:{}",imgUrl.loadTime(),fileDownloadDto);
			FileUtil.deleteFile(fileDownloadDto.getFilePath()+"/"+fileDownloadDto.getFileName());
			MonitorDataStorage.record(MonitorName.FAILIMG.name());
		}finally {
			imgUrl.setAlreadyLoad(true);
		}
	}


}
