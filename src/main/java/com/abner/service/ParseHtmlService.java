package com.abner.service;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.abner.annotation.Resource;
import com.abner.annotation.Service;
import com.abner.db.MonitorDataStorage;
import com.abner.db.UrlStorage;
import com.abner.enums.MonitorName;
import com.abner.filter.ImgFilter;
import com.abner.filter.LinkFilter;
import com.abner.pojo.MyUrl;

/**
 * 网页解析服务
 * @author wei.li
 * @time 2017年12月21日下午5:19:42
 */
@Service
public class ParseHtmlService {
		
	@Resource
	private VerifyService verifyService;

	/**
	 * 解析url
	 * @param html
	 * @param linkFilter      
	 * void
	 */
	public void parseReqUrl(String html, LinkFilter linkFilter) {
		Document doc = Jsoup.parse(html);
		Elements as = doc.select("a[href]");
		Elements frames = doc.select("frame[src]");
		Elements iframes = doc.select("iframe[src]");
		int num=0;
		//提取所有链接
		for(Element a:as){
		   String reqUrl = linkFilter.accept(a.attr("href"));
		   if(reqUrl!=null){
			   boolean add = UrlStorage.addUrl(new MyUrl(reqUrl));
			   if(add) {num++;};
		   }   
		}
		for(Element frame:frames){
		   String reqUrl = linkFilter.accept(frame.attr("src"));
		   if(reqUrl!=null){
			   boolean add = UrlStorage.addUrl(new MyUrl(reqUrl));
			   if(add) {num++;};
		   }
		}
		for(Element iframe:iframes){
		   String reqUrl = linkFilter.accept(iframe.attr("src"));
		   if(reqUrl!=null){
			   boolean add = UrlStorage.addUrl(new MyUrl(reqUrl));
			   if(add) {num++;};
		   }
		} 
		MonitorDataStorage.record(MonitorName.SUMURL.name(),num);
	}

	/**
	 * 解析图片地址
	 * @param html
	 * @param imgFilter      
	 * void
	 */
	public void parseImgUrl(String html, ImgFilter imgFilter) {
		Document doc = Jsoup.parse(html);
		String title = doc.head().select("title").text();
		title = verifyService.checkTitle(title.trim());
		if(!verifyService.checkKeyword(title)){
			return;
		}
		Elements imgs = doc.select("img[src]");
		int num=0;
		for(Element img:imgs){
			String imgUrl = imgFilter.accept(img.attr("src"));
			if(imgUrl!=null){
				boolean add = UrlStorage.addImg(new MyUrl(title, imgUrl));
				if(add){ num++;};
			}
		}
		MonitorDataStorage.record(MonitorName.SUMIMG.name(),num);
	}
	



}
