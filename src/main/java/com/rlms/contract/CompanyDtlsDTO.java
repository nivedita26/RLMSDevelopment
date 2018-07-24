package com.rlms.contract;

import java.util.List;

public class CompanyDtlsDTO {

	private Integer companyId;
	private String companyName;
	private String address;
	private String contactNumber;
	private String ownerName;
	private String ownerNumber;
	private String ownerEmail;
	private String emailId;
	private String panNumber;
	private String tinNumber;
	private String vatNumber;
	private List<BranchDtlsDto> listOfBranches;
	private Integer numberOfBranches;
	private List<UserDtlsDto> listOfTechnicians;
	private Integer numberOfTech;
	private Integer numberOfLifts;
	private List<LiftDtlsDto> listOfAllLifts;
	
	private String city;
	private String area;
	private Integer pinCode;
	private Integer activeFlag;
	
	
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	public String getTinNumber() {
		return tinNumber;
	}
	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}
	public String getVatNumber() {
		return vatNumber;
	}
	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}
	public List<BranchDtlsDto> getListOfBranches() {
		return listOfBranches;
	}
	public void setListOfBranches(List<BranchDtlsDto> listOfBranches) {
		this.listOfBranches = listOfBranches;
	}
	public List<UserDtlsDto> getListOfTechnicians() {
		return listOfTechnicians;
	}
	public void setListOfTechnicians(List<UserDtlsDto> listOfTechnicians) {
		this.listOfTechnicians = listOfTechnicians;
	}
	public Integer getNumberOfBranches() {
		return numberOfBranches;
	}
	public void setNumberOfBranches(Integer numberOfBranches) {
		this.numberOfBranches = numberOfBranches;
	}
	public Integer getNumberOfTech() {
		return numberOfTech;
	}
	public void setNumberOfTech(Integer numberOfTech) {
		this.numberOfTech = numberOfTech;
	}
	public Integer getNumberOfLifts() {
		return numberOfLifts;
	}
	public void setNumberOfLifts(Integer numberOfLifts) {
		this.numberOfLifts = numberOfLifts;
	}
	public List<LiftDtlsDto> getListOfAllLifts() {
		return listOfAllLifts;
	}
	public void setListOfAllLifts(List<LiftDtlsDto> listOfAllLifts) {
		this.listOfAllLifts = listOfAllLifts;
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
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerNumber() {
		return ownerNumber;
	}
	public void setOwnerNumber(String ownerNumber) {
		this.ownerNumber = ownerNumber;
	}
	public String getOwnerEmail() {
		return ownerEmail;
	}
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
}
