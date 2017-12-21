package com.abner.annotation;

import java.lang.annotation.*;

/**
 * 服务名注解
 * @author wei.li
 * @time 2017年11月21日下午3:53:32
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Service {
	
	String value() default "";//服务名
}