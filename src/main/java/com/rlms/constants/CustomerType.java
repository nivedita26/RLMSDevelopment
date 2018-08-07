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
	
	 public static String getStringFromID(Integer customerTypeId){
			if(customerTypeId == CustomerType.BUNGLO.getId()){
				return CustomerType.BUNGLO.getType();
			}
			else if(customerTypeId == CustomerType.COMMERTIAL.getId()){
				return CustomerType.COMMERTIAL.getType();
			}
			else if(customerTypeId == CustomerType.DUMB_WAITER.getId()){
				return CustomerType.DUMB_WAITER.getType();
			}
			else if(customerTypeId == CustomerType.GOODS.getId()){
				return CustomerType.GOODS.getType();
			}
			else if(customerTypeId == CustomerType.HOSPITAL.getId()){
				return CustomerType.HOSPITAL.getType();
			}
			else if(customerTypeId == CustomerType.RESIDENTIAL.getId()){
				return CustomerType.RESIDENTIAL.getType();
			}
			return "";
			
	 }
}
