package com.abner.service;

import org.apache.log4j.Logger;

import com.abner.enums.TaskName;
import com.abner.service.task.Task;
import com.abner.service.task.TaskFactory;

/**
 * 爬虫主服务
 * @author LW
 *
 */
public class ReptileService {
	
	private static  Logger logger=Logger.getLogger(ReptileService.class);
	
	/**
	 * 运行具体任务
	 */
	public void run(TaskName taskName){
		Task task = TaskFactory.getTask(taskName);
		if(task == null ){
			logger.error("找不到  "+taskName.getDesc()+" 运行任务");
			return;
		}
		task.execute();
	}
	
}
