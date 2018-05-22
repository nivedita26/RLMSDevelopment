package com.rlms.contract;

import java.util.Date;
import java.util.List;

public class SiteVisitReportDto {

	private Integer companyBranchMapId;
	private List<Integer> listOfUserRoleIds;
	private List<Integer> listOfStatusIds;
	private Date fromDate;
	private Date toDate;
	private List<Integer> listOfBranchCustoMapIds;
	
	private String techName;
	private String techNumber;
	private String complNumber;
	private String LiftNumber;
	private String customerName;
	private String complaintRegDate;
	private String complaintStatus;
	private String complaintResolveDate;
	private Integer totalComplaintAssigned;
	private Integer totalComplResolved;
	private Integer totalComplaintPending;
	private String totalTimeTaken;
	private Integer totalNoOfVisits;
	private Integer userRating;
	private List<SiteVisitDtlsDto> listOFAllVisits;
	private Integer serviceCallType;
	private String address;
	private String city;
	private String sericeDate;
	private String message;
	
	
	public Integer getCompanyBranchMapId() {
		return companyBranchMapId;
	}
	public void setCompanyBranchMapId(Integer companyBranchMapId) {
		this.companyBranchMapId = companyBranchMapId;
	}
	public List<Integer> getListOfUserRoleIds() {
		return listOfUserRoleIds;
	}
	public void setListOfUserRoleIds(List<Integer> listOfUserRoleIds) {
		this.listOfUserRoleIds = listOfUserRoleIds;
	}
	public List<Integer> getListOfStatusIds() {
		return listOfStatusIds;
	}
	public void setListOfStatusIds(List<Integer> listOfStatusIds) {
		this.listOfStatusIds = listOfStatusIds;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public List<Integer> getListOfBranchCustoMapIds() {
		return listOfBranchCustoMapIds;
	}
	public void setListOfBranchCustoMapIds(List<Integer> listOfBranchCustoMapIds) {
		this.listOfBranchCustoMapIds = listOfBranchCustoMapIds;
	}
	public String getTechName() {
		return techName;
	}
	public void setTechName(String techName) {
		this.techName = techName;
	}
	public String getTechNumber() {
		return techNumber;
	}
	public void setTechNumber(String techNumber) {
		this.techNumber = techNumber;
	}
	public String getComplNumber() {
		return complNumber;
	}
	public void setComplNumber(String complNumber) {
		this.complNumber = complNumber;
	}
	public String getLiftNumber() {
		return LiftNumber;
	}
	public void setLiftNumber(String liftNumber) {
		LiftNumber = liftNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getComplaintRegDate() {
		return complaintRegDate;
	}
	public void setComplaintRegDate(String complaintRegDate) {
		this.complaintRegDate = complaintRegDate;
	}
	public String getComplaintStatus() {
		return complaintStatus;
	}
	public void setComplaintStatus(String complaintStatus) {
		this.complaintStatus = complaintStatus;
	}
	public String getComplaintResolveDate() {
		return complaintResolveDate;
	}
	public void setComplaintResolveDate(String complaintResolveDate) {
		this.complaintResolveDate = complaintResolveDate;
	}
	public Integer getTotalComplaintAssigned() {
		return totalComplaintAssigned;
	}
	public void setTotalComplaintAssigned(Integer totalComplaintAssigned) {
		this.totalComplaintAssigned = totalComplaintAssigned;
	}
	public Integer getTotalComplResolved() {
		return totalComplResolved;
	}
	public void setTotalComplResolved(Integer totalComplResolved) {
		this.totalComplResolved = totalComplResolved;
	}
	public Integer getTotalComplaintPending() {
		return totalComplaintPending;
	}
	public void setTotalComplaintPending(Integer totalComplaintPending) {
		this.totalComplaintPending = totalComplaintPending;
	}
	public String getTotalTimeTaken() {
		return totalTimeTaken;
	}
	public void setTotalTimeTaken(String totalTimeTaken) {
		this.totalTimeTaken = totalTimeTaken;
	}
	public List<SiteVisitDtlsDto> getListOFAllVisits() {
		return listOFAllVisits;
	}
	public void setListOFAllVisits(List<SiteVisitDtlsDto> listOFAllVisits) {
		this.listOFAllVisits = listOFAllVisits;
	}
	public Integer getTotalNoOfVisits() {
		return totalNoOfVisits;
	}
	public void setTotalNoOfVisits(Integer totalNoOfVisits) {
		this.totalNoOfVisits = totalNoOfVisits;
	}
	public Integer getUserRating() {
		return userRating;
	}
	public void setUserRating(Integer userRating) {
		this.userRating = userRating;
	}
	public Integer getServiceCallType() {
		return serviceCallType;
	}
	public void setServiceCallType(Integer serviceCallType) {
		this.serviceCallType = serviceCallType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSericeDate() {
		return sericeDate;
	}
	public void setSericeDate(String sericeDate) {
		this.sericeDate = sericeDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
