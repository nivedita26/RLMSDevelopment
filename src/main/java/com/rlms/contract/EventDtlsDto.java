package com.rlms.contract;

import java.util.Date;
import java.util.List;

import com.rlms.model.RlmsLiftCustomerMap;

public class EventDtlsDto {

	//private Integer userRoleId;
	private Integer companyId;
	
	private String customerName;
	private String branchName;
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
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
	private List<Integer>branchCustomerMapId; 
	private RlmsLiftCustomerMap liftCustomerMap;
	private String liftNumber;
	private String liftAddress;
	private String City;
	
	public String getLiftNumber() {
		return liftNumber;
	}

	public String getLiftAddress() {
		return liftAddress;
	}

	public String getCity() {
		return City;
	}

	public void setLiftNumber(String liftNumber) {
		this.liftNumber = liftNumber;
	}

	public void setLiftAddress(String liftAddress) {
		this.liftAddress = liftAddress;
	}

	public void setCity(String city) {
		City = city;
	}

	public List<Integer> getBranchCustomerMapId() {
		return branchCustomerMapId;
	}

	public void setBranchCustomerMapId(List<Integer> branchCustomerMapId) {
		this.branchCustomerMapId = branchCustomerMapId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

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
