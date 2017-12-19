package com.abner.enums;
/**
 * 返回状态枚举
 * @author wei.li
 * @time 2017年12月19日下午3:52:25
 */
public enum ReptileRetData {
	
	SUCCESS(0,"成功"),
	FAIL(1,"失败"),
	OVER_LIMIT(2,"超出限制");
	
	private int code;
	
	private String desc;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private ReptileRetData(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	
	
}
