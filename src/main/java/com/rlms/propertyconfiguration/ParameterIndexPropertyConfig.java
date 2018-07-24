package com.rlms.propertyconfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:LsmEventIndexConfiguration.properties")
public class ParameterIndexPropertyConfig {
	
	/*@Value("${PARAMETERCOUNT}")
	private int parameterCount;*/
	
	public int getResMsgTime() {
		return resMsgTime;
	}

	public void setResMsgTime(int resMsgTime) {
		this.resMsgTime = resMsgTime;
	}

	public int getMsgTime() {
		return msgTime;
	}

	public void setMsgTime(int msgTime) {
		this.msgTime = msgTime;
	}

	@Value("${SERVICE}")
	private int service;
	
	@Value("${IMEI}")
	private int  imei;
	
	@Value("${EVENTTYPE}")
    private int eventType;
	
	@Value("${ERRORID}")
    private int errorCode;
	
	@Value("${ERRORMSG}")
    private int errorMsg;
	
	@Value("${FLOORNO}")
    private int floorNo;
	
	@Value("${MSGDATE}")
    private int messageDate;
		
	@Value("${RESMSGDATE}")
    private int resMessageDate;
	
	@Value("${RESERRORMSG}")
    private int resErrorMsg;
	
	@Value("${RESMSGTIME}")
    private int resMsgTime;
	
	@Value("${MSGTIME}")
    private int msgTime;
	
	@Value("${LMSRESPONSEUSERCONTACTNO}")
	private int lmsResponseUserContactNo;
	
	public int getLmsResponseUserContactNo() {
		return lmsResponseUserContactNo;
	}

	public void setLmsResponseUserContactNo(int lmsResponseUserContactNo) {
		this.lmsResponseUserContactNo = lmsResponseUserContactNo;
	}

	public int getResMessageDate() {
		return resMessageDate;
	}

	public void setResMessageDate(int resMessageDate) {
		this.resMessageDate = resMessageDate;
	}

	public int getResErrorMsg() {
		return resErrorMsg;
	}

	public void setResErrorMsg(int resErrorMsg) {
		this.resErrorMsg = resErrorMsg;
	}

/*	public int getParameterCount() {
		return parameterCount;
	}

	public void setParameterCount(int parameterCount) {
		this.parameterCount = parameterCount;
	}*/

	public int getService() {
		return service;
	}

	public void setService(int service) {
		this.service = service;
	}

	public int getImei() {
		return imei;
	}

	public void setImei(int imei) {
		this.imei = imei;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(int errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(int floorNo) {
		this.floorNo = floorNo;
	}

	public int getMessageDate() {
		return messageDate;
	}

	public void setMessageDate(int messageDate) {
		this.messageDate = messageDate;
	}

	}
