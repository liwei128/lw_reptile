package com.abner.filter;

import java.util.List;

/**
 * 图片地址过滤处理
 * @author wei.li
 * @time 2017年6月19日下午3:07:57
 */

import com.google.common.collect.Lists;

/**
 * 图片地址过滤器
 * 
 * @author wei.li
 * @time 2017年11月23日下午1:18:37
 */
public class ImgFilter extends BaseUrlFilter{
	
	private static List<String> suffixs=Lists.newArrayList(".jpg",".jpeg",".png",".gif",".bmp");
	
	
	public ImgFilter(String urlRq) {
		super(urlRq);
	}

	@Override
	public String accept(String url) {
		for(String suffix:suffixs){
			if(url.endsWith(suffix)){
				return format(url);
			}
		}
		return null;	
	}


}
