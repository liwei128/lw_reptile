package com.abner.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.abner.annotation.Async;
import com.abner.annotation.Resource;
import com.abner.annotation.Service;
import com.abner.annotation.Stop;
import com.abner.annotation.Timing;
import com.abner.db.MonitorDataStorage;
import com.abner.manage.Config;
import com.abner.enums.MonitorName;
import com.abner.enums.TimingType;
/**
 * 邮件发送服务
 * @author wei.li
 * @time 2017年11月23日下午1:26:41
 */
@Service
public class MailSendService{
	
	@Resource
	private HttpService httpService;
	
	@Resource
	private CommonService commonService;
	

	@Timing(initialDelay = 60, period = 60, type = TimingType.FIXED_RATE, unit = TimeUnit.MINUTES)
	public void sendMail() {
		if(Config.emailAddress==null){
			return;
		}
		Map<String, Long> allMonitorData = MonitorDataStorage.getAllMonitorData();
		String head = builderRunHead();
		String body=builderBody(allMonitorData);
		httpService.sendMail(Config.emailAddress, head+body);
		
	}

	@Stop(methods = { "sendMail" })
	@Async
	public void stopSendMail() {
		if(Config.emailAddress==null){
			return;
		}
		Map<String, Long> allMonitorData = MonitorDataStorage.getAllMonitorData();
		String head = builderEndHead();
		String body = builderBody(allMonitorData);
		httpService.sendMail(Config.emailAddress, head+body);
	}
	
	
	
	private String builderEndHead(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<head><style>h3{text-align:center}body{font-family:'楷体'}span{color:#00F;font-size:16px;}a{text-decoration:none;}</style></head><body>")
		.append("<h3>爬取已完成</h3><br/>");
		return stringBuilder.toString();
	}
	private String builderRunHead(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<head><style>h3{text-align:center}body{font-family:'楷体'}span{color:#00F;font-size:16px;}a{text-decoration:none;}</style></head><body>")
		.append("<h3>爬虫进度提示</h3><br/>");
		return stringBuilder.toString();
	}
	
	private String builderBody(Map<String, Long> monitorData) {
		//解析数据
		Long sumUrl = monitorData.get(MonitorName.SUMURL.name());
		Long doneUrl = monitorData.get(MonitorName.DONEURL.name());
		Long failUrl = monitorData.get(MonitorName.FAILURL.name());
		double urlRate = commonService.calculateRate(doneUrl, failUrl);
		Long sumImg = monitorData.get(MonitorName.SUMIMG.name());
		Long doneImg = monitorData.get(MonitorName.DONEIMG.name());
		Long failImg = monitorData.get(MonitorName.FAILIMG.name());
		double imgRate = commonService.calculateRate(doneImg, failImg);
		long memory = Runtime.getRuntime().totalMemory()/1024/1024;
		//组装邮件内容
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<span>已探测到链接数：").append(sumUrl).append("&nbsp;&nbsp;&nbsp;")
		.append("已抓取链接数：").append(doneUrl).append("&nbsp;&nbsp;&nbsp;")
		.append("失败链接数：").append(failUrl).append("&nbsp;&nbsp;&nbsp;")
		.append("链接失败率：").append(urlRate).append("%").append("</span><br/>")
		.append("<span>已探测到图片数：").append(sumImg).append("&nbsp;&nbsp;&nbsp;")
		.append("已下载图片数：").append(doneImg).append("&nbsp;&nbsp;&nbsp;")
		.append("图片下载失败数：").append(failImg).append("&nbsp;&nbsp;&nbsp;")
		.append("图片下载失败率：").append(imgRate).append("%").append("</span><br/>")
		.append("<span>内存占用：").append(memory).append("M</span><br/>")
		.append("<a href='http://www.baidu.com/'>版权所有-李威</a></body>");
		return stringBuilder.toString();
	}

	

}
