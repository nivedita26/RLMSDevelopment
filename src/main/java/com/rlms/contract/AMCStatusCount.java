package com.rlms.contract;

import java.math.BigInteger;

public class AMCStatusCount {

	private int statusId;
	private BigInteger statusCount;
	private int totalLiftCount;
	
	public int getTotalLiftCount() {
		return totalLiftCount;
	}

	public void setTotalLiftCount(int totalLiftCount) {
		this.totalLiftCount = totalLiftCount;
	}

	private String BranchName;
	private String CustomerName;
	private String City;
	private BigInteger underWarrantyCount;
	private BigInteger renewalDueCount;
    private BigInteger amcPendingCount;
    private BigInteger underAMCCount;
	public BigInteger getNotUnderWarranty() {
		return notUnderWarranty;
	}

	public void setNotUnderWarranty(BigInteger notUnderWarranty) {
		this.notUnderWarranty = notUnderWarranty;
	}

	private BigInteger  notUnderAMCCount;
	private BigInteger  notUnderWarranty;

	
	
	public BigInteger getUnderWarrantyCount() {
		return underWarrantyCount;
	}

	public BigInteger getRenewalDueCount() {
		return renewalDueCount;
	}

	public BigInteger getAmcPendingCount() {
		return amcPendingCount;
	}

	public BigInteger getUnderAMCCount() {
		return underAMCCount;
	}

	public BigInteger getNotUnderAMCCount() {
		return notUnderAMCCount;
	}

	public void setUnderWarrantyCount(BigInteger underWarrantyCount) {
		this.underWarrantyCount = underWarrantyCount;
	}

	public void setRenewalDueCount(BigInteger renewalDueCount) {
		this.renewalDueCount = renewalDueCount;
	}

	public void setAmcPendingCount(BigInteger amcPendingCount) {
		this.amcPendingCount = amcPendingCount;
	}

	public void setUnderAMCCount(BigInteger underAMCCount) {
		this.underAMCCount = underAMCCount;
	}

	public void setNotUnderAMCCount(BigInteger notUnderAMCCount) {
		this.notUnderAMCCount = notUnderAMCCount;
	}

	public String getBranchName() {
		return BranchName;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public String getCity() {
		return City;
	}

	public void setBranchName(String branchName) {
		BranchName = branchName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public void setCity(String city) {
		City = city;
	}


	public int getStatusId() {
		return statusId;
	}

	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}

	public BigInteger getStatusCount() {
		return statusCount;
	}

	public void setStatusCount(BigInteger statusCount) {
		this.statusCount = statusCount;
	}
	
}
