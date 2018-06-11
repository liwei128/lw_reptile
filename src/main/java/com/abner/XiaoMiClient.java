package com.abner;

import java.text.ParseException;

import com.abner.controller.XiaoMiController;
import com.abner.manage.ServiceFactory;


/**
 * 抢购小米
 * @author liwei
 * @date: 2018年6月8日 下午4:30:09
 *
 */
public class XiaoMiClient {
	
	public static void main(String[] args) throws ParseException {
		XiaoMiController miController = ServiceFactory.getService(XiaoMiController.class);
		miController.start();
	}

	

}
