package com.abner.pojo.mi;

import com.abner.utils.JsonUtil;

/**
 * 自定义规则
 * @author liwei
 * @date: 2018年6月8日 下午4:33:48
 *
 */
public class CustomRule {
	
	//并发抢购数量
	private int count;
	
	//抢购时间
	private String  date;//yyyy-MM-dd HH:mm:ss

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}
	
	

}
