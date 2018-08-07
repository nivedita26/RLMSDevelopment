package com.rlms.model;

import static javax.persistence.GenerationType.IDENTITY;
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
	@Table(name = "rlms_lift_manual_map")
	public class RlmsLiftManualMapDtls {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "lift_manual_map_id", unique = true, nullable = false)
	private Integer liftManualMapId;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "lift_customer_map_id")
	private RlmsLiftCustomerMap  rlmsLiftCustomerMap;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "company_manual_map_id")
	private RlmsCompanyManual  companyManual;
	
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
	
	public RlmsLiftCustomerMap getRlmsLiftCustomerMap() {
		return rlmsLiftCustomerMap;
	}
	public void setRlmsLiftCustomerMap(RlmsLiftCustomerMap rlmsLiftCustomerMap) {
		this.rlmsLiftCustomerMap = rlmsLiftCustomerMap;
	}
	public RlmsCompanyManual getCompanyManual() {
		return companyManual;
	}
	public void setCompanyManual(RlmsCompanyManual companyManual) {
		this.companyManual = companyManual;
	}
	public Date getCreatedDate() {
		return createdDate;
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
	public Integer getLiftManualMapId() {
		return liftManualMapId;
	}
	public void setLiftManualMapId(Integer liftManualMapId) {
		this.liftManualMapId = liftManualMapId;
	}
	
}
