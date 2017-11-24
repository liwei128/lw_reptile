package com.abner.service.task;

import java.util.List;

import org.apache.log4j.Logger;

import com.abner.annotation.Service;
import com.abner.db.UrlStorage;
import com.abner.manage.FilePathManage;
import com.abner.pojo.MyUrl;
import com.abner.enums.TaskName;
import com.abner.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
/**
 * 读取历史url
 * @author wei.li
 * @time 2017年11月23日下午1:25:22
 */
@Service(name = TaskName.LOADURLFILE)
public class LoadUrlFileTask implements Task{
	
	private static  Logger logger=Logger.getLogger(LoadUrlFileTask.class);

	@Override
	public void execute() {
		List<MyUrl> reqUrls=null;
		List<MyUrl> imgUrls=null;
		try{
			String reqUrlsStr = CommonUtil.readFileToString(FilePathManage.reqUrls);
			reqUrls = JSON.parseArray(reqUrlsStr, MyUrl.class);
			String imgUrlsStr = CommonUtil.readFileToString(FilePathManage.imgUrls);
			imgUrls = JSON.parseArray(imgUrlsStr, MyUrl.class);
		}catch(Exception e){
			logger.error("加载历史url失败",e);
		}
		if(reqUrls!=null){
			UrlStorage.addUrl(reqUrls);
		}
		if(imgUrls!=null){
			UrlStorage.addImg(imgUrls);
		}
	}

	@Override
	public void stop() {
		
	}

}
