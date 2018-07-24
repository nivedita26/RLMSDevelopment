package com.rlms.constants;

public enum AMCType {
	 COMPREHENSIVE(42,"Comprehensive"),
	 NON_COMPREHENSIVE(43,"NonComprehensive"),
	 ON_DEMAND(44,"OnDemand"),
	 OTHER(45,"Other");

	private int id;
	private String type;
	
	AMCType(int id, String type){
		this.id = id;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
