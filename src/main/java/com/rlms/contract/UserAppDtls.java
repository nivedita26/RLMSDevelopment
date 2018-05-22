package com.rlms.contract;

public class UserAppDtls {

	private Integer userId;
	private Integer userType;
	private String appRegId;
	private double latitude;
	private double longitude;
	private String address;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getAppRegId() {
		return appRegId;
	}
	public void setAppRegId(String appRegId) {
		this.appRegId = appRegId;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
}
