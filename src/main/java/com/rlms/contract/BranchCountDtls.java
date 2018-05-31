package com.rlms.contract;

import java.math.BigInteger;

public class BranchCountDtls {
	private String BranchCity;
	private BigInteger BranchCount;
	
	public BigInteger getBranchCount() {
		return BranchCount;
	}
	public void setBranchCount(BigInteger branchCount) {
		BranchCount = branchCount;
	}
	public String getBranchCity() {
		return BranchCity;
	}
	public void setBranchCity(String branchCity) {
		BranchCity = branchCity;
	}
	
}
