package com.abner.exception;
/**
 * 网页访问异常
 * @author wei.li
 * @time 2017年12月22日上午10:49:13
 */
public class GetUrlException extends RuntimeException{


	private static final long serialVersionUID = 4458091802636699035L;
	
	public GetUrlException() {
        super();
    }

    public GetUrlException(String message) {
        super(message);
    }
}
