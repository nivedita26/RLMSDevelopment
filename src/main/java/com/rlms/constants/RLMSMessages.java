package com.rlms.constants;

public enum RLMSMessages {

	COMPLAINT_REGISTERED(1, "complaint_registered"),
	COMPLAINT_ASSIGNED(1, "complaint_assigned"),
	USER_DEACTIVATED(1, "user_deactivated");
	
	private int id;
	private String message;
	
	RLMSMessages(int id, String message){
		this.id = id;
		this.message = message;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
