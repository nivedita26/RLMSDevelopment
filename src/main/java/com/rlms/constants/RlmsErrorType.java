package com.rlms.constants;

public enum RlmsErrorType {

	UNNKOWN_EXCEPTION_OCCHURS(1,"unknown_exception_occur"),
	ACCOUNT_CREATION_SUCCESS(2,"account_creation_success"),
	USER_ALREADY_REGISTERED(3,"user_already_registered"),
	CUSTOMER_ALREADY_ADDED(3,"customer_already_added"),
	PLEASE_PROVIDE_EMAILID(4,"please_provide_emailid"),
	BRANCH_CREATION_SUCCESSFUL(5,"branch_creation_successful"),
	BRANCH_ALREADY_EXISTS(6,"branch_already_exists"),
	ROLE_SUCCESSFULLY_ASSIGNED(7,"role_successfully_assigned"),
	ROLE_ALREADY_GIVEN(8,"role_already_given"),
	USER_REG_SUCCESFUL(9,"user_reg_succesful"),
	CUSTOMER_REG_SUCCESFUL(9,"customer_reg_succesful"),
	REGISTRATION_ID_INCORRECT(10,"registration_id_incorrect"),
	PUSH_NOTIFICATION_FAILED(11,"push_notification_failed"),
	INVALID_COMPANY_NAME(12,"invalid_company_name"),
	INVALID_BRANCH_NAME(12,"invalid_branch_name"),
	COMPANY_REG_SUCCESFUL(13,"company_reg_succesful"),
	LIFT_ADDED_SUCCESSFULLY(14,"lift_added_successfully"),
	LIFT_APPROVED(15,"lift_approved"),
	COMPLAINT_TITLE_BLANK(16,"complaint_title_blank"),
	COMPLAINT_REMARK_BLANK(17,"complaint_remark_blank"),
	LIFT_CUSTOMER_BLANK(18,"lift_customer_blank"),
	COMPLAINT_REG_SUCCESSFUL(19,"complaint_reg_successful"),
	MEMBER_WITH_SAME_CONTACT_NO(20,"member_with_same_contact_no"),
	MEMBER_REG_SUCCESSFUL(30,"member_reg_successful"),
	INVALID_CONTACT_NUMBER(31,"invalid_contact_number"),
	COMPLAINT_ASSIGNED_SUUCESSFULLY(32,"complaint_assigned_suucessfully"),
	COMPLAINT_ASSIGNED_ALREADY(32,"complaint_assigned_already"),
	PHOTO_UPDATED(33,"photo_updated"),
	STATUS_UPDATED(34,"status_updated"),
	SUCCESSFULLY(35,"successfully"),
	NO_COMPLAINT_ASSIGNED(36,"no_complaints_assigned"),
	ADDED_AMC_DTLS_SUCCESSFULLY(37,"added_amc_dtls_successfully"),
	INVALID_USER_ROLE_ID(38,"invalid_user_role_id"),
	INCOMPLETE_DATA(39,"incomplete_data"),
	VISIT_UPDATED_SUCCESS(40,"visit_updated_success"),
	USERNAME_ALREADY_USED(40,"username_already_used"),
	USER_ALREADY_REGISTERED_SYSTEM(42,"user_already_registered_system"),
	COMPANY_UPDATE_SUCCESFUL(43,"company_update_succesful"),
	COMPANY_DELETE_SUCCESFUL(44,"company_delete_succesful"),
	COMPLAINT_DELETE_SUCCESFUL(45,"complaint_delete_succesful"),
	TECHNICIAN_LOCATION_UPDATED(46,"location_updated");
	
	private Integer code;
	private String message;
	private RlmsErrorType(int code, String message){
		this.code = code;
		this.message = message;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}