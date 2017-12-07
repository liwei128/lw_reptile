package com.abner.service.task;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.abner.annotation.Service;
import com.abner.db.PhantomjsStorage;
import com.abner.manage.Config;
import com.abner.enums.TaskName;
import com.abner.utils.MyThreadPool;
/**
 * 定时清理假死的Phantomjs进程
 * @author wei.li
 * @time 2017年11月23日下午1:23:17
 */
@Service(TaskName.CLEANPJS)
public class CleanPhantomjsTask implements Task{
	
	private static  Logger logger=Logger.getLogger(CleanPhantomjsTask.class);
	
	private ScheduledFuture<?> future;

	@Override
	public void execute() {
		logger.info(TaskName.CLEANPJS.getDesc()+" 任务启动。。。");
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				PhantomjsStorage.clean();
			}
		}; 	
		future = MyThreadPool.scheduleAtFixedRate(runnable, Config.cleanPhantomjsTaskInterval, Config.cleanPhantomjsTaskInterval,TimeUnit.SECONDS); 
	}

	@Override
	public void stop() {
		if(future!=null){
			future.cancel(false);
			logger.info(TaskName.CLEANPJS.getDesc()+" 任务停止");
		}
	}

}
