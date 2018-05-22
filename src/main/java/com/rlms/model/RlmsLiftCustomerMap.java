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
import javax.persistence.Table;
import javax.persistence.ManyToOne;

import org.hibernate.envers.Audited;

@Entity
@Table(name = "rlms_lift_customer_map")
public class RlmsLiftCustomerMap implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer liftCustomerMapId;
	private RlmsLiftMaster liftMaster;
	private RlmsBranchCustomerMap branchCustomerMap;
	private Integer activeFlag;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "lift_customer_map_id", unique = true, nullable = false)
	public Integer getLiftCustomerMapId() {
		return liftCustomerMapId;
	}
	public void setLiftCustomerMapId(Integer liftCustomerMapId) {
		this.liftCustomerMapId = liftCustomerMapId;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "lift_id")
	public RlmsLiftMaster getLiftMaster() {
		return liftMaster;
	}
	public void setLiftMaster(RlmsLiftMaster liftMaster) {
		this.liftMaster = liftMaster;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "branch_cust_map_id")
	public RlmsBranchCustomerMap getBranchCustomerMap() {
		return branchCustomerMap;
	}
	public void setBranchCustomerMap(RlmsBranchCustomerMap branchCustomerMap) {
		this.branchCustomerMap = branchCustomerMap;
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
}
