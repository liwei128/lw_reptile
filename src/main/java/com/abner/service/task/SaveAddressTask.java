package com.abner.service.task;

import com.abner.annotation.Service;
import com.abner.db.UrlStorage;
import com.abner.manage.FilePathManage;
import com.abner.enums.TaskName;
import com.abner.utils.CommonUtil;
import com.alibaba.fastjson.JSON;
/**
 * 链接地址保存服务
 * @author wei.li
 * @time 2017年11月23日下午1:27:38
 */
@Service(name = TaskName.SAVEADDRESS)
public class SaveAddressTask implements Task{

	@Override
	public void execute() {
		CommonUtil.checkPath(FilePathManage.configPath);
		String reqUrls = JSON.toJSONString(UrlStorage.getUrls(),true);
		CommonUtil.writeToFile(reqUrls, FilePathManage.reqUrls);
		String imgUrls = JSON.toJSONString(UrlStorage.getImgs(),true);
		CommonUtil.writeToFile(imgUrls, FilePathManage.imgUrls);
	}

	@Override
	public void stop() {
		
	}
	

}
