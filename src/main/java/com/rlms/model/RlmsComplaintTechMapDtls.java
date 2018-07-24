package com.rlms.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
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
@Table(name = "rlms_complaint_tech_map_dtls")
public class RlmsComplaintTechMapDtls implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer complaintTechMapId;
	private RlmsComplaintMaster complaintMaster;
	private RlmsUserRoles userRoles;
	private Date assignedDate;
	private Date plannedEndDate;
	private Integer userRating;
	private Integer activeFlag;
	private Integer status;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "complaint_tech_map_id", unique = true, nullable = false)	
	public Integer getComplaintTechMapId() {
		return complaintTechMapId;
	}
	public void setComplaintTechMapId(Integer complaintTechMapId) {
		this.complaintTechMapId = complaintTechMapId;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "complaint_id", nullable = false)
	public RlmsComplaintMaster getComplaintMaster() {
		return complaintMaster;
	}
	public void setComplaintMaster(RlmsComplaintMaster complaintMaster) {
		this.complaintMaster = complaintMaster;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "user_role_id", nullable = false)
	public RlmsUserRoles getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(RlmsUserRoles userRoles) {
		this.userRoles = userRoles;
	}
	
	@Column(name = "assigned_date", unique = true, nullable = false)	
	public Date getAssignedDate() {
		return assignedDate;
	}
	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}
	
	@Column(name = "planned_end_date", unique = true, nullable = true)	
	public Date getPlannedEndDate() {
		return plannedEndDate;
	}
	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}
	
	@Column(name = "status", unique = true, nullable = false)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "active_flag", unique = true, nullable = false)
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	
	@Column(name = "created_date", unique = true, nullable = false)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "created_by", unique = true, nullable = false)
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "updated_date", unique = true, nullable = false)
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Column(name = "updated_by", unique = true, nullable = false)
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name = "user_rating", unique = true, nullable = true)
	public Integer getUserRating() {
		return userRating;
	}
	public void setUserRating(Integer userRating) {
		this.userRating = userRating;
	}
	
	
	
}
