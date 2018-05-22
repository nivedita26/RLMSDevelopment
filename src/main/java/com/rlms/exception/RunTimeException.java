package com.rlms.exception;

public class RunTimeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer exceptionCode;
	private String exceptionMessage;
	
	public RunTimeException(Integer code, String message){
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
