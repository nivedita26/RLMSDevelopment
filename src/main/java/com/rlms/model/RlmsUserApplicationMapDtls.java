package com.rlms.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rlms_user_application_map_dtls")
public class RlmsUserApplicationMapDtls {

	private Integer userAppMapId;
	private Integer userId;
	private Integer userRefType;
	private String appRegId;
	private Double latitude;
	private Double longitude;
	private String address;
	private Integer activeFlag;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
    private Date updatedDate;
    
    @Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_app_map_id", unique = true, nullable = false)
	public Integer getUserAppMapId() {
		return userAppMapId;
	}
	public void setUserAppMapId(Integer userAppMapId) {
		this.userAppMapId = userAppMapId;
	}
	
	@Column(name = "user_id", unique = true, nullable = false)
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	@Column(name = "user_ref_type", unique = true, nullable = false)
	public Integer getUserRefType() {
		return userRefType;
	}
	public void setUserRefType(Integer userRefType) {
		this.userRefType = userRefType;
	}
	
	@Column(name = "app_reg_id", unique = true, nullable = false)
	public String getAppRegId() {
		return appRegId;
	}
	public void setAppRegId(String appRegId) {
		this.appRegId = appRegId;
	}
	
	@Column(name = "latitude", unique = true, nullable = false)
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	
	@Column(name = "longitude", unique = true, nullable = false)
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	@Column(name = "active_flag", unique = true, nullable = false)
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	@Column(name = "created_by", unique = true, nullable = false)
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "created_date", unique = true, nullable = false)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "updated_by", unique = true, nullable = false)
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name = "updated_date", unique = true, nullable = false)
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Column(name = "address", unique = true, nullable = false)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
