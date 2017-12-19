package com.abner.pojo;

import java.util.UUID;

import com.abner.manage.Config;
import com.abner.utils.JsonUtil;

/**
 * 文件下载请求参数
 * @author wei.li
 * @time 2017年12月19日下午3:27:59
 */
public class FileDownloadDto {
	
	private String url;//文件链接
	
	private String filePath;//文件保存路径
	
	private String fileName;//文件名
	
	private long minLimit;//最小文件限制，单位kb
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getMinLimit() {
		return minLimit;
	}

	public void setMinLimit(long minLimit) {
		this.minLimit = minLimit;
	}

	@Override
	public String toString() {
		return JsonUtil.toString(this);
	}

	public FileDownloadDto() {
		super();
	}

	public FileDownloadDto(MyUrl imgUrl) {
		this.url = imgUrl.getUrl();
		this.filePath = Config.filePath+imgUrl.getTitle();
		this.fileName = UUID.randomUUID().toString()+url.substring(url.lastIndexOf("."));
		this.minLimit = Config.fileSize;
	}

	
	
	
	
	
}
