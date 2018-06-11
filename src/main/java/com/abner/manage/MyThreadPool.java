package com.abner.manage;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 线程池
 * @author wei.li
 * @time 2017年6月19日下午3:07:16
 */

public class MyThreadPool {
	
	private static  Logger logger = LoggerFactory.getLogger(MyThreadPool.class);
	
	/**
	 * 线程池基本大小
	 */
	private static int corePoolSize = 60;
	/**
	 * 线程池最大数量
	 */
	private static int maxPoolSize = 150;
	/**
	 * 线程池维护线程所允许的空闲时间
	 */
	private static long keepAliveTime = 10;
	/**
	 * 线程池所使用的缓冲队列
	 */
	private static ArrayBlockingQueue<Runnable> workQueue= new ArrayBlockingQueue<Runnable>(100);
	/**
	 * 线程创建策略
	 */
	private static ThreadFactory threadFactory = Thread::new;
	/**
	 * 提交任务失败时（如超过maximumPoolSize），处理策略
	 */
	private static RejectedExecutionHandler rejectedHandler =
			(r, executor) -> logger.info("线程数超过线程池允许最大线程数");

	/**
	 * 线程池的创建
	 */
	private static ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(corePoolSize,maxPoolSize, keepAliveTime, TimeUnit.SECONDS, 
			workQueue, threadFactory,rejectedHandler);
	
	/**
	 * 定时任务线程池创建
	 */
	private static ScheduledExecutorService scheduledthreadService = new ScheduledThreadPoolExecutor(5, threadFactory,rejectedHandler);

	/**
	 * 执行任务
	 * @param runnable      
	 * void
	 */
	public static void execute(Runnable runnable){
		threadPoolExecutor.execute(runnable);
	}
	
	/**
	 * 定时任务
	 * 上次执行开始到下次执行开始时间间隔 
	 * @param command 线程
	 * @param initialDelay 初始延迟
	 * @param period 时间间隔
	 * @param unit   时间单位   
	 * void
	 * @return 
	 */
	public static ScheduledFuture<?> scheduleAtFixedRate(Runnable command,long initialDelay,long period,TimeUnit unit){
		return scheduledthreadService.scheduleAtFixedRate(command, initialDelay, period, unit);
	}
	/**
	 * 定时任务
	 * 上次执行结束到下次执行开始时间间隔 
	 * @param command 线程
	 * @param initialDelay 初始延迟
	 * @param delay 时间间隔
	 * @param unit   时间单位   
	 * void
	 * @return 
	 */
	public static ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,long initialDelay,long delay,TimeUnit unit){
		return scheduledthreadService.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	}
	/**
	 * 固定时间执行一次
	 * @param command
	 * @param delay
	 * @param unit
	 */
	public static ScheduledFuture<?> schedule(Runnable command,long delay, TimeUnit unit){
		return scheduledthreadService.schedule(command,delay,unit);
	}
	
	


}
