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
@Table(name = "rlms_company_branch_map")
public class RlmsCompanyBranchMapDtls implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer companyBranchMapId;
	private RlmsBranchMaster rlmsBranchMaster;
	private RlmsCompanyMaster rlmsCompanyMaster;
	private Integer status;
	private Integer activeFlag;
	private Date createdDate;
	private Integer createdBy;
	private Date udpatedDate;
	private Integer updatedBy;
	
	public RlmsCompanyBranchMapDtls() {
		super();
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "company_branch_map_id", unique = true, nullable = false)
	public Integer getCompanyBranchMapId() {
		return companyBranchMapId;
	}

	public void setCompanyBranchMapId(Integer companyBranchMapId) {
		this.companyBranchMapId = companyBranchMapId;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "branch_id")
	public RlmsBranchMaster getRlmsBranchMaster() {
		return rlmsBranchMaster;
	}

	public void setRlmsBranchMaster(RlmsBranchMaster rlmsBranchMaster) {
		this.rlmsBranchMaster = rlmsBranchMaster;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "company_id")
	public RlmsCompanyMaster getRlmsCompanyMaster() {
		return rlmsCompanyMaster;
	}

	public void setRlmsCompanyMaster(RlmsCompanyMaster rlmsCompanyMaster) {
		this.rlmsCompanyMaster = rlmsCompanyMaster;
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
	public Date getUdpatedDate() {
		return udpatedDate;
	}

	public void setUdpatedDate(Date udpatedDate) {
		this.udpatedDate = udpatedDate;
	}

	@Column(name = "updated_by", unique = true, nullable = false)
	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
}
