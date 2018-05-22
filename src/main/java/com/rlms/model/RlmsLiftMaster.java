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
@Table(name = "rlms_lift_master")
public class RlmsLiftMaster implements Serializable{

	private static final long serialVersionUID = 1L;
	private Integer liftId;
	private String liftNumber;
	private String address;
	private String city;
	private String area;
	private Integer pincode;
	private String latitude;
	private String longitude;
	private Date serviceStartDate;
	private Date serviceEndDate;
	private Date dateOfInstallation; 
	
	private Date amcStartDate;
	private Date amcEndDate;
	private Integer amcType;
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
	private String ARD;
	private byte[] ARDPhoto;
	private Integer NoOfBatteries;
	private String BatteryCapacity;
	private String BatteryMake;
	private String COPMake;
	private byte[] COPPhoto;
	private String  LOPMake;
	private byte[] LOPPhoto;	
	private Integer collectiveType;
	private Integer SimplexDuplex;
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
	private Integer liftType;
	private Integer status;
	private Integer activeFlag;
	private Date createdDate;
	private Integer createdBy;
	private Date updatedDate;
	private Integer updatedBy;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "lift_id", unique = true, nullable = true)
	public Integer getLiftId() {
		return liftId;
	}
	public void setLiftId(Integer liftId) {
		this.liftId = liftId;
	}
	
	@Column(name = "lift_number", unique = true, nullable = true)
	public String getLiftNumber() {
		return liftNumber;
	}
	public void setLiftNumber(String liftNumber) {
		this.liftNumber = liftNumber;
	}
	
	@Column(name = "address", unique = true, nullable = true)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name = "latitude", unique = true, nullable = true)
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	@Column(name = "longitude", unique = true, nullable = true)
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	@Column(name = "service_start_date", unique = true, nullable = true)
	public Date getServiceStartDate() {
		return serviceStartDate;
	}
	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}
	
	@Column(name = "service_end_date", unique = true, nullable = true)
	public Date getServiceEndDate() {
		return serviceEndDate;
	}
	public void setServiceEndDate(Date serviceEndDate) {
		this.serviceEndDate = serviceEndDate;
	}
	
	@Column(name = "date_of_installation", unique = true, nullable = true)
	public Date getDateOfInstallation() {
		return dateOfInstallation;
	}
	public void setDateOfInstallation(Date dateOfInstallation) {
		this.dateOfInstallation = dateOfInstallation;
	}
	
	@Column(name = "amc_start_date", unique = true, nullable = true)
	public Date getAmcStartDate() {
		return amcStartDate;
	}
	public void setAmcStartDate(Date amcStartDate) {
		this.amcStartDate = amcStartDate;
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
	
	@Column(name = "door_type", unique = true, nullable = true)
	public Integer getDoorType() {
		return doorType;
	}
	public void setDoorType(Integer doorType) {
		this.doorType = doorType;
	}
	
	@Column(name = "no_of_stops", unique = true, nullable = true)
	public String getNoOfStops() {
		return noOfStops;
	}
	public void setNoOfStops(String noOfStops) {
		this.noOfStops = noOfStops;
	}
	
	@Column(name = "engine_type", unique = true, nullable = true)
	public Integer getEngineType() {
		return engineType;
	}
	public void setEngineType(Integer engineType) {
		this.engineType = engineType;
	}
	
	@Column(name = "machine_make", unique = true, nullable = true)
	public String getMachineMake() {
		return machineMake;
	}
	public void setMachineMake(String machineMake) {
		this.machineMake = machineMake;
	}
	
	@Column(name = "machine_capacity", unique = true, nullable = true)
	public String getMachineCapacity() {
		return machineCapacity;
	}
	public void setMachineCapacity(String machineCapacity) {
		this.machineCapacity = machineCapacity;
	}
	
	@Column(name = "machine_current", unique = true, nullable = true)
	public String getMachineCurrent() {
		return machineCurrent;
	}
	public void setMachineCurrent(String machineCurrent) {
		this.machineCurrent = machineCurrent;
	}
	
	@Column(name = "machine_photo", unique = true, nullable = true)
	public byte[] getMachinePhoto() {
		return machinePhoto;
	}
	public void setMachinePhoto(byte[] machinePhoto) {
		this.machinePhoto = machinePhoto;
	}
	
	@Column(name = "break_voltage", unique = true, nullable = true)
	public String getBreakVoltage() {
		return breakVoltage;
	}
	public void setBreakVoltage(String breakVoltage) {
		this.breakVoltage = breakVoltage;
	}
	
	@Column(name = "panel_make", unique = true, nullable = true)
	public String getPanelMake() {
		return panelMake;
	}
	public void setPanelMake(String panelMake) {
		this.panelMake = panelMake;
	}
	
	@Column(name = "panel_photo", unique = true, nullable = true)
	public byte[] getPanelPhoto() {
		return panelPhoto;
	}
	public void setPanelPhoto(byte[] panelPhoto) {
		this.panelPhoto = panelPhoto;
	}
	
	@Column(name = "ard", unique = true, nullable = true)
	public String getARD() {
		return ARD;
	}
	public void setARD(String aRD) {
		ARD = aRD;
	}
	
	@Column(name = "ard_photo", unique = true, nullable = true)
	public byte[] getARDPhoto() {
		return ARDPhoto;
	}
	public void setARDPhoto(byte[] aRDPhoto) {
		ARDPhoto = aRDPhoto;
	}
	
	@Column(name = "no_of_batteries", unique = true, nullable = true)
	public Integer getNoOfBatteries() {
		return NoOfBatteries;
	}
	public void setNoOfBatteries(Integer noOfBatteries) {
		NoOfBatteries = noOfBatteries;
	}
	
	@Column(name = "battery_capacity", unique = true, nullable = true)
	public String getBatteryCapacity() {
		return BatteryCapacity;
	}
	public void setBatteryCapacity(String batteryCapacity) {
		BatteryCapacity = batteryCapacity;
	}
	
	@Column(name = "battery_make", unique = true, nullable = true)
	public String getBatteryMake() {
		return BatteryMake;
	}
	public void setBatteryMake(String batteryMake) {
		BatteryMake = batteryMake;
	}
	
	@Column(name = "cop_make", unique = true, nullable = true)
	public String getCOPMake() {
		return COPMake;
	}
	public void setCOPMake(String cOPMake) {
		COPMake = cOPMake;
	}
	
	@Column(name = "cop_photo", unique = true, nullable = true)
	public byte[] getCOPPhoto() {
		return COPPhoto;
	}
	public void setCOPPhoto(byte[] cOPPhoto) {
		COPPhoto = cOPPhoto;
	}
	
	@Column(name = "lop_make", unique = true, nullable = true)
	public String getLOPMake() {
		return LOPMake;
	}
	public void setLOPMake(String lOPMake) {
		LOPMake = lOPMake;
	}
	
	@Column(name = "lop_photo", unique = true, nullable = true)
	public byte[] getLOPPhoto() {
		return LOPPhoto;
	}
	public void setLOPPhoto(byte[] lOPPhoto) {
		LOPPhoto = lOPPhoto;
	}
	
	@Column(name = "collective_type", unique = true, nullable = true)
	public Integer getCollectiveType() {
		return collectiveType;
	}
	public void setCollectiveType(Integer collectiveType) {
		this.collectiveType = collectiveType;
	}
	
	@Column(name = "simplex_duplex", unique = true, nullable = true)
	public Integer getSimplexDuplex() {
		return SimplexDuplex;
	}
	public void setSimplexDuplex(Integer simplexDuplex) {
		SimplexDuplex = simplexDuplex;
	}
	
	@Column(name = "cartop_photo", unique = true, nullable = true)
	public byte[] getCartopPhoto() {
		return cartopPhoto;
	}
	public void setCartopPhoto(byte[] cartopPhoto) {
		this.cartopPhoto = cartopPhoto;
	}
	
	@Column(name = "auto_door_make", unique = true, nullable = true)
	public String getAutoDoorMake() {
		return autoDoorMake;
	}
	public void setAutoDoorMake(String autoDoorMake) {
		this.autoDoorMake = autoDoorMake;
	}
	
	@Column(name = "auto_door_head_photo", unique = true, nullable = true)
	public byte[] getAutoDoorHeaderPhoto() {
		return autoDoorHeaderPhoto;
	}
	public void setAutoDoorHeaderPhoto(byte[] autoDoorHeaderPhoto) {
		this.autoDoorHeaderPhoto = autoDoorHeaderPhoto;
	}
	
	@Column(name = "wiring_scheme", unique = true, nullable = true)
	public Integer getWiringShceme() {
		return wiringShceme;
	}
	public void setWiringShceme(Integer wiringShceme) {
		this.wiringShceme = wiringShceme;
	}
	
	@Column(name = "wiring_photo", unique = true, nullable = true)
	public byte[] getWiringPhoto() {
		return wiringPhoto;
	}
	public void setWiringPhoto(byte[] wiringPhoto) {
		this.wiringPhoto = wiringPhoto;
	}
	
	@Column(name = "fire_mode", unique = true, nullable = true)
	public Integer getFireMode() {
		return fireMode;
	}
	public void setFireMode(Integer fireMode) {
		this.fireMode = fireMode;
	}
	
	@Column(name = "intercomm", unique = true, nullable = true)
	public String getIntercomm() {
		return intercomm;
	}
	public void setIntercomm(String intercomm) {
		this.intercomm = intercomm;
	}
	
	@Column(name = "alarm", unique = true, nullable = true)
	public Integer getAlarm() {
		return alarm;
	}
	public void setAlarm(Integer alarm) {
		this.alarm = alarm;
	}
	
	@Column(name = "alarm_battery", unique = true, nullable = true)
	public String getAlarmBattery() {
		return alarmBattery;
	}
	public void setAlarmBattery(String alarmBattery) {
		this.alarmBattery = alarmBattery;
	}
	
	@Column(name = "access_control", unique = true, nullable = true)
	public String getAccessControl() {
		return accessControl;
	}
	public void setAccessControl(String accessControl) {
		this.accessControl = accessControl;
	}
	
	@Column(name = "lobby_photo", unique = true, nullable = true)
	public byte[] getLobbyPhoto() {
		return lobbyPhoto;
	}
	public void setLobbyPhoto(byte[] lobbyPhoto) {
		this.lobbyPhoto = lobbyPhoto;
	}
	

	@Column(name = "active_flag", unique = true, nullable = true)
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	
	@Column(name = "created_date", unique = true, nullable = true)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "created_by", unique = true, nullable = true)
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "updated_date", unique = true, nullable = true)
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Column(name = "updated_by", unique = true, nullable = true)
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	@Column(name = "status", unique = true, nullable = true)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	
	
	@Column(name = "lift_type", unique = true, nullable = true)
	public Integer getLiftType() {
		return liftType;
	}
	public void setLiftType(Integer liftType) {
		this.liftType = liftType;
	}
	
	@Column(name = "amc_end_date", unique = true, nullable = true)
	public Date getAmcEndDate() {
		return amcEndDate;
	}
	public void setAmcEndDate(Date amcEndDate) {
		this.amcEndDate = amcEndDate;
	}
	
	@Column(name="imei")
	private String imei;
	
	
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}

	private String lmsEventContactNumber;

	@Column(name="lms_event_contact_no",unique = true, nullable = true)
  	public String getLmsEventContactNumber() {
		return lmsEventContactNumber;
	}
	public void setLmsEventContactNumber(String lmsEventContactNumber) {
		this.lmsEventContactNumber = lmsEventContactNumber;
	}

	
}
