package com.rlms.exception;

public enum ExceptionCode {

	VALIDATION_EXCEPTION(1,"Validation Exception"),
	RUNTIME_EXCEPTION(2,"Runtime Exception");
	
	private Integer exceptionCode;
	private String exceptionMessage;
	
	private ExceptionCode(Integer code, String message){
		this.exceptionCode = code;
		this.exceptionMessage = message;
	}

	public Integer getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(Integer exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	
	
}
