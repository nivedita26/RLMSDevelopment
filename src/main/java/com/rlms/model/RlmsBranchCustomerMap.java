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
@Table(name="rlms_branch_customer_map")
public class RlmsBranchCustomerMap implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer branchCustoMapId;
	private RlmsCompanyBranchMapDtls companyBranchMapDtls;
	private RlmsCustomerMaster customerMaster;
	private Integer activeFlag;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "branch_customer_map_id", unique = true, nullable = false)
	public Integer getBranchCustoMapId() {
		return branchCustoMapId;
	}
	public void setBranchCustoMapId(Integer branchCustoMapId) {
		this.branchCustoMapId = branchCustoMapId;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "branch_company_map_id")
	public RlmsCompanyBranchMapDtls getCompanyBranchMapDtls() {
		return companyBranchMapDtls;
	}
	public void setCompanyBranchMapDtls(
			RlmsCompanyBranchMapDtls companyBranchMapDtls) {
		this.companyBranchMapDtls = companyBranchMapDtls;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "customer_id")
	public RlmsCustomerMaster getCustomerMaster() {
		return customerMaster;
	}
	
	public void setCustomerMaster(RlmsCustomerMaster customerMaster) {
		this.customerMaster = customerMaster;
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
