package com.abner.pojo;

import com.abner.utils.JsonUtil;

/**
 * Phantomjs进程bean
 * @author wei.li
 * @time 2017年6月19日下午3:08:37
 */
public class PhantomjsProcesses {
	
	private Process p;
	
	private Long startTime;
	
	public Long getTime() {
		return System.currentTimeMillis()-startTime;
	}
	public void destroy(){
		p.destroy();
	}
	public boolean isAlive(){
		return p.isAlive();
	}
	public PhantomjsProcesses(Process p) {
		super();
		this.p = p;
		this.startTime = System.currentTimeMillis();
	}
	
	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}
	
	
}
