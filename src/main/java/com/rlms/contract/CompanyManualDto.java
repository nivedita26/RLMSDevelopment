package com.rlms.contract;

public class CompanyManualDto{
	
private Integer liftType;
private byte[] userManual;
private byte[] safetyGuide;
private Integer  companyId;
private String modelVersion;

public String getModelVersion() {
	return modelVersion;
}
public void setModelVersion(String modelVersion) {
	this.modelVersion = modelVersion;
}
private Integer liftCustomerMapId;
private Integer companyManualMapId;

public Integer getLiftCustomerMapId() {
	return liftCustomerMapId;
}
public void setLiftCustomerMapId(Integer liftCustomerMapId) {
	this.liftCustomerMapId = liftCustomerMapId;
}
public Integer getCompanyManualMapId() {
	return companyManualMapId;
}
public void setCompanyManualMapId(Integer companyManualMapId) {
	this.companyManualMapId = companyManualMapId;
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
	public Integer getCompanyId() {
		return companyId;
}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
}
}
