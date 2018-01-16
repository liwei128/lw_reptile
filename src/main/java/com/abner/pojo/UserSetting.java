package com.abner.pojo;

import com.abner.manage.Config;
/**
 * 记录用户使用习惯
 * @author wei.li
 * @time 2017年6月19日下午3:08:15
 */
public class UserSetting {
	
	private  String urls;
	
	private  String keywords;

	private  String fileSize;
	
	private  int velocity;

	private  String filePath;

	private  int isNowDomain;
	
	private  String emailAddress;

	public String getUrls() {
		return urls==null?"":urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}


	public String getFileSize() {
		return fileSize==null?"":fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return filePath==null?"":filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int velocity) {
		this.velocity = velocity;
		Config.setVelocity(velocity);
	}

	public int getIsNowDomain() {
		return isNowDomain;
	}

	public void setIsNowDomain(int isNowDomain) {
		this.isNowDomain = isNowDomain;
	}

	public String getEmailAddress() {
		return emailAddress==null?"":emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getKeywords() {
		return keywords==null?"":keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public void toConfig() throws Exception{
		Config.setURLS(urls);
		Config.setKeyword(keywords);
		Config.setVelocity(velocity);
		Config.setFilePath(filePath);
		Config.setFileSize(fileSize);
		Config.setIsNowDomain(isNowDomain);
		Config.setEmailAddress(emailAddress);
		Config.setUserSetting(this);
	}
	
}
