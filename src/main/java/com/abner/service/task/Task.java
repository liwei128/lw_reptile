package com.abner.service.task;
/**
 * 任务接口
 * @author wei.li
 * @time 2017年11月23日下午3:17:45
 */
public interface Task{
	
	/**
	 * 执行任务
	 */
	public void execute();
	/**
	 * 停止正在运行中的任务
	 */
	public void stop();
	
}
