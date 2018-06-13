package com.abner.interceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abner.annotation.Async;
import com.abner.annotation.Retry;
import com.abner.annotation.Retry2;
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
	
	private static  Logger logger = LoggerFactory.getLogger(TaskInterceptor.class);
	
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
		//重试方法2
		if(method.getAnnotation(Retry2.class)!=null){
			return retryTask2(obj,method,args,proxy);
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

	/**
	 * 
	 * @param obj
	 * @param method
	 * @param args
	 * @param proxy
	 * @return
	 */
	private Object retryTask2(Object obj, Method method, Object[] args, MethodProxy proxy) {
		Retry2 retry = method.getAnnotation(Retry2.class);
		Object result = null;
		while(!retry.success().equals(result)){
			try {
				Thread.sleep(retry.interval());
				result = proxy.invokeSuper(obj, args);
			} catch (Throwable e) {
				logger.error("error:{},msg:{}",e.getClass().getName(),e.getMessage());
			}
		}
		return result;
	}


	/**
	 * 重试方法
	 * @param obj
	 * @param method
	 * @param args
	 * @param proxy
	 * @return
	 * @throws Throwable      
	 * Object
	 */
	private Object retryTask(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Retry retry = method.getAnnotation(Retry.class);
		int count = 0;
		Throwable result = new RuntimeException("retry error");
		while(count<=retry.count()){
			if(count!=0){
				logger.info("重试:{},方法:{},参数:{}",count,method.getName(),args);
			}
			try {
				return proxy.invokeSuper(obj, args);
			} catch (Throwable e) {
				result = e;
				logger.error("error:{},msg:{}",e.getClass().getName(),e.getMessage());
				List<Class<?>> clazzs = Lists.newArrayList(retry.retException());
				if(!clazzs.contains(e.getClass())){
					throw e;
				}
			}
			count++;
		}
		throw result;
	}

	/**
	 * 异步方法
	 * @param obj
	 * @param method
	 * @param args
	 * @param proxy
	 * @return      
	 * Object
	 */
	private Object asyncTask(Object obj, Method method, Object[] args, MethodProxy proxy) {
		Async async = method.getAnnotation(Async.class);
		for(int i =0;i<async.value();i++){
			MyThreadPool.execute(() -> {
	            try {
	                proxy.invokeSuper(obj, args);
	            } catch (Throwable e) {
	                logger.error("异步方法 :{} 发生错误:{}",method.getName(),e.getMessage());
	            }
	        });
			try {
				Thread.sleep(async.interval());
			} catch (InterruptedException e) {
				
			}
		}
		return null;
	}

	/**
	 * 停止定时任务
	 * @param obj
	 * @param method
	 * @param args
	 * @param proxy      
	 * void
	 */
	private void stopTimingTask(Object obj, Method method, Object[] args, MethodProxy proxy) {
		String[] methods = method.getAnnotation(Stop.class).methods();
		for(String methodName : methods){
			ScheduledFuture<?> futureByName = futures.get(methodName);
			if(futureByName!=null){
				futureByName.cancel(false);
				logger.info("定时任务:{} 停止.",methodName);
			}
		}
	}

	/**
	 * 定时任务
	 * @param obj
	 * @param method
	 * @param args
	 * @param proxy
	 * @return      
	 * Object
	 */
	private Object timingTask(Object obj, Method method, Object[] args, MethodProxy proxy) {
		logger.info("定时任务:{} 开始.",method.getName());
		Timing timing = method.getAnnotation(Timing.class);
		Runnable runnable = () -> {
			try {
                proxy.invokeSuper(obj, args);
            } catch (Throwable e) {
                logger.error("定时任务:{} 发生错误:{}",method.getName(),e.getMessage());
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

	/**
	 * 校验单例方法
	 * @param method
	 * @return      
	 * boolean
	 */
	private boolean checkReq(Method method) {
		synchronized (method) {
			if(singletonMethods.get(method.getName())!=null){
				if(singletonMethods.get(method.getName())){
					singletonMethods.put(method.getName(), false);
					return true;
				}
				logger.error("方法:{} 不允许多次调用",method.getName());
				return false;
			}
			return true;
		}
		
	}

	/**
	 * 收集所有单例方法
	 * @param clazz
	 */
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
