package com.abner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 方法单次运行
 * 方法注解，可以和所有方法注解组合使用
 * @author wei.li
 * @time 2017年11月23日下午12:48:15
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Singleton {
	
	String value() default "";
}
