package com.abner.db;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.abner.pojo.MyUrl;
/**
 * 链接地址存储管理(线程安全)
 * @author wei.li
 * @time 2017年6月19日下午3:07:43
 */
public class UrlStorage {
	
	//图片地址集合
	private static final Set<MyUrl> IMGURLS = new HashSet<>();
	//网页地址集合
	private static final Set<MyUrl> REQURLS = new HashSet<>();
	
	/**
	 * 获取需要爬取的链接
	 * @param num 指定数目
	 * @return      
	 * List<MyUrl>
	 */
	public  static List<MyUrl> getNeedUrls(int num){
		return getNeeds(num,REQURLS);	
	}
	/**
	 * 获取需要下载的图片链接
	 * @param num 指定数目
	 * @return      
	 * List<MyUrl>
	 */
	public static List<MyUrl> getNeedImgs(int num) {
		return getNeeds(num,IMGURLS);
	}
	
	private static synchronized List<MyUrl> getNeeds(int num, Set<MyUrl> urls) {
		return urls.stream().filter(r->!r.isAlreadyLoad()).limit(num).collect(Collectors.toList());
	}
	/**
	 * 添加图片链接到仓库
	 * @param img
	 * @return      
	 * boolean
	 */
	public static synchronized boolean addImg(MyUrl img){
		return IMGURLS.add(img);
	}
	/**
	 * 添加url到仓库
	 * @param url
	 * @return      
	 * boolean
	 */
	public static synchronized boolean addUrl(MyUrl url){
		return REQURLS.add(url);
	}
	/**
	 * 添加链接集合
	 * @param urls      
	 * void
	 */
	public static void addUrlStr(List<String> urls){ 
		addUrl(urls.stream().map(MyUrl::new).collect(Collectors.toList()));
	}
	/**
	 * 链接是否全部已抓取
	 * @return      
	 * boolean
	 */
	public static synchronized boolean isFinish() {
		return REQURLS.stream().allMatch(r->r.isAlreadyLoad())&&IMGURLS.stream().allMatch(r->r.isAlreadyLoad());
	}
	/**
	 * 添加链接集合
	 * @param reqUrls
	 * @return      
	 * boolean
	 */
	public static synchronized boolean addUrl(List<MyUrl> reqUrls) {
		return REQURLS.addAll(reqUrls);
	}
	/**
	 * 添加图片地址集合
	 * @param imgUrls
	 * @return      
	 * boolean
	 */
	public static synchronized boolean addImg(List<MyUrl> imgUrls) {
		return IMGURLS.addAll(imgUrls);
	}
	/**
	 * 清理所有链接地址
	 *       
	 * void
	 */
	public static synchronized void clear() {
		REQURLS.clear();
		IMGURLS.clear();
	}
	/**
	 * 获取所有url
	 * @return      
	 * List<MyUrl>
	 */
	public static synchronized List<MyUrl> getUrls() {
		return REQURLS.stream().collect(Collectors.toList());
	}
	/**
	 * 获取所有图片地址
	 * @return      
	 * List<MyUrl>
	 */
	public static synchronized List<MyUrl> getImgs() {
		return IMGURLS.stream().collect(Collectors.toList());
	}
	

}
