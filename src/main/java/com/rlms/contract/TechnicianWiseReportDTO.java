package com.rlms.contract;

import java.util.List;

public class TechnicianWiseReportDTO {

	private Integer companyId;
	private Integer branchCompanyMapId;
	private List<Integer> listOFRatings;
	private List<Integer> listOfUserRoleIds;
	
	private String technicianName;
	private String contactNumber;
	private String branchname;
	private String companyName;
	private Integer totalComplaintsAssigned;
	private Integer totalComplaintsResolved;
	private String avgTimeTaken;
	private Integer userRating;
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
	public List<Integer> getListOFRatings() {
		return listOFRatings;
	}
	public void setListOFRatings(List<Integer> listOFRatings) {
		this.listOFRatings = listOFRatings;
	}
	public String getTechnicianName() {
		return technicianName;
	}
	public void setTechnicianName(String technicianName) {
		this.technicianName = technicianName;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public String getBranchname() {
		return branchname;
	}
	public void setBranchname(String branchname) {
		this.branchname = branchname;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Integer getTotalComplaintsAssigned() {
		return totalComplaintsAssigned;
	}
	public void setTotalComplaintsAssigned(Integer totalComplaintsAssigned) {
		this.totalComplaintsAssigned = totalComplaintsAssigned;
	}
	public Integer getTotalComplaintsResolved() {
		return totalComplaintsResolved;
	}
	public void setTotalComplaintsResolved(Integer totalComplaintsResolved) {
		this.totalComplaintsResolved = totalComplaintsResolved;
	}
	public String getAvgTimeTaken() {
		return avgTimeTaken;
	}
	public void setAvgTimeTaken(String avgTimeTaken) {
		this.avgTimeTaken = avgTimeTaken;
	}
	public Integer getUserRating() {
		return userRating;
	}
	public void setUserRating(Integer userRating) {
		this.userRating = userRating;
	}
	public List<Integer> getListOfUserRoleIds() {
		return listOfUserRoleIds;
	}
	public void setListOfUserRoleIds(List<Integer> listOfUserRoleIds) {
		this.listOfUserRoleIds = listOfUserRoleIds;
	}
	
}
