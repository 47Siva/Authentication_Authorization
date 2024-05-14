package com.app.Authentication.Authorization.advice;

public class MalformedJwtException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MalformedJwtException(String message) {
		super(message);
	}

	public MalformedJwtException() {
	} 

}
