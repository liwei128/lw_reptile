package com.abner.controller;


import java.text.ParseException;

import com.abner.annotation.Controller;
import com.abner.annotation.Resource;
import com.abner.pojo.mi.CustomRule;
import com.abner.pojo.mi.GoodsInfo;
import com.abner.pojo.mi.User;
import com.abner.pojo.mi.XiaoMiDto;
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
	
	public static final XiaoMiDto XIAOMI = XiaoMiDto.build(); 
	
	public static int submitCount = 0;
	
	@Resource
	private XiaoMiService xiaomiService;
	
	@Resource
	private LoadUrlsService loadUrlsService;
	
	
	public void start() throws ParseException{
			XIAOMI.buildUser(buildUser())
				.buildGoodsInfo(buildGoodsInfo())
				.buildCustomRule(buildCustomRule());
			loadUrlsService.cleanPhantomjs();
			xiaomiService.keepLoginStatus();
			xiaomiService.start();
	}
	
	private CustomRule buildCustomRule() {
		CustomRule customRule = new CustomRule();
		customRule.setCount(6);
		customRule.setDate("2018-06-11 16:35:00");
		return customRule;
	}

	private User buildUser() {
		User user = new User();
		user.setUserName("18576462480");
		user.setPassword("342117wacx");
		return user;
	}

	private GoodsInfo buildGoodsInfo() {
		GoodsInfo goodsInfo = new GoodsInfo();
		goodsInfo.setUrl("https://item.mi.com/product/10000093.html");
		goodsInfo.setParams_index(Lists.newArrayList(0));
		return goodsInfo;
	}
}
