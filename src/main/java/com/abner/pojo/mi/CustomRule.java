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
	
	//抢购时间
	private long buyTime;
	
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
	
	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public void builderTime(String startTime,String duration) throws Exception{

		int minute = Integer.parseInt(duration);
		if(minute>5){
			throw new Exception("抢购时长不得超过5分钟");
		}
		if(minute<=0){
			throw new Exception("抢购时长必须大于0");
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			this.buyTime = simpleDateFormat.parse(startTime).getTime();
		}catch(Exception e){
			throw new Exception("时间格式不正确");
		}
		if(buyTime<0){
			throw new Exception("貌似错过了抢购时间");
		}
		if(buyTime<60*1000){
			throw new Exception("时间太紧，来不及登录啊");
		}
		this.endTime = buyTime+minute*60*1000;
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
