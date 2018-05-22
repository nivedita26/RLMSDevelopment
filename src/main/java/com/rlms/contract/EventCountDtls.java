package com.rlms.contract;

import java.math.BigInteger;

public class EventCountDtls {

	
	private String BranchName;
	private String City;
	private String CustomerName;
	private BigInteger totolEventCount;
	private BigInteger totalErrorCount;
	private BigInteger totalResCount;
	private String liftNumber;
	
	
	public String getBranchName() {
		return BranchName;
	}
	public String getCity() {
		return City;
	}
	public String getCustomerName() {
		return CustomerName;
	}
	public BigInteger getTotolEventCount() {
		return totolEventCount;
	}
	public BigInteger getTotalErrorCount() {
		return totalErrorCount;
	}
	public BigInteger getTotalResCount() {
		return totalResCount;
	}
	public String getLiftNumber() {
		return liftNumber;
	}
	public void setBranchName(String branchName) {
		BranchName = branchName;
	}
	public void setCity(String city) {
		City = city;
	}
	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}
	public void setTotolEventCount(BigInteger totolEventCount) {
		this.totolEventCount = totolEventCount;
	}
	public void setTotalErrorCount(BigInteger totalErrorCount) {
		this.totalErrorCount = totalErrorCount;
	}
	public void setTotalResCount(BigInteger totalResCount) {
		this.totalResCount = totalResCount;
	}
	public void setLiftNumber(String liftNumber) {
		this.liftNumber = liftNumber;
	}
	
	
}
