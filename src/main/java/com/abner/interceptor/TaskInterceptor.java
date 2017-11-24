package com.abner.interceptor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.abner.annotation.Async;
import com.abner.annotation.Singleton;
import com.abner.utils.MyThreadPool;
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

	private List<String> asyncMethods = Lists.newArrayList();
	
	private Map<String,Boolean> singletonMethods = Maps.newHashMap();
	
	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
		Object rs = null;
		//单例方法不允许多次请求
		if(!checkReq(method)){
			return null;
		};
		//异步方法
		if(asyncMethods.contains(method.getName())){
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
		}else{
			//同步方法
			rs = proxy.invokeSuper(obj, args);
		}
		
		return rs;
		
	}


	private boolean checkReq(Method method) {
		synchronized (method) {
			if(singletonMethods.get(method.getName())!=null){
				if(singletonMethods.get(method.getName())){
					singletonMethods.put(method.getName(), false);
					return true;
				}
				return false;
			}
			return true;
		}
		
	}


	public TaskInterceptor(Class<?> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for(Method method:methods){
			Async async = method.getAnnotation(Async.class);
			Singleton singleton = method.getAnnotation(Singleton.class);
			if(async!=null){
				asyncMethods.add(method.getName());
			}
			if(singleton!=null){
				singletonMethods.put(method.getName(), true);
			}
		}
	}
}
