package com.abner.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.abner.pojo.MyUrl;
import com.google.common.collect.Lists;
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
	//锁，保证线程安全(set遍历时size不允许变化)
	private static final String IMGLOCK = "IMGLOCK";
	private static final String REQLOCK = "REQLOCK";
	
	/**
	 * 获取需要爬取的链接
	 * @param num 指定数目
	 * @return      
	 * List<MyUrl>
	 */
	public  static List<MyUrl> getNeedUrls(int num){
		return getNeeds(num,REQURLS,REQLOCK);	
	}
	/**
	 * 获取需要下载的图片链接
	 * @param num 指定数目
	 * @return      
	 * List<MyUrl>
	 */
	public static List<MyUrl> getNeedImgs(int num) {
		return getNeeds(num,IMGURLS,IMGLOCK);
	}
	
	private static List<MyUrl> getNeeds(int num, Set<MyUrl> urls,String lock) {
		int i=0;
		synchronized (lock) {
			//获取未访问地址
			List<MyUrl> urlsRs =new ArrayList<>();
			Iterator<MyUrl> iterator = urls.iterator();
			while(iterator.hasNext()){
				MyUrl myUrl = iterator.next();
				if(!myUrl.isAlreadyLoad()){
					urlsRs.add(myUrl);
					i++;
				}
				if(i==num){
					return urlsRs;
				}
			}
			return urlsRs;
		}
	}
	/**
	 * 添加图片链接到仓库
	 * @param img
	 * @return      
	 * boolean
	 */
	public static boolean addImg(MyUrl img){
		synchronized (IMGLOCK) {
			return IMGURLS.add(img);
		}
	}
	/**
	 * 添加url到仓库
	 * @param url
	 * @return      
	 * boolean
	 */
	public static boolean addUrl(MyUrl url){
		synchronized (REQLOCK) {
			return REQURLS.add(url);
		}
	}
	/**
	 * 添加链接集合
	 * @param urls      
	 * void
	 */
	public static void addUrlStr(List<String> urls){
		for(String url:urls){
			addUrl(new MyUrl(url));
		}
	}
	/**
	 * 链接是否全部已抓取
	 * @return      
	 * boolean
	 */
	public static boolean isFinish() {
		synchronized (REQLOCK) {
			for(MyUrl myUrl:REQURLS){
				if(!myUrl.isAlreadyLoad()){
					return  false;
				}
			}
			return true;
		}
	}
	/**
	 * 添加链接集合
	 * @param reqUrls
	 * @return      
	 * boolean
	 */
	public static boolean addUrl(List<MyUrl> reqUrls) {
		synchronized (REQLOCK) {
			return REQURLS.addAll(reqUrls);
		}
	}
	/**
	 * 添加图片地址集合
	 * @param imgUrls
	 * @return      
	 * boolean
	 */
	public static boolean addImg(List<MyUrl> imgUrls) {
		synchronized (IMGLOCK) {
			return IMGURLS.addAll(imgUrls);
		}
	}
	/**
	 * 清理所有链接地址
	 *       
	 * void
	 */
	public static void clear() {
		synchronized (REQLOCK) {
			REQURLS.clear();
		}
		synchronized (IMGLOCK) {
			IMGURLS.clear();
		}
	}
	/**
	 * 获取所有url
	 * @return      
	 * List<MyUrl>
	 */
	public static List<MyUrl> getUrls() {
		synchronized (REQLOCK) {
			List<MyUrl> urls = Lists.newArrayList();
			urls.addAll(REQURLS);
			return urls;
		}
	}
	/**
	 * 获取所有图片地址
	 * @return      
	 * List<MyUrl>
	 */
	public static List<MyUrl> getImgs() {
		synchronized (IMGLOCK) {
			List<MyUrl> imgs = Lists.newArrayList();
			imgs.addAll(IMGURLS);
			return imgs;
		}
	}
	

}
