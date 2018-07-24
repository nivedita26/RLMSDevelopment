package com.rlms.contract;

import java.math.BigInteger;

public class BranchCountDtls {
	private String branchCity;
	private BigInteger branchCount;
	private BigInteger branchActiveFlagCount;
	private BigInteger branchInactiveFlagCount;

	
	public BigInteger getBranchActiveFlagCount() {
		return branchActiveFlagCount;
	}
	public BigInteger getBranchInactiveFlagCount() {
		return branchInactiveFlagCount;
	}
	public void setBranchActiveFlagCount(BigInteger branchActiveFlagCount) {
		this.branchActiveFlagCount = branchActiveFlagCount;
	}
	public void setBranchInactiveFlagCount(BigInteger branchInactiveFlagCount) {
		this.branchInactiveFlagCount = branchInactiveFlagCount;
	}
	public String getBranchCity() {
		return branchCity;
	}
	public BigInteger getBranchCount() {
		return branchCount;
	}
	public void setBranchCity(String branchCity) {
		this.branchCity = branchCity;
	}
	public void setBranchCount(BigInteger branchCount) {
		this.branchCount = branchCount;
	}
}
