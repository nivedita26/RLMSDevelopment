package com.rlms.contract;

import javax.persistence.Column;

public class UserRoleDtlsDTO {

	private Integer userId;
	private Integer companyId;
	private Integer spocRoleId;
	private Integer companyBranchMapId;
	private String name;
	private String contactNumber;
	private Integer countOfComplaintsAssigned;
	private String currentAddress;
	private Integer userRoleId;
	private String companyName;
	private String city;
	private Integer activeFlag;
	private Double latitude;
	private Double longitude;
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getSpocRoleId() {
		return spocRoleId;
	}
	public void setSpocRoleId(Integer spocRoleId) {
		this.spocRoleId = spocRoleId;
	}
	public Integer getCompanyBranchMapId() {
		return companyBranchMapId;
	}
	public void setCompanyBranchMapId(Integer companyBranchMapId) {
		this.companyBranchMapId = companyBranchMapId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public Integer getCountOfComplaintsAssigned() {
		return countOfComplaintsAssigned;
	}
	public void setCountOfComplaintsAssigned(Integer countOfComplaintsAssigned) {
		this.countOfComplaintsAssigned = countOfComplaintsAssigned;
	}
	public String getCurrentAddress() {
		return currentAddress;
	}
	public void setCurrentAddress(String currentAddress) {
		this.currentAddress = currentAddress;
	}
	public Integer getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
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
	
	
}
