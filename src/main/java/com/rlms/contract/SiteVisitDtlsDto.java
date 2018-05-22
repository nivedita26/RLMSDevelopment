package com.rlms.contract;

import java.util.Date;

public class SiteVisitDtlsDto {

	private Integer userRoleId;
	private Integer complaintTechMapId;
	private Date fromDate;
	private Date toDate;
	private String fromDateDtr;
	private String toDateStr;
	private String totalTime;
	private String remark;
	
	public Integer getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}
	public Integer getComplaintTechMapId() {
		return complaintTechMapId;
	}
	public void setComplaintTechMapId(Integer complaintTechMapId) {
		this.complaintTechMapId = complaintTechMapId;
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
	public String getFromDateDtr() {
		return fromDateDtr;
	}
	public void setFromDateDtr(String fromDateDtr) {
		this.fromDateDtr = fromDateDtr;
	}
	public String getToDateStr() {
		return toDateStr;
	}
	public void setToDateStr(String toDateStr) {
		this.toDateStr = toDateStr;
	}
	public String getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
