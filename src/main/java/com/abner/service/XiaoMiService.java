package com.abner.service;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abner.annotation.Resource;
import com.abner.annotation.Service;
import com.abner.annotation.Singleton;
import com.abner.annotation.Timing;
import com.abner.enums.TimingType;
import com.abner.manage.FilePathManage;
import com.abner.manage.MyThreadPool;
import com.abner.manage.mi.Config;
import com.abner.pojo.mi.Cookie;
import com.abner.pojo.mi.User;
import com.abner.utils.FileUtil;
import com.abner.utils.JsonUtil;

/**
 * 小米抢购服务
 * @author liwei
 * @date: 2018年6月11日 下午1:48:31
 *
 */
@Service
public class XiaoMiService {
	
	private static  Logger logger = LoggerFactory.getLogger(XiaoMiService.class);
	
	@Resource
	private LoadUrlsService loadUrlsService;
	
	@Resource
	private HttpService httpService;
	
	
	public boolean islogin(){
		try{
			String miString = FileUtil.readFileToString(FilePathManage.userConfig);
			if(miString==null||miString.length()==0){
				return false;
			}
			User oldUser = JsonUtil.toBean(miString, User.class);
			if(oldUser==null){
				return false;
			}
			if(!oldUser.equals(Config.user)){
				return false;
			}
			if(oldUser.getCookies()==null||oldUser.getCookies().size()==0){
				return false;
			}
			String result = httpService.getNoRetry(FilePathManage.checkLoginStatusJs, "");
			if(result.equals("true")){
				return true;
			}
			return false;
		}catch(Exception e){
			logger.info("验证登录异常");
		}
		return false;
		

	}
	
	/**
	 * 保持登录状态
	 */
	@Timing(initialDelay = 0, period = 10, type = TimingType.FIXED_RATE, unit = TimeUnit.MINUTES)
	public void login() {
		if(islogin()){
			logger.info("用户:{} 已登录。",Config.user.getUserName());
			return ;
		}
		long start = System.currentTimeMillis();
		FileUtil.writeToFile(JsonUtil.toString(Config.user), FilePathManage.userConfig);
		String result = "";
		int loginCount = 0;
		while(result.length()==0||result.equals("false")){
			loginCount++;
			try{
				result = httpService.getNoRetry(FilePathManage.loginJs, "");
			}catch(Exception e){
				logger.error("用户登录失败:{},正准备重试。。。",loginCount);
			}
		}
		List<Cookie> cookies = JsonUtil.toList(result, Cookie.class);
		Config.user.setCookies(cookies);
		FileUtil.writeToFile(JsonUtil.toString(Config.user), FilePathManage.userConfig);
		logger.info("用户:{} 登录成功,时间:{}ms",Config.user.getUserName(),System.currentTimeMillis()-start);
	}
	/**
	 * 获取购买链接
	 */
	public void getBuyUrl(){
		List<String> buyUrl = null;
		while(buyUrl==null||buyUrl.size()==0){
			String result = httpService.getNoRetry(FilePathManage.buyGoodsJs, "");
			buyUrl = JsonUtil.toList(result, String.class);
			if(buyUrl==null||buyUrl.isEmpty()){
				logger.info("未发现购买按钮");
			}
		}
		logger.info("购买链接:{}",buyUrl);
		String miString = FileUtil.readFileToString(FilePathManage.userConfig);
		User user = JsonUtil.toBean(miString, User.class);
		buyGoodsForHttp(buyUrl,user.getCookies());
	}
	
	@Singleton
	@Timing(initialDelay = 0, period = 5, type = TimingType.FIXED_RATE, unit = TimeUnit.SECONDS)
	public void buyGoodsForHttp(List<String> buyUrl,List<Cookie> cookies) {
		for(int i =0;i<Config.customRule.getCount();i++){
			for(String url:buyUrl){
				long start = System.currentTimeMillis();
				String re = httpService.getForHttp(url, cookies);
				if(re!=null){
					Config.submitCount.addAndGet(1);
					logger.info("提交成功({}),看人品咯！{}ms,{}    {}",Config.submitCount.toString(),System.currentTimeMillis()-start,url,re);
				}
			}
		}
		
	}
	
	
	public void start(){
		//登录
		MyThreadPool.schedule(()->{
			logger.info("购买前2分钟,开始登录...");
			login();
		}, Config.customRule.getLoginTime(), TimeUnit.MILLISECONDS);
		//购买
		MyThreadPool.schedule(()->{
			FileUtil.writeToFile(JsonUtil.toString(Config.goodsInfo), FilePathManage.goodsInfoConfig);
			getBuyUrl();
		}, Config.customRule.getBuyTime(), TimeUnit.MILLISECONDS);
		
	}

}
