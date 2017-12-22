package com.abner.exception;
/**
 * 邮件异常
 * @author wei.li
 * @time 2017年12月22日上午10:48:56
 */
public class EmailException extends RuntimeException{

	private static final long serialVersionUID = -4450874089793103696L;
	
	public EmailException() {
        super();
    }

    public EmailException(String message) {
        super(message);
    }
}
