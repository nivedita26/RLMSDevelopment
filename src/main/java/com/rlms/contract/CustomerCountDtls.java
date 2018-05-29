package com.rlms.contract;

public class CustomerCountDtls {

	private String branchName;
	private String  city;
	private int customerCount;
	private int activeFlagCount;
	private int inactiveFlagCount;
	
	public int getActiveFlagCount() {
		return activeFlagCount;
	}
	public int getInactiveFlagCount() {
		return inactiveFlagCount;
	}
	public void setActiveFlagCount(int activeFlagCount) {
		this.activeFlagCount = activeFlagCount;
	}
	public void setInactiveFlagCount(int inactiveFlagCount) {
		this.inactiveFlagCount = inactiveFlagCount;
	}
	public int getCustomerCount() {
		return customerCount;
	}
	public void setCustomerCount(int customerCount) {
		this.customerCount = customerCount;
	}
	public String getBranchName() {
		return branchName;
	}
	
	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
}
