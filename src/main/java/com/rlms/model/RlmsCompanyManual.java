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
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rlms_company_manual")
public class RlmsCompanyManual implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "company_manual_id", unique = true, nullable = false)
	private Integer companyManualId;
	
	@Column(name="lift_type")
	private Integer liftType;
	
	@Column(name="user_manual")
     private byte[]  userManual;
	
	@Column(name="safety_guide")
	private byte[]  safetyGuide;
		
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "company_id")
	private RlmsCompanyMaster rlmsCompanyMaster;

	@Column(name="created_date")
	private Date createdDate;	
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="active_flag")
	private Integer activeFlag;
	
	@Column(name="model_version")
    private String modelVersion;
	
	public String getModelVersion() {
		return modelVersion;
	}

	public void setModelVersion(String modelVersion) {
		this.modelVersion = modelVersion;
	}

	public Integer getCompanyManualId() {
		return companyManualId;
	}

	public void setCompanyManualId(Integer companyManualId) {
		this.companyManualId = companyManualId;
	}

	public Integer getLiftType() {
		return liftType;
	}

	public void setLiftType(Integer liftType) {
		this.liftType = liftType;
	}
	public byte[] getUserManual() {
		return userManual;
	}

	public void setUserManual(byte[] userManual) {
		this.userManual = userManual;
	}

	public byte[] getSafetyGuide() {
		return safetyGuide;
	}

	public void setSafetyGuide(byte[] safetyGuide) {
		this.safetyGuide = safetyGuide;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}

	
	public RlmsCompanyMaster getRlmsCompanyMaster() {
		return rlmsCompanyMaster;
	}

	public void setRlmsCompanyMaster(RlmsCompanyMaster rlmsCompanyMaster) {
		this.rlmsCompanyMaster = rlmsCompanyMaster;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
}
