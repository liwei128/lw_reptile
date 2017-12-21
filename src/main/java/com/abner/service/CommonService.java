package com.abner.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.abner.annotation.Service;
import com.abner.annotation.Singleton;
import com.abner.db.MonitorDataStorage;
import com.abner.db.UrlStorage;
import com.abner.manage.Config;
import com.abner.manage.FilePathManage;
import com.abner.pojo.MyUrl;
import com.abner.pojo.UserSetting;
import com.abner.utils.CommonUtil;
import com.abner.utils.JsonUtil;
/**
 * 爬虫基础服务
 * @author LW
 * @time 2017年12月20日 下午7:45:08
 */
@Service()
public class CommonService {
	
	private static  Logger logger=Logger.getLogger(CommonService.class);
	
	
	public void cleanCache() {
		UrlStorage.clear();
		MonitorDataStorage.clear();
		CommonUtil.writeToFile("", FilePathManage.imgUrls);
		CommonUtil.writeToFile("", FilePathManage.reqUrls);
		logger.info("缓存清理完成");
	}
	@Singleton
	public void readUrlFile() {
		List<MyUrl> reqUrls=null;
		List<MyUrl> imgUrls=null;
		try{
			String reqUrlsStr = CommonUtil.readFileToString(FilePathManage.reqUrls);
			reqUrls = JsonUtil.toList(reqUrlsStr, MyUrl.class);
			String imgUrlsStr = CommonUtil.readFileToString(FilePathManage.imgUrls);
			imgUrls = JsonUtil.toList(imgUrlsStr, MyUrl.class);
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
	@Singleton
	public void readUserSetting() {
		try{
			String setting = CommonUtil.readFileToString(FilePathManage.userSetting);
			UserSetting userSetting = JsonUtil.toBean(setting, UserSetting.class);
			userSetting.toConfig();
		}catch(Exception e){
			logger.error("用户配置加载失败",e);
		}
	}
	
	@Singleton
	public void restoreLink() {
		restoreLink(UrlStorage.getImgs());
		restoreLink(UrlStorage.getUrls());
	}
	
	private void restoreLink(List<MyUrl> urls){
		for(MyUrl url : urls){
			if(!url.isAlreadyLoad()){
				url.setStartTime(0);
			}
		}
	}
	
	public void saveUrlFile() {
		CommonUtil.checkPath(FilePathManage.configPath);
		String reqUrls = JsonUtil.toString(UrlStorage.getUrls());
		CommonUtil.writeToFile(reqUrls, FilePathManage.reqUrls);
		String imgUrls = JsonUtil.toString(UrlStorage.getImgs());
		CommonUtil.writeToFile(imgUrls, FilePathManage.imgUrls);
	}
	
	public void saveUserSetting() {
		CommonUtil.checkPath(FilePathManage.configPath);
		CommonUtil.writeToFile(JsonUtil.toString(Config.userSetting), FilePathManage.userSetting);
	}

}
