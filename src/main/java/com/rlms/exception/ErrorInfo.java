package com.rlms.exception;

public class ErrorInfo {

	private Integer exceptionCode;
	private String exceptionMessage;
	
	public ErrorInfo(Integer exceptionCode, String exceptionMessage){
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
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
