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

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;



@Entity
@Table(name="rlms_lift_amc_dtls")
public class RlmsLiftAmcDtls {

	private Integer amcId;
	private RlmsLiftCustomerMap liftCustomerMap;
	private Date amcStartDate;
	private Date amcEndDate;
	private Date amcSlackStartDate;
	private Date amcSlackEndDate;
	private Integer status;
	private Integer amcType;
	private String amcAmount;
	private Integer activeFlag;
	private Integer createdBy;
	private Date craetedDate;
	private Integer updatedBy;
	private Date updatedDate;
	private Date amcDueDate;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "amc_id", unique = true, nullable = false)
	public Integer getAmcId() {
		return amcId;
	}
	public void setAmcId(Integer amcId) {
		this.amcId = amcId;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "lift_customer_map_id")
	public RlmsLiftCustomerMap getLiftCustomerMap() {
		return liftCustomerMap;
	}
	public void setLiftCustomerMap(RlmsLiftCustomerMap liftCustomerMap) {
		this.liftCustomerMap = liftCustomerMap;
	}
	
	@Column(name = "amc_start_date", unique = true, nullable = true)
	public Date getAmcStartDate() {
		return amcStartDate;
	}
	public void setAmcStartDate(Date amcStartDate) {
		this.amcStartDate = amcStartDate;
	}
	
	@Column(name = "amc_end_date", unique = true, nullable = true)
	public Date getAmcEndDate() {
		return amcEndDate;
	}
	public void setAmcEndDate(Date amcEndDate) {
		this.amcEndDate = amcEndDate;
	}
	
	@Column(name = "amc_slack_start_date", unique = true, nullable = true)
	public Date getAmcSlackStartDate() {
		return amcSlackStartDate;
	}
	public void setAmcSlackStartDate(Date amcSlackStartDate) {
		this.amcSlackStartDate = amcSlackStartDate;
	}
	
	@Column(name = "amc_slack_end_date", unique = true, nullable = true)
	public Date getAmcSlackEndDate() {
		return amcSlackEndDate;
	}
	public void setAmcSlackEndDate(Date amcSlackEndDate) {
		this.amcSlackEndDate = amcSlackEndDate;
	}
	
	@Column(name = "status", unique = true, nullable = true)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "active_flag", unique = true, nullable = true)
	public Integer getActiveFlag() {
		return activeFlag;
	}
	
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	@Column(name = "created_by", unique = true, nullable = true)
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "created_date", unique = true, nullable = true)
	public Date getCraetedDate() {
		return craetedDate;
	}
	public void setCraetedDate(Date craetedDate) {
		this.craetedDate = craetedDate;
	}
	
	@Column(name = "updated_by", unique = true, nullable = true)
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name = "updated_date", unique = true, nullable = true)
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Column(name = "amc_due_date", unique = true, nullable = true)
	public Date getAmcDueDate() {
		return amcDueDate;
	}
	public void setAmcDueDate(Date amcDueDate) {
		this.amcDueDate = amcDueDate;
	}
	
	@Column(name = "amc_type", unique = true, nullable = true)
	public Integer getAmcType() {
		return amcType;
	}
	public void setAmcType(Integer amcType) {
		this.amcType = amcType;
	}
	
	@Column(name = "amc_amount", unique = true, nullable = true)
	public String getAmcAmount() {
		return amcAmount;
	}
	public void setAmcAmount(String amcAmount) {
		this.amcAmount = amcAmount;
	}
	
	
}
