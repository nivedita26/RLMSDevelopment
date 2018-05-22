package com.rlms.contract;

import java.util.List;

public class BranchDtlsDto {

	private Integer id;
	private Integer companyId;
	private String branchName;
	private String branchAddress;
	private String companyName;
	private Integer numberOfTechnicians;
	private Integer numberOfLifts;
	private List<UserDtlsDto> listOfAllTechnicians;
	private List<LiftDtlsDto> listOfAllLifts;
	private String city;
	private String area;
	private Integer pinCode;
	private Integer activeFlag;
	
	
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getBranchAddress() {
		return branchAddress;
	}
	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Integer getNumberOfTechnicians() {
		return numberOfTechnicians;
	}
	public void setNumberOfTechnicians(Integer numberOfTechnicians) {
		this.numberOfTechnicians = numberOfTechnicians;
	}
	public Integer getNumberOfLifts() {
		return numberOfLifts;
	}
	public void setNumberOfLifts(Integer numberOfLifts) {
		this.numberOfLifts = numberOfLifts;
	}
	public List<UserDtlsDto> getListOfAllTechnicians() {
		return listOfAllTechnicians;
	}
	public void setListOfAllTechnicians(List<UserDtlsDto> listOfAllTechnicians) {
		this.listOfAllTechnicians = listOfAllTechnicians;
	}
	public List<LiftDtlsDto> getListOfAllLifts() {
		return listOfAllLifts;
	}
	public void setListOfAllLifts(List<LiftDtlsDto> listOfAllLifts) {
		this.listOfAllLifts = listOfAllLifts;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
}
