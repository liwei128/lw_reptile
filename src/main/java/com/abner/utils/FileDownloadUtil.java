package com.abner.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import com.abner.enums.ReptileRetData;
import com.abner.pojo.FileDownloadDto;
/**
 * 文件下载工具类
 * @author wei.li
 * @time 2017年11月28日上午10:27:42
 */

public class FileDownloadUtil{
	
	private static  Logger logger=Logger.getLogger(FileDownloadUtil.class);
    

	
	public static ReptileRetData download(FileDownloadDto fileDownloadDto){
		CloseableHttpClient httpClient=createHttpClient();
    	CloseableHttpResponse response=null;
    	InputStream in = null;
		FileOutputStream fo = null;
		try{
    		HttpGet httpGet = new HttpGet(fileDownloadDto.getUrl());
    		httpGet.setHeader("Connection", "keep-alive");
       		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Mobile Safari/537.36");
            response = httpClient.execute(httpGet);
            long contentLength = response.getEntity().getContentLength();
            if(contentLength>=fileDownloadDto.getMinLimit()*1024){
            	String filePath = fileDownloadDto.getFilePath();
            	CommonUtil.checkPath(filePath);
            	in=response.getEntity().getContent();
            	fo = new FileOutputStream(new File(filePath+"/"+fileDownloadDto.getFileName()));  
                byte[] buf = new byte[1024];  
                int length = 0;  
                while ((length = in.read(buf, 0, buf.length)) != -1) {  
                    fo.write(buf, 0, length);  
                }
                return ReptileRetData.SUCCESS;
            }else{
            	return ReptileRetData.OVER_LIMIT;
            }
		}catch(Exception e){
			logger.error("链接:"+fileDownloadDto.getUrl(),e);
			return ReptileRetData.FAIL;
    	}finally{
    		CommonUtil.closeStream(fo,in,response,httpClient);
    	}
	}
	

	private static CloseableHttpClient createHttpClient() {
		// 配置超时时间（连接服务端超时60s，请求数据返回超时60s）  
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(60000)  
                       .setConnectionRequestTimeout(60000).build();  
        // 设置默认跳转以及存储cookie  
        CloseableHttpClient httpClient = HttpClientBuilder.create()  
                     .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())  
                     .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)  
                     .setDefaultCookieStore(new BasicCookieStore()).build(); 
		return httpClient;
	}
	
}
