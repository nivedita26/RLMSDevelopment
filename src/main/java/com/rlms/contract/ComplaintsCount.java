package com.rlms.contract;

import java.math.BigInteger;

public class ComplaintsCount {

	private String callType;
	private BigInteger totalCallTypeCount;
	private BigInteger todaysCallTypeCount;
	private String callStatus;
	private BigInteger totalCallStatusCount;
	private BigInteger todaysCallStatusCount;
	private String avgLogsPerDay;
	private String avgResolvedPerDay;
	
	public String getAvgResolvedPerDay() {
		return avgResolvedPerDay;
	}
	public void setAvgResolvedPerDay(String avgResolvedPerDay) {
		this.avgResolvedPerDay = avgResolvedPerDay;
	}
	public String getAvgLogsPerDay() {
		return avgLogsPerDay;
	}
	public void setAvgLogsPerDay(String avgLogsPerDay) {
		this.avgLogsPerDay = avgLogsPerDay;
	}
	public BigInteger getTotalCallStatusCount() {
		return totalCallStatusCount;
	}
	public BigInteger getTodaysCallTypeCount() {
		return todaysCallTypeCount;
	}
	public BigInteger getTodaysCallStatusCount() {
		return todaysCallStatusCount;
	}
	public void setTodaysCallTypeCount(BigInteger todaysCallTypeCount) {
		this.todaysCallTypeCount = todaysCallTypeCount;
	}
	public void setTotalCallStatusCount(BigInteger totalCallStatusCount) {
		this.totalCallStatusCount = totalCallStatusCount;
	}
	public void setTodaysCallStatusCount(BigInteger todaysCallStatusCount) {
		this.todaysCallStatusCount = todaysCallStatusCount;
	}
	public BigInteger getTotalCallTypeCount() {
		return totalCallTypeCount;
	}

	public void setTotalCallTypeCount(BigInteger totalCallTypeCount) {
		this.totalCallTypeCount = totalCallTypeCount;
	}
	
	private String liftNumber;
	
	public String getLiftNumber() {
		return liftNumber;
	}
	public void setLiftNumber(String liftNumber) {
		this.liftNumber = liftNumber;
	}
	public String getCallType() {
		return callType;
	}
	public String getCallStatus() {
		return callStatus;
	}
	public void setCallType(String callType) {
		this.callType = callType;
	}
	public void setCallStatus(String callStatus) {
		this.callStatus = callStatus;
	}
}
