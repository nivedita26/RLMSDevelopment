package com.rlms.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.rlms.exception.ErrorInfo;
import com.rlms.exception.RunTimeException;
import com.rlms.exception.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

	@RequestMapping(produces = "application/json")
	@ExceptionHandler(value = ValidationException.class)
	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
	@ResponseBody
	public ErrorInfo validationExceptionHandler(HttpServletRequest request, ValidationException vex){
	//	logger.error(ExceptionUtils.getFullStackTrace(vex));
		ErrorInfo info = new ErrorInfo(vex.getExceptionCode(), vex.getExceptionMessage());
		return info;
		
	}
	
	@RequestMapping(produces = "application/json")
	@ExceptionHandler(value = RunTimeException.class)
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorInfo runtimeExceptionHandler(HttpServletRequest request, RunTimeException rex){
	//	logger.error(ExceptionUtils.getFullStackTrace(rex));
		ErrorInfo info = new ErrorInfo(rex.getExceptionCode(), rex.getExceptionMessage());
		return info;
		
	}
}
