package com.abner.service.task;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

import com.abner.annotation.Service;
import com.abner.interceptor.TaskInterceptor;
import com.abner.enums.TaskName;
import com.google.common.collect.Maps;
import net.sf.cglib.proxy.Enhancer;
/**
 * 任务工厂
 * @author LW
 *
 */
public class TaskFactory {	
	
	private static  Logger logger = Logger.getLogger(TaskFactory.class);
	
	private static Map<TaskName ,Task> taskMap = initTaskMap();
	

	private static Map<TaskName, Task> initTaskMap() {
		HashMap<TaskName, Task> newHashMap = Maps.newHashMap();
		Reflections reflections = new Reflections(TaskFactory.class.getPackage().getName());
		Set<Class<?>> taskClasss = reflections.getTypesAnnotatedWith(Service.class);
		for(Class<?> clazz:taskClasss){
			try {
				Service service = clazz.getAnnotation(Service.class);
				if(Task.class.isAssignableFrom(clazz)){
					//生成代理对象
					Enhancer enhancer=new Enhancer();  
					enhancer.setSuperclass(clazz);  
					enhancer.setCallback(new TaskInterceptor(clazz));  
					Task agencyTask=(Task)enhancer.create();  
					//装入Map
					newHashMap.put(service.name(), agencyTask);
				}
			} catch (Exception e) {
				logger.error("服务初始化失败, ",e);
			}
			
		}
		return newHashMap;
	}


	public static Task getTask(TaskName taskName) {
		return taskMap.get(taskName);
	}
	
	

}
