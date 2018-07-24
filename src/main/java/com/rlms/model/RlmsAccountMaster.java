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
@Table(name = "rlms_account_m")
public class RlmsAccountMaster implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long accountId;
	private String accountName;
	private Date serviceStartDate;
	private Date plannedServiceEndDate;
	private Integer maxAllowedLifts;
	private Integer maxAllowedTechnicians;
	private RlmsCompanyMaster rlmsCompanyMaster;
	private long amountPaid;
	private int status;
	private int activeFlag;
	private Date updatedDate;
	private Long updatedBy;
	private Date createdDate;
	private Long createdBy;
	
	public RlmsAccountMaster() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "account_id", unique = true, nullable = false)
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	@Column(name = "account_name", unique = true, nullable = false)
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	@Column(name = "service_start_date", unique = true, nullable = false)
	public Date getServiceStartDate() {
		return serviceStartDate;
	}

	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}

	@Column(name = "planned_service_end_date", unique = true, nullable = false)
	public Date getPlannedServiceEndDate() {
		return plannedServiceEndDate;
	}

	public void setPlannedServiceEndDate(Date plannedServiceEndDate) {
		this.plannedServiceEndDate = plannedServiceEndDate;
	}

	@Column(name = "max_allowed_lifts", unique = true, nullable = false)
	public Integer getMaxAllowedLifts() {
		return maxAllowedLifts;
	}

	public void setMaxAllowedLifts(Integer maxAllowedLifts) {
		this.maxAllowedLifts = maxAllowedLifts;
	}

	@Column(name = "max_allowed_technicians", unique = true, nullable = false)
	public Integer getMaxAllowedTechnicians() {
		return maxAllowedTechnicians;
	}

	public void setMaxAllowedTechnicians(Integer maxAllowedTechnicians) {
		this.maxAllowedTechnicians = maxAllowedTechnicians;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "company_id")
	public RlmsCompanyMaster getRlmsCompanyMaster() {
		return rlmsCompanyMaster;
	}

	public void setRlmsCompanyMaster(RlmsCompanyMaster rlmsCompanyMaster) {
		this.rlmsCompanyMaster = rlmsCompanyMaster;
	}

	@Column(name = "amount_paid", unique = true, nullable = false)
	public long getAmountPaid() {
		return amountPaid;
	}

	

	public void setAmountPaid(long amountPaid) {
		this.amountPaid = amountPaid;
	}

	@Column(name = "status", unique = true, nullable = false)
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "acive_flag", unique = true, nullable = false)
	public int getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(int activeFlag) {
		this.activeFlag = activeFlag;
	}

	@Column(name = "updated_date", unique = true, nullable = false)
	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "updated_by", unique = true, nullable = false)
	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Column(name = "created_date", unique = true, nullable = false)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "created_by", unique = true, nullable = false)
	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "RlmsAccountMaster [accountId=" + accountId + ", accountName="
				+ accountName + ", serviceStartDate=" + serviceStartDate
				+ ", plannedServiceEndDate=" + plannedServiceEndDate
				+ ", maxAllowedLifts=" + maxAllowedLifts
				+ ", maxAllowedTechnicians=" + maxAllowedTechnicians
				+ ", rlmsCompanyMaster=" + rlmsCompanyMaster + ", amountPaid="
				+ amountPaid + ", status=" + status + ", activeFlag="
				+ activeFlag + ", updatedDate=" + updatedDate + ", updatedBy="
				+ updatedBy + ", createdDate=" + createdDate + ", createdBy="
				+ createdBy + "]";
	}

	

	
	
	
}
