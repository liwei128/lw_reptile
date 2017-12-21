package com.abner.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import com.abner.db.PhantomjsStorage;
import com.abner.manage.FilePathManage;
/**
 * 链接地址抓取工具
 * @author wei.li
 * @time 2017年11月28日上午10:28:10
 */
public class LoadUrlUtil{
	
	private static  Logger logger=Logger.getLogger(LoadUrlUtil.class);
	
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
}
