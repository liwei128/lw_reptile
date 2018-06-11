package com.abner.pojo.mi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	private long buyTime;
	
	//登录时间
	private long loginTime;


	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}
	
	

	public int getCount() {
		return count;
	}



	public void setCount(int count) {
		this.count = count;
	}



	public long getBuyTime() {
		return buyTime;
	}



	public void setBuyTime(long buyTime) {
		this.buyTime = buyTime;
	}



	public long getLoginTime() {
		return loginTime;
	}



	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}



	public void builderTime(String timeFormat) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long time = simpleDateFormat.parse(timeFormat).getTime()-System.currentTimeMillis();
		this.loginTime = time-2*60*1000;
		this.buyTime = time;
	}
	
	

}
