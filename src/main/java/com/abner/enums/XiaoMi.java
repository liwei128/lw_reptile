package com.abner.enums;

import java.util.List;

import com.abner.utils.JsonUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;

/**
 * 小米相关枚举
 * @author liwei
 * @date: 2018年6月8日 下午5:03:40
 *
 */
public class XiaoMi {
	
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum GoodsInfo{

		mi8("https://item.mi.com/product/10000099.html",Lists.newArrayList(3,3,2)),
		mix2("https://item.mi.com/product/10000070.html",Lists.newArrayList(4,1,1)),
		mix2s("https://item.mi.com/product/10000085.html",Lists.newArrayList(3,2,1));
		
		private String url;
		
		private List<Integer> maxIndex;
		
		public String getUrl() {
			return url;
		}

		public List<Integer> getMaxIndex() {
			return maxIndex;
		}

		private GoodsInfo(String url, List<Integer> maxIndex) {
			this.url = url;
			this.maxIndex = maxIndex;
		}

		@Override
		public String toString(){
			return JsonUtil.toString(this);
		}
		
	}
	

}
