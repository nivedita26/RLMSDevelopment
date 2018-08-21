package com.rlms.contract;

import java.util.List;

public class CustomerLiftDtls {

	private String customerName;
	private List<LiftCustomerMap> liftCustomerMapList;
		
	public List<LiftCustomerMap> getLiftCustomerMapList() {
		return liftCustomerMapList;
	}
	public void setLiftCustomerMapList(List<LiftCustomerMap> liftCustomerMapList) {
		this.liftCustomerMapList = liftCustomerMapList;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

}
