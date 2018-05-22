package com.rlms.contract;

import java.util.Date;
import java.util.List;

public class MemberDtlsDto {

	
	private String firstName;
	private String lastName;
	private String companyName;
	private String contactNumber;
	private String address;
	private String emailId;
	private Integer branchCustoMapId;
	private String appRegId;
	private Double latitude;
	private Double longitude;
	private String branchName;
	private String customerName;
	private Date registrationDate;
	private String city;
	private String area;
	private Integer pinCode;
	private Integer memberId;
	private Integer serviceCallType;
	
	
	private List<CustomerDtlsDto> listOfCustomerDtls;
	
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public List<CustomerDtlsDto> getListOfCustomerDtls() {
		return listOfCustomerDtls;
	}
	public void setListOfCustomerDtls(List<CustomerDtlsDto> listOfCustomerDtls) {
		this.listOfCustomerDtls = listOfCustomerDtls;
	}
	public String getAppRegId() {
		return appRegId;
	}
	public void setAppRegId(String appRegId) {
		this.appRegId = appRegId;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	public Integer getBranchCustoMapId() {
		return branchCustoMapId;
	}
	public void setBranchCustoMapId(Integer branchCustoMapId) {
		this.branchCustoMapId = branchCustoMapId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Integer getPinCode() {
		return pinCode;
	}
	public void setPinCode(Integer pinCode) {
		this.pinCode = pinCode;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public Integer getServiceCallType() {
		return serviceCallType;
	}
	public void setServiceCallType(Integer serviceCallType) {
		this.serviceCallType = serviceCallType;
	}
}
