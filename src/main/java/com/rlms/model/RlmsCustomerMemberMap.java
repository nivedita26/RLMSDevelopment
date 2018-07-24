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
@Table(name = "rlms_customer_member_map")
public class RlmsCustomerMemberMap {

	private Integer customerMemberMapId;
	private RlmsBranchCustomerMap rlmsBranchCustomerMap;
	private RlmsMemberMaster rlmsMemberMaster;
	private Integer activeFlag;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "custo_member_map_id", unique = true, nullable = false)
	public Integer getCustomerMemberMapId() {
		return customerMemberMapId;
	}

	public void setCustomerMemberMapId(Integer customerMemberMapId) {
		this.customerMemberMapId = customerMemberMapId;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "branch_customer_map_id")
	public RlmsBranchCustomerMap getRlmsBranchCustomerMap() {
		return rlmsBranchCustomerMap;
	}

	public void setRlmsBranchCustomerMap(RlmsBranchCustomerMap rlmsBranchCustomerMap) {
		this.rlmsBranchCustomerMap = rlmsBranchCustomerMap;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "member_id")
	public RlmsMemberMaster getRlmsMemberMaster() {
		return rlmsMemberMaster;
	}	

	public void setRlmsMemberMaster(RlmsMemberMaster rlmsMemberMaster) {
		this.rlmsMemberMaster = rlmsMemberMaster;
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
