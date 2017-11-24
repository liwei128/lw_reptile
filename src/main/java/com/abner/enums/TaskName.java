package com.abner.enums;
/**
 * 任务名称
 * @author wei.li
 * @time 2017年11月23日下午1:07:09
 */
public enum TaskName {
	CLEANCACHE(1,"清理缓存"),
	CLEANPJS(2,"定时清理假死Phantomjs进程"),
	IMGDOWNLOAD(3,"异步图片下载"),
	LOADURLFILE(4,"加载历史URL记录"),
	LOADUSERSETTING(5,"加载用户配置"),
	LOG(6,"窗口日志输出"),
	MAILSEND(7,"定时邮件报告"),
	MONITORNETWORK(8,"网络状态监控"),
	SAVEADDRESS(9,"保存链接地址"),
	SAVEUSERSETTING(10,"保存用户配置"),
	ASYNCLOADURL(11,"异步抓取网页"),
	INIT(12,"初始化视图"),
	RUN(13,"启动服务"),
	STOP(14,"停止爬取"),
	RESTORELINK(15,"修复残留链接（上次在访问中，但是未完成）");
	
	
	
	
	private Integer code;
	
	private String desc;

	private TaskName(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}


}
