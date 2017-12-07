package com.abner.service.task.run;

import java.util.List;

import com.abner.annotation.Service;
import com.abner.enums.TaskName;
import com.abner.service.task.Task;
import com.abner.service.task.TaskFactory;
import com.google.common.collect.Lists;

/**
 * 初始化任务
 * @author wei.li
 * @time 2017年11月23日下午1:22:05
 */
@Service(TaskName.INIT)
public class InitTask implements Task{
	
	@Override
	public void execute() {
		List<Task> initTask=Lists.newArrayList(
				TaskFactory.getTask(TaskName.LOG),
				TaskFactory.getTask(TaskName.MONITORNETWORK),
				TaskFactory.getTask(TaskName.LOADUSERSETTING),
				TaskFactory.getTask(TaskName.LOADURLFILE),
				TaskFactory.getTask(TaskName.RESTORELINK));
		for(Task task : initTask){
			task.execute();
		}
		
	}

	@Override
	public void stop() {
		
	}
	

}
