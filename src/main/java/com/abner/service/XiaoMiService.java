package com.abner.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abner.annotation.Async;
import com.abner.annotation.Resource;
import com.abner.annotation.Service;
import com.abner.annotation.Timing;
import com.abner.controller.XiaoMiController;
import com.abner.enums.TimingType;
import com.abner.manage.FilePathManage;
import com.abner.manage.MyThreadPool;
import com.abner.pojo.mi.Cookie;
import com.abner.pojo.mi.CustomRule;
import com.abner.pojo.mi.XiaoMiDto;
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
		XiaoMiDto xiaomi = XiaoMiController.XIAOMI;
		String miString = FileUtil.readFileToString(FilePathManage.miConfig);
		if(miString==null||miString.length()==0){
			return false;
		}
		XiaoMiDto oldXiaomi = JsonUtil.toBean(miString, XiaoMiDto.class);
		if(oldXiaomi==null){
			return false;
		}
		if(!xiaomi.getUser().equals(oldXiaomi.getUser())){
			return false;
		}
		if(oldXiaomi.getUser().getCookies()==null||oldXiaomi.getUser().getCookies().size()==0){
			return false;
		}
		String result = httpService.get(FilePathManage.checkLoginStatusJs, "");
		if(result.length()==0||result.equals("false")){
			return false;
		}
		return true;

	}
	/**
	 * 保持登录状态
	 */
	@Timing(initialDelay = 10, period = 10, type = TimingType.FIXED_RATE, unit = TimeUnit.MINUTES)
	public void keepLoginStatus(){
		if(!islogin()){
			login();
			return;
		}
		logger.info("{} login status ok!",XiaoMiController.XIAOMI.getUser().getUserName());
	}
	
	public void login() {
		XiaoMiDto xiaomi = XiaoMiController.XIAOMI;
		logger.info("start login：{}",xiaomi.getUser().getUserName());
		long start = System.currentTimeMillis();
		FileUtil.writeToFile(JsonUtil.toString(xiaomi), FilePathManage.miConfig);
		String result = "";
		int loginCount = 0;
		while(result.length()==0||result.equals("false")){
			loginCount++;
			try{
				result = httpService.get(FilePathManage.loginJs, "");
			}catch(Exception e){
				logger.error("xiaomi login fail:{}",loginCount);
			}
		}
		List<Cookie> cookies = JsonUtil.toList(result, Cookie.class);
		xiaomi.getUser().setCookies(cookies);
		FileUtil.writeToFile(JsonUtil.toString(xiaomi), FilePathManage.miConfig);
		logger.info("xiaomi login success:{},time:{}ms",xiaomi.getUser().getUserName(),System.currentTimeMillis()-start);
	}
	
	@Async
	public boolean buyGoods() {
		XiaoMiDto xiaomi = XiaoMiController.XIAOMI;
		String result = httpService.get(FilePathManage.buyGoodsJs, "");
		if(result.length()!=0){
			XiaoMiController.submitCount++;
			logger.info("{},url:{},count:{}",result,xiaomi.getGoodsInfo().getUrl(),XiaoMiController.submitCount);
			return true;
		}
		return false;
	}
	
	@Timing(initialDelay = 0, period = 4, type = TimingType.FIXED_RATE, unit = TimeUnit.SECONDS)
	public void buyGoods(int count) {
		for(int i = 0;i<count;i++){
			buyGoods();
		}
	}
	
	
	public void start() throws ParseException {

		CustomRule customRule = XiaoMiController.XIAOMI.getCustomRule();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(customRule.getDate());
		if(!islogin()){
			login();
		}
		MyThreadPool.schedule(()->{
			buyGoods(customRule.getCount());
		}, date);
		
	}

}
