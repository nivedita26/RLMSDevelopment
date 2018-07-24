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
@Table(name="rlms_fya_tran_dtls")
public class RlmsFyaTranDtls {

	private Integer fyaTranId;
	private Integer fyaType;
	private RlmsLiftCustomerMap liftCustomerMap;
	private Integer initiatedBy;
	private Integer pendingWith;
	private Integer status;
	private Integer activeFlag;
	private Date createdDate;
	private Integer createdBy;
	private Integer udpatedBy;
	private Date updatedDate;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "fya_tran_id", unique = true, nullable = false)
	public Integer getFyaTranId() {
		return fyaTranId;
	}
	public void setFyaTranId(Integer fyaTranId) {
		this.fyaTranId = fyaTranId;
	}
	
	
	@Column(name = "fya_type", unique = true, nullable = false)
	public Integer getFyaType() {
		return fyaType;
	}
	
	public void setFyaType(Integer fyaType) {
		this.fyaType = fyaType;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "lift_customer_id")
	public RlmsLiftCustomerMap getLiftCustomerMap() {
		return liftCustomerMap;
	}
	public void setLiftCustomerMap(RlmsLiftCustomerMap liftCustomerMap) {
		this.liftCustomerMap = liftCustomerMap;
	}
	
	@Column(name = "initiated_by", unique = true, nullable = false)
	public Integer getInitiatedBy() {
		return initiatedBy;
	}
	public void setInitiatedBy(Integer initiatedBy) {
		this.initiatedBy = initiatedBy;
	}
	
	@Column(name = "pending_with", unique = true, nullable = false)
	public Integer getPendingWith() {
		return pendingWith;
	}
	public void setPendingWith(Integer pendingWith) {
		this.pendingWith = pendingWith;
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
	
	@Column(name = "updated_by", unique = true, nullable = false)
	public Integer getUdpatedBy() {
		return udpatedBy;
	}
	public void setUdpatedBy(Integer udpatedBy) {
		this.udpatedBy = udpatedBy;
	}
	
	@Column(name = "updated_date", unique = true, nullable = false)
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
}
