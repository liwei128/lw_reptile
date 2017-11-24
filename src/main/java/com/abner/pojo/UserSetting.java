package com.abner.pojo;

import com.abner.manage.Config;
/**
 * 记录用户使用习惯
 * @author wei.li
 * @time 2017年6月19日下午3:08:15
 */
public class UserSetting {
	
	private  String urls;

	private  String fileSize;

	private  String filePath;

	private  Boolean isNowDomain;
	
	private  String emailAddress;

	public String getUrls() {
		return urls;
	}

	public void setUrls(String urls) {
		this.urls = urls;
	}


	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	
	public Boolean getIsNowDomain() {
		return isNowDomain;
	}

	public void setIsNowDomain(Boolean isNowDomain) {
		this.isNowDomain = isNowDomain;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void toConfig() throws Exception{
		
		Config.setURLS(urls);
		Config.setFilePath(filePath);
		Config.setFileSize(fileSize);
		Config.setIsNowDomain(isNowDomain);
		Config.setEmailAddress(emailAddress);
		Config.setUserSetting(this);
	}
	
}
