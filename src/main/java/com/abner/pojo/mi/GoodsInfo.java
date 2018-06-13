package com.abner.pojo.mi;

import java.util.List;
import com.abner.utils.JsonUtil;
import com.google.common.collect.Lists;

/**
 * 要购买的商品
 * @author liwei
 * @date: 2018年6月8日 下午4:34:00
 *
 */
public class GoodsInfo {
	
	//购买或抢购页面地址
	private String url;
	
	//选项：版本、颜色、保障服务等
	private List<Option> params_index;
	
	//抢购链接
	private List<String> buyUrls;
	
	

	public List<String> getBuyUrls() {
		return buyUrls;
	}

	public void setBuyUrls(List<String> buyUrls) {
		this.buyUrls = buyUrls;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Option> getParams_index() {
		return params_index;
	}

	public void setParams_index(List<Option> params_index) {
		this.params_index = params_index;
	}

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}

	public GoodsInfo(String url, Integer ... params_index) throws Exception {
		super();
		if(url==null||url.length()==0){
			throw new Exception("链接地址不能为空");
		}
		if(params_index==null||params_index.length==0){
			throw new Exception("选项不合法");
		}
		this.url = url;
		this.params_index = Lists.newArrayList();
		for(int i =0;i<params_index.length;i++){
			if(params_index[i]!=0){
				this.params_index.add(new Option(i, params_index[i]-1));
			}
		}
	}

	public GoodsInfo() {
		super();
	}
	
	
	
	
	
	
	

}
