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
@Table(name = "rlms_company_role_map")
public class RlmsCompanyRoleMap implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer companyRoleId;
	private RlmsCompanyBranchMapDtls rlmsCompanyBranchMapDtls;
	private RlmsSpocRoleMaster rlmsSpocRoleMaster;
	private RlmsCompanyMaster rlmsCompanyMaster;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	
	public RlmsCompanyRoleMap() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "company_role_id", unique = true, nullable = false)
	public Integer getCompanyRoleId() {
		return companyRoleId;
	}

	public void setCompanyRoleId(Integer companyRoleId) {
		this.companyRoleId = companyRoleId;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "company_branch_map_id")
	public RlmsCompanyBranchMapDtls getRlmsCompanyBranchMapDtls() {
		return rlmsCompanyBranchMapDtls;
	}

	public void setRlmsCompanyBranchMapDtls(
			RlmsCompanyBranchMapDtls rlmsCompanyBranchMapDtls) {
		this.rlmsCompanyBranchMapDtls = rlmsCompanyBranchMapDtls;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "spoc_role_id")
	public RlmsSpocRoleMaster getRlmsSpocRoleMaster() {
		return rlmsSpocRoleMaster;
	}

	public void setRlmsSpocRoleMaster(RlmsSpocRoleMaster rlmsSpocRoleMaster) {
		this.rlmsSpocRoleMaster = rlmsSpocRoleMaster;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "company_id")
	public RlmsCompanyMaster getRlmsCompanyMaster() {
		return rlmsCompanyMaster;
	}

	public void setRlmsCompanyMaster(RlmsCompanyMaster rlmsCompanyMaster) {
		this.rlmsCompanyMaster = rlmsCompanyMaster;
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
