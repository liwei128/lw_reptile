package com.abner.pojo.mi;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.abner.utils.JsonUtil;

/**
 * 商品信息
 * @author liwei
 * @date: 2018年6月8日 下午4:34:00
 *
 */
public class GoodsInfo {
	
	public static final String BASE_INFOURL = "https://item.mi.com/product/";
	
	public static final String BASE_BUYURL = "https://cart.mi.com/cart/add/";
	
	
	public static final AtomicInteger ParseCount = new AtomicInteger(0);
	
	//商品名
	private String name;
	
	//商品购买页面url
	private String url;
	
	//商品购买请求Url
	private String buyUrl;
	
	//抢购时需要用到的随机码
	private String randomCode;
	
	//选项1：版本
	private List<String> version;
	
	//选项2：颜色
	private List<String> colors;
	
	//商品购买id
	private List<String> goodsIds;
	

	public List<String> getVersion() {
		return version;
	}

	public void setVersion(List<String> version) {
		this.version = version;
	}

	public List<String> getColors() {
		return colors;
	}

	public void setColors(List<String> colors) {
		this.colors = colors;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getGoodsIds() {
		return goodsIds;
	}

	public void setGoodsIds(List<String> goodsIds) {
		this.goodsIds = goodsIds;
	}
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getBuyUrl() {
		return buyUrl;
	}

	public void setBuyUrl(String buyUrl) {
		this.buyUrl = buyUrl;
	}
	
	public String getRandomCode() {
		return randomCode;
	}

	public void setRandomCode(String randomCode) {
		this.randomCode = randomCode;
	}

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}
	
	public void selectBuyUrl(int v,int c) throws Exception{
		int index = colors.size()*v+c;
		if("empty".equals(goodsIds.get(index))){
			throw new Exception("该商品颜色组合不存在,请重新选择");
		}
		
		this.buyUrl =  BASE_BUYURL+goodsIds.get(index);
	}
	
	public String randomBuyUrl() {
		long timeMillis = System.currentTimeMillis();
		return this.buyUrl+randomCode+"_"+(timeMillis-1)+"&_="+timeMillis;
	}

	public void selectBuyUrl(int index) throws Exception{
		if("empty".equals(goodsIds.get(index))){
			throw new Exception("该商品颜色组合不存在,请重新选择");
		}
		this.buyUrl =  BASE_BUYURL+goodsIds.get(index);
	}

}
