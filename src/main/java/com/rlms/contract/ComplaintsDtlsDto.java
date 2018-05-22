package com.rlms.contract;

import java.util.Date;
import java.util.List;

public class ComplaintsDtlsDto {

	private Integer branchCompanyMapId;
	private Integer liftCustomerMapId;
	private Integer companyId;
	private Integer branchCustomerMapId;
	private List<Integer> listOfLiftCustoMapId;
	private String complaintsTitle;
	private String complaintsRemark;
	private Integer registrationType;
	private List<Integer> statusList;
	private Integer complaintId;
	private Date fromDate;
	private Date toDate;
	private Integer memberId;
	private Integer serviceCallType;
	
	public Integer getLiftCustomerMapId() {
		return liftCustomerMapId;
	}
	public void setLiftCustomerMapId(Integer liftCustomerMapId) {
		this.liftCustomerMapId = liftCustomerMapId;
	}
	public String getComplaintsTitle() {
		return complaintsTitle;
	}
	public void setComplaintsTitle(String complaintsTitle) {
		this.complaintsTitle = complaintsTitle;
	}
	public String getComplaintsRemark() {
		return complaintsRemark;
	}
	public void setComplaintsRemark(String complaintsRemark) {
		this.complaintsRemark = complaintsRemark;
	}
	public Integer getRegistrationType() {
		return registrationType;
	}
	public void setRegistrationType(Integer registrationType) {
		this.registrationType = registrationType;
	}
	public Integer getBranchCompanyMapId() {
		return branchCompanyMapId;
	}
	public void setBranchCompanyMapId(Integer branchCompanyMapId) {
		this.branchCompanyMapId = branchCompanyMapId;
	}
	public List<Integer> getStatusList() {
		return statusList;
	}
	public void setStatusList(List<Integer> statusList) {
		this.statusList = statusList;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getBranchCustomerMapId() {
		return branchCustomerMapId;
	}
	public void setBranchCustomerMapId(Integer branchCustomerMapId) {
		this.branchCustomerMapId = branchCustomerMapId;
	}
	
	public Integer getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(Integer complaintId) {
		this.complaintId = complaintId;
	}
	public Date getFromDate() {
		return this.fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return this.toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public List<Integer> getListOfLiftCustoMapId() {
		return listOfLiftCustoMapId;
	}
	public void setListOfLiftCustoMapId(List<Integer> listOfLiftCustoMapId) {
		this.listOfLiftCustoMapId = listOfLiftCustoMapId;
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
