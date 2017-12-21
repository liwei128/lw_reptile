package com.abner.enums;
/**
 * 定时任务类型
 * @author LW
 * @time 2017年12月20日 上午9:24:18
 */
public enum TimingType {
	
	FIXED_RATE("固定频率"),
	FIXED_DELAY("固定延迟");
	
	private String desc;

	private TimingType(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}
	
}
