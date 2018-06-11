package com.abner.pojo.mi;
/**
 * 抢小米所需要的信息
 * @author liwei
 * @date: 2018年6月8日 下午4:38:09
 *
 */
public class XiaoMiDto {
	
	private User user;
	
	private GoodsInfo goodsInfo;
	
	private CustomRule customRule;

	public User getUser() {
		return user;
	}
	public GoodsInfo getGoodsInfo() {
		return goodsInfo;
	}
	public CustomRule getCustomRule() {
		return customRule;
	}
	
	public static XiaoMiDto build(){
		return new XiaoMiDto();
	}
	
	public XiaoMiDto buildUser(User user){
		this.user = user;
		return this;
	}
	
	public XiaoMiDto buildGoodsInfo(GoodsInfo goodsInfo){
		this.goodsInfo = goodsInfo;
		return this;
	}
	public XiaoMiDto buildCustomRule(CustomRule customRule){
		this.customRule = customRule;
		return this;
	}

}
