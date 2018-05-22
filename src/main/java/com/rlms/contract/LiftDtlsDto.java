package com.rlms.contract;

import java.math.BigInteger;
import java.util.Date;

public class LiftDtlsDto {

	private String liftNumber;
	private String address;
	private String customerName;
	private String branchName;
	private String companyName;
	private String city;
	private String area;
	private Integer pinCode;
	private Integer companyId;
	private Integer branchCompanyMapId;
	private Integer branchCustomerMapId;
	
	private Integer liftId;
	private String latitude;
	private String longitude;
	private Date serviceStartDate;
	private String serviceStartDateStr;
	private Date serviceEndDate;
	private String serviceEndDateStr;
	private Date dateOfInstallation;
	private String dateOfInstallationStr;
	private Date amcStartDate;
	private Date amcEndDate;
	private String amcStartDateStr;
	private String amcEndDateStr;
	private Integer amcType;
	private String amcTypeStr;
	private String amcAmount;
	private Integer doorType;
	private String noOfStops;
	private Integer engineType; 
	private String machineMake;
	private String machineCapacity;
	private String machineCurrent;
	private byte[] machinePhoto;
	private String breakVoltage;
	private String panelMake;
	private byte[] panelPhoto;
	private String ard;
	private byte[] ardPhoto;
	private Integer noOfBatteries;
	private String batteryCapacity;
	private String batteryMake;
	private String copMake;
	private byte[] copPhoto;
	private String  lopMake;
	private byte[] lopPhoto;
	private Integer collectiveType;
	private Integer simplexDuplex;
	private byte[] cartopPhoto;
	private String autoDoorMake;
	private byte[] autoDoorHeaderPhoto;
	private Integer wiringShceme;
	private byte[] wiringPhoto;
	private Integer fireMode;
	private String intercomm;
	private Integer alarm;
	private String alarmBattery;
	private String accessControl;
	private byte[] lobbyPhoto;
	private Integer fyaTranId;
	private Integer liftCustomerMapId;
	private Integer photoType;
	private Integer liftType;
	private boolean isBlank;
	private Integer activeFlag;
	private String imei;
	private BigInteger totalLiftCountForCustomer;
	
	public BigInteger getTotalLiftCountForCustomer() {
		return totalLiftCountForCustomer;
	}
	public void setTotalLiftCountForCustomer(BigInteger totalLiftCountForCustomer) {
		this.totalLiftCountForCustomer = totalLiftCountForCustomer;
	}
	public String getLiftNumber() {
		return liftNumber;
	}
	public void setLiftNumber(String liftNumber) {
		this.liftNumber = liftNumber;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getBranchName() {
		return branchName;
	}
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getBranchCompanyMapId() {
		return branchCompanyMapId;
	}
	public void setBranchCompanyMapId(Integer branchCompanyMapId) {
		this.branchCompanyMapId = branchCompanyMapId;
	}
	public Integer getBranchCustomerMapId() {
		return branchCustomerMapId;
	}
	public void setBranchCustomerMapId(Integer branchCustomerMapId) {
		this.branchCustomerMapId = branchCustomerMapId;
	}
	public Integer getLiftId() {
		return liftId;
	}
	public void setLiftId(Integer liftId) {
		this.liftId = liftId;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public Date getServiceStartDate() {
		return serviceStartDate;
	}
	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}
	public String getServiceStartDateStr() {
		return serviceStartDateStr;
	}
	public void setServiceStartDateStr(String serviceStartDateStr) {
		this.serviceStartDateStr = serviceStartDateStr;
	}
	public Date getServiceEndDate() {
		return serviceEndDate;
	}
	public void setServiceEndDate(Date serviceEndDate) {
		this.serviceEndDate = serviceEndDate;
	}
	public String getServiceEndDateStr() {
		return serviceEndDateStr;
	}
	public void setServiceEndDateStr(String serviceEndDateStr) {
		this.serviceEndDateStr = serviceEndDateStr;
	}
	public Date getDateOfInstallation() {
		return dateOfInstallation;
	}
	public void setDateOfInstallation(Date dateOfInstallation) {
		this.dateOfInstallation = dateOfInstallation;
	}
	public String getDateOfInstallationStr() {
		return dateOfInstallationStr;
	}
	public void setDateOfInstallationStr(String dateOfInstallationStr) {
		this.dateOfInstallationStr = dateOfInstallationStr;
	}
	public Date getAmcStartDate() {
		return amcStartDate;
	}
	public void setAmcStartDate(Date amcStartDate) {
		this.amcStartDate = amcStartDate;
	}
	public String getAmcStartDateStr() {
		return amcStartDateStr;
	}
	public void setAmcStartDateStr(String amcStartDateStr) {
		this.amcStartDateStr = amcStartDateStr;
	}
	public Integer getAmcType() {
		return amcType;
	}
	public void setAmcType(Integer amcType) {
		this.amcType = amcType;
	}
	public String getAmcAmount() {
		return amcAmount;
	}
	public void setAmcAmount(String amcAmount) {
		this.amcAmount = amcAmount;
	}
	public Integer getDoorType() {
		return doorType;
	}
	public void setDoorType(Integer doorType) {
		this.doorType = doorType;
	}
	public String getNoOfStops() {
		return noOfStops;
	}
	public void setNoOfStops(String noOfStops) {
		this.noOfStops = noOfStops;
	}
	public Integer getEngineType() {
		return engineType;
	}
	public void setEngineType(Integer engineType) {
		this.engineType = engineType;
	}
	public String getMachineMake() {
		return machineMake;
	}
	public void setMachineMake(String machineMake) {
		this.machineMake = machineMake;
	}
	public String getMachineCapacity() {
		return machineCapacity;
	}
	public void setMachineCapacity(String machineCapacity) {
		this.machineCapacity = machineCapacity;
	}
	public String getMachineCurrent() {
		return machineCurrent;
	}
	public void setMachineCurrent(String machineCurrent) {
		this.machineCurrent = machineCurrent;
	}
	public byte[] getMachinePhoto() {
		return machinePhoto;
	}
	public void setMachinePhoto(byte[] machinePhoto) {
		this.machinePhoto = machinePhoto;
	}
	public String getBreakVoltage() {
		return breakVoltage;
	}
	public void setBreakVoltage(String breakVoltage) {
		this.breakVoltage = breakVoltage;
	}
	public String getPanelMake() {
		return panelMake;
	}
	public void setPanelMake(String panelMake) {
		this.panelMake = panelMake;
	}
	public byte[] getPanelPhoto() {
		return panelPhoto;
	}
	public void setPanelPhoto(byte[] panelPhoto) {
		this.panelPhoto = panelPhoto;
	}
	public String getArd() {
		return ard;
	}
	public void setArd(String ard) {
		this.ard = ard;
	}
	public byte[] getArdPhoto() {
		return ardPhoto;
	}
	public void setArdPhoto(byte[] ardPhoto) {
		this.ardPhoto = ardPhoto;
	}
	public Integer getNoOfBatteries() {
		return noOfBatteries;
	}
	public void setNoOfBatteries(Integer noOfBatteries) {
		this.noOfBatteries = noOfBatteries;
	}
	public String getBatteryCapacity() {
		return batteryCapacity;
	}
	public void setBatteryCapacity(String batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}
	public String getBatteryMake() {
		return batteryMake;
	}
	public void setBatteryMake(String batteryMake) {
		this.batteryMake = batteryMake;
	}
	public String getCopMake() {
		return copMake;
	}
	public void setCopMake(String copMake) {
		this.copMake = copMake;
	}
	public byte[] getCopPhoto() {
		return copPhoto;
	}
	public void setCopPhoto(byte[] copPhoto) {
		this.copPhoto = copPhoto;
	}
	public String getLopMake() {
		return lopMake;
	}
	public void setLopMake(String lopMake) {
		this.lopMake = lopMake;
	}
	public byte[] getLopPhoto() {
		return lopPhoto;
	}
	public void setLopPhoto(byte[] lopPhoto) {
		this.lopPhoto = lopPhoto;
	}
	public Integer getCollectiveType() {
		return collectiveType;
	}
	public void setCollectiveType(Integer collectiveType) {
		this.collectiveType = collectiveType;
	}
	public Integer getSimplexDuplex() {
		return simplexDuplex;
	}
	public void setSimplexDuplex(Integer simplexDuplex) {
		this.simplexDuplex = simplexDuplex;
	}
	public byte[] getCartopPhoto() {
		return cartopPhoto;
	}
	public void setCartopPhoto(byte[] cartopPhoto) {
		this.cartopPhoto = cartopPhoto;
	}
	public String getAutoDoorMake() {
		return autoDoorMake;
	}
	public void setAutoDoorMake(String autoDoorMake) {
		this.autoDoorMake = autoDoorMake;
	}
	public byte[] getAutoDoorHeaderPhoto() {
		return autoDoorHeaderPhoto;
	}
	public void setAutoDoorHeaderPhoto(byte[] autoDoorHeaderPhoto) {
		this.autoDoorHeaderPhoto = autoDoorHeaderPhoto;
	}
	public Integer getWiringShceme() {
		return wiringShceme;
	}
	public void setWiringShceme(Integer wiringShceme) {
		this.wiringShceme = wiringShceme;
	}
	public byte[] getWiringPhoto() {
		return wiringPhoto;
	}
	public void setWiringPhoto(byte[] wiringPhoto) {
		this.wiringPhoto = wiringPhoto;
	}
	public Integer getFireMode() {
		return fireMode;
	}
	public void setFireMode(Integer fireMode) {
		this.fireMode = fireMode;
	}
	public String getIntercomm() {
		return intercomm;
	}
	public void setIntercomm(String intercomm) {
		this.intercomm = intercomm;
	}
	public Integer getAlarm() {
		return alarm;
	}
	public void setAlarm(Integer alarm) {
		this.alarm = alarm;
	}
	public String getAlarmBattery() {
		return alarmBattery;
	}
	public void setAlarmBattery(String alarmBattery) {
		this.alarmBattery = alarmBattery;
	}
	public String getAccessControl() {
		return accessControl;
	}
	public void setAccessControl(String accessControl) {
		this.accessControl = accessControl;
	}
	public byte[] getLobbyPhoto() {
		return lobbyPhoto;
	}
	public void setLobbyPhoto(byte[] lobbyPhoto) {
		this.lobbyPhoto = lobbyPhoto;
	}
	public Integer getFyaTranId() {
		return fyaTranId;
	}
	public void setFyaTranId(Integer fyaTranId) {
		this.fyaTranId = fyaTranId;
	}
	public Integer getLiftCustomerMapId() {
		return liftCustomerMapId;
	}
	public void setLiftCustomerMapId(Integer liftCustomerMapId) {
		this.liftCustomerMapId = liftCustomerMapId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Integer getPinCode() {
		return pinCode;
	}
	public void setPinCode(Integer pinCode) {
		this.pinCode = pinCode;
	}
	public Integer getPhotoType() {
		return photoType;
	}
	public void setPhotoType(Integer photoType) {
		this.photoType = photoType;
	}
	public Integer getLiftType() {
		return liftType;
	}
	public void setLiftType(Integer liftType) {
		this.liftType = liftType;
	}
	public boolean isBlank() {
		return isBlank;
	}
	public void setBlank(boolean isBlank) {
		this.isBlank = isBlank;
	}
	public Date getAmcEndDate() {
		return amcEndDate;
	}
	public void setAmcEndDate(Date amcEndDate) {
		this.amcEndDate = amcEndDate;
	}
	public String getAmcEndDateStr() {
		return amcEndDateStr;
	}
	public void setAmcEndDateStr(String amcEndDateStr) {
		this.amcEndDateStr = amcEndDateStr;
	}
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	public String getAmcTypeStr() {
		return amcTypeStr;
	}
	public void setAmcTypeStr(String amcTypeStr) {
		this.amcTypeStr = amcTypeStr;
	}	 
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	
	public String lmsEventFromContactNo;

	public String getLmsEventFromContactNo() {
		return lmsEventFromContactNo;
	}
	public void setLmsEventFromContactNo(String lmsEventFromContactNo) {
		this.lmsEventFromContactNo = lmsEventFromContactNo;
	}
	
}
