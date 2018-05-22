package com.rlms.contract;

import java.util.Date;

import com.rlms.model.RlmsLiftCustomerMap;

public class EventDtlsDto {

	//private Integer userRoleId;
	private Integer companyId;
	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	private Integer eventId;
	private String eventService;
	private String imei;
	private String eventType;
	private String eventCode;
	private String eventDescription;
	private int floorNo;
	private String date;
	private String eventFromContactNo;
	private RlmsLiftCustomerMap liftCustomerMap;
	
	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getEventService() {
		return eventService;
	}

	public void setEventService(String eventService) {
		this.eventService = eventService;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventCode() {
		return eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public int getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(int floorNo) {
		this.floorNo = floorNo;
	}

	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getEventFromContactNo() {
		return eventFromContactNo;
	}

	public void setEventFromContactNo(String eventFromContactNo) {
		this.eventFromContactNo = eventFromContactNo;
	}

	public RlmsLiftCustomerMap getLiftCustomerMap() {
		return liftCustomerMap;
	}

	public void setLiftCustomerMap(RlmsLiftCustomerMap liftCustomerMap) {
		this.liftCustomerMap = liftCustomerMap;
	}

	public Integer getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}

	//private Date generatedDate;
	//private String generatedDateStr;
	//private Integer generatedBy;
	//private Date updatedDate;
   //private String updatedDateStr;
	//private Integer updatedBy;
	private Integer activeFlag;
	
}
