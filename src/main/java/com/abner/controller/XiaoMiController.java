package com.abner.controller;


import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abner.annotation.Controller;
import com.abner.annotation.Resource;
import com.abner.manage.mi.Config;
import com.abner.pojo.mi.CustomRule;
import com.abner.pojo.mi.GoodsInfo;
import com.abner.pojo.mi.User;
import com.abner.service.LoadUrlsService;
import com.abner.service.XiaoMiService;
import com.google.common.collect.Lists;

/**
 * 抢购小米
 * @author liwei
 * @date: 2018年6月11日 上午11:54:32
 *
 */
@Controller
public class XiaoMiController {
	
	private static  Logger logger = LoggerFactory.getLogger(XiaoMiController.class);
	
	@Resource
	private XiaoMiService xiaomiService;
	
	@Resource
	private LoadUrlsService loadUrlsService;
	
	
	public void start() throws ParseException{
		if(buildParam()){
			
		}
		loadUrlsService.cleanPhantomjs();
		xiaomiService.start();
	}
	
	public boolean buildParam(){
		buildUser();
		buildGoodsInfo();
		return buildCustomRule();
	}
	private boolean buildCustomRule(){
		Config.customRule = new CustomRule();
		Config.customRule.setCount(10);
		try{
			Config.customRule.builderTime("2018-06-11 22:56:00");
			if(Config.customRule.getLoginTime()<0){
				logger.error("时间不合法");
				return false;
			}
		}catch(Exception e){
			logger.error("时间不合法");
			return false;
		}
		return true;
	}

	private void buildUser() {
		Config.user = new User();
		Config.user.setUserName("18576462480");
		Config.user.setPassword("342117wacx");
	}

	private void buildGoodsInfo() {
		Config.goodsInfo = new GoodsInfo();
		Config.goodsInfo.setUrl("https://item.mi.com/product/10000057.html");
		Config.goodsInfo.setParams_index(Lists.newArrayList(1,0));
	}
}
