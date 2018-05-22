package com.rlms.constants;

public enum CustomerType {
	
	 RESIDENTIAL(15,"Residential"),
	 COMMERTIAL(16,"Commertial"),
	 BUNGLO(17,"Bunglo"),
	 HOSPITAL(18,"Hospital"),
	 GOODS(19,"Goods"),
	 DUMB_WAITER(20,"Dumb Waiter");

	private int id;
	private String type;
	
	CustomerType(int id, String type){
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
