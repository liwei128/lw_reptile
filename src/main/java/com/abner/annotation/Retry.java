package com.abner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 重试
 * 方法注解，可以和@Singleton 组合使用
 * @author wei.li
 * @time 2017年12月22日上午10:02:56
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {
	
	int count() default 0;
	
	Class<?>[] retException();

}
