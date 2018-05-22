package com.rlms.constants;

public enum LiftType {

	type1(1,"type1"),
	type2(2,"type2"),
	type3(3,"type2");
	
	private int id;
	private String type;
	
	LiftType(int id, String type){
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
