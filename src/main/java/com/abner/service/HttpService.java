package com.abner.service;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abner.annotation.Resource;
import com.abner.annotation.Retry;
import com.abner.annotation.Service;
import com.abner.db.PhantomjsStorage;
import com.abner.enums.ReptileRetData;
import com.abner.exception.DownloadException;
import com.abner.exception.EmailException;
import com.abner.exception.GetUrlException;
import com.abner.manage.Config;
import com.abner.manage.FilePathManage;
import com.abner.pojo.FileDownloadDto;
import com.abner.pojo.mi.Cookie;
import com.abner.utils.FileUtil;

/**
 * 网络请求服务
 * @author wei.li
 * @time 2017年12月22日上午10:51:26
 */
@Service
public class HttpService {
	
	private static  Logger logger = LoggerFactory.getLogger(HttpService.class);
	
	private static Session session = createSession();
	
	@Resource
	private VerifyService verifyService;
    
	/**
	 * 文件下载
	 * @param fileDownloadDto
	 * @return      
	 * ReptileRetData
	 */
	@Retry(count = 2, retException = { DownloadException.class })
	public ReptileRetData download(FileDownloadDto fileDownloadDto){
		CloseableHttpClient httpClient = createHttpClient();
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
            	verifyService.checkPath(filePath);
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
			throw new DownloadException("链接:"+fileDownloadDto.getUrl()+"异常");
    	}finally{
    		closeStream(fo,in,response,httpClient);
    	}
	}
	/**
	 * 访问网页
	 * @param url
	 * @return      
	 * String
	 */
	@Retry(count = 1, retException = { GetUrlException.class })
	public String get(String url){
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
			String result = sbf.toString();
			if(result.length()==0){
				throw new GetUrlException("Phantomjs请求异常,网址:"+url);
			}
			return result;
		} catch (IOException e) {
			FileUtil.copyFile(FilePathManage.exeBackups,FilePathManage.exe);
			throw new GetUrlException("Phantomjs请求异常,网址:"+url);
		}finally {
			if(p!=null){
				p.destroy();
			}
		}		
	}
	/**
	 * 执行phantomjs
	 * @param jsPath
	 * @return
	 */
	public String execute(String jsPath){
		Process p=null;
		try {
			p= Runtime.getRuntime().exec(FilePathManage.exe+" " +jsPath);
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
		} catch (Exception e) {
			FileUtil.copyFile(FilePathManage.exeBackups,FilePathManage.exe);
			logger.error("Phantomjs请求异常,js:{}",jsPath);
			return "";
		}finally {
			if(p!=null){
				p.destroy();
			}
		}		
	}
	/**
	 * 携带cookies请求网页
	 * @param url
	 * @param cookies
	 * @return
	 */
	public String getByCookies(String url,List<Cookie> cookies){
		CloseableHttpClient httpClient = createCookiesHttpClient();
    	CloseableHttpResponse response=null;
		try{
    		HttpGet httpGet = new HttpGet(url);
    		httpGet.addHeader(new BasicHeader("Cookie",builderCookiesStr(cookies)));
    		httpGet.setHeader("Connection", "keep-alive");
       		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Mobile Safari/537.36");
            response = httpClient.execute(httpGet);
            return toString(response);
            
		}catch(Exception e){
			logger.info("链接:"+url+"异常");
			return null;
    	}finally{
    		closeStream(response);
    	}
	}
	
	private String builderCookiesStr(List<Cookie> cookies) {
		StringBuilder str = new StringBuilder();
		cookies.forEach(o->{
			str.append(o.getName()).append("=").append(o.getValue()).append(";");
		});
		return str.toString();
	}
	/**
	 * 携带cookies的HttpClient
	 * @param cookies
	 * @return
	 */
	private CloseableHttpClient createCookiesHttpClient() {

		RequestConfig requestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD_STRICT).setConnectTimeout(10000).setSocketTimeout(10000)  
                       .setConnectionRequestTimeout(10000).build();
        // 设置默认跳转以及存储cookie  
        CloseableHttpClient httpClient = HttpClientBuilder.create()  
                     .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())  
                     .setRedirectStrategy(new DefaultRedirectStrategy()).setDefaultRequestConfig(requestConfig)  
                     .setDefaultCookieStore(new BasicCookieStore()).build(); 
		return httpClient;
	}
	
	
	/**
	 * 发送邮件
	 * @param address
	 * @param body      
	 * void
	 */
	@Retry(count = 1, retException = { EmailException.class })
    public void sendMail(String address,String body) {
    	try{
    		//创建一封邮件
            MimeMessage message = createMimeMessage(address, body);

            //获取邮件传输对象
            Transport transport = session.getTransport();
            transport.connect("18576462480@163.com", "lw0116");
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            //关闭连接
            transport.close();
            logger.info("邮件成功发送到:{}",Config.emailAddress);
    	}catch(Exception e){
    		throw new EmailException("邮件发送失败"+Config.emailAddress);
    	}
        
    }
	/**
	 * 关闭资源
	 * @param streams      
	 * void
	 */
	public void closeStream(Closeable... streams) {
		for(Closeable stream:streams){
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					logger.error("资源关闭异常",e);
				}
			}
			
		}
	}

	private  MimeMessage createMimeMessage(String sendMail, String body) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("18576462480@163.com", "爬虫监控中心", "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(sendMail, "24k纯帅", "UTF-8"));
        message.setSubject("欢迎使用爬虫服务", "UTF-8");
        message.setContent(body, "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }
		
	private CloseableHttpClient createHttpClient() {
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
	
	private static Session createSession() {
        Properties props = new Properties(); 
        props.setProperty("mail.transport.protocol", "smtp"); 
        props.setProperty("mail.smtp.host", "smtp.163.com"); 
        props.setProperty("mail.smtp.auth", "true"); 
        props.setProperty("mail.smtp.socketFactory.port", "465");
        Session session = Session.getDefaultInstance(props);
        //session.setDebug(true);
		return session;
	}
	
	public String toString(CloseableHttpResponse httpResponse){  
        // 获取响应消息实体  
    	try{
        	int statusCode = httpResponse.getStatusLine().getStatusCode();
        	if(statusCode!=200){
	        	logger.error("状态值:{}",statusCode); 
        		return null;
        	}
    		HttpEntity entity = httpResponse.getEntity();
            return EntityUtils.toString(entity,"utf-8");  
    	}catch(Exception e){
    		logger.error("http返回数据转字符出现异常");  
    	}
    	return null;
        
    }  
	
}
