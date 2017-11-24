package com.abner.filter;
/**
 * 公共链接地址过滤器
 * @author wei.li
 * @time 2017年11月23日下午1:18:08
 */
public abstract class BaseUrlFilter {
	
	public String domain;
	public String urlRq;
	
	public abstract String accept(String url);
	
	public BaseUrlFilter(String urlRq) {
		this.urlRq =parseUrl(urlRq);
		this.domain=parseDomain(urlRq);
	}
	
	private String parseUrl(String urlRq) {
		int num=0;
		for(int i=0;i<urlRq.length();i++){
			if(urlRq.charAt(i)==47){
				num++;
			}
		}
		if(num==2){
			return urlRq+"/";
		}
		return urlRq;
	}
	
	
	
	public  static String parseDomain(String urlRq) {
		int start=0;
		int end=urlRq.length();
		int num =0;
		for(int i=0;i<urlRq.length();i++){
			if(urlRq.charAt(i)==47){
				num++;
				if(num==2){
					start=i+1;
				}
				if(num==3){
					end=i;
				}
			}
		}
		return urlRq.substring(start,end);
	}
	
	public String getAbsolutePath(String url) {
		int num=0;
		for(int i=0;i<urlRq.length();i++){
			if(urlRq.charAt(i)==47){
				num++;
			}
			if(num==3){
				String substring = urlRq.substring(0,i);
				return substring+url;
			}
		}
		return null;
	}
	
	public String format(String url) {
		url = url.replaceAll(" ","%20");
		if(url.trim().startsWith("http")){
			return url;
		}
		if(url.trim().startsWith("/")){
			return getAbsolutePath(url);
		}
		String substring = urlRq.substring(0,urlRq.lastIndexOf("/")+1);
		return substring+url;
	}
	
}
