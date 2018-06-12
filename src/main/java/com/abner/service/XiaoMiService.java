package com.abner.service;


import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abner.annotation.Async;
import com.abner.annotation.Resource;
import com.abner.annotation.Service;
import com.abner.annotation.Singleton;
import com.abner.annotation.Stop;
import com.abner.annotation.Timing;
import com.abner.enums.TimingType;
import com.abner.manage.FilePathManage;
import com.abner.manage.MyThreadPool;
import com.abner.manage.StatusManage;
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
	private HttpService httpService;
	
	
	public boolean islogin(){
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
		String result = httpService.execute(FilePathManage.checkLoginStatusJs);
		if(result.equals("true")){
			return true;
		}
		return false;

	}
	
	/**
	 * 保持登录状态
	 */
	@Singleton
	@Timing(initialDelay = 0, period = 10, type = TimingType.FIXED_RATE, unit = TimeUnit.MINUTES)
	public void keeplogin() {
		login();
	}
	
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
			if(loginCount!=0){
				logger.error("用户登录失败({}),正准备重试。。。",loginCount);
			}
			result = httpService.execute(FilePathManage.loginJs);
			loginCount++;
		}
		List<Cookie> cookies = JsonUtil.toList(result, Cookie.class);
		Config.user.setCookies(cookies);
		FileUtil.writeToFile(JsonUtil.toString(Config.user), FilePathManage.userConfig);
		logger.info("用户:{} 登录成功,时间:{}ms",Config.user.getUserName(),System.currentTimeMillis()-start);
	}
	
	/**
	 * 获取购买链接
	 */
	public List<String> getBuyUrl(){
		int count = 0;
		while(true){
			if(count!=0){
				logger.info("未发现购买链接({})",count);
			}
			String result = httpService.execute(FilePathManage.buyGoodsJs);
			if(result.startsWith("[")&&result.endsWith("]")){
				List<String> buyUrl = JsonUtil.toList(result, String.class);
				logger.info("购买链接:{}",buyUrl);
				return buyUrl;
				
			}
			count++;
		}
	}
	
	/**
	 * httpClient执行购买
	 * @param buyUrl
	 * @param cookies
	 */
	@Timing(initialDelay = 0, period = 300, type = TimingType.FIXED_RATE, unit = TimeUnit.MILLISECONDS)
	public void buyGoodsTask(List<String> buyUrl,List<Cookie> cookies) {
		buy(buyUrl,cookies);
	}
	
	@Async(50)
	public void buy(List<String> buyUrl,List<Cookie> cookies){
		for(String url:buyUrl){
			long start = System.currentTimeMillis();
			String re = httpService.getByCookies(url, cookies);
			if(re!=null){
				Config.submitCount.addAndGet(1);
				logger.info("提交成功({}),看人品咯！{}ms,{}",Config.submitCount.toString(),System.currentTimeMillis()-start,url);
			}
		}
	}
	
	public void start(){
		//登录
		MyThreadPool.schedule(()->{
			logger.info("离开放购买还有2分钟,准备登录中。。。");
			keeplogin();
		}, Config.customRule.getLoginTime(), TimeUnit.MILLISECONDS);
		
		//购买
		MyThreadPool.schedule(()->{
			logger.info("离开放购买还有30s,准备抢购中。。。");
			login();
			FileUtil.writeToFile(JsonUtil.toString(Config.goodsInfo), FilePathManage.goodsInfoConfig);
			List<String> buyUrl = getBuyUrl();
			String miString = FileUtil.readFileToString(FilePathManage.userConfig);
			User user = JsonUtil.toBean(miString, User.class);
			buyGoodsTask(buyUrl,user.getCookies());
		}, Config.customRule.getBuyTime(), TimeUnit.MILLISECONDS);
		//抢购时间截止
		MyThreadPool.schedule(()->{
			stop();
		}, Config.customRule.getEndTime(), TimeUnit.MILLISECONDS);

	}
	@Stop(methods = { "buyGoodsTask" })
	public void stop() {
		logger.info("抢购时间截止,停止抢购!");
		StatusManage.isEnd = true;
	}
	
}
