package com.abner.utils;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.abner.manage.Config;

import java.util.Date;
import java.util.Properties;
/**
 * 邮件发送工具类
 * @author wei.li
 * @time 2017年11月28日上午10:27:14
 */
public class EmailSendUtils {
	
	private static  Logger logger=Logger.getLogger(EmailSendUtils.class);
	
    private static Session session = createSession();

    public static boolean sendMail(String address,String body) {
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
            logger.info("邮件成功发送到:"+Config.emailAddress);
            return true;
    	}catch(Exception e){
    		logger.error("邮件发送失败",e);
    		return false;
    	}
        
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

	private static MimeMessage createMimeMessage(String sendMail, String body) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("18576462480@163.com", "爬虫监控中心", "UTF-8"));
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(sendMail, "24k纯帅", "UTF-8"));
        message.setSubject("欢迎使用爬虫服务", "UTF-8");
        message.setContent(body, "text/html;charset=UTF-8");
        message.setSentDate(new Date());
        message.saveChanges();
        return message;
    }


}
