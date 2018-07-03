package com.rlms.contract;

public class CallSpecificReportDto {
	
	
		private String complaintNumber;
		private String serviceCallTypeStr;
		private String title;
		private String registrationDateStr;
		private String liftNumber;
		private String customerName;
		private String customerAddress;
		private String customerCity;
		private String branchName;
		private String status;
		private SiteVisitReportDto siteVisitDetailsList ;
		private String registeredBy ;

		
		public String getRegisteredBy() {
			return registeredBy;
		}
		public void setRegisteredBy(String registeredBy) {
			this.registeredBy = registeredBy;
		}
		public String getComplaintNumber() {
			return complaintNumber;
		}
		public void setComplaintNumber(String complaintNumber) {
			this.complaintNumber = complaintNumber;
		}
		public String getServiceCallTypeStr() {
			return serviceCallTypeStr;
		}
		public void setServiceCallTypeStr(String serviceCallTypeStr) {
			this.serviceCallTypeStr = serviceCallTypeStr;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getRegistrationDateStr() {
			return registrationDateStr;
		}
		public void setRegistrationDateStr(String registrationDateStr) {
			this.registrationDateStr = registrationDateStr;
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
		public String getCustomerAddress() {
			return customerAddress;
		}
		public void setCustomerAddress(String customerAddress) {
			this.customerAddress = customerAddress;
		}
		public String getCustomerCity() {
			return customerCity;
		}
		public void setCustomerCity(String customerCity) {
			this.customerCity = customerCity;
		}
		public String getBranchName() {
			return branchName;
		}
		public void setBranchName(String branchName) {
			this.branchName = branchName;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public SiteVisitReportDto getSiteVisitDetailsList() {
			return siteVisitDetailsList;
		}
		public void setSiteVisitDetailsList(SiteVisitReportDto siteVisitDetailsList) {
			this.siteVisitDetailsList = siteVisitDetailsList;
		} 


		/*private Date fromDate;
		private Date toDate;
		private String fromDateStr;
		private String  toDateStr;
		private String liftAddress;
		private String latitude;
		private String longitude;
		private Integer registrationType;
		private String registrationTypeStr;
		private String remark;
		private String status;
		private String title;
		private String technicianDtls;
		private Integer complaintId;
		private Integer userRoleId;
		private Integer liftCustoMapId;
		private String complaintent;
		private String regType;
		private Integer complaintTechMapId;
		private Date updatedDate;
		private String companyName;
		private String city;
		private Integer serviceCallType;
		private Date CallAssignedDate;
		private String CallAssignedDateStr;
		private String resolvedDateStr;
		*/
}
