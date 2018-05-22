package com.rlms.constants;

public enum Status {

	PENDING(2, "Pending"),
	ASSIGNED(3,"Assigned"),
	INPROGESS(4,"In Progress"),
	RESOLVED(5,"Resolved"),
	PENDING_FOR_APPROVAL(9,"Pending For Approval"),
	COMPLETED(10,"Completed"),
	UNDER_WARRANTY(38,"Under Warranty"),
	RENEWAL_DUE(39,"Renewal Due"),
	AMC_PENDING(40,"AMC Pending"),
	UNDER_AMC(41,"Under AMC");
	
	private Integer statusId;
	private String statusMsg;
	
	private Status(Integer statusId, String statusMsg){
		this.statusId = statusId;
		this.statusMsg = statusMsg;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public String getStatusMsg() {
		return statusMsg;
	}

	public void setStatusMsg(String statusMsg) {
		this.statusMsg = statusMsg;
	}
	
	
	public static String getStringFromID(Integer statusId){
		if(statusId == Status.PENDING.getStatusId()){
			return Status.PENDING.getStatusMsg();
		}else if(statusId == Status.ASSIGNED.getStatusId()){
			return Status.ASSIGNED.getStatusMsg();
		}else if(statusId == Status.INPROGESS.getStatusId()){
			return Status.INPROGESS.getStatusMsg();
		}else if(statusId == Status.RESOLVED.getStatusId()){
			return Status.RESOLVED.getStatusMsg();
		}else if(statusId == Status.AMC_PENDING.getStatusId()){
			return Status.AMC_PENDING.getStatusMsg();
		}else if(statusId == Status.PENDING_FOR_APPROVAL.getStatusId()){
			return Status.PENDING_FOR_APPROVAL.getStatusMsg();
		}else if(statusId == Status.COMPLETED.getStatusId()){
			return Status.COMPLETED.getStatusMsg();
		}else if(statusId == Status.UNDER_WARRANTY.getStatusId()){
			return Status.UNDER_WARRANTY.getStatusMsg();
		}else if(statusId == Status.RENEWAL_DUE.getStatusId()){
			return Status.RENEWAL_DUE.getStatusMsg();
		}else if(statusId == Status.AMC_PENDING.getStatusId()){
			return Status.	AMC_PENDING.getStatusMsg();
		}else if(statusId == Status.UNDER_AMC.getStatusId()){
			return Status.UNDER_AMC.getStatusMsg();
		}
		
		return "";
	}
	
}
