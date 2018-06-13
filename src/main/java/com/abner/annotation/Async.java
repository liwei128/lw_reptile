package com.abner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 异步执行
 * 方法注解，可以和@Singleton @Stop 组合使用
 * @author wei.li
 * @time 2017年11月23日下午12:48:00
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Async {
	
	int value() default 1;
	
	long interval() default 0;

}
