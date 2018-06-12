package com.abner.pojo.mi;

import java.util.Arrays;
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
	private List<Integer> params_index;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Integer> getParams_index() {
		return params_index;
	}

	public void setParams_index(List<Integer> params_index) {
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
		this.params_index = Lists.newArrayList(Arrays.asList(params_index));
	}

	public GoodsInfo() {
		super();
	}
	
	
	
	
	
	
	

}
