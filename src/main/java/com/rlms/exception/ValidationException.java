package com.rlms.exception;

public class ValidationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer exceptionCode;
	private String exceptionMessage;
	
	public ValidationException(Integer code, String message){
		this.exceptionCode = code;
		this.exceptionMessage = message;
	}

	public Integer getExceptionCode() {
		return exceptionCode;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}
	
	
}
