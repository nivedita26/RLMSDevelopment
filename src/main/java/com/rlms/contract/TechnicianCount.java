package com.rlms.contract;

import java.math.BigInteger;

public class TechnicianCount {

	private String BranchName;
	private String City;
	private BigInteger count;
	private BigInteger totolActiveTechnician;
	private BigInteger totalInactiveTechnician;
	
	public BigInteger getTotolActiveTechnician() {
		return totolActiveTechnician;
	}
	public BigInteger getTotalInactiveTechnician() {
		return totalInactiveTechnician;
	}
	public void setTotolActiveTechnician(BigInteger totolActiveTechnician) {
		this.totolActiveTechnician = totolActiveTechnician;
	}
	public void setTotalInactiveTechnician(BigInteger totalInactiveTechnician) {
		this.totalInactiveTechnician = totalInactiveTechnician;
	}
	public String getBranchName() {
		return BranchName;
	}
	public void setBranchName(String branchName) {
		BranchName = branchName;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public BigInteger getCount() {
		return count;
	}
	public void setCount(BigInteger count) {
		this.count = count;
	}
	
	
}
