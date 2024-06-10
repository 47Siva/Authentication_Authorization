package com.app.Authentication.Authorization.advice;

public class InvalidJwtException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidJwtException(String message) {
        super(message);
    }
}