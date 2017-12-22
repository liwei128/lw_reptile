package com.abner.exception;
/**
 * 下载异常
 * @author wei.li
 * @time 2017年12月22日上午10:46:00
 */
public class DownloadException extends RuntimeException{

	private static final long serialVersionUID = 6591220805847732450L;
	
	public DownloadException() {
        super();
    }

    public DownloadException(String message) {
        super(message);
    }
	
}
