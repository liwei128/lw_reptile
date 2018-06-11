package com.abner.pojo.mi;

import java.util.List;

import com.abner.utils.JsonUtil;

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
	
	
	

}
