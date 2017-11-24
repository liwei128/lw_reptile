package com.abner.service.task;

import org.apache.log4j.Logger;

import com.abner.annotation.Service;
import com.abner.db.MonitorDataStorage;
import com.abner.db.UrlStorage;
import com.abner.manage.FilePathManage;
import com.abner.enums.TaskName;
import com.abner.utils.CommonUtil;
/**
 * 清理链接缓存
 * @author wei.li
 * @time 2017年11月23日下午1:22:59
 */
@Service(name = TaskName.CLEANCACHE)
public class CleanCacheTask implements Task{
	
	private static  Logger logger=Logger.getLogger(CleanCacheTask.class);

	@Override
	public void execute() {
		UrlStorage.clear();
		MonitorDataStorage.clear();
		CommonUtil.writeToFile("", FilePathManage.imgUrls);
		CommonUtil.writeToFile("", FilePathManage.reqUrls);
		logger.info("缓存清理完成");
	}

	@Override
	public void stop() {
		
	}

}
