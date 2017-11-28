package com.abner.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.abner.db.MonitorDataStorage;
import com.abner.db.PhantomjsStorage;
import com.abner.db.UrlStorage;
import com.abner.filter.BaseUrlFilter;
import com.abner.manage.FilePathManage;
import com.abner.enums.MonitorName;
import com.abner.pojo.MyUrl;
import com.google.common.collect.Lists;
/**
 * 链接地址抓取工具
 * @author wei.li
 * @time 2017年11月28日上午10:28:10
 */
public class LoadUrlUtil{
	
	private static  Logger logger=Logger.getLogger(LoadUrlUtil.class);
	//win文件名不合法字符
	private static List<Character> specialChars=Lists.newArrayList('/','\\',':','*','?','"','<','>','|');
	
	public static String get(String url){
		Process p=null;
		try {
			p= Runtime.getRuntime().exec(FilePathManage.exe+" " +FilePathManage.js+" "+url);
			PhantomjsStorage.add(p);
			InputStream is = p.getInputStream(); 
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8")); 
			StringBuffer sbf = new StringBuffer();  
			String tmp = "";  
			while((tmp = br.readLine())!=null){  
				   sbf.append(tmp); 
			}
			br.close();
			is.close();
			return sbf.toString();
		} catch (IOException e) {
			logger.error("Phantomjs请求异常,网址:"+url,e);
			//phantomjs.exe有可能被杀毒软件干掉，加上自我保护
			boolean copyFile = CommonUtil.copyFile(FilePathManage.exeBackups,FilePathManage.exe);
			if(copyFile){
				logger.info("Phantomjs.exe已成功恢复");
			}
			return "";
		}finally {
			if(p!=null){
				p.destroy();
			}
				
		}
			
	}
		
	public static void loadImageUrl(String html,BaseUrlFilter baseUrlFilter) {  
		Document doc = Jsoup.parse(html);
		String title = doc.head().select("title").text();
		title=checkTitle(title.trim());
		Elements imgs = doc.select("img[src]");
		int num=0;
		for(Element img:imgs){
			String imgUrl = baseUrlFilter.accept(img.attr("src"));
			if(imgUrl!=null){
				boolean add = UrlStorage.addImg(new MyUrl(title, imgUrl));
				if(add){ num++;};
			}
		}
		MonitorDataStorage.record(MonitorName.SUMIMG.name(),num);
	}
		       	 
	public static void loadReqUrl(String html,BaseUrlFilter baseUrlFilter) {
		Document doc = Jsoup.parse(html);
		Elements as = doc.select("a[href]");
		Elements frames = doc.select("frame[src]");
		Elements iframes = doc.select("iframe[src]");
		int num=0;
		//提取所有链接
		for(Element a:as){
		   String reqUrl = baseUrlFilter.accept(a.attr("href"));
		   if(reqUrl!=null){
			   boolean add = UrlStorage.addUrl(new MyUrl(reqUrl));
			   if(add) {num++;};
		   }   
		}
		for(Element frame:frames){
		   String reqUrl = baseUrlFilter.accept(frame.attr("src"));
		   if(reqUrl!=null){
			   boolean add = UrlStorage.addUrl(new MyUrl(reqUrl));
			   if(add) {num++;};
		   }
		}
		for(Element iframe:iframes){
		   String reqUrl = baseUrlFilter.accept(iframe.attr("src"));
		   if(reqUrl!=null){
			   boolean add = UrlStorage.addUrl(new MyUrl(reqUrl));
			   if(add) {num++;};
		   }
		} 
		MonitorDataStorage.record(MonitorName.SUMURL.name(),num);
	}

		
	public static String checkTitle(String title){
		StringBuffer titles = new StringBuffer();
		char[] charArray = title.toCharArray();
		for(char a:charArray){
			if(!specialChars.contains(a)){
				titles.append(a);
			}
		}
		return titles.toString();
	}
}
