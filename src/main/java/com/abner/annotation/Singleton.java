package com.abner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 方法是否单次运行
 * @author wei.li
 * @time 2017年11月23日下午12:48:15
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Singleton {
	
	String value() default "";
}
