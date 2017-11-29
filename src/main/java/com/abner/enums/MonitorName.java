package com.abner.enums;
/**
 * 监控名
 * @author wei.li
 * @time 2017年11月23日下午1:06:58
 */
public enum MonitorName {
	DONEURL(1,"已抓取链接数"),
	DONEIMG(2,"已下载图片数"),
	SUMURL(3,"已探测到链接数"),
	SUMIMG(4,"已探测到图片数"),
	FAILURL(5,"失败链接数"),
	FAILIMG(6,"图片下载失败数");
	
	private Integer code;
	
	private String desc;

	private MonitorName(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	
	
}
