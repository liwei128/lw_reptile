package com.abner.manage;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.reflections.Reflections;

import com.abner.annotation.Controller;
import com.abner.annotation.Resource;
import com.abner.annotation.Service;
import com.abner.interceptor.TaskInterceptor;
import com.google.common.collect.Maps;
import net.sf.cglib.proxy.Enhancer;
/**
 * 任务工厂
 * @author LW
 *
 */
public class ServiceFactory {	
	
	private static  Logger logger = Logger.getLogger(ServiceFactory.class);
	
	private static String servicePackage = "com.abner.service";
	
	private static String controllerPackage = "com.abner.controller";
	
	private static Map<String ,Object> serviceMap = initService();
	
	private static Map<String ,Object> controllerMap = initController();
	
	

	private static Map<String ,Object> initService() {
		Map<String ,Object> newHashMap = Maps.newHashMap();
		Reflections reflections = new Reflections(servicePackage);
		Set<Class<?>> taskClasss = reflections.getTypesAnnotatedWith(Service.class);
		for(Class<?> clazz:taskClasss){
			try {
				//生成代理对象
				Object instance = createInstance(clazz); 
				//装入Map
				newHashMap.put(clazz.getName(), instance);
			} catch (Exception e) {
				logger.error("服务初始化失败, ",e);
			}
			
		}
		return newHashMap;
	}


	private static Map<String, Object> initController() {
		Map<String ,Object> newHashMap = Maps.newHashMap();
		Reflections reflections = new Reflections(controllerPackage);
		Set<Class<?>> taskClasss = reflections.getTypesAnnotatedWith(Controller.class);
		for(Class<?> clazz:taskClasss){
			try {
				//生成代理对象 
				Object instance = createInstance(clazz); 
				//赋值
				Field[] fields = clazz.getDeclaredFields();
				for(Field field :fields){
					if(field.getAnnotation(Resource.class) != null){
						field.setAccessible(true);
						Object object = serviceMap.get(field.getType().getName());
						field.set(instance, object);
					}
				}
				//装入Map
				newHashMap.put(clazz.getName(), instance);
			} catch (Exception e) {
				logger.error("服务初始化失败, ",e);
			}
			
		}
		
		return newHashMap;
	}


	private static Object createInstance(Class<?> clazz) {
		Enhancer enhancer=new Enhancer();  
		enhancer.setSuperclass(clazz);  
		enhancer.setCallback(new TaskInterceptor(clazz));  
		return enhancer.create();  
	}


	@SuppressWarnings("unchecked")
	public static <T> T getController(Class<T> clazz) {
		return (T) controllerMap.get(clazz.getName());
	}
	
	

}
