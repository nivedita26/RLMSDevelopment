package com.rlms.contract;

import java.util.Date;
import java.util.List;

public class ComplaintsDto {

	private String complaintNumber;
	private Date registrationDate;
	private String registrationDateStr;
	private Date serviceStartDate;
	private String serviceStartDateStr;
	private Date actualServiceEndDate;
	private String actualServiceEndDateStr;
	private String liftNumber;
	private String branchName;
	private String customerName;
	private String customerAddress;
	private String customerCity;
	private Date fromDate;
	private Date toDate;
	private String fromDateStr;
	private String  toDateStr;
   private SiteVisitReportDto siteVisitDetailsList ; 
	
	public SiteVisitReportDto getSiteVisitDetailsList() {
	return siteVisitDetailsList;
}
public void setSiteVisitDetailsList(SiteVisitReportDto siteVisitDetailsList) {
	this.siteVisitDetailsList = siteVisitDetailsList;
}
	public String getFromDateStr() {
		return fromDateStr;
	}
	public void setFromDateStr(String fromDateStr) {
		this.fromDateStr = fromDateStr;
	}
	public String getToDateStr() {
		return toDateStr;
	}
	public void setToDateStr(String toDateStr) {
		this.toDateStr = toDateStr;
	}
	private String liftAddress;
	private String latitude;
	private String longitude;
	private Integer registrationType;
	private String registrationTypeStr;
	private String remark;
	private String status;
	private String title;
	private String technicianDtls;
	private Integer complaintId;
	private Integer userRoleId;
	private Integer liftCustoMapId;
	private String complaintent;
	private String regType;
	private Integer complaintTechMapId;
	private Date updatedDate;
	private String companyName;
	private String city;
	private Integer serviceCallType;
	private String registeredBy ;
	private String serviceCallTypeStr;
	private Date CallAssignedDate;
	private String CallAssignedDateStr;
	private String resolvedDateStr;

	public String getResolvedDateStr() {
		return resolvedDateStr;
	}
	public void setResolvedDateStr(String resolvedDateStr) {
		this.resolvedDateStr = resolvedDateStr;
	}
	public String getCallAssignedDateStr() {
		return CallAssignedDateStr;
	}
	public void setCallAssignedDateStr(String callAssignedDateStr) {
		CallAssignedDateStr = callAssignedDateStr;
	}
	private int  totalDaysRequiredToResolveComplaint;
	private Date lastVisitedDate;
	private String lastVisitedDateStr;


	public String getLastVisitedDateStr() {
		return lastVisitedDateStr;
	}
	public void setLastVisitedDateStr(String lastVisitedDateStr) {
		this.lastVisitedDateStr = lastVisitedDateStr;
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
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getCustomerCity() {
		return customerCity;
	}
	public void setCustomerCity(String customerCity) {
		this.customerCity = customerCity;
	}
	
	public Date getLastVisitedDate() {
		return lastVisitedDate;
	}
	public void setLastVisitedDate(Date lastVisitedDate) {
		this.lastVisitedDate = lastVisitedDate;
	}
	public int getTotalDaysRequiredToResolveComplaint() {
		return totalDaysRequiredToResolveComplaint;
	}
	public void setTotalDaysRequiredToResolveComplaint(int totalDaysRequiredToResolveComplaint) {
		this.totalDaysRequiredToResolveComplaint = totalDaysRequiredToResolveComplaint;
	}
	public Date getCallAssignedDate() {
		return CallAssignedDate;
	}
	public void setCallAssignedDate(Date callAssignedDate) {
		CallAssignedDate = callAssignedDate;
	}
	
	public String getServiceCallTypeStr() {
		return serviceCallTypeStr;
	}

	public void setServiceCallTypeStr(String serviceCallTypeStr) {
		this.serviceCallTypeStr = serviceCallTypeStr;
	}

	public String getRegisteredBy() {
		return registeredBy;
	}

	public void setRegisteredBy(String registeredBy) {
		this.registeredBy = registeredBy;
	}

	public String getComplaintNumber() {
		return complaintNumber;
	}

	public void setComplaintNumber(String complaintNumber) {
		this.complaintNumber = complaintNumber;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Date getServiceStartDate() {
		return serviceStartDate;
	}

	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}

	public Date getActualServiceEndDate() {
		return actualServiceEndDate;
	}

	public void setActualServiceEndDate(Date actualServiceEndDate) {
		this.actualServiceEndDate = actualServiceEndDate;
	}

	public String getLiftNumber() {
		return liftNumber;
	}

	public void setLiftNumber(String liftNumber) {
		this.liftNumber = liftNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getLiftAddress() {
		return liftAddress;
	}

	public void setLiftAddress(String liftAddress) {
		this.liftAddress = liftAddress;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public Integer getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(Integer registrationType) {
		this.registrationType = registrationType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRegistrationTypeStr() {
		return registrationTypeStr;
	}

	public void setRegistrationTypeStr(String registrationTypeStr) {
		this.registrationTypeStr = registrationTypeStr;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTechnicianDtls() {
		return technicianDtls;
	}

	public void setTechnicianDtls(String technicianDtls) {
		this.technicianDtls = technicianDtls;
	}

	public Integer getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(Integer complaintId) {
		this.complaintId = complaintId;
	}

	public Integer getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getRegistrationDateStr() {
		return registrationDateStr;
	}

	public void setRegistrationDateStr(String registrationDateStr) {
		this.registrationDateStr = registrationDateStr;
	}

	public String getServiceStartDateStr() {
		return serviceStartDateStr;
	}

	public void setServiceStartDateStr(String serviceStartDateStr) {
		this.serviceStartDateStr = serviceStartDateStr;
	}

	public String getActualServiceEndDateStr() {
		return actualServiceEndDateStr;
	}

	public void setActualServiceEndDateStr(String actualServiceEndDateStr) {
		this.actualServiceEndDateStr = actualServiceEndDateStr;
	}

	public Integer getLiftCustoMapId() {
		return liftCustoMapId;
	}

	public void setLiftCustoMapId(Integer liftCustoMapId) {
		this.liftCustoMapId = liftCustoMapId;
	}

	public String getComplaintent() {
		return complaintent;
	}

	public void setComplaintent(String complaintent) {
		this.complaintent = complaintent;
	}

	public String getRegType() {
		return regType;
	}

	public void setRegType(String regType) {
		this.regType = regType;
	}

	public Integer getComplaintTechMapId() {
		return complaintTechMapId;
	}

	public void setComplaintTechMapId(Integer complaintTechMapId) {
		this.complaintTechMapId = complaintTechMapId;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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
	public Integer getServiceCallType() {
		return serviceCallType;
	}
	public void setServiceCallType(Integer serviceCallType) {
		this.serviceCallType = serviceCallType;
	}
}
