package com.abner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 任务停止注解,用于停止定时任务
 * 方法注解，可以和@Async @Singleton 组合使用
 * @author LW
 * @time 2017年12月20日 上午9:58:23
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Stop {
	
	String[] methods();
}
