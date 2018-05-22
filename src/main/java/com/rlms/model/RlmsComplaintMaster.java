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
@Table(name = "rlms_complaint_master")
public class RlmsComplaintMaster implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer complaintId;
	private String complaintNumber;
	private Date registrationDate;
	private Date serviceStartDate;
	private Date actualServiceEndDate;
	private RlmsLiftCustomerMap liftCustomerMap;
	private Integer registrationType;
	private String title;
	private String remark;
	private Integer status;
	private Integer activeFlag;
	private Date createdDate;
	private Integer createdBy;
	private Integer createdByRef;
	private Date updatedDate;
	private Integer updatedBy;
	private Integer callType;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "complaint_id", unique = true, nullable = false)	
	public Integer getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(Integer complaintId) {
		this.complaintId = complaintId;
	}
	
	@Column(name = "complaint_number", unique = true, nullable = false)
	public String getComplaintNumber() {
		return complaintNumber;
	}
	public void setComplaintNumber(String complaintNumber) {
		this.complaintNumber = complaintNumber;
	}
	
	@Column(name = "registration_date")
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	
	@Column(name = "service_start_date")
	public Date getServiceStartDate() {
		return serviceStartDate;
	}
	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}
	
	@Column(name = "actual_service_end_date")
	public Date getActualServiceEndDate() {
		return actualServiceEndDate;
	}
	public void setActualServiceEndDate(Date actualServiceEndDate) {
		this.actualServiceEndDate = actualServiceEndDate;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "lift_customer_map_id")
	public RlmsLiftCustomerMap getLiftCustomerMap() {
		return liftCustomerMap;
	}
	public void setLiftCustomerMap(RlmsLiftCustomerMap liftCustomerMap) {
		this.liftCustomerMap = liftCustomerMap;
	}
	
	@Column(name = "registration_type")
	public Integer getRegistrationType() {
		return registrationType;
	}
	public void setRegistrationType(Integer registrationType) {
		this.registrationType = registrationType;
	}
	
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "active_flag")
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	
	@Column(name = "created_date")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "created_by")
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "updated_date")
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Column(name = "updated_by")
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name = "title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Column(name = "call_type", nullable = true)	
	public Integer getCallType() {
		return callType;
	}
	public void setCallType(Integer callType) {
		this.callType = callType;
	}
	
	
	
}
