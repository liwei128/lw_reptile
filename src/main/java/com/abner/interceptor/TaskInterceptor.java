package com.abner.interceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.apache.log4j.Logger;

import com.abner.annotation.Async;
import com.abner.annotation.Retry;
import com.abner.annotation.Singleton;
import com.abner.annotation.Stop;
import com.abner.annotation.Timing;
import com.abner.enums.TimingType;
import com.abner.manage.MyThreadPool;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
/**
 * 任务拦截器（用来生成代理对象）
 * @author wei.li
 * @time 2017年11月23日上午11:46:31
 */
public class TaskInterceptor implements MethodInterceptor{
	
	private static  Logger logger=Logger.getLogger(TaskInterceptor.class);
	
	private Map<String,Boolean> singletonMethods = Maps.newHashMap();
	
	private Map<String,ScheduledFuture<?>> futures = Maps.newHashMap();
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		
		//单例方法不允许多次请求
		if(!checkReq(method)){
			return null;
		}
		//重试方法
		if(method.getAnnotation(Retry.class)!=null){
			return retryTask(obj,method,args,proxy);
		}
		//定时任务
		if(method.getAnnotation(Timing.class)!=null){
			return timingTask(obj,method,args,proxy);
		}
		//取消定时任务
		if(method.getAnnotation(Stop.class)!=null){
			stopTimingTask(obj,method,args,proxy);
			if(method.getAnnotation(Async.class)!=null){
				return asyncTask(obj,method,args,proxy);
			}else{
				return proxy.invokeSuper(obj, args);
			}
		}
		//异步方法
		if(method.getAnnotation(Async.class)!=null){
			return asyncTask(obj,method,args,proxy);
		}
		//同步方法
		return proxy.invokeSuper(obj, args);
		
	}


	private Object retryTask(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Retry retry = method.getAnnotation(Retry.class);
		int count = 0;
		while(count<retry.count()){
			count++;
			try {
				return proxy.invokeSuper(obj, args);
			} catch (Throwable e) {
				List<Class<?>> clazzs = Lists.newArrayList(retry.retException());
				logger.error("error:",e);
				if(clazzs.contains(e.getClass())){
					logger.info("准备重试:"+count);
				}else{
					throw e;
				}
			}
		}
		return proxy.invokeSuper(obj, args);
	}


	private Object asyncTask(Object obj, Method method, Object[] args, MethodProxy proxy) {
		MyThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					proxy.invokeSuper(obj, args);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		return null;
	}


	private void stopTimingTask(Object obj, Method method, Object[] args, MethodProxy proxy) {
		String[] methods = method.getAnnotation(Stop.class).methods();
		for(String methodName : methods){
			ScheduledFuture<?> futureByName = futures.get(methodName);
			if(futureByName!=null){
				futureByName.cancel(false);
				logger.info("定时任务："+methodName+" 停止");
			}
		}
	}


	private Object timingTask(Object obj, Method method, Object[] args, MethodProxy proxy) {
		logger.info("定时任务："+method.getName()+" 启动");
		Timing timing = method.getAnnotation(Timing.class);
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try {
					proxy.invokeSuper(obj, args);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		};
		ScheduledFuture<?> future = null;
		if(timing.type() == TimingType.FIXED_DELAY){
			future = MyThreadPool.scheduleWithFixedDelay(runnable, timing.initialDelay(), timing.period(), timing.unit());
		}else{
			future = MyThreadPool.scheduleAtFixedRate(runnable, timing.initialDelay(), timing.period(), timing.unit());
		}
		futures.put(method.getName(), future);
		return null;
	}


	private boolean checkReq(Method method) {
		synchronized (method) {
			if(singletonMethods.get(method.getName())!=null){
				if(singletonMethods.get(method.getName())){
					singletonMethods.put(method.getName(), false);
					return true;
				}
				logger.error("方法："+method.getName()+" 不允许多次调用");
				return false;
			}
			return true;
		}
		
	}


	public TaskInterceptor(Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		for(Method method:methods){
			Singleton singleton = method.getAnnotation(Singleton.class);
			if(singleton!=null){
				singletonMethods.put(method.getName(), true);
			}
		}
	}
}
