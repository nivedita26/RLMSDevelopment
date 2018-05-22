package com.rlms.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="rlms_customer_master")
public class RlmsCustomerMaster implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer customerId;
	private String customerName;
	private String address;
	private String vatNumber;
	private String tinNumber;
	private String panNumber;
	private Integer customerType;
	private String emailID;
	private String cntNumber;
	private String city;
	private String area;
	private Integer pincode;
	private Integer activeFlag;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	
	private String chairmanName;
	private String chairmanNumber;
	private String chairmanEmail;
	private String secretaryName;
	private String secretaryNumber;
	private String secretaryEmail;
	private String treasurerName;
	private String treasurerNumber;
	private String treasurerEmail;
	private String watchmenName;
	private String watchmenNumber;
	private String watchmenEmail;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "customer_id", unique = true, nullable = false)
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
	@Column(name = "customer_name", unique = true, nullable = false)
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	@Column(name = "address", unique = true, nullable = false)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "vat_number", unique = true, nullable = false)
	public String getVatNumber() {
		return vatNumber;
	}
	public void setVatNumber(String vatNumber) {
		this.vatNumber = vatNumber;
	}
	
	@Column(name = "tin_number", unique = true, nullable = false)
	public String getTinNumber() {
		return tinNumber;
	}
	public void setTinNumber(String tinNumber) {
		this.tinNumber = tinNumber;
	}
	
	@Column(name = "pan_number", unique = true, nullable = false)
	public String getPanNumber() {
		return panNumber;
	}
	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}
	
	@Column(name = "customer_type", unique = true, nullable = false)
	public Integer getCustomerType() {
		return customerType;
	}
	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}
	
	@Column(name = "email_id", unique = true, nullable = false)
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	
	@Column(name = "cnt_number", unique = true, nullable = false)
	public String getCntNumber() {
		return cntNumber;
	}
	public void setCntNumber(String cntNumber) {
		this.cntNumber = cntNumber;
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
	
	@Column(name = "city", unique = true, nullable = false)
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "area", unique = true, nullable = false)
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	@Column(name = "pincode", unique = true, nullable = false)
	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}
	
	@Column(name = "chairman_name", unique = true, nullable = false)
	public String getChairmanName() {
		return chairmanName;
	}
	public void setChairmanName(String chairmanName) {
		this.chairmanName = chairmanName;
	}
	
	@Column(name = "chairman_number", unique = true, nullable = false)
	public String getChairmanNumber() {
		return chairmanNumber;
	}
	public void setChairmanNumber(String chairmanNumber) {
		this.chairmanNumber = chairmanNumber;
	}
	
	@Column(name = "chairman_email", unique = true, nullable = false)
	public String getChairmanEmail() {
		return chairmanEmail;
	}
	public void setChairmanEmail(String chairmanEmail) {
		this.chairmanEmail = chairmanEmail;
	}
	
	@Column(name = "secretary_name", unique = true, nullable = false)
	public String getSecretaryName() {
		return secretaryName;
	}
	public void setSecretaryName(String secretaryName) {
		this.secretaryName = secretaryName;
	}
	
	@Column(name = "secretary_number", unique = true, nullable = false)
	public String getSecretaryNumber() {
		return secretaryNumber;
	}
	public void setSecretaryNumber(String secretaryNumber) {
		this.secretaryNumber = secretaryNumber;
	}
	
	@Column(name = "secretary_email", unique = true, nullable = false)
	public String getSecretaryEmail() {
		return secretaryEmail;
	}
	public void setSecretaryEmail(String secretaryEmail) {
		this.secretaryEmail = secretaryEmail;
	}
	
	@Column(name = "treasurer_name", unique = true, nullable = false)
	public String getTreasurerName() {
		return treasurerName;
	}
	public void setTreasurerName(String treasurerName) {
		this.treasurerName = treasurerName;
	}
	
	@Column(name = "treasurer_number", unique = true, nullable = false)
	public String getTreasurerNumber() {
		return treasurerNumber;
	}
	public void setTreasurerNumber(String treasurerNumber) {
		this.treasurerNumber = treasurerNumber;
	}
	
	@Column(name = "treasurer_email", unique = true, nullable = false)
	public String getTreasurerEmail() {
		return treasurerEmail;
	}
	public void setTreasurerEmail(String treasurerEmail) {
		this.treasurerEmail = treasurerEmail;
	}
	
	@Column(name = "watchmen_name", unique = true, nullable = false)
	public String getWatchmenName() {
		return watchmenName;
	}
	public void setWatchmenName(String watchmenName) {
		this.watchmenName = watchmenName;
	}
	
	@Column(name = "watchmen_number", unique = true, nullable = false)
	public String getWatchmenNumber() {
		return watchmenNumber;
	}
	public void setWatchmenNumber(String watchmenNumber) {
		this.watchmenNumber = watchmenNumber;
	}
	
	@Column(name = "watchmen_email", unique = true, nullable = false)
	public String getWatchmenEmail() {
		return watchmenEmail;
	}
	public void setWatchmenEmail(String watchmenEmail) {
		this.watchmenEmail = watchmenEmail;
	}
	
}
