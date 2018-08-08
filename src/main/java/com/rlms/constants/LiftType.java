package com.rlms.constants;

public enum LiftType {

	type1(1,"Auto Door"),//Geared
	type2(2,"Manual Door"),//Gearless
	type3(3,"Hydraulic");
	
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
