package com.abner.service.task.run;

import java.util.List;

import com.abner.annotation.Service;
import com.abner.enums.TaskName;
import com.abner.service.task.Task;
import com.abner.service.task.TaskFactory;
import com.google.common.collect.Lists;
/**
 * 启动任务
 * @author wei.li
 * @time 2017年11月23日下午1:22:20
 */
@Service(TaskName.RUN)
public class RunTask implements Task{
	
	@Override
	public void execute() {
		List<Task> runTask=Lists.newArrayList(
				TaskFactory.getTask(TaskName.ASYNCLOADURL),
				TaskFactory.getTask(TaskName.IMGDOWNLOAD),
				TaskFactory.getTask(TaskName.CLEANPJS),
				TaskFactory.getTask(TaskName.MAILSEND));
		for(Task task :runTask){
			task.execute();
		}
		
	}

	@Override
	public void stop() {
		
	}

}
