package com.abner.pojo.mi;

import java.text.SimpleDateFormat;
import com.abner.utils.JsonUtil;

/**
 * 自定义规则
 * @author liwei
 * @date: 2018年6月8日 下午4:33:48
 *
 */
public class CustomRule {
	
	//登录时间
	private long loginTime;//提前2分钟
	
	//抢购时间
	private long buyTime;//提前30s
	
	//抢购截止时间
	private long endTime;

	@Override
	public String toString() {
		return JsonUtil.toString(this);
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
	
	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public void builderTime(String startTime,String duration) throws Exception{
		try{
			int minute = Integer.parseInt(duration);
			if(minute>5){
				throw new Exception("抢购时长不得超过5分钟");
			}
			if(minute<=0){
				throw new Exception("抢购时长必须大于0");
			}
			
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long time = simpleDateFormat.parse(startTime).getTime()-System.currentTimeMillis();
			this.buyTime = time-30*1000;
			this.loginTime = time-2*60*1000;
			this.endTime = time+minute*60*1000;
		}catch(Exception e){
			throw new Exception("时间格式不正确");
		}
	}
	
	public CustomRule(String buyTime, String duration) throws Exception {
		if(buyTime==null||buyTime.length()==0){
			throw new Exception("抢购时间不能为空");
		}
		if(duration ==null||duration.length()==0){
			throw new Exception("抢购时长不能为空");
		}
		builderTime(buyTime, duration);
	}

	public CustomRule() {
		super();
	}
	
	
	
	

}
