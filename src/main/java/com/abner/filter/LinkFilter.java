package com.abner.filter;


import com.abner.manage.Config;

/**
 * 链接地址过滤及处理
 * @author wei.li
 * @time 2017年6月19日下午3:08:07
 */
public class LinkFilter extends BaseUrlFilter{


	public LinkFilter(String urlRq) {
		super(urlRq);
	}


	@Override
	public String accept(String url) {

		//校验域名
		if(url.trim().startsWith("http")){
			if(Config.isNowDomain&&!checkDomain(url)){
				return null;
			}
			return url;
		}
		//不获取js
		if(url.trim().startsWith("javascript")){
			return null;
		}
		
		return format(url);
		
	}
	
	
	private boolean checkDomain(String url) {
		if(url.startsWith("http://"+domain)||url.startsWith("https://"+domain)){
			return true;
		}
		return false;
	}

	

}
