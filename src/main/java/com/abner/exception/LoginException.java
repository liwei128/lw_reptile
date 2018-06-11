package com.abner.exception;

public class LoginException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2514743784841364372L;

	public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }
}
