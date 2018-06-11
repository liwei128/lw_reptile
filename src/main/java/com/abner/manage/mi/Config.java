package com.abner.manage.mi;

import java.util.concurrent.atomic.AtomicInteger;

import com.abner.pojo.mi.CustomRule;
import com.abner.pojo.mi.GoodsInfo;
import com.abner.pojo.mi.User;

public class Config {
	
	public static User user;
	
	public static GoodsInfo goodsInfo;
	
	public static CustomRule customRule ;
	
	public static AtomicInteger submitCount = new AtomicInteger(0);

}
