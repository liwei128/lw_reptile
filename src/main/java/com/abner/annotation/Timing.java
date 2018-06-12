package com.abner.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import com.abner.enums.TimingType;
/**
 * 定时任务
 * 方法注解，可以和@Singleton 组合使用 
 * @author LW
 * @time 2017年12月20日 上午9:20:55
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Timing {
	
	TimingType type();
	
	int initialDelay();
	
	int period();
	
	TimeUnit unit();


}
