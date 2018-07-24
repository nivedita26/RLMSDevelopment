package com.rlms.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rlms_site_visit_dtls")
public class RlmsSiteVisitDtls {

	private Integer siteVisitId;
	private RlmsUserRoles userRoles;
	private Date fromDate;
	private Date toDate;
	private Long totalTime;
	private RlmsComplaintTechMapDtls complaintTechMapDtls;
	private String remark;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "site_visit_id", unique = true, nullable = false)
	public Integer getSiteVisitId() {
		return siteVisitId;
	}
	public void setSiteVisitId(Integer siteVisitId) {
		this.siteVisitId = siteVisitId;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "user_role_id")
	public RlmsUserRoles getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(RlmsUserRoles userRoles) {
		this.userRoles = userRoles;
	}
	
	@Column(name = "from_date", unique = true, nullable = true)
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	@Column(name = "to_date", unique = true, nullable = true)
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	
	
	@Column(name = "total_time", unique = true, nullable = true)
	public Long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Long totalTime) {
		this.totalTime = totalTime;
	}
	
	@Column(name = "remark", unique = true, nullable = true)
	public String getRemark() {
		return this.remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "complaint_tech_map_id")
	public RlmsComplaintTechMapDtls getComplaintTechMapDtls() {
		return complaintTechMapDtls;
	}
	public void setComplaintTechMapDtls(
			RlmsComplaintTechMapDtls complaintTechMapDtls) {
		this.complaintTechMapDtls = complaintTechMapDtls;
	}
	
	@Column(name = "created_date", unique = true, nullable = true)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "created_by", unique = true, nullable = true)
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "updated_date", unique = true, nullable = true)
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Column(name = "updated_by", unique = true, nullable = true)
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	
}
