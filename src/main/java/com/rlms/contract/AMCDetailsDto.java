package com.rlms.contract;

import java.util.Date;
import java.util.List;

import com.rlms.model.ServiceCall;

public class AMCDetailsDto {

	private Integer companyId;
	private Integer branchCompanyMapId;
	private List<Integer> liftCustomerMapId;
	private List<Integer> listOfBranchCustomerMapId;
	private List<Integer> listOFStatusIds;
	private String liftNumber;
	private String customerName;
	private String amcStartDate;
	private String amcEndDate;
	private String lackStartDate;
	private String lackEndDate;
	private String status;
	private String dueDate;
	private String amcAmount;
	private String slackStartDate;
	private String slackEndDate;
	private Integer slackperiod;
	private Integer liftCustoMapId;
	private Integer amcType;
	private String amcTypeStr;
	private String city;
	private String area;
	private Date amcStDate;
	private Date amcEdDate;
	private Date amcDueDate;
	private Integer activeFlag;
	private String companyName;
	private List<ServiceCall> amcServiceCalls;
	
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getBranchCompanyMapId() {
		return branchCompanyMapId;
	}
	public void setBranchCompanyMapId(Integer branchCompanyMapId) {
		this.branchCompanyMapId = branchCompanyMapId;
	}
	public List<Integer> getLiftCustomerMapId() {
		return liftCustomerMapId;
	}
	public void setLiftCustomerMapId(List<Integer> liftCustomerMapId) {
		this.liftCustomerMapId = liftCustomerMapId;
	}
	public List<Integer> getListOfBranchCustomerMapId() {
		return listOfBranchCustomerMapId;
	}
	public void setListOfBranchCustomerMapId(List<Integer> listOfBranchCustomerMapId) {
		this.listOfBranchCustomerMapId = listOfBranchCustomerMapId;
	}
	public String getLiftNumber() {
		return liftNumber;
	}
	public void setLiftNumber(String liftNumber) {
		this.liftNumber = liftNumber;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getAmcStartDate() {
		return amcStartDate;
	}
	public void setAmcStartDate(String amcStartDate) {
		this.amcStartDate = amcStartDate;
	}
	public String getAmcEndDate() {
		return amcEndDate;
	}
	public void setAmcEndDate(String amcEndDate) {
		this.amcEndDate = amcEndDate;
	}
	public String getLackStartDate() {
		return lackStartDate;
	}
	public void setLackStartDate(String lackStartDate) {
		this.lackStartDate = lackStartDate;
	}
	public String getLackEndDate() {
		return lackEndDate;
	}
	public void setLackEndDate(String lackEndDate) {
		this.lackEndDate = lackEndDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public List<Integer> getListOFStatusIds() {
		return listOFStatusIds;
	}
	public void setListOFStatusIds(List<Integer> listOFStatusIds) {
		this.listOFStatusIds = listOFStatusIds;
	}
	public String getAmcAmount() {
		return amcAmount;
	}
	public void setAmcAmount(String amcAmount) {
		this.amcAmount = amcAmount;
	}
	public String getSlackStartDate() {
		return slackStartDate;
	}
	public void setSlackStartDate(String slackStartDate) {
		this.slackStartDate = slackStartDate;
	}
	public String getSlackEndDate() {
		return slackEndDate;
	}
	public void setSlackEndDate(String slackEndDate) {
		this.slackEndDate = slackEndDate;
	}
	public Integer getSlackperiod() {
		return slackperiod;
	}
	public void setSlackperiod(Integer slackperiod) {
		this.slackperiod = slackperiod;
	}
	public Integer getLiftCustoMapId() {
		return liftCustoMapId;
	}
	public void setLiftCustoMapId(Integer liftCustoMapId) {
		this.liftCustoMapId = liftCustoMapId;
	}
	public Integer getAmcType() {
		return amcType;
	}
	public void setAmcType(Integer amcType) {
		this.amcType = amcType;
	}
	public String getAmcTypeStr() {
		return amcTypeStr;
	}
	public void setAmcTypeStr(String amcTypeStr) {
		this.amcTypeStr = amcTypeStr;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Date getAmcStDate() {
		return amcStDate;
	}
	public void setAmcStDate(Date amcStDate) {
		this.amcStDate = amcStDate;
	}
	public Date getAmcEdDate() {
		return amcEdDate;
	}
	public void setAmcEdDate(Date amcEdDate) {
		this.amcEdDate = amcEdDate;
	}
	public Date getAmcDueDate() {
		return amcDueDate;
	}
	public void setAmcDueDate(Date amcDueDate) {
		this.amcDueDate = amcDueDate;
	}
	public Integer getActiveFlag() {
		return activeFlag;
	}
	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public List<ServiceCall> getAmcServiceCalls() {
		return amcServiceCalls;
	}
	public void setAmcServiceCalls(List<ServiceCall> amcServiceCalls) {
		this.amcServiceCalls = amcServiceCalls;
	}
	
	
	
}
