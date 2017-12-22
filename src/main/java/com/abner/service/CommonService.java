package com.abner.service;

import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abner.annotation.Resource;
import com.abner.annotation.Service;
import com.abner.annotation.Singleton;
import com.abner.db.MonitorDataStorage;
import com.abner.db.UrlStorage;
import com.abner.manage.Config;
import com.abner.manage.FilePathManage;
import com.abner.pojo.MyUrl;
import com.abner.pojo.UserSetting;
import com.abner.utils.FileUtil;
import com.abner.utils.JsonUtil;
/**
 * 爬虫基础服务
 * @author LW
 * @time 2017年12月20日 下午7:45:08
 */
@Service
public class CommonService {
	
	private static  Logger logger = LoggerFactory.getLogger(CommonService.class);
	
	@Resource
	private VerifyService verifyService;
	/**
	 * 清除缓存
	 *       
	 * void
	 */
	public void cleanCache() {
		UrlStorage.clear();
		MonitorDataStorage.clear();
		FileUtil.writeToFile("", FilePathManage.imgUrls);
		FileUtil.writeToFile("", FilePathManage.reqUrls);
		logger.info("缓存清理完成");
	}
	/**
	 * 读取历史URL
	 *       
	 * void
	 */
	@Singleton
	public void readUrlFile() {
		List<MyUrl> reqUrls=null;
		List<MyUrl> imgUrls=null;
		try{
			String reqUrlsStr = FileUtil.readFileToString(FilePathManage.reqUrls);
			reqUrls = JsonUtil.toList(reqUrlsStr, MyUrl.class);
			String imgUrlsStr = FileUtil.readFileToString(FilePathManage.imgUrls);
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
	
	/**
	 * 读取用户配置信息
	 *       
	 * void
	 */
	@Singleton
	public void readUserSetting() {
		try{
			String setting = FileUtil.readFileToString(FilePathManage.userSetting);
			UserSetting userSetting = JsonUtil.toBean(setting, UserSetting.class);
			userSetting.toConfig();
		}catch(Exception e){
			logger.error("用户配置加载失败",e);
		}
	}
	
	/**
	 * 修复上次残留URL
	 *       
	 * void
	 */
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
	/**
	 * 保存url
	 *       
	 * void
	 */
	public void saveUrlFile() {
		long startTime = System.currentTimeMillis();
		verifyService.checkPath(FilePathManage.configPath);
		String reqUrls = JsonUtil.toString(UrlStorage.getUrls());
		FileUtil.writeToFile(reqUrls, FilePathManage.reqUrls);
		String imgUrls = JsonUtil.toString(UrlStorage.getImgs());
		FileUtil.writeToFile(imgUrls, FilePathManage.imgUrls);
		logger.info("地址保存，耗时:{}ms",System.currentTimeMillis()-startTime);
	}
	/**
	 * 保存用户配置
	 *       
	 * void
	 */
	public void saveUserSetting() {
		verifyService.checkPath(FilePathManage.configPath);
		FileUtil.writeToFile(JsonUtil.toString(Config.userSetting), FilePathManage.userSetting);
	}
	
	/**
	 * 计算失败率
	 */
	public double calculateRate(long doneNum, long failNum) {
		long sum = doneNum + failNum;
		if(sum==0){
			return 0;
		}
		BigDecimal b1 = new BigDecimal(failNum);
		BigDecimal b2 = new BigDecimal(sum);
		BigDecimal b3 = new BigDecimal(100);
		BigDecimal rate = b1.divide(b2,4,BigDecimal.ROUND_HALF_UP).multiply(b3);
		return rate.doubleValue();
	}

}
