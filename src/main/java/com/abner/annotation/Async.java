package com.abner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 异步执行方法注解
 * @author wei.li
 * @time 2017年11月23日下午12:48:00
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Async {
	
	String value() default "";

}
