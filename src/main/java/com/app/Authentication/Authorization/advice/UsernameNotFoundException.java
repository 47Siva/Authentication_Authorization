package com.app.Authentication.Authorization.advice;

public class UsernameNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UsernameNotFoundException(String message) {
        super(message);
    }
}
