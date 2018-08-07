package com.rlms.contract;
import java.sql.Blob;

public class CompanyManualDto{
	
private Integer liftType;
private Blob userManual;
private Blob safetyGuide;
private Integer  companyId;
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
	public Blob getUserManual() {
		return userManual;
}
	public void setUserManual(Blob userManual) {
		this.userManual = userManual;
}
	public Blob getSafetyGuide() {
		return safetyGuide;
}
	public void setSafetyGuide(Blob safetyGuide) {
		this.safetyGuide = safetyGuide;
}
	public Integer getCompanyId() {
		return companyId;
}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
}
}
