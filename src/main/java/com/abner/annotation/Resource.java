package com.abner.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * 依赖注入
 * @author LW
 * @time 2017年12月20日 下午7:42:26
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Resource {
	
	String value() default "";
	
}
