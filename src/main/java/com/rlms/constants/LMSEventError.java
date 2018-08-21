package com.rlms.constants;

public enum LMSEventError {
	SUCCESS(200, "success"),
	FAIL(201,"fail");
	
	private int id;
	private String msg;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	private LMSEventError(int id, String msg) {
		this.id = id;
		this.msg = msg;
	}
	
}
